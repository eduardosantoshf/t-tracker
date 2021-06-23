package t_tracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import t_tracker.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    
    public Optional<Order> findById(int id);
    public List<Order> findByClientId(int id);
    public List<Order> findByStatus(String status);
    public List<Order> findAll();

}
