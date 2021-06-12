package t_tracker.model;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Entity
@Table(name = "Client")
public class Client extends User {

    @OneToMany(mappedBy = "client")
    private List<Order> orderlist;

    public Client() {}

    public Client(String name, String username, String email, String password) {
        super(name, username, email, password);
    }

    @Autowired
    public Client(String name, String username, String email, String password, int phoneNumber, Coordinates homeLocation) {
        super(name, username, email, password, phoneNumber, homeLocation);
    }

    @Override
    public String toString() {

        return super.toString() + "{" +
            " orderlist='" + getOrderlist() + "'" +
            "}";
    }

    private List<Order> getOrderlist() {
        return this.orderlist;
    }

}
