package t_tracker.repository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import t_tracker.model.Coordinates;
import t_tracker.model.Lab;

@DataJpaTest
class LabRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LabRepository labRepository;
    
    private Lab laboratory;

    @BeforeEach
    void setUp() {
        Coordinates labCoord = new Coordinates(99.9999, 99.9999);
        entityManager.persistAndFlush(labCoord);
        laboratory = new Lab(1, "labtoken", "Very Good Lab", labCoord);
        entityManager.persistAndFlush(laboratory);
    }

    @Test
    void whenFindLabByExistingId_thenReturnValidLab() {
        Optional<Lab> labFound = labRepository.findById(laboratory.getId());
        
        assertThat( labFound.isPresent(), is(true) );
        assertThat( labFound.get(), is(laboratory) );
    }

    @Test
    void whenFindByInvalidId_thenReturnInvalidLab() {
        Integer invalidId = 99999;
        Optional<Lab> labFound = labRepository.findById(invalidId);

        assertThat(labFound.isPresent(), is(false));
    }
}
