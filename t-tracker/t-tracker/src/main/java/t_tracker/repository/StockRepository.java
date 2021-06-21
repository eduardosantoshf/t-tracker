package t_tracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import t_tracker.model.Stock;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {

    List<Stock> findAll();
    
}
