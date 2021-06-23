package t_tracker.repository;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import t_tracker.model.Client;
import t_tracker.model.Coordinates;

@DataJpaTest
class ClientRepositoryTests {
    
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClientRepository clientRepository;

    private Client jason;

    @BeforeEach
    void setUp() {
        Coordinates jasonCoord = new Coordinates(38.87201966193823, -77.05634200409688);
        entityManager.persistAndFlush(jasonCoord);
        jason = new Client("Jason Bourne", "YouKnowMyName", "bourne@cia.com", "jesuschristitsjasonbourne", 999888777, jasonCoord);
        entityManager.persistAndFlush(jason);
    }

    @Test
    void whenFindClientByExistingId_thenReturnValidClient() {
        Optional<Client> clientFound = clientRepository.findById(jason.getId());

        assertThat( clientFound.isPresent(), is(true) );
        assertThat( clientFound.get(), is(jason) );
    }

    @Test
    void whenFindClientByInvalidId_thenReturnNull() {
        Integer invalidId = 9999;
        Optional<Client> clientFound = clientRepository.findById(invalidId);

        assertThat( clientFound.isPresent(), is(false) );
    }

    @Test
    void whenFindClientByValidUsername_thenReturnValidClient() {
        Optional<Client> clientFound = clientRepository.findByUsername(jason.getUsername());

        assertThat( clientFound.isPresent(), is(true) );
        assertThat( clientFound.get(), is(jason) );
    }
    
    @Test
    void whenFindClientByInvalidUsername_thenReturnNull() {
        String invalidUsername = "TotallyNotValidUsername";
        Optional<Client> clientFound = clientRepository.findByUsername(invalidUsername);

        assertThat( clientFound.isPresent(), is(false) );
    }

    @Test
    void whenFindClientByValidEmail_thenReturnValidClient() {
        Optional<Client> clientFound = clientRepository.findByEmail(jason.getEmail());

        assertThat( clientFound.isPresent(), is(true) );
        assertThat( clientFound.get(), is(jason) );
    }
    
    @Test
    void whenFindClientByInvalidEmail_thenReturnNull() {
        String invalidEmail = "TotallyNotValidEmail@1234.asdfa";
        Optional<Client> clientFound = clientRepository.findByEmail(invalidEmail);

        assertThat( clientFound.isPresent(), is(false) );
    }

    @Test
    void whenFindClientByValidPhoneNumber_thenReturnValidClient() {
        Optional<Client> clientFound = clientRepository.findByPhoneNumber(jason.getPhoneNumber());

        assertThat( clientFound.isPresent(), is(true) );
        assertThat( clientFound.get(), is(jason) );
    }
    
    @Test
    void whenFindClientByInvalidPhoneNumber_thenReturnNull() {
        int invalidPhoneNumber = 1;
        Optional<Client> clientFound = clientRepository.findByPhoneNumber(invalidPhoneNumber);

        assertThat( clientFound.isPresent(), is(false) );
    }

    @Test
    void whenFindAllClients_thenReturnAllClients() {
        Coordinates shrekCoord = new Coordinates(1.1, 1.2);
        Coordinates donkeyCoord = new Coordinates(1.2, 1.3);
        Coordinates pussCoord = new Coordinates(1.3, 1.4);
        entityManager.persist(shrekCoord);
        entityManager.persist(donkeyCoord);
        entityManager.persist(pussCoord);

        Client shrek = new Client("Shrek", "SwampMaster", "bigandgreen@org.com", "getoutofmyswamp", 999888777, shrekCoord);
        Client donkey = new Client("Donkey", "ShreksFriend", "donkey@org.com", "iS2dragons", 999888777, donkeyCoord);
        Client puss = new Client("Puss", "PussInBoots", "wantedcat@org.com", "cutekitty", 999888777, pussCoord);
        entityManager.persist(shrek);
        entityManager.persist(donkey);
        entityManager.persist(puss);
        
        entityManager.flush();

        List<Client> allClients = clientRepository.findAll();

        assertThat( allClients.size(), is(4) );
        assertThat( allClients.contains(shrek), is(true) );
        assertThat( allClients.contains(donkey), is(true) );
        assertThat( allClients.contains(puss), is(true) );
        assertThat( allClients.contains(jason), is(true) );
    }

}
