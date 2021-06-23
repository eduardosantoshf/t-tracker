package t_tracker.service;

import t_tracker.model.*;

public interface OrderService {
    
    Order placeAnOrder(Order order);

    void updateStatus(Order order, int status);

    void rateOrder(Order order, int rating);

}
