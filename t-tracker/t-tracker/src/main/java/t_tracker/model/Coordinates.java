package t_tracker.model;

import java.util.Objects;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;

@Entity
public class Coordinates {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @OneToOne(mappedBy = "location")
    private Lab lab;

    @OneToOne(mappedBy = "homeLocation")
    private User user;

    @OneToOne(mappedBy = "pickupLocation")
    private Order pickupOrder;

    @OneToOne(mappedBy = "deliverLocation")
    private Order deliverOrder;

    public Coordinates() {}

    @Autowired
    public Coordinates(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private Integer getId() {
        return id;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Coordinates)) {
            return false;
        }
        Coordinates coordinates = (Coordinates) o;
        return id == coordinates.id && Objects.equals(latitude, coordinates.latitude) && Objects.equals(longitude, coordinates.longitude) && Objects.equals(lab, coordinates.lab) && Objects.equals(user, coordinates.user) && Objects.equals(pickupOrder, coordinates.pickupOrder) && Objects.equals(deliverOrder, coordinates.deliverOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, latitude, longitude, lab, user, pickupOrder, deliverOrder);
    }

    @Override
    public String toString() {
        return "{" +
            "latitude=" + getLatitude() + "" +
            ", longitude=" + getLongitude() + "" +
            "}";
    }
    
}
