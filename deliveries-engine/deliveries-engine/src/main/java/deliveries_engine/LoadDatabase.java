package deliveries_engine;

import deliveries_engine.model.Admin;
import deliveries_engine.repository.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {

        return args -> {

            //Admin admin = new Admin("admin", "admin@ua.pt", "admin", passwordEncoder.encode("password"), 912345678);

            //log.info("Preloading " + adminRepository.save(admin));

        };

    }


}
