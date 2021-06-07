package deliveries_engine.controller;

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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = DeliveriesEngineApplication.class)
@AutoConfigureMockMvc
class StoreControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @MockBean
    private StoreService storeService;

    private Store newStore;

    @BeforeEach
    void setUp() {
        newStore = new Store("CTT", "Pidgeon Man");
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
    void whenSignUpInvalidStore_thenReturnConflictStatusCode() {
        // given( storeService.registerStore(newStore) ).willReturn(null);

        // mvc.perform( post("/store").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(newStore)) )
        //     .andExpect( status().isConflict() );
    }

}
