package t_tracker.model;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;
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
    private Double orderTotal;

    @Column(name = "driver_id")
    private int driverId;
    
    @OneToMany(mappedBy = "order")
    private List<Stock> listOfProducts;

    @Column(name = "is_delivered", nullable = false)
    private boolean isDelivered;
    
    public Order() {}

    @Autowired
    public Order(Client client, Coordinates pickupLocation, Coordinates deliverLocation, Double orderTotal, List<Stock> listOfProducts) {
        this.client = client;
        this.pickupLocation = pickupLocation;
        this.deliverLocation = deliverLocation;
        this.orderTotal = orderTotal;
        this.listOfProducts = listOfProducts;
        this.isDelivered = false;
    }


    public UUID getId() {
        return this.id;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Coordinates getPickupLocation() {
        return this.pickupLocation;
    }

    public void setPickupLocation(Coordinates pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public Coordinates getDeliverLocation() {
        return this.deliverLocation;
    }

    public void setDeliverLocation(Coordinates deliverLocation) {
        this.deliverLocation = deliverLocation;
    }

    public Double getOrderTotal() {
        return this.orderTotal;
    }

    public void setOrderTotal(Double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public int getDriverId() {
        return this.driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public List<Stock> getListOfProducts() {
        return this.listOfProducts;
    }

    public void setListOfProducts(List<Stock> listOfProducts) {
        this.listOfProducts = listOfProducts;
    }

    public boolean isIsDelivered() {
        return this.isDelivered;
    }

    public boolean getIsDelivered() {
        return this.isDelivered;
    }

    public void setIsDelivered(boolean isDelivered) {
        this.isDelivered = isDelivered;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Order)) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(client, order.client) && Objects.equals(pickupLocation, order.pickupLocation) && Objects.equals(deliverLocation, order.deliverLocation) && Objects.equals(orderTotal, order.orderTotal) && driverId == order.driverId && Objects.equals(listOfProducts, order.listOfProducts) && isDelivered == order.isDelivered;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, client, pickupLocation, deliverLocation, orderTotal, driverId, listOfProducts, isDelivered);
    }


}
