package t_tracker.model;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Entity
public class Lab {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToOne
    @JoinColumn(name = "coordinates_id")
    private Coordinates location;

    @OneToMany(mappedBy = "lab")
    private List<Stock> stocks;

    @OneToMany(mappedBy = "labId")
    private List<Order> orders;

    public Lab() {}

    @Autowired
    public Lab(String name, Coordinates location) {
        this.name = name;
        this.location = location;
    }

    public void addStock(Stock stockToAdd) {
        for (Stock stock : stocks)
            if ( stockToAdd.getProduct().equals(stock.getProduct()) ) {
                stock.addQuantity( stockToAdd.getQuantity() );
                return;
            }

        stocks.add(stockToAdd);
    }

    public void removeStock(Stock stockToRemove) {
        for (Stock stock : stocks)
            if ( stockToRemove.getProduct().equals(stock.getProduct()) ) {
                stock.removeQuantity( stockToRemove.getQuantity() );
                return;
            }
    }

    public int getId() {
        return this.id;
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
    
}
