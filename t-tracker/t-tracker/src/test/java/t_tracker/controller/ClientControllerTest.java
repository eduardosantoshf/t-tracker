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
import static org.hamcrest.Matchers.hasSize;

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
import t_tracker.model.Client;
import t_tracker.model.Coordinates;
import t_tracker.model.Order;
import t_tracker.service.ClientService;
import t_tracker.service.JwtTokenService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TTrackerApplication.class)
@AutoConfigureMockMvc
class ClientControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ClientService clientService;

    private List<Order> orderList;
    private Client willy;
    String token;

    @BeforeEach
    void setUp() {
        willy = new Client("Willy Wonka", "ChocolateMan", "chocofactory@org.com", "iS2oompaloompas", 123123123, new Coordinates(12.1, 12.2));
        willy.setPhoneNumber(931313444);
        willy.setHomeLocation(new Coordinates(46.991750174814946, 15.907980069174572));
        willy.setId(1);
        
        orderList = new ArrayList<>(Arrays.asList(new Order(willy, new Coordinates(1.0, 2.0), new Coordinates(1.1, 2.1), 49.99, new ArrayList<>())));

        token = JwtTokenService.generateToken(willy.getUsername(), new DefaultClaims());
    }

    // @AfterEach
    // void cleanUp() {
    //     reset(clientService);
    // }

    @Test
    void whenSignupNewClient_thenReturnClientAnd200() throws Exception {
        when( clientService.registerClient(willy) ).thenReturn(willy);

        mvc.perform( post("/client/signup").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(willy)) )
            .andExpect( status().isOk() )
            .andExpect( jsonPath("$.name", is(willy.getName())) )
            .andExpect( jsonPath("$.username", is(willy.getUsername())) )
            .andExpect( jsonPath("$.email", is(willy.getEmail())) )
            .andExpect( jsonPath("$.username", is(willy.getUsername())) )
            .andExpect( jsonPath("$.phoneNumber", is(willy.getPhoneNumber())) );

    }

    @Test
    void whenSignupDuplicateClient_ThenReturnNullAnd409() throws Exception {
        when( clientService.registerClient(any()) ).thenThrow( ResponseStatusException.class );

        mvc.perform( post("/client/signup").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(willy)) )
            .andExpect( status().isConflict() );
    
    }

    @Test
    void whenGetClientOrder_thenReturnOrderList() throws Exception {
        when(clientService.getOrders(any())).thenReturn(orderList);
        
        mvc.perform( get("/client/orders").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(willy)) )
            .andExpect( status().isOk() )
            .andExpect( jsonPath("$.*", hasSize(1)) )
            .andExpect( jsonPath("$.[0].clientId", is(willy.getId())) )
            .andExpect( jsonPath("$.[0].pickupLocation.latitude", is(1.0)) )
            .andExpect( jsonPath("$.[0].pickupLocation.longitude", is(2.0)) )
            .andExpect( jsonPath("$.[0].deliverLocation.latitude", is(1.1)) )
            .andExpect( jsonPath("$.[0].deliverLocation.longitude", is(2.1)) )
            .andExpect( jsonPath("$.[0].orderTotal", is(49.99)) );
    }

    @Test
    void whenGetInvalidClientOrder_thenReturn404() throws Exception {
        when(clientService.getOrders(any())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found."));
        
        mvc.perform( get("/client/orders").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(willy)) )
            .andExpect( status().isNotFound() )
            .andExpect( jsonPath("$", is("Client not found.")) );
    }

    @Test
    void whenVerifyValidToken_thenReturnSuccess() throws Exception {
        mvc.perform( get("/client/verify").header("Authorization", "Bearer " + token) )
            .andExpect( jsonPath("$", is("SUCCESS")) );

    }

    @Test
    void whenVerifyInvalidToken_thenReturnFailure() throws Exception {
        mvc.perform( get("/client/verify").header("Authorization", "Bearer eyJ0eXAiOiJKV2QiLCJhbGciOiJIUzUxMiJ9.eyJhdXRdb3JpdGllcyI6W10sInN1YiI6IlVavXJuYW1lZWUiLCJleHAiOjE2MjQ0MTU2Njl9.rh7nmld82KuT_lrCp0f8oRizDz25Jf_LvYMDB-c41R5dSA0-SyVpyPV3s9IPORuTh31gEjFMynKJtu2lyjHkGg") )
        .andExpect(status().isUnauthorized());

    }

}
