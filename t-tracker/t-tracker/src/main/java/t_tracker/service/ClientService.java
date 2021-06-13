package t_tracker.service;

import java.util.Map;

import t_tracker.model.Client;

public interface ClientService {

    Client registerClient(Client client);
    Map<String, Object> convertClientToMap(Client client);

}
