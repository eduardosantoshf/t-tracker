package t_tracker.controller;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.core.Is.is;

import org.springframework.http.MediaType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import t_tracker.JsonUtil;
import t_tracker.TTrackerApplication;
import t_tracker.model.Client;
import t_tracker.model.Coordinates;
import t_tracker.service.ClientServiceImpl;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = TTrackerApplication.class)
@AutoConfigureMockMvc
public class ClientControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ClientServiceImpl clientService;


    @AfterEach
    void cleanUp() {
        reset(clientService);
    }

    @Test
    void whenSignupNewClient_thenReturnClientAnd200() throws Exception {
        Client newClient = new Client("Willy Wonka", "ChocolateMan", "chocofactory@org.com", "iS2oompaloompas");
        newClient.setPhoneNumber(931313444);
        newClient.setHomeLocation(new Coordinates(46.991750174814946, 15.907980069174572));
        
        when( clientService.registerClient(newClient) ).thenReturn(newClient);

        mvc.perform( post("/client/signup").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(newClient)) )
            .andExpect( status().isOk() )
            .andExpect( jsonPath("$.name", is(newClient.getName())) )
            .andExpect( jsonPath("$.username", is(newClient.getUsername())) )
            .andExpect( jsonPath("$.email", is(newClient.getEmail())) )
            .andExpect( jsonPath("$.username", is(newClient.getUsername())) )
            .andExpect( jsonPath("$.phoneNumber", is(newClient.getPhoneNumber())) )
            .andExpect( jsonPath("$.homeLocation", is(newClient.getHomeLocation())) );

    }

    @Test
    void whenSignupDuplicateClient_ThenReturnNullAnd409() throws Exception {
        Client newClient = new Client("Willy Wonka", "ChocolateMan", "chocofactory@org.com", "iS2oompaloompas");
        newClient.setPhoneNumber(931313444);
        newClient.setHomeLocation(new Coordinates(46.991750174814946, 15.907980069174572));

        when( clientService.registerClient(newClient) ).thenReturn(null);

        mvc.perform( post("/client/signup").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(newClient)) )
            .andExpect( status().isConflict() );
    
    }

}
