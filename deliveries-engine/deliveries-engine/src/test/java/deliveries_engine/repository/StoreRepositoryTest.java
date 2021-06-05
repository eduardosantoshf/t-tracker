package deliveries_engine.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.CoreMatchers.nullValue;

import deliveries_engine.model.Store;

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

        Store storeFound = storeRepository.findById(tTracker.getId());
        assertThat( storeFound, is(tTracker) );
    }

    @Test
    public void whenFindStoreByInvalidId_thenReturnNull() {
        int invalidId = 99999;
        Store storeFound = storeRepository.findById(invalidId);
        assertThat( storeFound, is(nullValue()) );
    }

    @Test
    public void whenFindStoreByValidName_thenReturnValidStore() {
        Store tTracker = new Store("T-Tracker", "Owner Name");
        entityManager.persistAndFlush(tTracker);

        Store storeFound = storeRepository.findByName(tTracker.getName());
        assertThat( storeFound, is(tTracker) );
    }

    @Test
    public void whenFindByInvalidName_thenReturnNull() {
        Store storeFound = storeRepository.findByName("Invalid Store");
        assertThat( storeFound, is(nullValue()) );
    }

}
