package deliveries_engine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import deliveries_engine.model.Delivery;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Integer>{
    
}
