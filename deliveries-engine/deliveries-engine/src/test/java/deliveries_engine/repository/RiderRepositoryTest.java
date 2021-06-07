package deliveries_engine.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.CoreMatchers.nullValue;

import deliveries_engine.model.Rider;

import java.util.List;

@DataJpaTest
class RiderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RiderRepository riderRepository;

    @Test
    public void whenFindRiderByValidId_thenReturnValidRider() {
        Rider testRider = new Rider("Test User", "test@user.com", "UserForTests", "testPassword1234", 933399999);
        entityManager.persistAndFlush(testRider);

        Rider riderFound = riderRepository.findById(testRider.getId());
        assertThat( riderFound, is(testRider) );
    }

    @Test
    public void whenFindRiderByInvalidId_thenReturnNull() {
        int invalidId = 99999;
        Rider riderFound = riderRepository.findById(invalidId);
        assertThat( riderFound, is(nullValue()) );
    }

    @Test
    public void whenFindRidersByStatus_thenReturnValidRiders() {
        Rider john = new Rider("John", "john@user.com", "john", "johnPassword", 911111111);
        Rider alice = new Rider("Alice", "alice@user.com", "alice", "alicePassword", 922222222);
        Rider alex = new Rider("Alex", "alex@user.com", "alex", "alexPassword", 933333333);
        alice.setStatus(1);
        alex.setStatus(1);

        entityManager.persist(john);
        entityManager.persist(alice);
        entityManager.persist(alex);
        entityManager.flush();

        List<Rider> ridersFound = riderRepository.findByStatus(1);

        assertThat( ridersFound.size(), is(2) );
        assertThat( ridersFound.contains(john), is(false) );
        assertThat( ridersFound.contains(alice), is(true) );
        assertThat( ridersFound.contains(alex), is(true) );
    }

}
