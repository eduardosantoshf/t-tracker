// package t_tracker.controller;

// import org.junit.jupiter.api.BeforeEach;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
// import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.test.annotation.DirtiesContext;
// import org.springframework.test.annotation.DirtiesContext.ClassMode;
// import org.springframework.test.web.servlet.MockMvc;

// import io.jsonwebtoken.impl.DefaultClaims;
// import t_tracker.TTrackerApplication;
// import t_tracker.model.Product;
// import t_tracker.model.Stock;
// import t_tracker.repository.CoordinatesRepository;
// import t_tracker.repository.LabRepository;
// import t_tracker.service.JwtTokenService;
// import t_tracker.service.LabService;

// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TTrackerApplication.class)
// @AutoConfigureMockMvc
// @DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
// @AutoConfigureTestDatabase(replace = Replace.ANY)
// class LabControllerIntegrationTests {

//     @Autowired
//     private MockMvc mvc;

//     @MockBean
//     private LabService labService;

//     @Autowired
//     private LabRepository labRepository;

//     @Autowired
//     private CoordinatesRepository coordinatesRepository;

//     private Stock testStock1, testStock2;
//     private Product testProduct1, testProduct2;
//     private String token;
    
//     @BeforeEach
//     void setUp() {
//         token = JwtTokenService.generateToken("ClientUsername", new DefaultClaims());
//         testProduct1 = new Product("Covid Test", 49.99, "dna", "DNA testing for covid-19.");
//         testProduct2 = new Product("The Best Covid Test", 999.99, "soul", "Testing your soul for traces of covid-19.");

//         testStock1 = new Stock(testProduct1, 10);
//         testStock2 = new Stock(testProduct2, 3);
//     }

//     // @Test
//     // void whenGetLabStock_thenReturnValidStock() throws Exception {
//     //     Coordinates coord = new Coordinates(10.1, 10.2);
        
//     //     Lab newLab = new Lab(1, "testtoken", "CovidLab", coordinatesRepository.save(coord));
//     //     labRepository.save(newLab);

//     //     System.out.println("AAAAAAAAAAAAAAAAAa");
//     //     System.out.print(labRepository.findAll());
//     //     labService.addStockToLab(testStock1);
//     //     labService.addStockToLab(testStock2);

//     //     System.out.println(labRepository.findAll());

//     //     mvc.perform( get("/lab/stock").header("Authorization", "Bearer " + token) )
//     //         .andExpect( status().isOk() )
//     //         .andExpect( jsonPath("$.*", hasSize(2)) )
//     //         .andExpect( jsonPath("$[*].product.name", containsInAnyOrder(testProduct1.getName(), testProduct2.getName())) )
//     //         .andExpect( jsonPath("$[*].product.type", containsInAnyOrder(testProduct1.getType(), testProduct2.getType())) )
//     //         .andExpect( jsonPath("$[*].product.price", containsInAnyOrder(testProduct1.getPrice(), testProduct2.getPrice())) )
//     //         .andExpect( jsonPath("$[*].product.description", containsInAnyOrder(testProduct1.getDescription(), testProduct2.getDescription())) )
//     //         .andExpect( jsonPath("$[*].quantity", containsInAnyOrder(testStock1.getQuantity(), testStock2.getQuantity())) );

//     // }

//     // @Test
//     // void whenAddStock_thenReturnFullStock() throws Exception {
//     //     Coordinates coord = new Coordinates(10.1, 10.2);
        
//     //     Lab newLab = new Lab(1, "testtoken", "CovidLab", coordinatesRepository.save(coord));
//     //     labRepository.save(newLab);

//     //     mvc.perform( post("/lab/stock/add").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(testStock1)) )
//     //         .andExpect( status().isOk() )
//     //         .andExpect( jsonPath("$.*", hasSize(1)) )
//     //         .andExpect( jsonPath("$[*].product.name", containsInAnyOrder(testProduct1.getName())) )
//     //         .andExpect( jsonPath("$[*].product.type", containsInAnyOrder(testProduct1.getType())) )
//     //         .andExpect( jsonPath("$[*].product.price", containsInAnyOrder(testProduct1.getPrice())) )
//     //         .andExpect( jsonPath("$[*].product.description", containsInAnyOrder(testProduct1.getDescription())) )
//     //         .andExpect( jsonPath("$[*].quantity", containsInAnyOrder(testStock1.getQuantity())) );

//     // }

// }
