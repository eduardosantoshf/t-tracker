package deliveries_engine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import deliveries_engine.model.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer>{

    public Admin findById(int id);
    
}
