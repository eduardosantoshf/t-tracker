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
import deliveries_engine.model.Rider;
import deliveries_engine.service.RiderService;
import deliveries_engine.JsonUtil;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = DeliveriesEngineApplication.class)
@AutoConfigureMockMvc
class RiderControllerTest {
    
    @Autowired
    private MockMvc mvc;

    @MockBean
    private RiderService riderService;

    private Rider newRider;

    @BeforeEach
    void setUp() {
        newRider = new Rider("Jones", "indiana@jones.org", "CrystalSkull", "losttemple", 912345678, "Kingdom of The Crystal Skull", "Akator", "9090-666");
    }

    @AfterEach
    void cleanUp() {
        reset(riderService);
    }

    @Test
    void whenSignUpRider_thenRiderIsRegistered() throws Exception {
        given( riderService.registerRider(any(Rider.class)) ).willReturn(newRider);

        mvc.perform( post("/rider/signup").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(newRider)) )
            .andExpect( status().isOk() )
            .andExpect( jsonPath("$.name", is(newRider.getName())) )
            .andExpect( jsonPath("$.email", is(newRider.getEmail())) )
            .andExpect( jsonPath("$.username", is(newRider.getUsername())) )
            .andExpect( jsonPath("$.phoneNumber", is(newRider.getPhoneNumber())) )
            .andExpect( jsonPath("$.status", is(newRider.getStatus())) )
            .andExpect( jsonPath("$.address", is(newRider.getAddress())) )
            .andExpect( jsonPath("$.city", is(newRider.getCity())) )
            .andExpect( jsonPath("$.zipCode", is(newRider.getZipCode())) );
        
        verify(riderService, VerificationModeFactory.times(1)).registerRider(any(Rider.class));
    }

    @Test
    void whenSignUpInvalidRider_thenReturnConflictStatusCode() {
        // given( riderService.registerRider(newRider) ).willReturn(null);

        // mvc.perform( post("/rider/signup").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(newRider)) )
        //     .andExpect( status().isConflict() );
    }

    @Test
    void whenUpdateValidRiderStatus_thenIsOk() {
        // given( riderService.updateRiderStatus(newRider) ).willReturn(newRider);

        // mvc.perform( post("/rider/status/0").contentType(MediaType.APPLICATION_JSON) )
        //     .andExpect( status().isOk() );
    }

    @Test
    void whenUpdateInvalidRiderStatus_thenIsConflict() {
        // given( riderService.updateRiderStatus(newRider) ).willReturn(newRider);

        // mvc.perform( post("/rider/status/99").contentType(MediaType.APPLICATION_JSON) )
        //     .andExpect( status().isConflict() );
    }

}
