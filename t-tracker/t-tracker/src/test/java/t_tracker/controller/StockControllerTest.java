package t_tracker.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import io.jsonwebtoken.impl.DefaultClaims;
import t_tracker.JsonUtil;
import t_tracker.TTrackerApplication;
import t_tracker.model.Product;
import t_tracker.model.Stock;
import t_tracker.service.JwtTokenService;
import t_tracker.service.StockServiceImpl;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TTrackerApplication.class)
@AutoConfigureMockMvc
class StockControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private StockServiceImpl stockService;

    private Stock testStock1, testStock2;
    private Product testProduct1, testProduct2;
    private String token;
    
    @BeforeEach
    void setUp() {
        token = JwtTokenService.generateToken("ClientUsername", new DefaultClaims());
        testProduct1 = new Product("Covid Test", 49.99, "dna", "DNA testing for covid-19.");
        testProduct2 = new Product("The Best Covid Test", 999.99, "soul", "Testing your soul for traces of covid-19.");

        testStock1 = new Stock(testProduct1, 10);
        testStock2 = new Stock(testProduct2, 3);
    }

    @Test
    void whenGetLabStock_thenReturnValidStock() throws Exception {
        when( stockService.getStock() ).thenReturn(new ArrayList<>(Arrays.asList(testStock1, testStock2)));

        mvc.perform( get("/stock").header("Authorization", "Bearer " + token) )
            .andExpect( status().isOk() )
            .andExpect( jsonPath("$.*", hasSize(2)) )
            .andExpect( jsonPath("$[*].product.name", containsInAnyOrder(testProduct1.getName(), testProduct2.getName())) )
            .andExpect( jsonPath("$[*].product.type", containsInAnyOrder(testProduct1.getType(), testProduct2.getType())) )
            .andExpect( jsonPath("$[*].product.price", containsInAnyOrder(testProduct1.getPrice(), testProduct2.getPrice())) )
            .andExpect( jsonPath("$[*].product.description", containsInAnyOrder(testProduct1.getDescription(), testProduct2.getDescription())) )
            .andExpect( jsonPath("$[*].quantity", containsInAnyOrder(testStock1.getQuantity(), testStock2.getQuantity())) );

    }

    @Test
    void whenAddStock_thenReturnFullStock() throws Exception {
        when( stockService.addStock(testStock1.getProduct(), testStock1.getQuantity()) ).thenReturn(new ArrayList<>(Arrays.asList(testStock1)));

        mvc.perform( post("/stock/add").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(testStock1)) )
            .andExpect( status().isOk() )
            .andExpect( jsonPath("$.*", hasSize(1)) )
            .andExpect( jsonPath("$[*].product.name", containsInAnyOrder(testProduct1.getName())) )
            .andExpect( jsonPath("$[*].product.type", containsInAnyOrder(testProduct1.getType())) )
            .andExpect( jsonPath("$[*].product.price", containsInAnyOrder(testProduct1.getPrice())) )
            .andExpect( jsonPath("$[*].product.description", containsInAnyOrder(testProduct1.getDescription())) )
            .andExpect( jsonPath("$[*].quantity", containsInAnyOrder(testStock1.getQuantity())) );

    }

    @Test
    void whenRemoveStock_thenReturnResultingStock() throws Exception {
        when( stockService.removeStock(testStock1.getProduct(), testStock1.getQuantity()) ).thenReturn(new ArrayList<>(Arrays.asList(testStock1)));

        mvc.perform( post("/stock/remove").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(testStock1)) )
            .andExpect( status().isOk() )
            .andExpect( jsonPath("$.*", hasSize(1)) )
            .andExpect( jsonPath("$[*].product.name", containsInAnyOrder(testProduct1.getName())) )
            .andExpect( jsonPath("$[*].product.type", containsInAnyOrder(testProduct1.getType())) )
            .andExpect( jsonPath("$[*].product.price", containsInAnyOrder(testProduct1.getPrice())) )
            .andExpect( jsonPath("$[*].product.description", containsInAnyOrder(testProduct1.getDescription())) )
            .andExpect( jsonPath("$[*].quantity", containsInAnyOrder(testStock1.getQuantity())) );

    }

}
