package t_tracker.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import t_tracker.model.Order;
import t_tracker.model.Stock;
import t_tracker.repository.ClientRepository;
import t_tracker.repository.LabRepository;
import t_tracker.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    LabRepository labRepository;

    @Autowired
    ClientRepository clientRepository;

    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public Order placeAnOrder(Order order) {

        if ( !clientRepository.findById(order.getClient().getId()).isPresent() )
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found.");
        if ( !labRepository.findById(order.getLab().getId()).isPresent() )
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Laboratory not found.");

        for ( Stock stockOrder : order.getListOfProducts() )
            if ( !isInStock(stockOrder) )
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Product out of stock.");
        
            ResponseEntity<String> responseEnt = this.restTemplate.getForEntity(1, String.class);

        // TO DO
        order.setDriverId(0);
        
        return orderRepository.save(order);
    }

    @Override
    public boolean isInStock(Stock products) {
        List<Stock> labStocks = products.getLab().getStocks();

        for ( Stock stock : labStocks )
            if ( stock.getProduct().equals(products.getProduct()) )
                if ( stock.getQuantity() >= products.getQuantity() )
                    return true;
                else
                    return false;

        return false;
    }

}
