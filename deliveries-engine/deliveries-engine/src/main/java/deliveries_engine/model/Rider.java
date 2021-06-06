package deliveries_engine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;

@Entity
//@Table(name = "Rider")
public class Rider extends User {

    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    // private int id;

    @Column(name = "status", nullable = true)
    private int status;

    @OneToMany(mappedBy = "rider")
    //@JsonManagedReference
    @JsonIgnore
    private List<Delivery> deliveries;

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

    public int getId() {
        return super.getId();
    }

    public String getUsername(){
        return super.getUsername();
    }

    public String getEmail(){
        return super.getEmail();
    }

    public String getPassword(){
        return super.getPassword();
    }

    public String getName() {
        return super.getName();
    }

    public int getPhoneNumber() {
        return super.getPhoneNumber();
    }

    public String getAddress() {
        return super.getAddress();
    }

    public String getCity() {
        return super.getCity();
    }

    public String getZipCode() {
        return super.getZipCode();
    }

    public void setPassword(String password){
        super.setPassword(password);
    }

}
