package deliveries_engine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;

@Entity
public class Rider extends User {

    @Column(name = "status", nullable = true)
    private int status;

    @OneToMany(mappedBy = "rider")
    @JsonIgnore
    private List<Delivery> deliveries;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    public Rider() {}

    @Autowired
    public Rider(String name, String email, String username, String password, int phoneNumber){
        super(name, email, username, password, phoneNumber);
        this.status = 0;
    }

    @Autowired
    public Rider(String name, String email, String username, String password, int phoneNumber, String address, String city, String zipCode){
        super(name, email, username, password, phoneNumber, address, city, zipCode);
        this.status = 0;
    }

    @Autowired
    public Rider(String name, String email, String username, String password, int phoneNumber, String address, String city, String zipCode, double latitude, double longitude){
        super(name, email, username, password, phoneNumber, address, city, zipCode);
        this.status = 0;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String toString(){
        return "Rider: " + this.getUsername() + " " + this.getEmail();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
