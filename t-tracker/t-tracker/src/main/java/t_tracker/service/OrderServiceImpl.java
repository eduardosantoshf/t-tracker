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

import t_tracker.model.Coordinates;
import t_tracker.model.Lab;
import t_tracker.model.Order;
import t_tracker.model.OrderItem;
import t_tracker.repository.LabRepository;
import t_tracker.repository.OrderItemRepository;
import t_tracker.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private LabRepository labRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private StockServiceImpl stockService;

    @Autowired
    private RestTemplate restTemplate;

    private HttpHeaders httpHeaders;
    private String storeSignupUrl = "http://backend-engine:8080/store";
    private String orderPlacementUrl = "http://backend-engine:8080/store/order/";
    private String ratingPlacementUrl = "http://backend-engine:8080/store/driver/rating/";
    private String labInfo = "{\"name\":\"CovidTestsDeliveries" + java.time.LocalDateTime.now() + "\",\"ownerName\":\"TqsG101\",\"latitude\":\"1.0\",\"longitude\":\"2.0\"}";

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
            JSONObject body = response.getBody();
            if (body != null)
                orderToStore.setDriverId(Integer.parseInt(body.get("id").toString()));
            else
                throw new NullPointerException();
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
        orderToStore.setStatus("Delivering");
        Order orderPlaced = orderRepository.save(orderToStore);

        for (OrderItem item : order.getProducts()) {
            OrderItem newItem = new OrderItem(item.getProduct(), item.getQuantity(), orderPlaced);
            orderItemRepository.save(newItem);
        }

        return orderPlaced;
    }

    @Override
    public Lab getLabDetails() {
        List<Lab> allDetails = labRepository.findAll();

        Lab authDetails;
        if (allDetails.size() == 0) {
            httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> labDetailsRequest = new HttpEntity<>(labInfo, httpHeaders);

            ResponseEntity<Lab> response = restTemplate.postForEntity(storeSignupUrl, labDetailsRequest, Lab.class);

            Lab body = response.getBody();
            if (body != null)
                authDetails = labRepository.save(body);
            else
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Error retrieving authentication details from deliveries API.");

        } else {
            authDetails = allDetails.get(0);

        }

        return authDetails;
    }

    @Override
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
    public Order getOrder(int id) {
        Optional<Order> orderFound = orderRepository.findById(id);

        if (!orderFound.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found.");

        return orderFound.get();
    }

    @Override
    public Order updateStatus(Order order, int status) {
        if (status == 0)
            order.setStatus("Pending");
        else if (status == 1)
            order.setStatus("Delivering");
        else if (status == 2)
            order.setStatus("Delivered");

        return orderRepository.save(order);

    }

    @Override
    public Order rateOrder(Order order, int rating) {
        List<Lab> allDetails = labRepository.findAll();

        if (rating < 0 || rating > 10)
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Rating value out of bounds");

        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Authorization", allDetails.get(0).getToken());

        HttpEntity<String> requestContent = new HttpEntity<>("{\"rating\":" + rating + "}", httpHeaders);

        restTemplate.postForEntity(ratingPlacementUrl + allDetails.get(0).getId() + "/" + order.getDriverId(),
                requestContent, JSONObject.class);

        order.setRating(rating);
        return orderRepository.save(order);
    }

}
