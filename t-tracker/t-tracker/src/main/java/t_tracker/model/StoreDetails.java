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

    @Column(name = "auth_token")
    private String authToken;


    public StoreDetails() {
    }

    @Autowired
    public StoreDetails(int id, String authToken) {
        this.id = id;
        this.authToken = authToken;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthToken() {
        return this.authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof StoreDetails)) {
            return false;
        }
        StoreDetails storeDetails = (StoreDetails) o;
        return id == storeDetails.id && Objects.equals(authToken, storeDetails.authToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, authToken);
    }

}
