package t_tracker.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import t_tracker.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    
    public Order findById(UUID id);
    public List<Order> findAll();
    public List<Order> findByClientId(int id);
    public List<Order> findByDriverId(int id);
    public List<Order> findByIsDelivered(boolean isDelivered);

}
