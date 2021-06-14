package t_tracker.model;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;

public class Rider {

    private String name;

    private String email;

    private int phoneNumber;

    private int status;

    private Coordinates location;

    public Rider() {}

    @Autowired
    public Rider(String name, String email, int phoneNumber, Double latitude, Double longitude, int status){
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.location = new Coordinates(latitude, longitude);
        this.status = status;

    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Rider)) {
            return false;
        }
        Rider rider = (Rider) o;
        return Objects.equals(name, rider.name) && Objects.equals(email, rider.email) && phoneNumber == rider.phoneNumber && status == rider.status && Objects.equals(location, rider.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, phoneNumber, status, location);
    }

}
