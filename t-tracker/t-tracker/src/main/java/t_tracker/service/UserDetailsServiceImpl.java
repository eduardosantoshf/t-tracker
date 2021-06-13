package t_tracker.service;

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

import t_tracker.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    PasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository<t_tracker.model.User> userRepository;

    public UserDetailsServiceImpl(UserRepository<t_tracker.model.User> userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<t_tracker.model.User> u = userRepository.findByUsername(username);
        if (u.isPresent()) {
            t_tracker.model.User user = u.get();
            List<SimpleGrantedAuthority> l = new ArrayList<SimpleGrantedAuthority>();
            return new User(user.getUsername(),user.getPassword(),l);
        }
        throw new UsernameNotFoundException(username);
    }
}
