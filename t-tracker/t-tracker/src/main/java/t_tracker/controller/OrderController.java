package t_tracker.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import t_tracker.model.*;
import t_tracker.repository.ClientRepository;
import t_tracker.service.ClientService;
import t_tracker.service.OrderService;
import t_tracker.service.ProductService;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    ProductService productService;

    @Autowired
    ClientService clientService;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> placeAnOrder(@RequestBody List<OrderDTO> productList, HttpServletRequest request) throws Exception {
        Principal principal = request.getUserPrincipal();
        Client client;
        System.out.println("Cheeckkk1");
        try {
            System.out.println(principal.getName());
            client = clientService.getClientByUsername(principal.getName());
            System.out.println(client);
        } catch(ResponseStatusException e) {
            return new ResponseEntity<String>("Unauthorized client.", HttpStatus.FORBIDDEN);
        }
        System.out.println("Cheeckk2");
        Product orderProduct;
        Order orderPlaced = new Order(client);
        System.out.println("Cheeckk2.5");
        try {
            for (OrderDTO order : productList) {
                System.out.println(order);
                orderProduct = productService.getProduct(order.getProductId());
                orderPlaced.addProduct(new Stock(orderProduct, order.getQuantity()));
            }
        } catch( ResponseStatusException e ) {
            return new ResponseEntity<>(e.getReason(), e.getStatus());
        }
        System.out.println("Cheeckk3");
        try {
            orderPlaced = orderService.placeAnOrder(orderPlaced);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatus());
        }
        System.out.println("Cheeckk4");
        return new ResponseEntity<>(orderPlaced, HttpStatus.OK);
    }

}
