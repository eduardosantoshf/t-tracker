package t_tracker.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;

@Entity
public class StoreDetails {
    
    @Id
    private int id;

    @Column(name = "token")
    private String token;


    public StoreDetails() {
    }

    @Autowired
    public StoreDetails(int id, String token) {
        this.id = id;
        this.token = token;
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

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof StoreDetails)) {
            return false;
        }
        StoreDetails storeDetails = (StoreDetails) o;
        return id == storeDetails.id && Objects.equals(token, storeDetails.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token);
    }


    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", token='" + getToken() + "'" +
            "}";
    }


}
