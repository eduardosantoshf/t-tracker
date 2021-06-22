package t_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import t_tracker.repository.ProductRepository;
import t_tracker.model.Product;

@SpringBootApplication
public class TTrackerApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(TTrackerApplication.class, args);
        
    }
}
