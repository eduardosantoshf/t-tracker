package t_tracker.service;

import t_tracker.model.*;

public interface OrderService {
    
    Order placeAnOrder(Order order);

    boolean isInStock(Lab lab, Stock products);

}
