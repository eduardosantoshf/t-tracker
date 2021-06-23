package t_tracker.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import t_tracker.model.Client;
import t_tracker.model.Coordinates;
import t_tracker.model.Lab;
import t_tracker.model.Order;
import t_tracker.model.OrderItem;
import t_tracker.model.Product;
import t_tracker.model.Stock;
import t_tracker.repository.CoordinatesRepository;
import t_tracker.repository.LabRepository;
import t_tracker.repository.OrderItemRepository;
import t_tracker.repository.OrderRepository;
import t_tracker.repository.ProductRepository;
import t_tracker.repository.StockRepository;

@ExtendWith(MockitoExtension.class)
class OrderServiceUnitTests {

    @Mock(lenient = true)
    private OrderRepository orderRepository;

    @Mock(lenient = true)
    private LabRepository labRepository;

    @Mock(lenient = true)
    private OrderItemRepository orderItemRepository;

    @Mock(lenient = true)
    private RestTemplate restTemplate;

    @InjectMocks
    @Spy
    private OrderServiceImpl orderService;

    @Mock(lenient = true)
    private CoordinatesRepository coordinatesRepository;

    @Mock(lenient = true)
    private ProductRepository productRepository;

    @Mock(lenient = true)
    private StockRepository stockRepository;

    @Spy
    private StockServiceImpl stockService;

    Lab testLab;

    Client testClient;

    Order testOrder, fullTestOrder;

    Coordinates clientHome, labLocation;

    Product product;

    OrderItem orderItem;

    @BeforeEach
    void setUp() {
        product = new Product("TestProductName", 100.0, "testtype", "This is a test product.");
        orderItem = new OrderItem(product, 2);
        clientHome = new Coordinates(1.0, 2.0);

        testClient = new Client("Test Name", "TestUsername", "test@email.com", "12345", 321231132, clientHome);
        testClient.setId(1);

        testOrder = new Order(testClient);
        testOrder.addProduct(orderItem);

        labLocation = new Coordinates(1.1, 2.1);
        testLab = new Lab(1, "testtoken", "T-Tracker Lab", labLocation);

        fullTestOrder = new Order(testClient, clientHome, labLocation, 200.0,
                new ArrayList<>(Arrays.asList(orderItem)));
        fullTestOrder.setId(1);
    }

    @Test
    void whenPlaceValidOrder_thenReturnOrderPlaced() throws Exception {
        Mockito.doReturn(testLab).when(orderService).getLabDetails();

        JSONParser parser = new JSONParser();
        ResponseEntity<JSONObject> response = new ResponseEntity<>((JSONObject) parser.parse("{\"id\":\"0\"}"),
                HttpStatus.OK);

        Mockito.doReturn(response).when(restTemplate).postForEntity(anyString(), any(), any());
        Mockito.doReturn(new ArrayList<>(Arrays.asList(new Stock(product, 20)))).when(stockService)
                .removeStock(orderItem.getProduct(), orderItem.getQuantity());
        Mockito.when(orderRepository.save(any())).thenReturn(fullTestOrder);

        Order orderPlaced = orderService.placeAnOrder(testOrder);

        assertThat(orderPlaced.getClientId(), is(1));

    }

    @Test
    void whenPlaceOrderButNoLabIsFound_thenReturn409() throws Exception {
        Mockito.doThrow(new ResponseStatusException(HttpStatus.CONFLICT,
                "Error retrieving authentication details from deliveries API.")).when(orderService).getLabDetails();

        ResponseStatusException exceptionThrown = assertThrows(ResponseStatusException.class,
                () -> orderService.placeAnOrder(testOrder));

        assertThat(exceptionThrown.getStatus(), is(HttpStatus.CONFLICT));

    }

    @Test
    void whenPlaceOrderButNoDriverFound_thenReturn502() throws Exception {
        Mockito.doReturn(testLab).when(orderService).getLabDetails();

        Mockito.doThrow(new ResponseStatusException(HttpStatus.BAD_GATEWAY)).when(restTemplate)
                .postForEntity(anyString(), any(), any());

        ResponseStatusException exceptionThrown = assertThrows(ResponseStatusException.class,
                () -> orderService.placeAnOrder(testOrder));

        assertThat(exceptionThrown.getStatus(), is(HttpStatus.BAD_GATEWAY));
    }

    @Test
    void whenPlaceOrderButDeliveriesAPIReturnsNull_thenReturn502() throws Exception {
        Mockito.doReturn(testLab).when(orderService).getLabDetails();

        JSONParser parser = new JSONParser();
        ResponseEntity<JSONObject> response = new ResponseEntity<>((JSONObject) parser.parse("{}"), HttpStatus.OK);

        Mockito.doReturn(response).when(restTemplate).postForEntity(anyString(), any(), any());

        ResponseStatusException exceptionThrown = assertThrows(ResponseStatusException.class,
                () -> orderService.placeAnOrder(testOrder));

        assertThat(exceptionThrown.getStatus(), is(HttpStatus.BAD_GATEWAY));
    }

    @Test
    void whenPlaceOrderWithInvalidItems_thenThrowException() throws Exception {
        Mockito.doReturn(testLab).when(orderService).getLabDetails();

        JSONParser parser = new JSONParser();
        ResponseEntity<JSONObject> response = new ResponseEntity<>((JSONObject) parser.parse("{\"id\":\"0\"}"),
                HttpStatus.OK);

        Mockito.doReturn(response).when(restTemplate).postForEntity(anyString(), any(), any());
        Mockito.doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(stockService)
                .removeStock(orderItem.getProduct(), orderItem.getQuantity());

        ResponseStatusException exceptionThrown = assertThrows(ResponseStatusException.class,
                () -> orderService.placeAnOrder(testOrder));

        assertThat(exceptionThrown.getStatus(), is(HttpStatus.NOT_FOUND));
    }

    @Test
    void whenGetExistingLabDetails_thenReturnLab() {
        Mockito.when(labRepository.findAll()).thenReturn(new ArrayList<>(Arrays.asList(testLab)));

        Lab labFound = orderService.getLabDetails();

        assertThat(labFound.getName(), is(testLab.getName()));
        assertThat(labFound.getLocation(), is(testLab.getLocation()));
        assertThat(labFound.getToken(), is(testLab.getToken()));
    }

    @Test
    void whenGetInexistantLabDetails_thenFetchLabDetails() throws Exception {
        Mockito.when(labRepository.findAll()).thenReturn(new ArrayList<>());

        Lab responseLab = new Lab(1, "testtoken", "Deliveries Lab", new Coordinates(1.1, 1.2));

        ResponseEntity<Lab> response = new ResponseEntity<>(responseLab, HttpStatus.OK);

        Mockito.doReturn(response).when(restTemplate).postForEntity(anyString(), any(), any());

        Mockito.when(labRepository.save(responseLab)).thenReturn(responseLab);

        Lab labFound = orderService.getLabDetails();

        assertThat(labFound.getName(), is(responseLab.getName()));
        assertThat(labFound.getLocation(), is(responseLab.getLocation()));
        assertThat(labFound.getToken(), is(responseLab.getToken()));
    }

    @Test
    void whenFetchLabDetailsReturnsNull_thenReturn409() {
        Mockito.when(labRepository.findAll()).thenReturn(new ArrayList<>());

        ResponseEntity<Lab> response = new ResponseEntity<>(new Lab(), HttpStatus.OK);

        Mockito.doReturn(response).when(restTemplate).postForEntity(anyString(), any(), any());

        Mockito.when(labRepository.save(new Lab())).thenThrow(NullPointerException.class);

        ResponseStatusException exceptionThrown = assertThrows(ResponseStatusException.class,
                () -> orderService.placeAnOrder(testOrder));

        assertThat(exceptionThrown.getStatus(), is(HttpStatus.CONFLICT));
    }

    @Test
    void whenGetValidOrder_thenReturnOrder() {
        Mockito.when(orderRepository.findById(1)).thenReturn(Optional.of(fullTestOrder));

        Order orderFound = orderService.getOrder(1);

        assertThat(orderFound, is(fullTestOrder));
    }

    @Test
    void whenGetInvalidOrder_thenReturn404() {
        Mockito.when(orderRepository.findById(1)).thenReturn(Optional.ofNullable(null));

        ResponseStatusException exceptionThrown = assertThrows(ResponseStatusException.class, () -> orderService.getOrder(1));

        assertThat(exceptionThrown.getStatus(), is(HttpStatus.NOT_FOUND));
        assertThat(exceptionThrown.getReason(), is("Order not found."));
    }

    @Test
    void whenUpdateOrder_thenReturnOk() {
        fullTestOrder.setStatus("Delivered");
        Mockito.when(orderRepository.save(any())).thenReturn(fullTestOrder);
        
        fullTestOrder.setStatus("Pending");
        Order orderAfterUpdate = orderService.updateStatus(fullTestOrder, 2);

        assertThat(orderAfterUpdate, is(fullTestOrder));

        Mockito.when(orderRepository.save(any())).thenReturn(fullTestOrder);
        
        orderAfterUpdate = orderService.updateStatus(fullTestOrder, 0);

        assertThat(orderAfterUpdate, is(fullTestOrder));

        fullTestOrder.setStatus("Delivering");
        Mockito.when(orderRepository.save(any())).thenReturn(fullTestOrder);
        
        fullTestOrder.setStatus("Pending");
        orderAfterUpdate = orderService.updateStatus(fullTestOrder, 1);

        assertThat(orderAfterUpdate, is(fullTestOrder));
    }

    @Test
    void whenRateOrder_thenReturnRatedOrder() {
        Mockito.when(labRepository.findAll()).thenReturn(new ArrayList<>(Arrays.asList(testLab)));

        ResponseEntity<HttpStatus> response = new ResponseEntity<>(HttpStatus.OK);
        Mockito.doReturn(response).when(restTemplate).postForEntity(anyString(), any(), any());
    
        fullTestOrder.setRating(5);
        Mockito.when(orderRepository.save(any())).thenReturn(fullTestOrder);
        
        fullTestOrder.setRating(2);
        Order orderAfterUpdate = orderService.rateOrder(fullTestOrder, 5);
        
        assertThat(orderAfterUpdate, is(fullTestOrder));
        
    }

    @Test
    void whenRateOrderOutOfBounds_thenReturn406() {
        Mockito.when(labRepository.findAll()).thenReturn(new ArrayList<>(Arrays.asList(testLab)));
        
        ResponseStatusException exceptionThrown = assertThrows(ResponseStatusException.class, () -> orderService.rateOrder(testOrder, 14));

        assertThat(exceptionThrown.getStatus(), is(HttpStatus.NOT_ACCEPTABLE));
        assertThat(exceptionThrown.getReason(), is("Rating value out of bounds"));
        
    }
}
