package t_tracker.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import t_tracker.model.*;
import t_tracker.repository.OrderRepository;
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

    @Autowired
    OrderRepository orderRepository;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> placeAnOrder(@RequestBody List<OrderDTO> productList, HttpServletRequest request)
            throws ResponseStatusException {
        Principal principal = request.getUserPrincipal();
        Client client;

        try {
            client = clientService.getClientByUsername(principal.getName());

        } catch (ResponseStatusException e) {
            return new ResponseEntity<>("Unauthorized client.", HttpStatus.FORBIDDEN);
        }

        Product orderProduct;
        Order orderPlaced = new Order(client);

        try {
            for (OrderDTO order : productList) {
                orderProduct = productService.getProduct(order.getProductId());
                orderPlaced.addProduct(new OrderItem(orderProduct, order.getQuantity()));
            }
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatus());
        }

        try {
            orderPlaced = orderService.placeAnOrder(orderPlaced);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatus());
        }

        return new ResponseEntity<>(orderPlaced, HttpStatus.OK);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<?> getClientOrders(@RequestParam int orderId, HttpServletRequest request)
            throws ResponseStatusException {
        System.out.print(orderRepository.findAll());
        return new ResponseEntity<>(orderRepository.findById(orderId), HttpStatus.OK);
    }

    @PostMapping(value = "/update/{orderId}/{status}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable(value = "orderId") int orderId,
            @PathVariable(value = "status") int status, HttpServletRequest request) throws ResponseStatusException {
        Optional<Order> order = orderRepository.findById(orderId);

        if (!order.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found.");

        orderService.updateStatus(order.get(), status);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/rate/{orderId}/{rating}")
    public ResponseEntity<?> rateOrder(@PathVariable(value = "orderId") int orderId,
            @PathVariable(value = "rating") int rating, HttpServletRequest request) throws ResponseStatusException {
        Optional<Order> order = orderRepository.findById(orderId);

        if (!order.isPresent())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found.");

        orderService.rateOrder(order.get(), rating);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
