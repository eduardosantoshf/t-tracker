package t_tracker.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import t_tracker.model.*;
import t_tracker.repository.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceUnitTests {

    @Mock(lenient = true)
    private OrderRepository orderRepository;

    @Mock(lenient = true)
    private LabRepository labRepository;

    @Mock(lenient = true)
    private ClientRepository clientRepository;

    @Mock(lenient = true)
    private CoordinatesRepository coordRepository;

    @Mock(lenient = true)
    private StockRepository stockRepository;

    @Mock(lenient = true)
    private ProductRepository productRepository;

    @Mock(lenient = true)
    private RestTemplate restTemplate;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order testOrder1, testOrder2;
    private Lab pickupLab;

    @BeforeEach
    void setUp() {
        Coordinates deliverLocation = new Coordinates(1.1112, 1.1112);
        // Start of order building section //
        Client orderClient = new Client("Client Name", "ClientUsername", "email@org.com", "password1234", 123321231, deliverLocation);

        Coordinates pickupLocation = new Coordinates(1.1111, 1.1111);
        
        Product product1 = new Product("Covid Test 1", 49.99, "Infrared Test", "Infrared test description.");
        Product product2 = new Product("Covid Test 2", 99.99, "Molecular Test", "Molecular test description.");

        Stock labStock1 = new Stock(product1, 3);
        Stock labStock2 = new Stock(product2, 4);
        List<Stock> pickupLabFullStock = new ArrayList<>(Arrays.asList(labStock1, labStock2));
        pickupLab = new Lab(1, "labtoken", "CovidTestsDeliveries", new Coordinates(1.0, 2.0));
        pickupLab.setStocks(pickupLabFullStock);

        Stock orderStock1 = new Stock(product1, 2);
        orderStock1.setLab(pickupLab);
        Stock orderStock2 = new Stock(product2, 1);
        orderStock2.setLab(pickupLab);
        Stock orderStock3 = new Stock(product2, 5);
        orderStock3.setLab(pickupLab);
        List<Stock> listOfOrderProducts1 = new ArrayList<>(Arrays.asList(orderStock1, orderStock2));
        List<Stock> listOfOrderProducts2 = new ArrayList<>(Arrays.asList(orderStock1, orderStock3));

        Double orderTotal = orderStock1.getTotalPrice() + orderStock2.getTotalPrice();

        testOrder1 = new Order(1, pickupLocation, deliverLocation, orderTotal,
                pickupLab.getId(), listOfOrderProducts1);
        testOrder2 = new Order(1, pickupLocation, deliverLocation, orderTotal,
                pickupLab.getId(), listOfOrderProducts2);
        
        // Start of repository mocks //
        Mockito.when(labRepository.findAll()).thenReturn(new ArrayList<>(Arrays.asList(pickupLab)));
        
        Mockito.when(clientRepository.findById(testOrder1.getClientId()))
                .thenReturn(Optional.of(orderClient));
        
        Mockito.when(orderRepository.save(testOrder1)).thenReturn(testOrder1);

        Mockito.when(productRepository.findByNameAndPriceAndType(
            orderStock1.getProduct().getName(), orderStock1.getProduct().getPrice(),
            orderStock1.getProduct().getType())).thenReturn(Optional.of(orderStock1.getProduct()));
        Mockito.when(productRepository.findByNameAndPriceAndType(
            orderStock2.getProduct().getName(), orderStock2.getProduct().getPrice(),
            orderStock2.getProduct().getType())).thenReturn(Optional.of(orderStock2.getProduct()));
        
        // Start of store signup mocks, by mocking rest template //
        
        String labInfo = "{\"name\":\"CovidTestsDeliveries\",\"ownerName\":\"TqsG101\",\"latitude\":\"1.0\",\"longitude\":\"2.0\"}";
        
        HttpHeaders storeDetailsRequestHeaders = new HttpHeaders();
        storeDetailsRequestHeaders.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<String> storeDetailsRequest = new HttpEntity<String>(labInfo, storeDetailsRequestHeaders);

        Mockito.when(restTemplate.postForEntity("http://localhost:8080/store", storeDetailsRequest, Lab.class))
                .thenReturn(new ResponseEntity<Lab>(pickupLab, HttpStatus.OK));

        Mockito.when(labRepository.save( any(Lab.class) )).thenReturn(pickupLab);

        // Start of driver request mocks, by mocking rest template //

        HashMap<String, String> driverDetailsMap = new HashMap<>();
        driverDetailsMap.put("id", "1");
        JSONObject driverDetailsResponse = new JSONObject(driverDetailsMap);

        HashMap<String, String> deliveryDetailsMap = new HashMap<>();
        deliveryDetailsMap.put("name", "" + testOrder1.getLabId() + java.time.LocalDate.now());
        deliveryDetailsMap.put("comission", "" + testOrder1.getOrderTotal() * 0.1);
        deliveryDetailsMap.put("deliveryLatitude", testOrder1.getDeliverLocation().getLatitude().toString());
        deliveryDetailsMap.put("deliveryLongitude", testOrder1.getDeliverLocation().getLongitude().toString());
        JSONObject orderDetailsRequest = new JSONObject(deliveryDetailsMap);

        HttpHeaders deliveryDetailsRequestHeaders = new HttpHeaders();
        deliveryDetailsRequestHeaders.setContentType(MediaType.APPLICATION_JSON);
        deliveryDetailsRequestHeaders.add("Authorization", pickupLab.getToken());

        HttpEntity<String> deliveryDetailsRequest = new HttpEntity<String>(orderDetailsRequest.toString(), deliveryDetailsRequestHeaders);

        Mockito.when(restTemplate.postForEntity("http://localhost:8080/store/order/" + pickupLab.getId(), deliveryDetailsRequest, JSONObject.class))
                .thenReturn(new ResponseEntity<JSONObject>(driverDetailsResponse, HttpStatus.OK));

    }

    @Test
    void whenProductIsInStock_thenReturnTrue() {
        Product newProduct = new Product("Covid Test 1", 49.99, "Infrared Test", "Infrared test description.");
        Stock newStock = new Stock(newProduct, 1);
        newStock.setLab(pickupLab);

        boolean isInStock = orderService.isInStock(pickupLab, newStock);

        assertThat(isInStock, is(true));
    }

    @Test
    void whenProductIsNotInStock_thenReturnFalse() {
        Product newProduct = new Product("Covid Test 1", 49.99, "Infrared Test", "Infrared test description.");
        Stock newStock = new Stock(newProduct, 10);
        newStock.setLab(pickupLab);

        boolean isInStock = orderService.isInStock(pickupLab, newStock);

        assertThat(isInStock, is(false));
    }

    @Test
    void whenPlacingValidOrder_thenReturnValidOrder() {
        Order placedOrder = orderService.placeAnOrder(testOrder1);

        assertThat(placedOrder.getClientId(), is(testOrder1.getClientId()));
        assertThat(placedOrder.getDeliverLocation(), is(testOrder1.getDeliverLocation()));
        assertThat(placedOrder.getPickupLocation(), is(testOrder1.getPickupLocation()));
        assertThat(placedOrder.getIsDelivered(), is(false));
        assertThat(placedOrder.getListOfProducts(), is(testOrder1.getListOfProducts()));
        assertThat(placedOrder.getOrderTotal(), is(testOrder1.getOrderTotal()));
    }

    @Test
    void whenPlacingInvalidOrder_thenReturnNull() {
        ResponseStatusException exceptionThrown = assertThrows(ResponseStatusException.class, () -> orderService.placeAnOrder(testOrder2)) ;

        assertThat( exceptionThrown.getStatus(), is(HttpStatus.CONFLICT) );
        assertThat( exceptionThrown.getReason(), is("Product out of stock.") );
    }

}
