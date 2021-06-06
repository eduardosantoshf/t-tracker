package deliveries_engine.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

import javax.persistence.*;

@Entity
public class Rider extends User {

    @Column(name = "status", nullable = false)
    private int status;

    @OneToMany(mappedBy = "rider")
    @JsonIgnore
    private List<Delivery> deliveries;

    public Rider(String name, String email, String username, String password, int phoneNumber, String address, String city, String zipCode, int status){
        super(name, email, username, password, phoneNumber, address, city, zipCode);
        this.status = status;
    }

    public Rider() {

    }

    public int getStatus() {
        return status;
    }

    public List<Delivery> getDeliveries() {
        return deliveries;
    }

    public String toString(){
        return "Rider: " + this.getUsername() + " " + this.getEmail();
    }
}