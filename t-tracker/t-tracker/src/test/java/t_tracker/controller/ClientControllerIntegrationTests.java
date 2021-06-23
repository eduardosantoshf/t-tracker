package t_tracker.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.core.Is.is;

import org.springframework.http.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;

import t_tracker.JsonUtil;
import t_tracker.TTrackerApplication;
import t_tracker.model.Client;
import t_tracker.model.Coordinates;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TTrackerApplication.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = Replace.ANY)
class ClientControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    private Client willy;

    @BeforeEach
    void setUp() {
        willy = new Client("Willy Wonka", "ChocolateMan", "chocofactory@org.com", "iS2oompaloompas");
        willy.setPhoneNumber(931313444);
        willy.setHomeLocation(new Coordinates(46.991750174814946, 15.907980069174572));
    }

    @Test
    void whenSignupNewClient_thenReturnClientAnd200() throws Exception {
        
        mvc.perform( post("/client/signup").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(willy)) )
            .andExpect( status().isOk() )
            .andExpect( jsonPath("$.name", is(willy.getName())) )
            .andExpect( jsonPath("$.username", is(willy.getUsername())) )
            .andExpect( jsonPath("$.email", is(willy.getEmail())) )
            .andExpect( jsonPath("$.username", is(willy.getUsername())) )
            .andExpect( jsonPath("$.phoneNumber", is(willy.getPhoneNumber())) );

    }

    @Test
    void whenSignupDuplicateClient_ThenReturnNullAnd409() throws Exception {

        mvc.perform( post("/client/signup").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(willy)) )
            .andExpect( status().isOk() );

        mvc.perform( post("/client/signup").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(willy)) )
            .andExpect( status().isConflict() );
    
    }

}
