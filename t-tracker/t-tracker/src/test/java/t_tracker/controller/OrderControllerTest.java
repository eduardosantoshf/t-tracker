package t_tracker.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.core.Is.is;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import io.jsonwebtoken.impl.DefaultClaims;
import t_tracker.JsonUtil;
import t_tracker.TTrackerApplication;
import t_tracker.model.*;
import t_tracker.service.JwtTokenService;
import t_tracker.service.OrderService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TTrackerApplication.class)
@AutoConfigureMockMvc
public class OrderControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderService orderService;

    private Order testOrder;
    private String token;

    @BeforeEach
    void setUp() {
        token = JwtTokenService.generateToken("testtoken", new DefaultClaims());
        
        testOrder = new Order(
            1,
            new Coordinates(1.23456, 2.34567),
            new Coordinates(1.23457, 2.34566),
            49.99,
            1,
            new ArrayList<>(Arrays.asList(new Stock(new Product("Covid Test 1", 49.99, "Infrared Test", "Very nice test."), 1)))
        );
    }

    @Test
    void whenPlaceAValidOrder_thenReturnValidOrder() throws Exception {
        when( orderService.placeAnOrder(testOrder) ).thenReturn(testOrder);

        mvc.perform( post("/order").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(testOrder)) )
            .andExpect( status().isOk() )
            .andExpect( jsonPath("$.clientUsername", is(testOrder.getClientId())) )
            .andExpect( jsonPath("$.pickupLocation.latitude", is(testOrder.getPickupLocation().getLatitude())) )
            .andExpect( jsonPath("$.pickupLocation.longitude", is(testOrder.getPickupLocation().getLongitude())) )
            .andExpect( jsonPath("$.deliverLocation.latitude", is(testOrder.getDeliverLocation().getLatitude())) )
            .andExpect( jsonPath("$.deliverLocation.longitude", is(testOrder.getDeliverLocation().getLongitude())) )
            .andExpect( jsonPath("$.orderTotal", is(testOrder.getOrderTotal())) );
    }

    @Test
    void whenPlaceOrderWithInvalidClient_thenReturn404() throws Exception {
        when( orderService.placeAnOrder(any(Order.class)) ).thenThrow( new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found."));

        mvc.perform( post("/order").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(testOrder)) )
            .andExpect( status().isNotFound() );
    
    }

}
