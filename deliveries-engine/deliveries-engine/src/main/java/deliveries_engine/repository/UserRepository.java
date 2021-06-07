package deliveries_engine.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import deliveries_engine.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    Optional<User> findByEmail(String email);
    public User findById(int id);
    Optional<User> findByUsername(String username);
    public User findByPhoneNumber(int phoneNumber);

}