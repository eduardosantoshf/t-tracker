package deliveries_engine.controller;

import deliveries_engine.service.JwtTokenService;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = DeliveriesEngineApplication.class)
@AutoConfigureMockMvc
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
//@WebAppConfiguration
class RiderControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RiderService riderService;

    private Rider newRider = new Rider("Jones", "indiana@jones.org", "CrystalSkull", "losttemple", 912345678, "Kingdom of The Crystal Skull", "Akator", "9090-666");;

    @Before
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @After
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
    void whenUpdatingRiderLocation_thenCheckChangedLocation() throws Exception {
        Rider rider = new Rider("name", "email", "username", "pw", 912345678, "address", "city", "zip", 40.631858, -8.650833);

        String token = JwtTokenService.generateToken("olezito", new DefaultClaims());
        assertNotNull(token);

        given(riderService.updateLocation(any(Double.class), any(Double.class), any(Rider.class))).willReturn(rider);

        mvc.perform(post("/rider/location/40.631858/-8.650833").header("Authorization", "Bearer " + token).contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(rider)) )
            .andExpect(status().isOk())
            .andExpect( jsonPath("$.latitude", is(rider.getLatitude())))
            .andExpect( jsonPath("$.longitude", is(rider.getLongitude())));
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
