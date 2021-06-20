package t_tracker.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import t_tracker.model.Client;
import t_tracker.model.Order;
import t_tracker.repository.ClientRepository;
import t_tracker.repository.CoordinatesRepository;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private CoordinatesRepository coordRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Client registerClient(Client newClient) {
        Optional<Client> clientFoundByUsername = clientRepository.findByUsername(newClient.getUsername());
        Optional<Client> clientFoundByEmail = clientRepository.findByEmail(newClient.getEmail());

        if (newClient.getHomeLocation() == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "THome location is required.");
        }
        
        if (clientFoundByUsername.isPresent()) {
            throw new ResponseStatusException(HttpStatus.OK, "The provided username is already being used.");
        }

        if (clientFoundByEmail.isPresent()) {
            throw new ResponseStatusException(HttpStatus.OK, "The provided email is already being used.");
        }

        newClient.setPassword(passwordEncoder.encode(newClient.getPassword()));
        Client clientToRegister = newClient;

        coordRepository.save(clientToRegister.getHomeLocation());

        return clientRepository.save(clientToRegister);
    }

    @Override
    public List<Order> getOrders(String clientUsername) {
        Optional<Client> clientFound = clientRepository.findByUsername(clientUsername);
        System.out.println(clientFound);
        if (!clientFound.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found.");
        }

        return clientFound.get().getOrderlist();
    }

    @Override
    public Map<String, Object> convertClientToMap(Client client) {
        Map<String, Object> mappedClient = new HashMap<>();

        mappedClient.put("name", client.getName());
        mappedClient.put("username", client.getUsername());
        mappedClient.put("email", client.getEmail());
        mappedClient.put("phoneNumber", client.getPhoneNumber());
        mappedClient.put("homeLocation", client.getHomeLocation());
        mappedClient.put("orderList", client.getOrderlist());

        return mappedClient;
    }

    @Override
    public Client getClientByUsername(String username) {
        Optional<Client> clientFoundByUsername = clientRepository.findByUsername(username);

        if (!clientFoundByUsername.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found.");
        }

        return clientFoundByUsername.get();
    }

}
