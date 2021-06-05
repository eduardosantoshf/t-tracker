package deliveries_engine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import deliveries_engine.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    
    public User findById(int id);
    public User findByUsername(String username);
    public User findByPhoneNumber(int phoneNumber);

}