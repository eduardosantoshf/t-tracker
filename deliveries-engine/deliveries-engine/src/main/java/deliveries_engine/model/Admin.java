package deliveries_engine.model;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Table(name = "Admin")
public class Admin extends User {

    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    // private int id;

    public Admin(){

    }

    public Admin(String name, String email, String username, String password, int phoneNumber){
        super(name, email, username, password, phoneNumber);
    }

    @Autowired
    public Admin(String name, String email, String username, String password, int phoneNumber, String address, String city, String zipCode){
        super(name, email, username, password, phoneNumber, address, city, zipCode);
    }

}
