package t_tracker.model;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Entity
public class Client extends User {

    @OneToMany(mappedBy = "client")
    private List<Order> orderlist;

    public Client(String name, String username, String email, String password, int phoneNumber) {
        super(name, username, email, password, phoneNumber);
    }

    public Client(String name, String username, String email, String password, Coordinates homeLocation) {
        super(name, username, email, password, homeLocation);
    }

    @Autowired
    public Client(String name, String username, String email, String password, int phoneNumber, Coordinates homeLocation) {
        super(name, username, email, password, phoneNumber, homeLocation);
    }

}
