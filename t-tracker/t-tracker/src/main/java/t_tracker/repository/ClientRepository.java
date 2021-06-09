package t_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import t_tracker.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    public Client findById(int id);
    public Client findByEmail(String email);
    public Client findByPhoneNumber(int phoneNumber);

}
