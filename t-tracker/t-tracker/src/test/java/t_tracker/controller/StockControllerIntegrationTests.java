package t_tracker.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;

import io.jsonwebtoken.impl.DefaultClaims;
import t_tracker.JsonUtil;
import t_tracker.TTrackerApplication;
import t_tracker.model.Client;
import t_tracker.model.Coordinates;
import t_tracker.model.Product;
import t_tracker.model.Stock;
import t_tracker.repository.ClientRepository;
import t_tracker.repository.CoordinatesRepository;
import t_tracker.repository.ProductRepository;
import t_tracker.repository.StockRepository;
import t_tracker.service.JwtTokenService;
import t_tracker.service.StockServiceImpl;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TTrackerApplication.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
class StockControllerIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CoordinatesRepository coordinatesRepository;

    @Autowired
    private ClientRepository clientRepository;

    private Stock testStock1, testStock2;
    private Product testProduct1, testProduct2;
    private String token;

    
    @BeforeEach
    void setUp() {
        token = JwtTokenService.generateToken("ClientUsername", new DefaultClaims());
        Coordinates home = new Coordinates(1.0, 2.0);
        coordinatesRepository.save(home);
        Client testClient = new Client("Test Name", "ClientUsername", "test@email.com", "123", 123123123, home);
        clientRepository.save(testClient);

        testProduct1 = new Product("Covid Test", 49.99, "dna", "DNA testing for covid-19.");
        testProduct2 = new Product("The Best Covid Test", 999.99, "soul", "Testing your soul for traces of covid-19.");

        testStock1 = new Stock(testProduct1, 10);
        testStock2 = new Stock(testProduct2, 3);

    }

    @Test
    void whenGetLabStock_thenReturnValidStock() throws Exception {
        productRepository.save(testProduct1);
        productRepository.save(testProduct2);
        stockRepository.save(testStock1);
        stockRepository.save(testStock2);
        stockRepository.flush();

        mvc.perform( get("/stock").contentType(MediaType.APPLICATION_JSON) )
            .andExpect( status().isOk() )
            .andExpect( jsonPath("$.*", hasSize(2)) )
            .andExpect( jsonPath("$[*].product.name", containsInAnyOrder(testProduct1.getName(), testProduct2.getName())) )
            .andExpect( jsonPath("$[*].product.type", containsInAnyOrder(testProduct1.getType(), testProduct2.getType())) )
            .andExpect( jsonPath("$[*].product.price", containsInAnyOrder(testProduct1.getPrice(), testProduct2.getPrice())) )
            .andExpect( jsonPath("$[*].product.description", containsInAnyOrder(testProduct1.getDescription(), testProduct2.getDescription())) )
            .andExpect( jsonPath("$[*].quantity", containsInAnyOrder(testStock1.getQuantity(), testStock2.getQuantity())) );

    }

    @Test
    void whenAddExistingStock_thenReturnFullStock() throws Exception {
        productRepository.save(testProduct1);
        stockRepository.save(testStock1);
        stockRepository.flush();

        Stock stockToAdd = new Stock(testProduct1, 5);

        mvc.perform( post("/stock/add").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(stockToAdd)) )
            .andExpect( status().isOk() )
            .andExpect( jsonPath("$.*", hasSize(1)) )
            .andExpect( jsonPath("$[*].product.name", containsInAnyOrder(testProduct1.getName())) )
            .andExpect( jsonPath("$[*].product.type", containsInAnyOrder(testProduct1.getType())) )
            .andExpect( jsonPath("$[*].product.price", containsInAnyOrder(testProduct1.getPrice())) )
            .andExpect( jsonPath("$[*].product.description", containsInAnyOrder(testProduct1.getDescription())) )
            .andExpect( jsonPath("$[*].quantity", containsInAnyOrder(testStock1.getQuantity()+stockToAdd.getQuantity())) );

    }

    @Test
    void whenAddInexistantStock_thenReturnFullStock() throws Exception {

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
        productRepository.save(testProduct1);
        stockRepository.save(testStock1);
        stockRepository.flush();

        mvc.perform( post("/stock/remove").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(testStock1)) )
            .andExpect( status().isOk() )
            .andExpect( jsonPath("$.*", hasSize(1)) )
            .andExpect( jsonPath("$[*].product.name", containsInAnyOrder(testProduct1.getName())) )
            .andExpect( jsonPath("$[*].product.type", containsInAnyOrder(testProduct1.getType())) )
            .andExpect( jsonPath("$[*].product.price", containsInAnyOrder(testProduct1.getPrice())) )
            .andExpect( jsonPath("$[*].product.description", containsInAnyOrder(testProduct1.getDescription())) )
            .andExpect( jsonPath("$[*].quantity", containsInAnyOrder(0)) );

    }

    @Test
    void whenRemoveStockWithNotEnoughQuantity_thenReturn409() throws Exception {
        productRepository.save(testProduct1);
        stockRepository.save(testStock1);
        stockRepository.flush();

        Stock stockToRemove = new Stock(testProduct1, 50);

        mvc.perform( post("/stock/remove").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(stockToRemove)) )
            .andExpect( status().isConflict() )
            .andExpect(status().reason(containsString("Not enough stock to process request.")));

    }

    @Test
    void whenRemoveStockWithUnknownProduct_thenReturn404() throws Exception {
        productRepository.save(testProduct1);
        stockRepository.save(testStock1);
        stockRepository.flush();

        Stock stockToRemove = new Stock(new Product("name", 1.1, "type", "descr"), 5);

        mvc.perform( post("/stock/remove").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(stockToRemove)) )
            .andExpect( status().isNotFound() )
            .andExpect(status().reason(containsString("Product not Found.")));

    }

}
