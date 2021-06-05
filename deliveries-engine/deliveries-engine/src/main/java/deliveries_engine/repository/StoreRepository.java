package deliveries_engine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import deliveries_engine.model.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long>{
    
    public Store findById(int id);
    public Store findByName(String name);

}
