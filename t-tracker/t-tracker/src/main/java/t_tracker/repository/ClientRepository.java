package t_tracker.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import t_tracker.model.Client;

@Repository
public interface ClientRepository extends UserRepository<Client> {

    List<Client> findAll();

}
