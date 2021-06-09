package t_tracker.model;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
public class Order {
    
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "client_id", nullable = false)
    private int clientId;

    @Column(name = "pickup_location", nullable = false)
    private Coordinates pickupLocation;

    @Column(name = "deliver_location", nullable = false)
    private Coordinates deliverLocation;

    @Column(name = "order_total", nullable = false)
    private int orderTotal;

    @Column(name = "driver_id")
    private int driverId;

    @Column(name = "list_of_products", nullable = false)
    private List<Stock> listOfProducts;

    @Column(name = "is_delivered", nullable = false)
    private boolean isDelivered;

    public Order(int clientId, Coordinates pickupLocation, Coordinates deliverLocation, int orderTotal, List<Stock> listOfProducts) {
        this.clientId = clientId;
        this.pickupLocation = pickupLocation;
        this.deliverLocation = deliverLocation;
        this.orderTotal = orderTotal;
        this.listOfProducts = listOfProducts;
        this.isDelivered = false;
    }

}
