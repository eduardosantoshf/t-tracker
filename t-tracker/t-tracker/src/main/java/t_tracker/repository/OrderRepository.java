package t_tracker.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import t_tracker.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    
    public Optional<Order> findById(UUID id);
    public List<Order> findByClientUsername(String username);
    public List<Order> findByIsDelivered(boolean isDelivered);
    public List<Order> findAll();

}
