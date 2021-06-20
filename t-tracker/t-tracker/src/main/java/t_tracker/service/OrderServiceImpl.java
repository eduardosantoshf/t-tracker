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
import t_tracker.model.StoreDetails;
import t_tracker.repository.ClientRepository;
import t_tracker.repository.CoordinatesRepository;
import t_tracker.repository.LabRepository;
import t_tracker.repository.OrderRepository;
import t_tracker.repository.ProductRepository;
import t_tracker.repository.StockRepository;
import t_tracker.repository.StoreDetailsRepository;

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
    private StoreDetailsRepository storeDetailsRepository;

    @Autowired
    private RestTemplate restTemplate;

    private HttpHeaders httpHeaders;
    private String storeSignupUrl = "http://localhost:8080/store";
    private String orderPlacementUrl = "http://localhost:8080/store/order/";

    @Override
    public Order placeAnOrder(Order order) {

        Optional<Client> clientFound = clientRepository.findById(order.getClientId());
        Optional<Lab> labFound = labRepository.findById(order.getLabId());

        if (!clientFound.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found.");
        if (!labFound.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Laboratory not found.");

        for (Stock stockOrder : order.getListOfProducts())
            if (!isInStock(labFound.get(), stockOrder))
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Product out of stock.");

        Client client = clientFound.get();
        StoreDetails authDetails = getStoreDetails();

        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", authDetails.getToken());

        JSONObject orderRequest = buildOrderRequest("" + order.getLabId() + order.getId(),
                "" + order.getOrderTotal() * 0.1, order.getDeliverLocation().getLatitude().toString(),
                order.getDeliverLocation().getLongitude().toString());

        HttpEntity<String> requestContent = new HttpEntity<String>(orderRequest.toString(), httpHeaders);

        try {
            ResponseEntity<JSONObject> response = restTemplate.postForEntity(orderPlacementUrl + authDetails.getId(),
                    requestContent, JSONObject.class);
            // Order orderToStore = order;
            order.setDriverId(Integer.parseInt(response.getBody().get("id").toString()));
            coordRepository.save(order.getPickupLocation());
            coordRepository.save(order.getDeliverLocation());

            // Store order products and quantities
            List<Stock> orderStock = order.getListOfProducts();
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
            }

            Order orderStored = orderRepository.save(order);

            // Update lab stock
            for (Stock s : orderStock) {
                labFound.get().removeStock(s);
                s.setOrder(order);
                stockRepository.save(s);
            }
            labRepository.save(labFound.get());

            client.addOrder(orderStored);
            clientRepository.save(client);

            return orderStored;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, e.getMessage());
        }
    }

    public StoreDetails getStoreDetails() {

        List<StoreDetails> allDetails = storeDetailsRepository.findAll();

        StoreDetails authDetails = new StoreDetails();
        if (allDetails.size() == 0) {
            httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            HashMap<String, String> storeDetails = new HashMap<>();
            storeDetails.put("name", "CovidTestsDeliveries2");
            storeDetails.put("ownerName", "TqsG101");
            JSONObject storeRequest = new JSONObject(storeDetails);

            HttpEntity<String> requestContent = new HttpEntity<String>(storeRequest.toString(), httpHeaders);

            ResponseEntity<StoreDetails> response = restTemplate.postForEntity(storeSignupUrl, requestContent,
                    StoreDetails.class);

            authDetails = storeDetailsRepository.save(response.getBody());

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
            if (stock.getProduct().equals(products.getProduct()))
                if (stock.getQuantity() >= products.getQuantity())
                    return true;
                else
                    return false;

        return false;
    }

}
