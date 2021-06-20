package t_tracker.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import t_tracker.service.ClientService;
import t_tracker.service.JwtTokenService;
import t_tracker.service.OrderService;
import t_tracker.service.ProductService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TTrackerApplication.class)
@AutoConfigureMockMvc
public class OrderControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private ProductService productService;

    @MockBean
    private ClientService clientService;

    private List<OrderDTO> testOrder;
    private Order resultOrder;
    private Product product;
    private Client client;
    private String token;

    @BeforeEach
    void setUp() {
        token = JwtTokenService.generateToken("ClientUsername", new DefaultClaims());
        testOrder = new ArrayList<OrderDTO>(Arrays.asList(new OrderDTO(1, 3)));
        client = new Client("Client Name", "ClientUsername", "email@org.com", "password1234", 123321231, new Coordinates(1.2, 1.3));
        client.setId(1);
        product = new Product("Covid Test 1", 49.99, "Infrared Test", "Very nice test.");
        resultOrder = new Order(
            1,
            new Coordinates(1.23456, 2.34567),
            new Coordinates(1.23457, 2.34566),
            49.99,
            1,
            new ArrayList<>(Arrays.asList(new Stock(product, 1)))
        );
    }

    @Test
    void whenPlaceAValidOrder_thenReturnValidOrder() throws Exception {
        when( orderService.placeAnOrder(resultOrder) ).thenReturn(resultOrder);
        when( productService.getProduct(product.getId()) ).thenReturn(product);
        when( clientService.getClientByUsername(client.getUsername()) ).thenReturn(client);

        mvc.perform( post("/order").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(testOrder)) )
            .andExpect( status().isOk() );
    }

    @Test
    void whenPlaceOrderWithInvalidClient_thenReturn404() throws Exception {
        when( clientService.getClientByUsername(client.getUsername()) ).thenThrow( new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found."));
        when( productService.getProduct(product.getId()) ).thenReturn(product);
        when( orderService.placeAnOrder(any(Order.class)) ).thenThrow( new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found."));

        mvc.perform( post("/order").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(testOrder)) )
            .andExpect( status().isForbidden() );
    
    }

}
