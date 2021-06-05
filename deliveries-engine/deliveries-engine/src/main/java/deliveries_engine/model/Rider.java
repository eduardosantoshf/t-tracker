package deliveries_engine.model;

import java.util.List;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Table(name = "Rider")
public class Rider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "status", nullable = false)
    private boolean status;

    @OneToMany(mappedBy = "rider")
    private List<Delivery> deliveries;

    public Rider() {}

    @Autowired
    public Rider(User user) {
        this.user = user;
        this.status = false;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<Delivery> getDeliveries() {
        return this.deliveries;
    }

    public void setDeliveries(List<Delivery> deliveries) {
        this.deliveries = deliveries;
    }

    public void addDelivery(Delivery delivery) {
        this.deliveries.add(delivery);
    }

    public void deleteDelivery(Delivery delivery) {
        this.deliveries.remove(delivery);
    }

}
