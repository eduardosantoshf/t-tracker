package t_tracker.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import t_tracker.model.Client;
import t_tracker.model.Coordinates;
import t_tracker.model.Order;
import t_tracker.repository.ClientRepository;
import t_tracker.repository.CoordinatesRepository;

@ExtendWith(MockitoExtension.class)
class ClientServiceUnitTests {

    @Mock( lenient = true)
    private ClientRepository clientRepository;

    @Mock( lenient = true)
    private CoordinatesRepository coordRepository;

    @Mock( lenient = true)
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ClientServiceImpl clientService;

    private Client neo, emailUsedUser, usernameUsedUser;
    private String usedEmail = "commonemail@org.com";
    private String usedUsername = "CommonUsername";

    @BeforeEach
    void setUp() {
        neo = new Client("Thomas Anderson", "Neo", "thisisasimulation@sim.com", "keanuisbreathtaking");
        neo.setPhoneNumber(911222333);
        neo.setHomeLocation(new Coordinates(25.00198217925069, -70.99974266535844));
        emailUsedUser = new Client("Common Name", "NotSoCommonUsername", usedEmail, "CommonPassword1234", 123123123, new Coordinates(12.0, 13.0));
        usernameUsedUser = new Client("Common Name", usedUsername, "notsocommonemail@org.com", "CommonPassword1234", 123124123, new Coordinates(14.0, 13.0));
        
        Mockito.when(clientRepository.findByEmail(neo.getEmail())).thenReturn(Optional.ofNullable(null));
        Mockito.when(clientRepository.findByUsername(neo.getUsername())).thenReturn(Optional.ofNullable(null));
        Mockito.when(clientRepository.save(any(Client.class))).thenReturn(neo);
        Mockito.when(passwordEncoder.encode(any(String.class))).thenReturn(neo.getPassword());
        
        Mockito.when(clientRepository.findByEmail(emailUsedUser.getEmail())).thenReturn(Optional.of(emailUsedUser));
        Mockito.when(clientRepository.findByUsername(emailUsedUser.getUsername())).thenReturn(Optional.ofNullable(null));
        
        Mockito.when(clientRepository.findByEmail(usernameUsedUser.getEmail())).thenReturn(Optional.ofNullable(null));
        Mockito.when(clientRepository.findByUsername(usernameUsedUser.getUsername())).thenReturn(Optional.of(usernameUsedUser));

    }

    @Test
    void whenRegisterNewClient_thenReturnCreatedClient() {
        Client registeredClient = clientService.registerClient(neo);
        assertThat( registeredClient, is(neo) );

        verifyFindByEmailIsCalledOnce(neo.getEmail());
        verifyFindByUsernameIsCalledOnce(neo.getUsername());
    }

    @Test
    void whenRegisterClientWithNoHomeLocation_thenReturn409() {
        Client noHLClient = new Client("Client Name", "Client Username", "email@email.com", "1234");
        ResponseStatusException thrownException = assertThrows( ResponseStatusException.class, () -> clientService.registerClient(noHLClient) );
        assertThat( thrownException.getStatus(), is(HttpStatus.CONFLICT) );
        assertThat( thrownException.getReason(), is("Home location is required.") );

        verifyFindByEmailIsCalledOnce(noHLClient.getEmail());
        verifyFindByUsernameIsCalledOnce(noHLClient.getUsername());
    }

    @Test
    void whenRegisterClientWithDuplicateEmail_thenThrowException() {
        ResponseStatusException exceptionThrown = assertThrows( ResponseStatusException.class, () -> { clientService.registerClient(emailUsedUser); });

        assertThat( exceptionThrown.getReason(), is("The provided email is already being used.") );

        verifyFindByEmailIsCalledOnce(emailUsedUser.getEmail());
        verifyFindByUsernameIsCalledOnce(emailUsedUser.getUsername());
    }

    @Test
    void whenRegisterClientWithDuplicateUsername_thenThrowException() {
        ResponseStatusException exceptionThrown = assertThrows( ResponseStatusException.class, () -> { clientService.registerClient(usernameUsedUser); });

        assertThat( exceptionThrown.getReason(), is("The provided username is already being used.") );
        
        verifyFindByEmailIsCalledOnce(usernameUsedUser.getEmail());
        verifyFindByUsernameIsCalledOnce(usernameUsedUser.getUsername());
    }

    @Test
    void whenGetValidClientOrder_thenReturnOrders() {
        Mockito.when( clientRepository.findByUsername(usedUsername) ).thenReturn(Optional.of(usernameUsedUser));

        List<Order> orderList = clientService.getOrders(usedUsername);

        assertThat(orderList, is(usernameUsedUser.getOrderlist()));
    }

    @Test
    void whenGetInvalidClientOrder_thenReturn404() {
        ResponseStatusException thrownException = assertThrows(ResponseStatusException.class, () -> clientService.getOrders(neo.getUsername()));

        assertThat(thrownException.getStatus(), is(HttpStatus.NOT_FOUND));
        assertThat(thrownException.getReason(), is("Client not found."));
    }

    @Test
    void whenConvertClientToMap_thenReturnMappedClient() {
        Map<String, Object> mappedClient = clientService.convertClientToMap(neo);

        assertThat(mappedClient.get("name"), is(neo.getName()));
        assertThat(mappedClient.get("username"), is(neo.getUsername()));
        assertThat(mappedClient.get("email"), is(neo.getEmail()));
        assertThat(mappedClient.get("phoneNumber"), is(neo.getPhoneNumber()));
        assertThat(mappedClient.get("homeLocation"), is(neo.getHomeLocation()));
        assertThat(mappedClient.get("orderList"), is(neo.getOrderlist()));
    }

    @Test
    void whenGetClientByValidUsername_thenReturnClient() {
        Mockito.when( clientRepository.findByUsername(usedUsername) ).thenReturn(Optional.of(usernameUsedUser));

        Client clientFound = clientService.getClientByUsername(usedUsername);

        assertThat(clientFound, is(neo));

    }

    @Test
    void whenGetClientByInvalidUsername_thenReturn404() {
        Mockito.when( clientRepository.findByUsername(neo.getUsername()) ).thenReturn(Optional.ofNullable(null));

        ResponseStatusException thrownException = assertThrows(ResponseStatusException.class, () -> clientService.getClientByUsername(neo.getUsername()));

        assertThat(thrownException.getStatus(), is(HttpStatus.NOT_FOUND));
        assertThat(thrownException.getReason(), is("Client not found."));
    }

    private void verifyFindByEmailIsCalledOnce(String email) {
        Mockito.verify(clientRepository, VerificationModeFactory.times(1)).findByEmail(email);
    }
    private void verifyFindByUsernameIsCalledOnce(String username) {
        Mockito.verify(clientRepository, VerificationModeFactory.times(1)).findByUsername(username);
    }
    
}