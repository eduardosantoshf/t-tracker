package t_tracker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import t_tracker.model.Lab;

@Repository
public interface LabRepository extends JpaRepository<Lab, Integer> {

    @Nullable
    public Lab save();

    public Optional<Lab> findById(int id);
    public List<Lab> findAll();
    
}
