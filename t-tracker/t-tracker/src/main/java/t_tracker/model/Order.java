package t_tracker.model;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name="orders")
public class Order {
    
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name="client_id")
    private Client client;

    @OneToOne
    @JoinColumn(name = "coordinates_id", insertable=false, updatable=false)
    private Coordinates pickupLocation;

    @OneToOne
    @JoinColumn(name = "coordinates_id", insertable=false, updatable=false)
    private Coordinates deliverLocation;

    @Column(name = "order_total", nullable = false)
    private int orderTotal;

    @Column(name = "driver_id")
    private int driverId;
    
    @OneToMany(mappedBy = "order")
    private List<Stock> listOfProducts;

    @Column(name = "is_delivered", nullable = false)
    private boolean isDelivered;
    
    public Order() {}

    @Autowired
    public Order(Client client, Coordinates pickupLocation, Coordinates deliverLocation, int orderTotal, List<Stock> listOfProducts) {
        this.client = client;
        this.pickupLocation = pickupLocation;
        this.deliverLocation = deliverLocation;
        this.orderTotal = orderTotal;
        this.listOfProducts = listOfProducts;
        this.isDelivered = false;
    }

}
