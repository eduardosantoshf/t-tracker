package t_tracker.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import t_tracker.model.*;

@DataJpaTest
class OrderRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;
    
    private Order testOrder1, testOrder2;
    private Client orderClient;

    @BeforeEach
    void setUp() {
        orderClient = new Client("Client Name", "ClientUsername", "email@org.com", "password1234");
        
        Coordinates pickupLocation = new Coordinates(1.1111, 1.1111);
        Coordinates deliverLocation = new Coordinates(1.1112, 1.1112);
        
        Product product1 = new Product("Covid Test 1", 49.99, "Infrared Test");
        Product product2 = new Product("Covid Test 2", 99.99, "Molecular Test");
        Stock orderStock1 = new Stock(product1, 2);
        Stock orderStock2 = new Stock(product2, 1);
        List<Stock> listOfProducts = new ArrayList<>(Arrays.asList(orderStock1, orderStock2));

        Lab orderLab = new Lab("Chemical Lab lda", pickupLocation);

        Double orderTotal = orderStock1.getTotalPrice() + orderStock2.getTotalPrice();
        
        testOrder1 = new Order(orderClient.getUsername(), pickupLocation, deliverLocation, orderTotal, orderLab.getId(), listOfProducts);
        testOrder2 = new Order(orderClient.getUsername(), pickupLocation, deliverLocation, orderTotal, orderLab.getId(), listOfProducts);

        testOrder2.setIsDelivered(true);

        entityManager.persist(orderClient);
        entityManager.persist(pickupLocation);
        entityManager.persist(deliverLocation);
        entityManager.persist(product1);
        entityManager.persist(product2);
        entityManager.persist(orderStock1);
        entityManager.persist(orderStock2);
        entityManager.persist(orderLab);
        entityManager.persist(testOrder1);
        entityManager.persist(testOrder2);
        entityManager.flush();

    }

    @Test
    void whenFindOrderByExistingId_thenReturnValidOrder() {
        Optional<Order> orderFound = orderRepository.findById(testOrder1.getId());

        assertThat( orderFound.isPresent(), is(true) );
        assertThat( orderFound.get(), is(testOrder1) );
    }

    @Test
    void whenFindByInvalidId_thenReturnInvalidOrder() {
        UUID invalidId = UUID.randomUUID();
        Optional<Order> orderFound = orderRepository.findById(invalidId);

        assertThat( orderFound.isPresent(), is(false) );
    }

    @Test
    void whenFindOrderByClientId_thenReturnOrder() {
        List<Order> orderFound = orderRepository.findByClientUsername(orderClient.getUsername());

        assertThat( orderFound.size(), is(2) );
        assertThat( orderFound.contains(testOrder1), is(true) );
        assertThat( orderFound.contains(testOrder2), is(true) );
    }

    @Test
    void whenFindByInvalidClientId_thenReturnNullOrder() {
        String invalidUsername = "ThisUsernameIsDefinitelyInvalid";
        List<Order> orderFound = orderRepository.findByClientUsername(invalidUsername);

        assertThat( orderFound.size(), is(0) );
    }

    @Test
    void whenFindOrderByIsDelivered_thenReturnOrder() {
        List<Order> orderFound = orderRepository.findByIsDelivered(false);

        assertThat( orderFound.size(), is(1) );
        assertThat( orderFound.contains(testOrder1), is(true) );
    }

    @Test
    void whenFindAllOrders_thenReturnOrders() {
        List<Order> orderFound = orderRepository.findAll();

        assertThat( orderFound.size(), is(2) );
        assertThat( orderFound.contains(testOrder1), is(true) );
        assertThat( orderFound.contains(testOrder2), is(true) );
    }
    
}
