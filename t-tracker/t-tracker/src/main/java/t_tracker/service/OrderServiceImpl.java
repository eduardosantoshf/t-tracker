package t_tracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import t_tracker.model.Order;
import t_tracker.model.Stock;
import t_tracker.repository.LabRepository;
import t_tracker.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    LabRepository labRepository;

    @Override
    public Order placeAnOrder(Order order) {
        return null;
    }

    @Override
    public boolean isInStock(Stock products) {
        return false;
    }

}
