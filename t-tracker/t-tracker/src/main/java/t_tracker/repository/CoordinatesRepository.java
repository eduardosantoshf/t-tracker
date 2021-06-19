package t_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import t_tracker.model.Coordinates;

@Repository
public interface CoordinatesRepository extends JpaRepository<Coordinates, Integer> {
    
}
