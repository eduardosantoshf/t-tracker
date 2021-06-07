package deliveries_engine.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.CoreMatchers.nullValue;

import deliveries_engine.model.Admin;

@DataJpaTest
class AdminRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AdminRepository adminRepository;

    @Test
    public void whenFindAdminByValidId_thenReturnValidAdmin() {
        Admin admin = new Admin("Mr Admin", "admin@org.com", "Admin", "admin1234", 901010101);
        entityManager.persistAndFlush(admin);

        Admin adminFound = adminRepository.findById(admin.getId());
        assertThat( adminFound, is(admin) );
    }

    @Test
    public void whenFindRiderByInvalidId_thenReturnNull() {
        int invalidId = 99999;
        Admin adminFound = adminRepository.findById(invalidId);
        assertThat( adminFound, is(nullValue()) );
    }
}

