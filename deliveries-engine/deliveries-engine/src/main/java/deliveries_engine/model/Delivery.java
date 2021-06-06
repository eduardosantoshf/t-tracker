package deliveries_engine.model;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Table(name = "Delivery")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "comission", nullable = false)
    private double commission;

    @Column(name = "status", nullable = false)
    private boolean status;

    @ManyToOne
    @JoinColumn(name="rider_id")
    //@JsonBackReference
    private Rider rider;

    @ManyToOne
    @JoinColumn(name="store_id")
    private Store store;

    @Column(name = "pickup_location", nullable = false)
    private String pickupLocation;

    @Column(name = "delivery_location", nullable = false)
    private String deliveryLocation;

    @Column(name = "duration", nullable = false)
    private double duration;

    public Delivery() {}

    @Autowired
    public Delivery(String name, double commission, Rider rider, Store store, String pickupLocation, String deliveryLocation, double duration) {
        this.name = name;
        this.commission = commission;
        this.status = false;
        this.rider = rider;
        this.store = store;
        this.pickupLocation = pickupLocation;
        this.deliveryLocation = deliveryLocation;
        this.duration = duration;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCommission() {
        return this.commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    public boolean getStatus() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Rider getRider() {
        return this.rider;
    }

    public void setRider(Rider rider) {
        this.rider = rider;
    }

    public Store getStore() {
        return this.store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public String getPickupLocation() {
        return this.pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDeliveryLocation() {
        return this.deliveryLocation;
    }

    public void setDeliveryLocation(String deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    public double getDuration() {
        return this.duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }


}
