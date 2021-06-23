package t_tracker.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;

import io.jsonwebtoken.impl.DefaultClaims;
import t_tracker.TTrackerApplication;
import t_tracker.model.Product;
import t_tracker.repository.ProductRepository;
import t_tracker.service.JwtTokenService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TTrackerApplication.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = Replace.ANY)
class ProductControllerIntegrationTests {
    
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ProductRepository productRepository;

    private Product testProduct1, testProduct2;
    private String token;

    @BeforeEach
    void setUp() {
        token = JwtTokenService.generateToken("ClientUsername", new DefaultClaims());
        testProduct1 = new Product("Covid Test", 49.99, "dna", "DNA testing for covid-19.");
        testProduct2 = new Product("The Best Covid Test", 999.99, "soul", "Testing your soul for traces of covid-19.");

        productRepository.save(testProduct1);
        productRepository.save(testProduct2);
        productRepository.flush();
    }

    @Test
    void whenGetProductWithValidId_thenReturnProductAnd200() throws Exception {

        mvc.perform( get("/product/" + testProduct1.getId()).header("Authorization", "Bearer " + token) )
            .andExpect( status().isOk() )
            .andExpect( jsonPath("$.name", is(testProduct1.getName())) )
            .andExpect( jsonPath("$.price", is(testProduct1.getPrice())) )
            .andExpect( jsonPath("$.type", is(testProduct1.getType())) )
            .andExpect( jsonPath("$.description", is(testProduct1.getDescription())) );
    }

    @Test
    void whenGetProductWithInvalidId_thenReturn404() throws Exception {
        int invalidId = 9999;

        mvc.perform( get("/product/" + invalidId).header("Authorization", "Bearer " + token) )
            .andExpect( status().isNotFound() )
            .andExpect( status().reason(containsString("Product not found.")) );
    }

    @Test
    void whenGetAllProducts_thenReturnListOfProducts() throws Exception {

        mvc.perform( get("/product/all").header("Authorization", "Bearer " + token) )
            .andExpect( status().isOk() )
            .andExpect( jsonPath("$[*].name", containsInAnyOrder(testProduct1.getName(), testProduct2.getName())) )
            .andExpect( jsonPath("$[*].price", containsInAnyOrder(testProduct1.getPrice(), testProduct2.getPrice())) )
            .andExpect( jsonPath("$[*].type", containsInAnyOrder(testProduct1.getType(), testProduct2.getType())) )
            .andExpect( jsonPath("$[*].description", containsInAnyOrder(testProduct1.getDescription(), testProduct2.getDescription())) );
    }

}
