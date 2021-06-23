package t_tracker.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="orders")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @JsonIgnore
    @JoinColumn(name = "client_id", insertable = false, updatable = false)
    @ManyToOne(targetEntity = Client.class)
    private Client client;

    @Column(name="client_id")
    private int clientId;

    @OneToOne
    @JoinColumn(name = "pickup_id")
    @JsonManagedReference("pickup")
    private Coordinates pickupLocation;

    @OneToOne
    @JoinColumn(name = "deliver_id")
    @JsonManagedReference("deliver")
    private Coordinates deliverLocation;

    @Column(name = "order_total", nullable = false)
    private Double orderTotal;

    @Column(name = "driver_id")
    private int driverId;
    
    @OneToMany(mappedBy = "orderId", cascade = CascadeType.ALL)
    private List<OrderItem> products;

    @Column(name = "is_delivered", nullable = false)
    private String status;

    @Column(name = "rating", nullable = false)
    private int rating;
    
    public Order() {}

    @Autowired
    public Order(Client client, Coordinates pickupLocation, Coordinates deliverLocation, Double orderTotal, List<OrderItem> products) {
        this.client = client;
        this.clientId = client.getId();
        this.pickupLocation = pickupLocation;
        this.deliverLocation = deliverLocation;
        this.orderTotal = orderTotal;
        this.products = products;
        this.status = "Pending";
    }

    public Order(Client client) {
        this.client = client;
        this.clientId = client.getId();
        products = new ArrayList<>();
        this.status = "Pending";
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
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

    public List<OrderItem> getProducts() {
        return this.products;
    }

    public void setProducts(List<OrderItem> products) {
        this.products = products;
    }

    public void addProduct(OrderItem stock) {
        this.products.add(stock);
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getRating() {
        return this.rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Double getTotalPrice() {
        Double totalPrice = 0.0;
        
        for (OrderItem s : products) {
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
        return Objects.equals(id, order.id) && Objects.equals(clientId, order.clientId) && Objects.equals(pickupLocation, order.pickupLocation) && Objects.equals(deliverLocation, order.deliverLocation) && Objects.equals(orderTotal, order.orderTotal) && driverId == order.driverId && Objects.equals(products, order.products) && status == order.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clientId, pickupLocation, deliverLocation, orderTotal, driverId, products, status);
    }


    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", clientId='" + getClientId() + "'" +
            ", pickupLocation='" + getPickupLocation() + "'" +
            ", deliverLocation='" + getDeliverLocation() + "'" +
            ", orderTotal='" + getOrderTotal() + "'" +
            ", driverId='" + getDriverId() + "'" +
            ", products='" + getProducts() + "'" +
            ", status='" + getStatus() + "'" +
            ", rating='" + getRating() + "'" +
            "}";
    }


}
