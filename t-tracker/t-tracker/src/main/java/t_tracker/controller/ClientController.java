package t_tracker.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.jsonwebtoken.JwtException;
import t_tracker.model.Client;
import t_tracker.model.Order;
import t_tracker.repository.OrderRepository;
import t_tracker.repository.StockRepository;
import t_tracker.service.ClientService;
import t_tracker.service.JwtTokenService;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private StockRepository stockRepository;
    
    @Autowired
    private OrderRepository orderRepository;

    @PostMapping(value = "/signup", consumes = "application/json")
    public ResponseEntity<?> registerClient(@RequestBody Client client, HttpServletRequest request)
            throws ResponseStatusException {
                
        Client registeredClient;

        try {
            registeredClient = clientService.registerClient(client);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }

        Client returnClientData = new Client();
        returnClientData.setName(registeredClient.getName());
        returnClientData.setUsername(registeredClient.getUsername());
        returnClientData.setEmail(registeredClient.getEmail());
        returnClientData.setPhoneNumber(registeredClient.getPhoneNumber());
        returnClientData.setHomeLocation(registeredClient.getHomeLocation());
        returnClientData.setOrderlist(registeredClient.getOrderlist());

        return new ResponseEntity<>(returnClientData, HttpStatus.OK);
    }

    @GetMapping(value = "/orders", produces = "application/json")
    public ResponseEntity<?> getClientOrders(HttpServletRequest request)
            throws ResponseStatusException {
        Principal principal = request.getUserPrincipal();
        List<Order> orders;
        System.out.println(orderRepository.findAll());
        System.out.println(stockRepository.findAll());

        try {
            orders = clientService.getOrders(principal.getName());
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatus());
        }

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping(value = "/verify", produces = "application/json")
    public String checkToken(@RequestHeader(name = "Authorization") String token, HttpServletRequest request)
            throws Exception {
        try {
            JwtTokenService.verifyToken(token);
        } catch (JwtException e) {
            throw new Exception("FAILED TO VERIFY TOKEN");
        }
        return "SUCCESS";
    }

}
