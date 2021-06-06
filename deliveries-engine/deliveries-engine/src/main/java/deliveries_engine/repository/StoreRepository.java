package deliveries_engine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import deliveries_engine.model.Store;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Integer>{
    Optional<Store> findStoreByName(String name);
}
