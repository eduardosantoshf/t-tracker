package deliveries_engine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import deliveries_engine.model.Rider;

import java.util.List;

@Repository
public interface RiderRepository extends JpaRepository<Rider, Long>{
    
    public Rider findById(int id);
    public List<Rider> findByStatus(boolean status);

}
