package deliveries_engine.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.List;

import static org.hamcrest.CoreMatchers.nullValue;

import deliveries_engine.model.User;
import deliveries_engine.model.Rider;

@DataJpaTest
class RiderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RiderRepository riderRepository;

    @Test
    public void whenFindRiderByValidId_thenReturnValidRider() {
        Rider testRider = new Rider(new User("Test User", "test@user.com", "UserForTests", "testPassword1234", 933399999));
        entityManager.persistAndFlush(testRider);

        Rider riderFound = riderRepository.findById(testRider.getId());
        assertThat( riderFound, is(testRider) );
    }

    @Test
    public void whenFindRiderByInvalidId_thenReturnNull() {
        Long invalidId = 99999L;
        Rider riderFound = riderRepository.findById(invalidId);
        assertThat( riderFound, is(nullValue()) );
    }

    @Test
    public void whenFindRidersByStatus_thenReturnValidRiders() {
        Rider john = new Rider(new User("John", "john@user.com", "john", "johnPassword", 911111111));
        Rider alice = new Rider(new User("Alice", "alice@user.com", "alice", "alicePassword", 922222222));
        Rider alex = new Rider(new User("Alex", "alex@user.com", "alex", "alexPassword", 933333333));
        alice.setStatus(true);
        alex.setStatus(true);

        entityManager.persist(john);
        entityManager.persist(alice);
        entityManager.persist(alex);
        entityManager.flush();

        List<Rider> ridersFound = riderRepository.findByStatus(true);

        assertThat( ridersFound.size(), is(2) );
        assertThat( ridersFound.contains(john), is(false) );
        assertThat( ridersFound.contains(alice), is(true) );
        assertThat( ridersFound.contains(alex), is(true) );
    }

}
