package t_tracker.service;

import java.util.List;
import java.util.Map;

import t_tracker.model.Client;
import t_tracker.model.Order;

public interface ClientService {

    Client registerClient(Client client);
    Client getClientByUsername(String username);
    List<Order> getOrders(String clientUsername);
    Map<String, Object> convertClientToMap(Client client);

}
