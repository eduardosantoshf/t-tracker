package t_tracker.service;

import java.util.HashMap;
import java.util.List;

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

import t_tracker.model.Coordinates;
import t_tracker.model.Lab;
import t_tracker.model.Order;
import t_tracker.model.OrderItem;
import t_tracker.model.Stock;
import t_tracker.repository.LabRepository;
import t_tracker.repository.OrderItemRepository;
import t_tracker.repository.OrderRepository;
import t_tracker.repository.StockRepository;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private LabRepository labRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private StockServiceImpl stockService;

    @Autowired
    private RestTemplate restTemplate;

    private HttpHeaders httpHeaders;
    private String storeSignupUrl = "http://backend-engine:8080/store";
    private String orderPlacementUrl = "http://backend-engine:8080/store/order/";
    private String labInfo = "{\"name\":\"CovidTestsDeliveries\",\"ownerName\":\"TqsG101\",\"latitude\":\"1.0\",\"longitude\":\"2.0\"}";

    @Override
    public Order placeAnOrder(Order order) {

        Order orderToStore = new Order(order.getClient());

        // Retrieve lab details for delivery placement
        Lab lab;
        try {
            lab = getLabDetails();
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(e.getStatus(), e.getMessage());
        }

        // Request driver
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", lab.getToken());

        Coordinates deliverLocation = orderToStore.getClient().getHomeLocation();

        JSONObject orderRequest = buildOrderRequest("" + lab.getName() + java.time.LocalDate.now(),
                "" + order.getTotalPrice() * 0.1, deliverLocation.getLatitude().toString(),
                deliverLocation.getLongitude().toString());

        HttpEntity<String> requestContent = new HttpEntity<>(orderRequest.toString(), httpHeaders);

        ResponseEntity<JSONObject> response;

        try {
            response = restTemplate.postForEntity(orderPlacementUrl + lab.getId(), requestContent, JSONObject.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, e.getMessage());
        }

        try {
            orderToStore.setDriverId(Integer.parseInt(response.getBody().get("id").toString()));
        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Error getting response from drivers api.");
        }

        // Update stock if order products are valid
        for (OrderItem s : order.getProducts()) {
            try {
                stockService.removeStock(s.getProduct(), s.getQuantity());
            } catch (ResponseStatusException e) {
                throw new ResponseStatusException(e.getStatus(), e.getMessage());
            }
        }

        orderToStore.setPickupLocation(lab.getLocation());
        orderToStore.setOrderTotal(order.getTotalPrice());
        orderToStore.setDeliverLocation(order.getClient().getHomeLocation());

        Order orderPlaced = orderRepository.save(orderToStore);

        for (OrderItem item : order.getProducts()) {
            OrderItem newItem = new OrderItem(item.getProduct(), item.getQuantity(), orderPlaced);
            orderItemRepository.save(newItem);
        }

        return orderPlaced;
    }

    public Lab getLabDetails() {
        List<Lab> allDetails = labRepository.findAll();

        Lab authDetails;
        if (allDetails.size() == 0) {
            httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> labDetailsRequest = new HttpEntity<>(labInfo, httpHeaders);

            ResponseEntity<Lab> response = restTemplate.postForEntity(storeSignupUrl, labDetailsRequest, Lab.class);
            try {
                authDetails = labRepository.save(response.getBody());
            } catch (NullPointerException e) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Error retrieving authentication details from deliveries API.");
            }

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
    public boolean isInStock(Stock products) {
        List<Stock> availableStock = stockRepository.findAll();

        for (Stock stock : availableStock)
            if (stock.getProduct().equals(products.getProduct())) {
                if (stock.getQuantity() >= products.getQuantity())
                    return true;
                return false;
            }

        return false;
    }

}
