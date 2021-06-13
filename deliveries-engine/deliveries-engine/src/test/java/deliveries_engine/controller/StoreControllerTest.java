package deliveries_engine.controller;

import com.google.gson.JsonObject;
import deliveries_engine.model.Delivery;
import deliveries_engine.model.Rider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.reset;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import deliveries_engine.DeliveriesEngineApplication;
import deliveries_engine.model.Store;
import deliveries_engine.service.StoreService;
import deliveries_engine.JsonUtil;

import org.json.JSONObject;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = DeliveriesEngineApplication.class)
@AutoConfigureMockMvc
class StoreControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @MockBean
    private StoreService storeService;

    private Store newStore;

    private Delivery delivery;

    private String token;

    private int storeId;

    private Rider newRider;

    @BeforeEach
    void setUp() {
        newStore = new Store("CTT", "Pidgeon Man");
        delivery = new Delivery("delivery", 2.5, 40.631858, -8.650833);
        token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzdG9yZXRlc3RlMiIsImV4cCI6MTYyMzU5NjE4NX0";
        storeId = 3;
        newRider = new Rider("Jones", "indiana@jones.org", "CrystalSkull", "losttemple", 912345678, "Kingdom of The Crystal Skull", "Akator", "9090-666");

    }

    @AfterEach
    void cleanUp() {
        reset(storeService);
    }

    @Test
    void whenSignUpStore_thenStoreIsRegistered() throws Exception {
        given( storeService.registerStore(any(Store.class)) ).willReturn(newStore);

        mvc.perform( post("/store").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(newStore)) )
            .andExpect( status().isOk() )
            .andExpect( jsonPath("$.name", is(newStore.getName())) )
            .andExpect( jsonPath("$.ownerName", is(newStore.getOwnerName())) );
        
        verify(storeService, VerificationModeFactory.times(1)).registerStore(any(Store.class));
    }

    @Test
    void whenSignUpInvalidStore_thenReturnConflictStatusCode() throws Exception {
         given( storeService.registerStore(any(Store.class)) ).willThrow(new Exception("Store registered"));

         mvc.perform( post("/store").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(newStore)) )
             .andExpect( status().isConflict() );
    }

    @Test
    void whenOrder_getClosestRider() throws Exception {
        given(storeService.getClosestRider(delivery.getDeliveryLatitude(), delivery.getDeliveryLongitude(), token, storeId)).willReturn(newRider);

        JSONObject json = new JSONObject();
        json.put("name", delivery.getName());
        json.put("comission", Double.toString(delivery.getCommission()));
        json.put("deliveryLatitude", delivery.getDeliveryLatitude().toString());
        json.put("deliveryLongitude", delivery.getDeliveryLongitude().toString());


        mvc.perform( post("/store/order/1").header("Authorization", token).contentType(MediaType.APPLICATION_JSON).content(json.toString().getBytes()))
                .andExpect(status().isOk());
    }

}
