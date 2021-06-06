package deliveries_engine.model;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "Store")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "store")
    private List<Delivery> deliveries;

    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    public Store(String name, List<Delivery> deliveries, String ownerName) {
        this.name = name;
        this.deliveries = deliveries;
        this.ownerName = ownerName;
    }

    public Store() {

    }

    public String getName() {
        return name;
    }

    public List<Delivery> getDeliveries() {
        return deliveries;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDeliveries(List<Delivery> deliveries) {
        this.deliveries = deliveries;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}
