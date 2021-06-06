package deliveries_engine.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import deliveries_engine.model.Rider;

@Repository
public interface RiderRepository extends JpaRepository<Rider, Long>{

    Optional<Rider> findByUsername(String username);
    
}
