package deliveries_engine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import deliveries_engine.model.Store;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long>{
    
    public Store findById(int id);
    public Store findByName(String name);
    public List<Store> findAll();

}
