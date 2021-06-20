package t_tracker.model;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Client")
public class Client extends User {

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "clientUsername", cascade = CascadeType.ALL)
    private List<Order> orderlist;

    public Client() {}

    public Client(String name, String username, String email, String password) {
        super(name, username, email, password);
    }

    @Autowired
    public Client(String name, String username, String email, String password, int phoneNumber, Coordinates homeLocation) {
        super(name, username, email, password, phoneNumber, homeLocation);
    }

    public List<Order> getOrderlist() {
        return this.orderlist;
    }

    public void setOrderlist(List<Order> orderList) {
        this.orderlist = orderList;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Client)) {
            return false;
        }
        Client client = (Client) o;
        return Objects.equals(orderlist, client.orderlist);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(orderlist);
    }

    @Override
    public String toString() {

        return super.toString() + "{" +
            " orderlist='" + getOrderlist() + "'" +
            "}";
    }

}
