package deliveries_engine.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.CoreMatchers.nullValue;

import deliveries_engine.model.Delivery;
import deliveries_engine.model.Rider;
import deliveries_engine.model.Store;

import java.util.List;

@DataJpaTest
class DeliveryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private DeliveryRepository deliveryRepository;

    private Rider deliveryRider;
    private Store deliveryStore;

    @BeforeEach
    public void setUp() {
        deliveryRider = new Rider("Alex Jones", "alexjones1@mail.com", "AlexJonesOfficial", "alexijoni", 913444555);
        deliveryStore = new Store("Nozama", "Beff Jezos");
        entityManager.persist(deliveryRider);
        entityManager.persist(deliveryStore);
        entityManager.flush();
    }

    @Test
    public void whenFindDeliveryByExistingId_thenReturnDelivery() {
        Delivery delivery = new Delivery("Chocolate Delivery", 14.99, deliveryRider, deliveryStore, "Location Ave., Nº123", "Location Ave., Nº123", 20);
        entityManager.persistAndFlush(delivery);

        Delivery deliveryFound = deliveryRepository.findById(delivery.getId());
        assertThat( deliveryFound, is(delivery) );
    }

    @Test
    public void whenFindDeliveryByInvalidId_thenReturnNull() {
        int invalidId = 99999;
        Delivery deliveryFound = deliveryRepository.findById(invalidId);
        assertThat( deliveryFound, is(nullValue()) );
    }

    @Test
    public void whenFindByStatus_thenReturnCorrectDeliveries() {
        Delivery delivery1 = new Delivery("Chocolate Delivery", 14.99, deliveryRider, deliveryStore, "Location Ave., Nº123", "Location Ave., Nº123", 20);
        Delivery delivery2 = new Delivery("Bread Delivery", 4.99, deliveryRider, deliveryStore, "Location Ave., Nº234", "Location Ave., Nº234", 10);
        Delivery delivery3 = new Delivery("Water Delivery", 9.99, deliveryRider, deliveryStore, "Location Ave., Nº345", "Location Ave., Nº345", 15);
        delivery1.setStatus(true);
        delivery2.setStatus(true);

        entityManager.persist(delivery1);
        entityManager.persist(delivery2);
        entityManager.persist(delivery3);
        entityManager.flush();

        List<Delivery> deliveriesFound = deliveryRepository.findByStatus(true);
        
        assertThat( deliveriesFound.size(), is(2) );
        assertThat( deliveriesFound.contains(delivery1), is(true) );
        assertThat( deliveriesFound.contains(delivery2), is(true) );
        assertThat( deliveriesFound.contains(delivery3), is(false) );
    }

    @Test
    public void whenFindAll_thenReturnAllDeliveries() {
        Delivery delivery1 = new Delivery("Chocolate Delivery", 14.99, deliveryRider, deliveryStore, "Location Ave., Nº123", "Location Ave., Nº123", 20);
        Delivery delivery2 = new Delivery("Bread Delivery", 4.99, deliveryRider, deliveryStore, "Location Ave., Nº234", "Location Ave., Nº234", 10);
        Delivery delivery3 = new Delivery("Water Delivery", 9.99, deliveryRider, deliveryStore, "Location Ave., Nº345", "Location Ave., Nº345", 15);
    
        entityManager.persist(delivery1);
        entityManager.persist(delivery2);
        entityManager.persist(delivery3);
        entityManager.flush();

        List<Delivery> deliveriesFound = deliveryRepository.findAll();

        assertThat( deliveriesFound.size(), is(3) );
        assertThat( deliveriesFound.contains(delivery1), is(true) );
        assertThat( deliveriesFound.contains(delivery2), is(true) );
        assertThat( deliveriesFound.contains(delivery3), is(true) );
    }

}
