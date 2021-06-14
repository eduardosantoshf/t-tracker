package deliveries_engine.service;

import deliveries_engine.DeliveriesEngineApplication;
import deliveries_engine.model.Rider;
import deliveries_engine.model.Store;
import deliveries_engine.repository.RiderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = DeliveriesEngineApplication.class)
@AutoConfigureMockMvc
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class StoreServiceTest {

    @Autowired
    private StoreService storeService;

    private Store newStore;

    private Store store;

    private String token;

    @MockBean
    private RiderRepository riderRepository;

    @BeforeEach
    void setUp() throws Exception {
        newStore = new Store("storeTest", "Eduardo");
        store = storeService.registerStore(newStore);
        token = store.getToken();

    }

    @AfterEach
    void cleanUp() {
        //reset(riderService);
    }

    @Test
    void whenSignUpStore_StoreIsRegistered() throws Exception{
        assertEquals(store, newStore);
    }

    @Test
    void whenRequestClosestRider_getRider() throws Exception {
        List<Rider> riders = new ArrayList<>();
        Rider rider1 = new Rider("Jones", "indiana@jones.org", "CrystalSkull", "losttemple", 912345678,
                            "Kingdom of The Crystal Skull", "Akator", "9090-666", 40.631858, -8.650833);
        Rider rider2 = new Rider("Ze", "ze@ze.org", "zezito", "losttemple", 912345678,
                            "Kingdom of The Crystal Skull", "Akator", "9090-666", 38.631858, -6.650833);
        riders.add(rider1);
        riders.add(rider2);


        given(riderRepository.findAll()).willReturn(riders);

        Rider response_rider = storeService.getClosestRider(40.741858, -8.470833, token, store.getId());

        assertEquals(rider1, response_rider);

    }

}
