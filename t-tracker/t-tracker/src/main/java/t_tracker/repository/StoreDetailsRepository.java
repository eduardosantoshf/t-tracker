package t_tracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import t_tracker.model.StoreDetails;

@Repository
public interface StoreDetailsRepository extends JpaRepository<StoreDetails, Integer> {
    
    public List<StoreDetails> findAll();

}
