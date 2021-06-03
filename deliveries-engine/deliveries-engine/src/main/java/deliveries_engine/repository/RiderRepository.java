package deliveries_engine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import deliveries_engine.model.Rider;

@Repository
public interface RiderRepository extends JpaRepository<Rider, Integer>{
    Rider findByName(String name);
    Rider findById(int id);
}
