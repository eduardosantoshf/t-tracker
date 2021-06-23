package t_tracker.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;

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
class OrderControllerTest {

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
        client = new Client("Client Name", "ClientUsername", "email@org.com", "password1234", 123321231,
                new Coordinates(1.2, 1.3));
        client.setId(1);
        product = new Product("Covid Test 1", 49.99, "Infrared Test", "Very nice test.");
        resultOrder = new Order(client, new Coordinates(1.23456, 2.34567), new Coordinates(1.23457, 2.34566), 49.99,
                new ArrayList<>(Arrays.asList(new OrderItem(product, 3))));
    }

    @Test
    void whenPlaceAValidOrder_thenReturnValidOrder() throws Exception {
        when(clientService.getClientByUsername(client.getUsername())).thenReturn(client);
        when(productService.getProduct(1)).thenReturn(product);
        when(orderService.placeAnOrder(resultOrder)).thenReturn(resultOrder);

        mvc.perform(post("/order").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(testOrder))).andExpect(status().isOk());
    }

    @Test
    void whenPlaceOrderWithInvalidClient_thenReturn404() throws Exception {
        when(clientService.getClientByUsername(client.getUsername()))
                .thenThrow(new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized client."));

        mvc.perform(post("/order").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(testOrder))).andExpect(status().isForbidden());

    }

    @Test
    void whenPlaceOrderWithInvalidProduct_thenReturn404() throws Exception {
        when(clientService.getClientByUsername(client.getUsername())).thenReturn(client);
        when(productService.getProduct(1))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found."));

        mvc.perform(post("/order").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(testOrder))).andExpect(status().isNotFound());

    }

    @Test
    void whenPlaceInvalidOrder_thenReturn409() throws Exception {
        when(clientService.getClientByUsername(client.getUsername())).thenReturn(client);
        when(productService.getProduct(1)).thenReturn(product);
        when(orderService.placeAnOrder(any())).thenThrow(new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Error getting response from drivers api."));

        mvc.perform(post("/order").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.toJson(testOrder))).andExpect(status().isBadGateway())
                .andExpect( status().reason(containsString("Error getting response from drivers api.")) );
    }

    @Test
    void whenGetOrderByValidId_thenReturnOrder() throws Exception {
        when(orderService.getOrder(1)).thenReturn(resultOrder);

        mvc.perform(get("/order?orderId=" + 1).header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON))
            .andExpect( status().isOk() )
            .andExpect( jsonPath("$.clientId", is(resultOrder.getClientId())) )
            .andExpect( jsonPath("$.totalPrice", is(resultOrder.getTotalPrice())) )
            .andExpect( jsonPath("$.pickupLocation.latitude", is(resultOrder.getPickupLocation().getLatitude())) )
            .andExpect( jsonPath("$.pickupLocation.longitude", is(resultOrder.getPickupLocation().getLongitude())) )
            .andExpect( jsonPath("$.deliverLocation.latitude", is(resultOrder.getDeliverLocation().getLatitude())) )
            .andExpect( jsonPath("$.deliverLocation.latitude", is(resultOrder.getDeliverLocation().getLatitude())) );
    }

    @Test
    void whenGetOrderByInvalidId_thenReturn404() throws Exception {
        when(orderService.getOrder(1)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found."));

        mvc.perform(get("/order?orderId=" + 1).header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON))
        .andExpect( status().isNotFound() );
    }

    @Test
    void whenUpdateValidOrder_thenReturnOk() throws Exception {
        when(orderService.getOrder(1)).thenReturn(resultOrder);

        mvc.perform(post("/order/update/" + 1 + "/" + 2).header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON))
            .andExpect( status().isOk() );
    }

    @Test
    void whenUpdateInvalidOrder_thenReturn404() throws Exception {
        when(orderService.getOrder(1)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found."));

        mvc.perform(post("/order/update/" + 1 + "/" + 2).header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON))
            .andExpect( status().isNotFound() );
    }

    @Test
    void whenRateValidOrder_thenReturnOk() throws Exception {
        when(orderService.getOrder(1)).thenReturn(resultOrder);

        mvc.perform(post("/order/rate/" + 1 + "/" + 2).header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON))
            .andExpect( status().isOk() );
    }

    @Test
    void whenRateInvalidOrder_thenReturn404() throws Exception {
        when(orderService.getOrder(1)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found."));

        mvc.perform(post("/order/rate/" + 1 + "/" + 2).header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON))
            .andExpect( status().isNotFound() );
    }

}
