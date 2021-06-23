package t_tracker.service;

import org.json.simple.JSONObject;

import t_tracker.model.*;

public interface OrderService {

    Order placeAnOrder(Order order);

    Order updateStatus(Order order, int status);

    Order getOrder(int id);

    Order rateOrder(Order order, int rating);

    Lab getLabDetails();

    JSONObject buildOrderRequest(String name, String comission, String deliveryLatitude, String deliveryLongitude);

}
