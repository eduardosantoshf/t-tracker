package t_tracker.model;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

@Entity
public class Lab {

    @Id
    private int id;

    @Column(name = "token")
    private String token;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne
    @JoinColumn(name = "coordinates_id")
    private Coordinates location;

    public Lab() {}

    @Autowired
    public Lab(int id, String token, String name, Coordinates location) {
        this.id = id;
        this.token = token;
        this.name = name;
        this.location = location;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return this.name;
    }

    public Coordinates getLocation() {
        return this.location;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Lab)) {
            return false;
        }
        Lab lab = (Lab) o;
        return Objects.equals(token, lab.token) && Objects.equals(name, lab.name) && Objects.equals(location, lab.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, name, location);
    }
    


    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", token='" + getToken() + "'" +
            ", name='" + getName() + "'" +
            ", location='" + getLocation() + "'" +
            "}";
    }
    

}
