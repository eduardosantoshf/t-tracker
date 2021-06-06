package deliveries_engine.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.nullValue;

import deliveries_engine.model.User;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void whenFindUserByExistingId_thenReturnUser() {
        User john = new User("John Doe", "john@doe.com", "JohnTheDoe", "testpassword", 999999999, "Test Address", "Test City", "Test-Zipcode");
        entityManager.persistAndFlush(john);

        User userFound = userRepository.findById(john.getId());
        assertThat( userFound, is(john) );
    }

    @Test
    public void whenFindUserByInvalidId_thenReturnNull() {
        int invalidId = 99999;
        User userFound = userRepository.findById(invalidId);
        assertThat( userFound, is(nullValue()) );
    }

    @Test
    public void whenFindUserByValidUsername_thenReturnValidUser() {
        User john = new User("John Doe", "john@doe.com", "JohnTheDoe", "testpassword", 999999999, "Test Address", "Test City", "Test-Zipcode");
        entityManager.persistAndFlush(john);

        Optional<User> userFound = userRepository.findByUsername(john.getUsername());
        userFound.ifPresent(user -> assertThat( user, is(john) ) );
    }

    @Test
    public void whenFindUserByInvalidUsername_thenReturnNull() {
        String invalidUsername = "ThisUsernameDoesNotExist";
        Optional<User> userFound = userRepository.findByUsername(invalidUsername);
        assertThat( userFound.isPresent(), is(false) );
    }

    @Test
    public void whenFindByValidPhoneNumber_thenReturnValidUser() {
        User john = new User("John Doe", "john@doe.com", "JohnTheDoe", "testpassword", 999999999, "Test Address", "Test City", "Test-Zipcode");
        entityManager.persistAndFlush(john);

        User userFound = userRepository.findByPhoneNumber(john.getPhoneNumber());
        assertThat( userFound, is(john) );
    }

    @Test
    public void whenFindUserByInvalidPhoneNumber_thenReturnNull() {
        int invalidPhoneNumber = 1234;
        User userFound = userRepository.findByPhoneNumber(invalidPhoneNumber);
        assertThat( userFound, is(nullValue()) );
    }

}
