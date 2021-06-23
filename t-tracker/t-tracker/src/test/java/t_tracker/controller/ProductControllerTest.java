package t_tracker.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import io.jsonwebtoken.impl.DefaultClaims;
import t_tracker.JsonUtil;
import t_tracker.TTrackerApplication;
import t_tracker.model.Product;
import t_tracker.service.JwtTokenService;
import t_tracker.service.ProductService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TTrackerApplication.class)
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService productService;

    private Product testProduct1, testProduct2;
    private String token;

    @BeforeEach
    void setUp() {
        token = JwtTokenService.generateToken("ClientUsername", new DefaultClaims());
        testProduct1 = new Product("Covid Test", 49.99, "dna", "DNA testing for covid-19.");
        testProduct2 = new Product("The Best Covid Test", 999.99, "soul", "Testing your soul for traces of covid-19.");
    }

    @Test
    void whenRegisterNewValidProduct_thenReturnProduct() throws Exception {
        when(productService.registerProduct(testProduct1)).thenReturn(testProduct1);

        mvc.perform(post("/product").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(testProduct1)))
                .andExpect( status().isOk() )
                .andExpect(jsonPath("$.name", is(testProduct1.getName())))
                .andExpect(jsonPath("$.price", is(testProduct1.getPrice())))
                .andExpect(jsonPath("$.type", is(testProduct1.getType())))
                .andExpect(jsonPath("$.description", is(testProduct1.getDescription())));
    }

    @Test
    void whenRegisterNewInvalidProduct_thenReturn409() throws Exception {
        when(productService.registerProduct(testProduct1)).thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Failed to register product."));

        mvc.perform(post("/product").header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(testProduct1)))
                .andExpect( status().isConflict() )
                .andExpect( status().reason(containsString("Failed to register product.")) );
    }

    @Test
    void whenGetProductWithValidId_thenReturnProductAnd200() throws Exception {
        int validId = 1;
        when(productService.getProduct(validId)).thenReturn(testProduct1);

        mvc.perform(get("/product/" + validId).header("Authorization", "Bearer " + token))
                .andExpect(status().isOk()).andExpect(jsonPath("$.name", is(testProduct1.getName())))
                .andExpect(jsonPath("$.price", is(testProduct1.getPrice())))
                .andExpect(jsonPath("$.type", is(testProduct1.getType())))
                .andExpect(jsonPath("$.description", is(testProduct1.getDescription())));
    }

    @Test
    void whenGetProductWithInvalidId_thenReturn404() throws Exception {
        int invalidId = 9999;
        when(productService.getProduct(invalidId))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found."));

        mvc.perform(get("/product/" + invalidId).header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect( status().reason(containsString("Product not found.")) );
    }

    @Test
    void whenGetAllProducts_thenReturnListOfProducts() throws Exception {
        when(productService.getAllProducts()).thenReturn(new ArrayList<>(Arrays.asList(testProduct1, testProduct2)));

        mvc.perform(get("/product/all").header("Authorization", "Bearer " + token)).andExpect(status().isOk())
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(testProduct1.getName(), testProduct2.getName())))
                .andExpect(jsonPath("$[*].price", containsInAnyOrder(testProduct1.getPrice(), testProduct2.getPrice())))
                .andExpect(jsonPath("$[*].type", containsInAnyOrder(testProduct1.getType(), testProduct2.getType())))
                .andExpect(jsonPath("$[*].description",
                        containsInAnyOrder(testProduct1.getDescription(), testProduct2.getDescription())));
    }

}
