package t_tracker.model;

import java.util.Objects;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.beans.factory.annotation.Autowired;

@Entity
public class Coordinates {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Integer id;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @JsonIgnore
    @OneToOne(mappedBy = "location")
    private Lab lab;

    @JsonIgnore
    @OneToOne(mappedBy = "homeLocation")
    private User user;

    @OneToOne
    @JoinColumn(name = "pickup_id")
    @JsonBackReference("pickup")
    private Order pickup;

    @OneToOne
    @JoinColumn(name = "deliver_id")
    @JsonBackReference("deliver")
    private Order deliver;

    public Coordinates() {}

    @Autowired
    public Coordinates(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Integer getId() {
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
        return id == coordinates.id && Objects.equals(latitude, coordinates.latitude) && Objects.equals(longitude, coordinates.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, latitude, longitude);
    }

    @Override
    public String toString() {
        return "{" +
            "latitude=" + getLatitude() + "" +
            ", longitude=" + getLongitude() + "" +
            "}";
    }
    
}
