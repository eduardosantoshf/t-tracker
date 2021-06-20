package deliveries_engine.service;

import deliveries_engine.DeliveriesEngineApplication;
import deliveries_engine.model.Delivery;
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

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = DeliveriesEngineApplication.class)
@AutoConfigureMockMvc
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class RiderServiceTest {

    @Autowired
    private RiderService riderService;

    private Rider newRider;

    @MockBean
    private RiderRepository riderRepository;

    @BeforeEach
    void setUp() {
        newRider = new Rider("Jones", "indiana@jones.org", "CrystalSkull", "losttemple", 912345678, "Kingdom of The Crystal Skull", "Akator", "9090-666");

    }

    @AfterEach
    void cleanUp() {
        reset(riderRepository);
    }

    @Test
    void whenSignUpRider_thenRiderIsRegistered() throws Exception {
        Rider rider = riderService.registerRider(newRider);
        assertEquals(rider, newRider);
    }

    @Test
    void whenUpdateLocation_thenLocationIsChanged() throws Exception {
        Rider rider = riderService.updateLocation(40.631858, -8.650833, newRider);
        assertEquals(40.631858, rider.getLatitude(), 0);
        assertEquals(-8.650833, rider.getLongitude(), 0);
    }

    @Test
    void whenUpdateStatus_thenStatusIsChanged() throws Exception {
        Rider rider = riderService.updateStatus(1, newRider);
        assertEquals(1, rider.getStatus());
    }

    @Test
    void whenRequestDeliveries_getDeliveries(){
        List<Delivery> deliveryList = new ArrayList<>();
        Delivery d1 = new Delivery("deliver1", 3.5, 40.631858, -8.650833);
        deliveryList.add(d1);
        newRider.setDeliveries(deliveryList);

        given(riderRepository.findById(newRider.getId())).willReturn(newRider);

        List<Delivery> response = riderService.getDeliveries(newRider);

        assertEquals(deliveryList, response);
    }

}
