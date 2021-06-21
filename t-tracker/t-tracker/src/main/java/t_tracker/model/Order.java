package t_tracker.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name="orders")
public class Order {
    
    @Id
    @GeneratedValue
    private UUID id;
    
    @JoinColumn(name = "client_id", insertable = false, updatable = false)
    @ManyToOne(targetEntity = Client.class, fetch = FetchType.EAGER)
    private Client client;

    @Column(name="client_id")
    private int clientId;

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
    
    @JsonManagedReference(value="order-stock")
    @OneToMany(mappedBy = "order")
    private List<Stock> listOfProducts;

    @Column(name = "is_delivered", nullable = false)
    private boolean isDelivered;
    
    public Order() {}

    @Autowired
    public Order(int clientId, Coordinates pickupLocation, Coordinates deliverLocation, Double orderTotal, int labId, List<Stock> listOfProducts) {
        this.clientId = clientId;
        this.pickupLocation = pickupLocation;
        this.deliverLocation = deliverLocation;
        this.orderTotal = orderTotal;
        this.labId = labId;
        this.listOfProducts = listOfProducts;
        this.isDelivered = false;
    }

    public Order(Client client) {
        this.clientId = client.getId();
        listOfProducts = new ArrayList<>();
        this.isDelivered = false;
    }

    public UUID getId() {
        return this.id;
    }

    public int getClientId() {
        return this.clientId;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
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

    public void addProduct(Stock stock) {
        this.listOfProducts.add(stock);
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

    public Double getTotalPrice() {
        Double totalPrice = 0.0;
        
        for (Stock s : listOfProducts) {
            totalPrice += s.getTotalPrice();
        }
        
        return totalPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Order)) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(clientId, order.clientId) && Objects.equals(pickupLocation, order.pickupLocation) && Objects.equals(deliverLocation, order.deliverLocation) && Objects.equals(orderTotal, order.orderTotal) && driverId == order.driverId && Objects.equals(labId, order.labId) && Objects.equals(listOfProducts, order.listOfProducts) && isDelivered == order.isDelivered;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientId, pickupLocation, deliverLocation, orderTotal, driverId, labId, listOfProducts, isDelivered);
    }

}
