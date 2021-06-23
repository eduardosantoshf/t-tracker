package t_tracker.controller;

import java.security.Principal;
import java.util.List;

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
    public ResponseEntity<Order> placeAnOrder(@RequestBody List<OrderDTO> productList, HttpServletRequest request)
            throws ResponseStatusException {
        Principal principal = request.getUserPrincipal();
        Client client;

        try {
            client = clientService.getClientByUsername(principal.getName());

        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized client.");
        }

        Product orderProduct;
        Order orderPlaced = new Order(client);

        try {
            for (OrderDTO order : productList) {
                orderProduct = productService.getProduct(order.getProductId());
                orderPlaced.addProduct(new OrderItem(orderProduct, order.getQuantity()));
            }
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(e.getStatus(), e.getReason());
        }

        return new ResponseEntity<>(orderService.placeAnOrder(orderPlaced), HttpStatus.OK);
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<Order> getClientOrders(@RequestParam int orderId, HttpServletRequest request)
            throws ResponseStatusException {

        return new ResponseEntity<>(orderService.getOrder(orderId), HttpStatus.OK);
    }

    @PostMapping(value = "/update/{orderId}/{status}")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable(value = "orderId") int orderId,
            @PathVariable(value = "status") int status, HttpServletRequest request) throws ResponseStatusException {
        Order order;

        try {
            order = orderService.getOrder(orderId);
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(e.getStatus(), e.getReason());
        }

        orderService.updateStatus(order, status);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/rate/{orderId}/{rating}")
    public ResponseEntity<Order> rateOrder(@PathVariable(value = "orderId") int orderId,
            @PathVariable(value = "rating") int rating, HttpServletRequest request) throws ResponseStatusException {
        Order order;

        try {
            order = orderService.getOrder(orderId);
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(e.getStatus(), e.getReason());
        }

        orderService.rateOrder(order, rating);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
