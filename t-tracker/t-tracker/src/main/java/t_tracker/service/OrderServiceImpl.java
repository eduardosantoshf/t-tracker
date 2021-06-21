package t_tracker.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import t_tracker.model.Client;
import t_tracker.model.Lab;
import t_tracker.model.Order;
import t_tracker.model.Product;
import t_tracker.model.Stock;
import t_tracker.repository.ClientRepository;
import t_tracker.repository.CoordinatesRepository;
import t_tracker.repository.LabRepository;
import t_tracker.repository.OrderRepository;
import t_tracker.repository.ProductRepository;
import t_tracker.repository.StockRepository;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private LabRepository labRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CoordinatesRepository coordRepository;

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private RestTemplate restTemplate;

    private HttpHeaders httpHeaders;
    private String storeSignupUrl = "http://localhost:8080/store";
    private String orderPlacementUrl = "http://localhost:8080/store/order/";
    private String labInfo = "{\"name\":\"CovidTestsDeliveries\",\"ownerName\":\"TqsG101\",\"latitude\":\"1.0\",\"longitude\":\"2.0\"}";

    @Override
    public Order placeAnOrder(Order order) {

        List<Lab> labFound = labRepository.findAll();

        if (labFound.size() == 0)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Laboratory not found.");

        for (Stock stockOrder : order.getProducts())
            if (!isInStock(labFound.get(0), stockOrder))
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Product out of stock.");

        Optional<Client> potentialClient = clientRepository.findById(order.getClientId());

        if (!potentialClient.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found.");

        Client client = potentialClient.get();

        order.setPickupLocation(labFound.get(0).getLocation());
        order.setLabId(labFound.get(0).getId());
        order.setOrderTotal(order.getTotalPrice());
        order.setDeliverLocation(client.getHomeLocation());
        order.setPickupLocation(labFound.get(0).getLocation());

        Lab authDetails = getLabDetails();

        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", authDetails.getToken());

        JSONObject orderRequest = buildOrderRequest("" + order.getLabId() + java.time.LocalDate.now(),
                "" + order.getOrderTotal() * 0.1, order.getDeliverLocation().getLatitude().toString(),
                order.getDeliverLocation().getLongitude().toString());

        HttpEntity<String> requestContent = new HttpEntity<>(orderRequest.toString(), httpHeaders);

        ResponseEntity<JSONObject> response;

        try {

            response = restTemplate.postForEntity(orderPlacementUrl + authDetails.getId(),
                    requestContent, JSONObject.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, e.getMessage());
        }

        if (response == null)
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Error getting response from drivers api.");

        order.setDriverId(Integer.parseInt(response.getBody().get("id").toString()));

        // Store order products and quantities
        List<Stock> orderStock = order.getProducts();
        Product actualProduct;

        for (Stock s : orderStock) {
            Optional<Product> productFound = productRepository.findByNameAndPriceAndType(
                    s.getProduct().getName(), s.getProduct().getPrice(),
                    s.getProduct().getType());

            if (productFound.isPresent())
                actualProduct = productFound.get();
            else
                actualProduct = productRepository.save(s.getProduct());

            s.setProduct(actualProduct);
            labFound.get(0).removeStock(s);

            stockRepository.save(s);
        }

        Order orderStored = orderRepository.save(order);
        orderStock = orderStored.getProducts();

        for (Stock s : orderStock) {
            s.setOrder(order);
            stockRepository.save(s);
        }

        coordRepository.save(order.getPickupLocation());
        coordRepository.save(order.getDeliverLocation());

        orderRepository.save(orderStored);

        labRepository.save(labFound.get(0));

        client.addOrder(orderStored);
        clientRepository.save(client);

        return orderStored;
        
    }

    public Lab getLabDetails() {
        List<Lab> allDetails = labRepository.findAll();

        Lab authDetails;
        if (allDetails.size() == 0) {
            httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> labDetailsRequest = new HttpEntity<>(labInfo, httpHeaders);

            ResponseEntity<Lab> response = restTemplate.postForEntity(storeSignupUrl, labDetailsRequest,
                    Lab.class);

            authDetails = labRepository.save(response.getBody());

        } else {
            authDetails = allDetails.get(0);

        }

        return authDetails;
    }

    public JSONObject buildOrderRequest(String name, String comission, String deliveryLatitude,
            String deliveryLongitude) {
        HashMap<String, String> orderDetails = new HashMap<>();

        orderDetails.put("name", name);
        orderDetails.put("comission", comission);
        orderDetails.put("deliveryLatitude", deliveryLatitude);
        orderDetails.put("deliveryLongitude", deliveryLongitude);

        return new JSONObject(orderDetails);
    }

    @Override
    public boolean isInStock(Lab lab, Stock products) {
        List<Stock> labStocks = lab.getStocks();

        for (Stock stock : labStocks)
            if (stock.getProduct().equals(products.getProduct())) {
                if (stock.getQuantity() >= products.getQuantity())
                    return true;
                return false;
            }

        return false;
    }

}
