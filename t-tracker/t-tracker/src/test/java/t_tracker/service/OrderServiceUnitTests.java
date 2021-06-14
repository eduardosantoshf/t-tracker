package t_tracker.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.CoreMatchers.nullValue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import t_tracker.model.*;
import t_tracker.repository.LabRepository;
import t_tracker.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
public class OrderServiceUnitTests {
    
    @Mock( lenient = true)
    private OrderRepository orderRepository;

    @Mock( lenient = true)
    LabRepository labRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order testOrder1, testOrder2;
    private Lab pickupLab;

    @BeforeEach
    void setUp() {
        Client orderClient = new Client("Client Name", "ClientUsername", "email@org.com", "password1234");
        Coordinates pickupLocation = new Coordinates(1.1111, 1.1111);
        Coordinates deliverLocation = new Coordinates(1.1112, 1.1112);
        
        Product product1 = new Product("Covid Test 1", 49.99, "Infrared Test");
        Product product2 = new Product("Covid Test 2", 99.99, "Molecular Test");

        Stock labStock1 = new Stock(product1, 3);
        Stock labStock2 = new Stock(product2, 4);
        List<Stock> pickupLabFullStock = new ArrayList<>(Arrays.asList(labStock1, labStock2));
        pickupLab = new Lab("Test Deliveries Lab. lda", pickupLocation);
        pickupLab.setStocks(pickupLabFullStock);
        
        Stock orderStock1 = new Stock(product1, 2);
        Stock orderStock2 = new Stock(product2, 1);
        Stock orderStock3 = new Stock(product2, 5);
        List<Stock> listOfOrderProducts1 = new ArrayList<>(Arrays.asList(orderStock1, orderStock2));
        List<Stock> listOfOrderProducts2 = new ArrayList<>(Arrays.asList(orderStock1, orderStock3));

        Double orderTotal = orderStock1.getTotalPrice() + orderStock2.getTotalPrice();
        
        testOrder1 = new Order(orderClient, pickupLocation, deliverLocation, orderTotal, pickupLab, listOfOrderProducts1);
        testOrder2 = new Order(orderClient, pickupLocation, deliverLocation, orderTotal, pickupLab, listOfOrderProducts2);

        System.out.println(orderStock1.getLab());
        Mockito.when(labRepository.findById(testOrder1.getLab().getId())).thenReturn(Optional.of(pickupLab));

    }

    @Test
    void whenProductIsInStock_thenReturnTrue() {
        Product newProduct = new Product("Covid Test 1", 49.99, "Infrared Test");
        Stock newStock =  new Stock(newProduct, 1);
        newStock.setLab(pickupLab);

        boolean isInStock = orderService.isInStock( newStock );

        assertThat( isInStock, is(true) );
    }

    @Test
    void whenProductIsNotInStock_thenReturnFalse() {
        Product newProduct = new Product("Covid Test 1", 49.99, "Infrared Test");
        Stock newStock =  new Stock(newProduct, 10);
        newStock.setLab(pickupLab);

        boolean isInStock = orderService.isInStock( newStock );

        assertThat( isInStock, is(false) );
    }

    @Test
    void whenPlacingValidOrder_thenReturnValidOrder() {
        Order placedOrder = orderService.placeAnOrder( testOrder1 );

        assertThat( placedOrder.getClient(), is(testOrder1.getClient()) );
        assertThat( placedOrder.getDeliverLocation(), is(testOrder1.getDeliverLocation()) );
        assertThat( placedOrder.getPickupLocation(), is(testOrder1.getPickupLocation()) );
        assertThat( placedOrder.getIsDelivered(), is(false) );
        assertThat( placedOrder.getListOfProducts(), is(testOrder1.getListOfProducts()) );
        assertThat( placedOrder.getOrderTotal(), is(testOrder1.getOrderTotal()) );
    }

    @Test
    void whenPlacingInvalidOrder_thenReturnNull() {
        Order placedOrder = orderService.placeAnOrder( testOrder2 );

        assertThat( placedOrder.getClient(), is(nullValue()) );
    }


}
