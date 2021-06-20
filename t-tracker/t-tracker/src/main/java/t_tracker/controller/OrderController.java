package t_tracker.controller;

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
import t_tracker.service.OrderService;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> placeAnOrder(@RequestBody Order order, HttpServletRequest request) throws Exception {
        Order orderPlaced;

        try {
            orderPlaced = orderService.placeAnOrder(order);
        } catch (ResponseStatusException e) {
            return new ResponseEntity<>(e.getReason(), e.getStatus());
        }

        return new ResponseEntity<>(orderPlaced, HttpStatus.OK);
    }

}
