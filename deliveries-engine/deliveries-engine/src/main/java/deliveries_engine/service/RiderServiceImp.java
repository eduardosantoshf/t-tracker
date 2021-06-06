package deliveries_engine.service;

import java.util.Optional;

import deliveries_engine.model.Rider;
import deliveries_engine.model.User;
import deliveries_engine.repository.RiderRepository;
import deliveries_engine.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RiderServiceImp implements RiderService {

    @Autowired
    private RiderRepository riderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public RiderServiceImp(RiderRepository riderRepository, UserRepository userRepository){
        this.riderRepository = riderRepository;
        this.userRepository = userRepository;
    }

    public Rider registerRider(Rider rider) throws Exception{
        
        // cehck if rider is already registered 
        Optional<Rider> potentialRider = riderRepository.findByUsername(rider.getUsername());

        if (potentialRider.isPresent()){
            throw new Exception("Rider is already registered");
        }

        // new rider object
        Rider newRider = rider;

        // check wether username or email is already used
        Optional<User> check_username = userRepository.findByUsername(rider.getUsername());
        Optional<User> check_email = userRepository.findByEmail(rider.getEmail());

        // in that case, throw exceptions
        if(check_username.isPresent()){
            throw new Exception("Username already exists");
        }
        else if(check_email.isPresent()){
            throw new Exception("Email already exists");
        }

        // set encoded password
        newRider.setPassword(passwordEncoder.encode(newRider.getPassword()));

        // save rider in database
        riderRepository.save(newRider);

        return newRider;

    }

}
