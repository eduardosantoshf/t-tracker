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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

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
    private OrderItemRepository orderItemRepository;

    @Mock(lenient = true)
    private RestTemplate restTemplate;

    @Mock(lenient = true)
    private StockServiceImpl stockService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order testOrder1, testOrder2;
    private Lab pickupLab;

    @BeforeEach
    void setUp() {
        pickupLab = new Lab(1, "labtoken", "CovidTestsDeliveries", new Coordinates(1.0, 2.0));

        Coordinates deliverLocation = new Coordinates(1.0, 2.0);
        // Start of order building section //
        Client orderClient = new Client("Client Name", "ClientUsername", "email@org.com", "password1234", 123321231,
                deliverLocation);
        orderClient.setId(1);

        Coordinates pickupLocation = pickupLab.getLocation();
        Product product1 = new Product("Covid Test 1", 50.0, "Infrared Test", "Infrared test description.");
        Product product2 = new Product("Covid Test 2", 40.0, "Molecular Test", "Molecular test description.");
        OrderItem orderItem1 = new OrderItem(product1, 2);
        OrderItem orderItem2 = new OrderItem(product2, 1);
        OrderItem orderItem3 = new OrderItem(product2, 5);
        List<OrderItem> listOfOrderProducts1 = new ArrayList<>(Arrays.asList(orderItem1, orderItem2));
        List<OrderItem> listOfOrderProducts2 = new ArrayList<>(Arrays.asList(orderItem1, orderItem3));
        Double orderTotal1 = orderItem1.getTotalPrice() + orderItem2.getTotalPrice();
        Double orderTotal2 = orderItem1.getTotalPrice() + orderItem3.getTotalPrice();
        testOrder1 = new Order(orderClient, pickupLocation, deliverLocation, orderTotal1, listOfOrderProducts1);
        testOrder2 = new Order(orderClient, pickupLocation, deliverLocation, orderTotal2, listOfOrderProducts2);
        testOrder1.setId(1);
        testOrder1.setId(2);

        // Start of repository mocks //
        Mockito.when(labRepository.findAll()).thenReturn(new ArrayList<>(Arrays.asList(pickupLab)));

        Mockito.when(clientRepository.findById(orderClient.getId())).thenReturn(Optional.of(orderClient));

        System.out.println("Test order: " + testOrder1);
        Order emptyTestOrder1 = new Order(orderClient, pickupLocation, deliverLocation, orderTotal1, new ArrayList<>());
        Order emptyTestOrder2 = new Order(orderClient, pickupLocation, deliverLocation, orderTotal2, new ArrayList<>());
        System.out.println();
        System.out.println();
        Mockito.when(orderRepository.save(emptyTestOrder1)).thenReturn(emptyTestOrder1);
        Mockito.when(orderRepository.save(emptyTestOrder2)).thenReturn(emptyTestOrder2);
        Mockito.when(orderRepository.save(testOrder1)).thenReturn(testOrder1);
        Mockito.when(orderRepository.save(testOrder2)).thenReturn(testOrder2);
        Mockito.when(productRepository.save(product2)).thenReturn(product2);
        Mockito.when(orderItemRepository.save(orderItem1)).thenReturn(orderItem1);
        Mockito.when(orderItemRepository.save(orderItem2)).thenReturn(orderItem2);
        Mockito.when(orderItemRepository.save(orderItem3)).thenReturn(orderItem3);

        Mockito.when(productRepository.findByNameAndPriceAndType(product1.getName(), product1.getPrice(),
                product1.getType())).thenReturn(Optional.of(orderItem1.getProduct()));
        Mockito.when(productRepository.findByNameAndPriceAndType(product2.getName(), product2.getPrice(),
                product2.getType())).thenReturn(Optional.of(orderItem2.getProduct()));
        // Mockito.when(productRepository.findByNameAndPriceAndType(orderItem3.getProduct().getName(),
        // orderItem3.getProduct().getPrice(), orderItem3.getProduct().getType()))
        // .thenReturn(Optional.ofNullable(null));

        // Mockito.when(stockService.removeStock(orderItem1.getProduct(),
        // orderItem1.getQuantity())).thenReturn(null);
        // Mockito.when(stockService.removeStock(orderItem2.getProduct(),
        // orderItem2.getQuantity())).thenReturn(null);

        // Mockito.when(stockService.removeStock(orderItem3.getProduct(),
        // orderItem3.getQuantity()))
        // .thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Not enough stock
        // to process request."));

        // Start of store signup mocks, by mocking rest template //

        // // String labInfo =
        // "{\"name\":\"CovidTestsDeliveries\",\"ownerName\":\"TqsG101\",\"latitude\":\"1.0\",\"longitude\":\"2.0\"}";

        // // HttpHeaders storeDetailsRequestHeaders = new HttpHeaders();
        // // storeDetailsRequestHeaders.setContentType(MediaType.APPLICATION_JSON);

        // // HttpEntity<String> storeDetailsRequest = new HttpEntity<String>(labInfo,
        // storeDetailsRequestHeaders);

        // //
        // Mockito.when(restTemplate.postForEntity("http://backend-engine:8080/store",
        // storeDetailsRequest, Lab.class))
        // // .thenReturn(new ResponseEntity<Lab>(pickupLab, HttpStatus.OK));

        // // Mockito.when(labRepository.save(any(Lab.class))).thenReturn(pickupLab);

        // Start of driver request mocks, by mocking rest template //

        HashMap<String, String> driverDetailsMap = new HashMap<>();
        driverDetailsMap.put("id", "0");
        JSONObject driverDetailsResponse = new JSONObject(driverDetailsMap);

        HashMap<String, String> deliveryDetailsMap = new HashMap<>();
        deliveryDetailsMap.put("name", "" + pickupLab.getName() + java.time.LocalDate.now());
        deliveryDetailsMap.put("comission", "" + testOrder1.getTotalPrice() * 0.1);
        deliveryDetailsMap.put("deliveryLatitude", orderClient.getHomeLocation().getLatitude().toString());
        deliveryDetailsMap.put("deliveryLongitude", orderClient.getHomeLocation().getLongitude().toString());
        JSONObject orderDetailsRequest = new JSONObject(deliveryDetailsMap);

        HttpHeaders deliveryDetailsRequestHeaders = new HttpHeaders();
        deliveryDetailsRequestHeaders.setContentType(MediaType.APPLICATION_JSON);
        deliveryDetailsRequestHeaders.add("Authorization", pickupLab.getToken());

        HttpEntity<String> deliveryDetailsRequest = new HttpEntity<String>(orderDetailsRequest.toString(),
                deliveryDetailsRequestHeaders);
        System.out.println("On test: " + deliveryDetailsRequest);
        Mockito.when(restTemplate.postForEntity(eq("http://backend-engine:8080/store/order/" + pickupLab.getId()),
                any(), eq(JSONObject.class))).thenReturn(new ResponseEntity<JSONObject>(driverDetailsResponse, HttpStatus.OK));

    }

    @Test
    void whenPlacingValidOrder_thenReturnValidOrder() {
        Mockito.when(orderService.placeAnOrder(testOrder1)).thenReturn(testOrder1);

        Order placedOrder = orderService.placeAnOrder(testOrder1);

        assertThat(placedOrder.getClientId(), is(testOrder1.getClientId()));
        assertThat(placedOrder.getDeliverLocation(), is(testOrder1.getDeliverLocation()));
        assertThat(placedOrder.getPickupLocation(), is(testOrder1.getPickupLocation()));
        assertThat(placedOrder.getStatus(), is("Pending"));
        assertThat(placedOrder.getOrderTotal(), is(testOrder1.getOrderTotal()));
    }

    // @Test
    // void whenPlacingOrderWithNotEnoughStock_thenThrow409() {
    //     Mockito.when(stockService.removeStock(any(), any())).thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Not enough stock to process request."));
    //     ResponseStatusException exceptionThrown = assertThrows(ResponseStatusException.class,
    //             () -> orderService.placeAnOrder(testOrder2));

    //     assertThat(exceptionThrown.getStatus(), is(HttpStatus.CONFLICT));
    //     assertThat(exceptionThrown.getReason(), is("Not enough stock to process request."));
    // }

    // @Test
    // void whenPlacingOrderWithInvalidLab_thenReturn404() {
    // Mockito.when(labRepository.findAll()).thenReturn(new ArrayList<>());

    // ResponseStatusException exceptionThrown =
    // assertThrows(ResponseStatusException.class,
    // () -> orderService.placeAnOrder(testOrder1));

    // assertThat(exceptionThrown.getStatus(), is(HttpStatus.NOT_FOUND));
    // assertThat(exceptionThrown.getReason(), is("Laboratory not found."));
    // }

    // @Test
    // void whenPlacingOrderWithInvalidClient_thenReturn404() {
    // Mockito.when(clientRepository.findById(testOrder1.getClientId())).thenReturn(Optional.ofNullable(null));

    // ResponseStatusException exceptionThrown =
    // assertThrows(ResponseStatusException.class,
    // () -> orderService.placeAnOrder(testOrder1));

    // assertThat(exceptionThrown.getStatus(), is(HttpStatus.NOT_FOUND));
    // assertThat(exceptionThrown.getReason(), is("Client not found."));
    // }

    // @Test
    // void whenPlacingOrderWithExternalApiError_thenReturn409() {
    // Mockito.when(restTemplate.postForEntity(anyString(), any(),
    // any())).thenThrow(ResponseStatusException.class);

    // ResponseStatusException exceptionThrown =
    // assertThrows(ResponseStatusException.class,
    // () -> orderService.placeAnOrder(testOrder1));

    // assertThat(exceptionThrown.getStatus(), is(HttpStatus.BAD_GATEWAY));
    // }

    // @Test
    // void whenPlacingOrderWithExternalApiReturnNull_thenReturn409() {
    // Mockito.when(restTemplate.postForEntity(anyString(), any(),
    // any())).thenReturn(null);

    // ResponseStatusException exceptionThrown =
    // assertThrows(ResponseStatusException.class,
    // () -> orderService.placeAnOrder(testOrder1));

    // assertThat(exceptionThrown.getStatus(), is(HttpStatus.BAD_GATEWAY));
    // assertThat(exceptionThrown.getReason(), is("Error getting response from
    // drivers api."));
    // }

    // @Test
    // void whenGetLabDetails_thenReturnLabDetails() {
    // Lab labDetails = orderService.getLabDetails();

    // assertThat(labDetails.getName(), is("CovidTestsDeliveries"));
    // assertThat(labDetails.getLocation().getLatitude(), is(1.0));
    // assertThat(labDetails.getLocation().getLongitude(), is(2.0));

    // }

    // @Test
    // void whenGetNoLabDetails_thenFetchAndReturnLabDetails() {
    // Mockito.when(labRepository.findAll()).thenReturn(new ArrayList<>());
    // Lab labDetails = orderService.getLabDetails();

    // assertThat(labDetails.getName(), is("CovidTestsDeliveries"));
    // assertThat(labDetails.getLocation().getLatitude(), is(1.0));
    // assertThat(labDetails.getLocation().getLongitude(), is(2.0));

    // }

}
