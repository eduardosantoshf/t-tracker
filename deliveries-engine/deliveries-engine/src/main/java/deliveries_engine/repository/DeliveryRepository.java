package deliveries_engine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import deliveries_engine.model.Delivery;

import java.util.List;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Long>{
    
    public Delivery findById(int id);
    public List<Delivery> findByStatus(boolean status);

}
