package t_tracker.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.core.Is.is;

import org.springframework.http.MediaType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import t_tracker.JsonUtil;
import t_tracker.TTrackerApplication;
import t_tracker.model.Client;
import t_tracker.model.Coordinates;
import t_tracker.service.ClientServiceImpl;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TTrackerApplication.class)
@AutoConfigureMockMvc
class ClientControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ClientServiceImpl clientService;

    Client willy;

    @BeforeEach
    void setUp() {
        willy = new Client("Willy Wonka", "ChocolateMan", "chocofactory@org.com", "iS2oompaloompas");
        willy.setPhoneNumber(931313444);
        willy.setHomeLocation(new Coordinates(46.991750174814946, 15.907980069174572));
    }

    @AfterEach
    void cleanUp() {
        reset(clientService);
    }

    @Test
    void whenSignupNewClient_thenReturnClientAnd200() throws Exception {
        
        System.out.println("Test: " + willy);
        when( clientService.registerClient(willy) ).thenReturn(willy);

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

        when( clientService.registerClient(any()) ).thenThrow( ResponseStatusException.class );

        mvc.perform( post("/client/signup").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(willy)) )
            .andExpect( status().isConflict() );
    
    }

}
