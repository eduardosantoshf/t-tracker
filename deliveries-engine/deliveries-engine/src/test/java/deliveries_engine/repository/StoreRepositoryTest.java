package deliveries_engine.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.CoreMatchers.nullValue;

import deliveries_engine.model.Store;

import java.util.List;
import java.util.Optional;

@DataJpaTest
class StoreRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StoreRepository storeRepository;

    @Test
    public void whenFindStoreByExistingId_thenReturnStore() {
        Store tTracker = new Store("T-Tracker", "Owner Name");
        entityManager.persistAndFlush(tTracker);

        Optional<Store> storeFound = storeRepository.findById(tTracker.getId());
        assertThat( storeFound, is(tTracker) );
    }

    @Test
    public void whenFindStoreByInvalidId_thenReturnNull() {
        int invalidId = 99999;
        Optional<Store> storeFound = storeRepository.findById(invalidId);
        assertThat( storeFound, is(nullValue()) );
    }

    @Test
    public void whenFindStoreByValidName_thenReturnValidStore() {
        Store tTracker = new Store("T-Tracker", "Owner Name");
        entityManager.persistAndFlush(tTracker);

        Optional<Store> storeFound = storeRepository.findByName(tTracker.getName());
        storeFound.ifPresent(store -> assertThat( store, is(tTracker) ));
    }

    @Test
    public void whenFindByInvalidName_thenReturnNull() {
        Optional<Store> storeFound = storeRepository.findByName("Invalid Store");
        assertThat( storeFound.isPresent(), is(false) );
    }

    @Test
    public void whenFindAll_thenReturnAllStores() {
        Store tTracker = new Store("T-Tracker", "Owner Name");
        Store amazon = new Store("Amazon", "Jeff B.");
        Store fnac = new Store("Fnac", "Fnac Owner");
    
        entityManager.persist(tTracker);
        entityManager.persist(amazon);
        entityManager.persist(fnac);
        entityManager.flush();

        List<Store> deliveriesFound = storeRepository.findAll();

        assertThat( deliveriesFound.size(), is(3) );
        assertThat( deliveriesFound.contains(tTracker), is(true) );
        assertThat( deliveriesFound.contains(amazon), is(true) );
        assertThat( deliveriesFound.contains(fnac), is(true) );
    }

}
