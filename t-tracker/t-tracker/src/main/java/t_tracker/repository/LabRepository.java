package t_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import t_tracker.model.Lab;

@Repository
public interface LabRepository extends JpaRepository<Lab, Integer> {

    public Lab findById(int id);
    
}
