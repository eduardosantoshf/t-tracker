package deliveries_engine.service;

import java.util.Optional;

import deliveries_engine.exception.ErrorWarning;
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
            throw new ErrorWarning("Rider is already registered");
        }

        // new rider object
        Rider newRider = rider;

        // check wether username or email is already used
        Optional<User> checkUsername = userRepository.findByUsername(rider.getUsername());
        Optional<User> checkEmail = userRepository.findByEmail(rider.getEmail());

        // in that case, throw exceptions
        if(checkUsername.isPresent()){
            throw new Exception("Username already exists");
        }
        else if(checkEmail.isPresent()){
            throw new Exception("Email already exists");
        }

        // set encoded password
        newRider.setPassword(passwordEncoder.encode(newRider.getPassword()));

        // save rider in database
        try{
            riderRepository.save(newRider);
        }catch (Exception e){
            throw new ErrorWarning("Failed to Register user");
        };


        return newRider;

    }

}
