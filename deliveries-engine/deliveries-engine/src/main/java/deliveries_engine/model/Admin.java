package deliveries_engine.model;

import javax.persistence.*;

@Entity
@Table(name = "Admin")
public class Admin extends User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public Admin(String name, String email, String username, String password, int phoneNumber, String address, String city, String zipCode){
        super(name, email, username, password, phoneNumber, address, city, zipCode);
    }

}
