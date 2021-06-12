package t_tracker.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import t_tracker.model.Client;
import t_tracker.model.Coordinates;
import t_tracker.repository.ClientRepository;

@ExtendWith(MockitoExtension.class)
public class ClientServiceUnitTests {

    @Mock( lenient = true)
    private ClientRepository clientRepository;

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
        emailUsedUser = new Client("Common Name", "NotSoCommonUsername", usedEmail, "CommonPassword1234");
        usernameUsedUser = new Client("Common Name", usedUsername, "notsocommonemail@org.com", "CommonPassword1234");
        
        Mockito.when(clientRepository.findByEmail(neo.getEmail())).thenReturn(Optional.ofNullable(null));
        Mockito.when(clientRepository.findByUsername(neo.getUsername())).thenReturn(Optional.ofNullable(null));

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
    void whenRegisterClientWithDuplicateEmail_thenThrowException() {
        ResponseStatusException exceptionThrown = assertThrows( ResponseStatusException.class, () -> { clientService.registerClient(emailUsedUser); });

        assertThat( exceptionThrown.getMessage(), is("The provided email is already being used.") );

        verifyFindByEmailIsCalledOnce(emailUsedUser.getEmail());
        verifyFindByUsernameIsCalledOnce(emailUsedUser.getUsername());
    }

    @Test
    void whenRegisterClientWithDuplicateUsername_thenThrowException() {
        ResponseStatusException exceptionThrown = assertThrows( ResponseStatusException.class, () -> { clientService.registerClient(usernameUsedUser); });

        assertThat( exceptionThrown.getMessage(), is("The provided username is already being used.") );

        verifyFindByEmailIsCalledOnce(usernameUsedUser.getEmail());
        verifyFindByUsernameIsCalledOnce(usernameUsedUser.getUsername());
    }

    private void verifyFindByEmailIsCalledOnce(String email) {
        Mockito.verify(clientRepository, VerificationModeFactory.times(1)).findByEmail(email);
    }
    private void verifyFindByUsernameIsCalledOnce(String username) {
        Mockito.verify(clientRepository, VerificationModeFactory.times(1)).findByEmail(username);
    }
    
}