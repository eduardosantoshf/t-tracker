package t_tracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import t_tracker.model.User;

@Repository
public interface UserRepository<T extends User> extends JpaRepository<T, Integer> {

    Optional<T> findById(Integer id);
    Optional<T> findByUsername(String username);
    Optional<T> findByEmail(String email);
    Optional<T> findByPhoneNumber(int phoneNumber);
    List<T> findAll();

}
