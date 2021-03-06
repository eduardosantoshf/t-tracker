package t_tracker.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        Coordinates deliverLocation = new Coordinates(1.1112, 1.1112);
        entityManager.persistAndFlush(deliverLocation);

        orderClient = new Client("Client Name", "ClientUsername", "email@org.com", "password1234", 123123123,
                deliverLocation);

        Coordinates pickupLocation = new Coordinates(1.1111, 1.1111);
        entityManager.persistAndFlush(pickupLocation);

        Product product1 = new Product("Covid Test 1", 49.99, "Infrared Test", "Very nice test 1.");
        Product product2 = new Product("Covid Test 2", 99.99, "Molecular Test", "Very nice test 1.");
        OrderItem orderItem1 = new OrderItem(product1, 2);
        OrderItem orderItem2 = new OrderItem(product2, 1);
        List<OrderItem> listOfProducts = new ArrayList<>(Arrays.asList(orderItem1, orderItem2));

        Lab orderLab = new Lab(1, "labtoken", "Chemical Lab lda", pickupLocation);

        Double orderTotal = orderItem1.getTotalPrice() + orderItem2.getTotalPrice();

        entityManager.persistAndFlush(orderClient);

        testOrder1 = new Order(orderClient, pickupLocation, deliverLocation, orderTotal, listOfProducts);
        testOrder2 = new Order(orderClient, pickupLocation, deliverLocation, orderTotal, listOfProducts);

        testOrder2.setStatus("Delivered");

        entityManager.persist(deliverLocation);
        entityManager.persist(product1);
        entityManager.persist(product2);
        entityManager.persist(orderItem1);
        entityManager.persist(orderItem2);
        entityManager.persist(orderLab);
        entityManager.persist(testOrder1);
        entityManager.persist(testOrder2);
        entityManager.flush();

    }

    @Test
    void whenFindOrderByExistingId_thenReturnValidOrder() {
        Optional<Order> orderFound = orderRepository.findById(testOrder1.getId());

        assertThat(orderFound.isPresent(), is(true));
        assertThat(orderFound.get(), is(testOrder1));
    }

    @Test
    void whenFindByInvalidId_thenReturnInvalidOrder() {
        int invalidId = 9999;
        Optional<Order> orderFound = orderRepository.findById(invalidId);

        assertThat(orderFound.isPresent(), is(false));
    }

    @Test
    void whenFindOrderByClientId_thenReturnOrder() {
        List<Order> orderFound = orderRepository.findByClientId(orderClient.getId());

        assertThat(orderFound.size(), is(2));
        assertThat(orderFound.contains(testOrder1), is(true));
        assertThat(orderFound.contains(testOrder2), is(true));
    }

    @Test
    void whenFindByInvalidClientId_thenReturnNullOrder() {
        int invalidId = 9999999;
        List<Order> orderFound = orderRepository.findByClientId(invalidId);

        assertThat(orderFound.size(), is(0));
    }

    @Test
    void whenFindOrderByStatus_thenReturnOrder() {
        List<Order> orderFound = orderRepository.findByStatus("Delivered");

        assertThat(orderFound.size(), is(1));
        assertThat(orderFound.contains(testOrder1), is(false));
        assertThat(orderFound.contains(testOrder2), is(true));
    }

    @Test
    void whenFindAllOrders_thenReturnOrders() {
        List<Order> orderFound = orderRepository.findAll();

        assertThat(orderFound.size(), is(2));
        assertThat(orderFound.contains(testOrder1), is(true));
        assertThat(orderFound.contains(testOrder2), is(true));
    }

}
