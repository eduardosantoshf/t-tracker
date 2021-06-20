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

    @Column(name="client_username")
    private String clientUsername;

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

    @Column(name="lab_id")
    private int labId;
    
    @OneToMany(mappedBy = "order")
    private List<Stock> listOfProducts;

    @Column(name = "is_delivered", nullable = false)
    private boolean isDelivered;
    
    public Order() {}

    @Autowired
    public Order(String clientUsername, Coordinates pickupLocation, Coordinates deliverLocation, Double orderTotal, int labId, List<Stock> listOfProducts) {
        this.clientUsername = clientUsername;
        this.pickupLocation = pickupLocation;
        this.deliverLocation = deliverLocation;
        this.orderTotal = orderTotal;
        this.labId = labId;
        this.listOfProducts = listOfProducts;
        this.isDelivered = false;
    }

    public UUID getId() {
        return this.id;
    }

    public String getClientUsername() {
        return this.clientUsername;
    }

    public void setClientUsername(String clientUsername) {
        this.clientUsername = clientUsername;
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

    public int getLabId() {
        return this.labId;
    }

    public void setLabId(int labId) {
        this.labId = labId;
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
        return Objects.equals(id, order.id) && Objects.equals(clientUsername, order.clientUsername) && Objects.equals(pickupLocation, order.pickupLocation) && Objects.equals(deliverLocation, order.deliverLocation) && Objects.equals(orderTotal, order.orderTotal) && driverId == order.driverId && Objects.equals(labId, order.labId) && Objects.equals(listOfProducts, order.listOfProducts) && isDelivered == order.isDelivered;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientUsername, pickupLocation, deliverLocation, orderTotal, driverId, labId, listOfProducts, isDelivered);
    }

}
