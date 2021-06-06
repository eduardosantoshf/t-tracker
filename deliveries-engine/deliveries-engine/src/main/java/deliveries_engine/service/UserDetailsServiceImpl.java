package deliveries_engine.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import deliveries_engine.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<deliveries_engine.model.User> u = userRepository.findByUsername(username);
        if (u.isPresent()) {
            deliveries_engine.model.User user = u.get();
            List<SimpleGrantedAuthority> l = new ArrayList<SimpleGrantedAuthority>();
            return new User(user.getUsername(),user.getPassword(),l);
        }
        throw new UsernameNotFoundException(username);
    }
}
