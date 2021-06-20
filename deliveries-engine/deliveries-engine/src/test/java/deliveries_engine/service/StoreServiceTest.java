package deliveries_engine.service;

import deliveries_engine.DeliveriesEngineApplication;
import deliveries_engine.model.Delivery;
import deliveries_engine.model.Rider;
import deliveries_engine.model.Store;
import deliveries_engine.repository.DeliveryRepository;
import deliveries_engine.repository.RiderRepository;
import deliveries_engine.repository.StoreRepository;
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
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;


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

    @MockBean
    private StoreRepository storeRepository;

    @MockBean
    private DeliveryRepository deliveryRepository;

    private Optional<Store> opt;

    @BeforeEach
    void setUp() throws Exception {
        newStore = new Store("storeTest", "Eduardo");
        given(storeRepository.save(newStore)).willReturn(newStore);
        store = storeService.registerStore(newStore);
        //System.out.println(store.getToken());
        //System.out.println(store.toString());
        token = store.getToken();

    }

    @AfterEach
    void cleanUp() {
        reset(riderRepository);
        reset(storeRepository);
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
        given(storeRepository.findById(store.getId())).willReturn(Optional.of(store));


        Delivery delivery = new Delivery("name", 3.4, 40.741858, -8.470833);

        given(deliveryRepository.save(delivery)).willReturn(delivery);

        Rider response_rider = storeService.getClosestRider(delivery,40.741858, -8.470833, token, store.getId());

        assertEquals(rider1, response_rider);

    }

    @Test
    void whenUpdateRatings_ratingsAreUpdated() throws Exception {
        Rider rider1 = new Rider("Jones", "indiana@jones.org", "CrystalSkull", "losttemple", 912345678,
                "Kingdom of The Crystal Skull", "Akator", "9090-666", 40.631858, -8.650833);

        given(riderRepository.findById(rider1.getId())).willReturn(rider1);
        given(storeRepository.findById(store.getId())).willReturn(Optional.of(store));

        List<Integer> response_ratings = storeService.updateRatings(3, token, store.getId(), rider1.getId());

        List<Integer> expected_ratings = new ArrayList<>();
        expected_ratings.add(3);

        assertEquals(expected_ratings, response_ratings);
    }

    @Test
    void whenUpdateComments_commentsAreUpdated() throws Exception {
        Rider rider1 = new Rider("Jones", "indiana@jones.org", "CrystalSkull", "losttemple", 912345678,
                "Kingdom of The Crystal Skull", "Akator", "9090-666", 40.631858, -8.650833);

        given(riderRepository.findById(rider1.getId())).willReturn(rider1);
        given(storeRepository.findById(store.getId())).willReturn(Optional.of(store));

        List<String> response_comments = storeService.updateComments("que bonito", token, store.getId(), rider1.getId());

        List<String> expected_comments = new ArrayList<>();
        expected_comments.add("que bonito");

        assertEquals(expected_comments, response_comments);
    }

    @Test
    void whenGetRatings_returnRatings() throws Exception {
        Rider rider1 = new Rider("Jones", "indiana@jones.org", "CrystalSkull", "losttemple", 912345678,
                "Kingdom of The Crystal Skull", "Akator", "9090-666", 40.631858, -8.650833);

        given(riderRepository.findById(rider1.getId())).willReturn(rider1);
        given(storeRepository.findById(store.getId())).willReturn(Optional.of(store));

        storeService.updateRatings(3, token, store.getId(), rider1.getId());
        storeService.updateRatings(3, token, store.getId(), rider1.getId());
        storeService.updateRatings(2, token, store.getId(), rider1.getId());

        List<Integer> expected_ratings = new ArrayList<>();
        expected_ratings.add(3);
        expected_ratings.add(3);
        expected_ratings.add(2);

        List <Integer> response_ratings = storeService.getRatings(token, store.getId(), rider1.getId());

        assertEquals(expected_ratings, response_ratings);
    }

    @Test
    void whenGetComments_returnComments() throws Exception {
        Rider rider1 = new Rider("Jones", "indiana@jones.org", "CrystalSkull", "losttemple", 912345678,
                "Kingdom of The Crystal Skull", "Akator", "9090-666", 40.631858, -8.650833);

        given(riderRepository.findById(rider1.getId())).willReturn(rider1);
        given(storeRepository.findById(store.getId())).willReturn(Optional.of(store));

        storeService.updateComments("bonito", token, store.getId(), rider1.getId());
        storeService.updateComments("lindo", token, store.getId(), rider1.getId());
        storeService.updateComments("no cap", token, store.getId(), rider1.getId());

        List<String> expected_comments = new ArrayList<>();
        expected_comments.add("bonito");
        expected_comments.add("lindo");
        expected_comments.add("no cap");

        List <String> response_comments = storeService.getComments(token, store.getId(), rider1.getId());

        assertEquals(expected_comments, response_comments);
    }



}
