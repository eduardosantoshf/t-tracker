package t_tracker.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import t_tracker.model.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @PostMapping(value = "/", consumes = "application/json")
    public ResponseEntity<Order> placeAnOrder(@RequestBody Order order, HttpServletRequest request) throws Exception {

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
