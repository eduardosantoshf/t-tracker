package deliveries_engine.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import deliveries_engine.model.Rider;

import java.util.List;

@Repository
public interface RiderRepository extends JpaRepository<Rider, Long>{

    Optional<Rider> findByUsername(String username);
    Rider findById(int id);
    List<Rider> findByStatus(int status);
    List<Rider> findAll();


}
