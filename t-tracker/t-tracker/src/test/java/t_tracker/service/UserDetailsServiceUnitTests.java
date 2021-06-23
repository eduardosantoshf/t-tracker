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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import t_tracker.model.User;
import t_tracker.model.Client;
import t_tracker.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceUnitTests {

    @Mock(lenient = true)
    private PasswordEncoder bCryptPasswordEncoder;

    @Mock(lenient = true)
    private UserRepository<t_tracker.model.User> userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    User validUser, invalidUser;
    String validUsername, invalidUsername;

    @BeforeEach
    void setUp() {
        validUsername = "Hary Pota";
        invalidUsername = "Not Hary Pota";
        validUser = new Client("Wizard", validUsername, "wizards@hogwarts.com", "wand1234");

        Mockito.when(userRepository.findByUsername(validUsername)).thenReturn(Optional.of(validUser));
        Mockito.when(userRepository.findByUsername(invalidUsername)).thenReturn(Optional.ofNullable(null));
    }

    @Test
    void whenLoadByValidUsername_thenReturnUserDetails() {
        UserDetails details = userDetailsService.loadUserByUsername(validUsername);

        assertThat( details.getUsername(), is(validUsername));
    }

    @Test
    void whenLoadByInvalidUsername_thenThrow() {
        UsernameNotFoundException throwException = assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(invalidUsername));

        assertThat( throwException.getMessage(), is(invalidUsername));
    }

}
