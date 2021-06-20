package deliveries_engine.model;

import deliveries_engine.DeliveriesEngineApplication;
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

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = DeliveriesEngineApplication.class)
@AutoConfigureMockMvc
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class StoreModuleTest {

    private Store store;

    @MockBean
    private StoreRepository storeRepository;

    @Test
    void operationsOnDeliveries(){
        store = new Store("t-tracker", "owner");
        Delivery delivery1 = new Delivery("delivery1", 3.4, 40.631858, -8.650833);
        Delivery delivery2 = new Delivery("delivery2", 2.2, 42.451858, -6.750833);
        store.addDelivery(delivery1);
        storeRepository.save(store);

        given(storeRepository.findById(store.getId())).willReturn(Optional.ofNullable(store));


        Optional<Store> opt = storeRepository.findById(store.getId());
        Store rep_store = opt.get();

        assertEquals(store.getDeliveries(), rep_store.getDeliveries());

        List<Delivery> deliveryList = new ArrayList<>();
        deliveryList.add(delivery1);
        deliveryList.add(delivery2);

        store.setDeliveries(deliveryList);
        storeRepository.save(store);

        assertEquals(store.getDeliveries(), deliveryList);

        store.deleteDelivery(delivery1);
        storeRepository.save(store);

        deliveryList.remove(delivery1);

        assertEquals(store.getDeliveries(), deliveryList);



    }

}
