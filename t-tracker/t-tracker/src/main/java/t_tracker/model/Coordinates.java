package t_tracker.model;

import javax.persistence.*;

@Entity
public class Coordinates {

    @Id
    @GeneratedValue
    private long id;

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

    public Coordinates(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
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
    
}
