package deliveries_engine.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import deliveries_engine.model.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long>{
    
    public Store findById(int id);
    Optional<Store> findByName(String name);
    public List<Store> findAll();

}
