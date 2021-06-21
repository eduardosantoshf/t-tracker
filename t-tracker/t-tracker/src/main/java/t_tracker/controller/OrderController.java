package t_tracker.controller;

import java.security.Principal;
import java.util.List;

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
    public ResponseEntity<?> placeAnOrder(@RequestBody List<OrderDTO> productList, HttpServletRequest request) throws ResponseStatusException {
        Principal principal = request.getUserPrincipal();
        Client client;

        try {
            client = clientService.getClientByUsername(principal.getName());

        } catch(ResponseStatusException e) {
            return new ResponseEntity<String>("Unauthorized client.", HttpStatus.FORBIDDEN);
        }

        Product orderProduct;
        Order orderPlaced = new Order(client);

        try {
            for (OrderDTO order : productList) {
                orderProduct = productService.getProduct(order.getProductId());
                orderPlaced.addProduct(new Stock(orderProduct, order.getQuantity()));
            }
        } catch( ResponseStatusException e ) {
            return new ResponseEntity<>(e.getReason(), e.getStatus());
        }

        try {
            orderPlaced = orderService.placeAnOrder(orderPlaced);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatus());
        }

        return new ResponseEntity<>(orderPlaced, HttpStatus.OK);
    }

}
