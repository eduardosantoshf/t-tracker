package t_tracker.model;

import java.util.Objects;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;

@Entity
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    public Stock() {}

    @Autowired
    public Stock(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Integer getId() {
        return this.id;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void addQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void removeQuantity(int quantity) {
        int result = this.quantity - quantity;
        this.quantity = result <= 0 ? 0 : result;
    }

    public Double getTotalPrice() {
        return this.getProduct().getPrice() * this.getQuantity();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Stock)) {
            return false;
        }
        Stock stock = (Stock) o;
        return Objects.equals(product, stock.product) && quantity == stock.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, quantity);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", product='" + getProduct() + "'" +
            ", quantity='" + getQuantity() + "'" +
            "}";
    }

}
