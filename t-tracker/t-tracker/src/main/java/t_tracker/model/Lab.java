package t_tracker.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
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

    @JsonManagedReference
    @OneToMany(mappedBy = "lab", cascade = {CascadeType.ALL})
    private List<Stock> stocks;

    @OneToMany(mappedBy = "labId")
    private List<Order> orders;

    public Lab() {}

    @Autowired
    public Lab(int id, String token, String name, Coordinates location) {
        this.id = id;
        this.token = token;
        this.name = name;
        this.location = location;
    }

    public void addStock(Stock stockToAdd) {
        stocks.add(stockToAdd);
    }

    public void removeStock(Stock stockToRemove) {
        for (Stock stock : stocks)
            if ( stockToRemove.getProduct().equals(stock.getProduct()) ) {
                stock.removeQuantity( stockToRemove.getQuantity() );
                
                if ( stock.getQuantity() == 0 )
                    stocks.remove(stock);
                
                return;
            }
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

    public void setId(String token) {
        this.token = token;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getLocation() {
        return this.location;
    }

    public void setLocation(Coordinates location) {
        this.location = location;
    }

    public List<Stock> getStocks() {
        return this.stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
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
