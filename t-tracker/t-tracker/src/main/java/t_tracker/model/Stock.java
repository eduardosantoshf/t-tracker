package t_tracker.model;

import java.util.Objects;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import org.springframework.beans.factory.annotation.Autowired;

@Entity
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name="lab_id", referencedColumnName = "id")
    private Lab lab;

    public Stock() {}

    @Autowired
    public Stock(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public int getId() {
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

    public Order getOrder() {
        return this.order;
    }

    public Lab getLab() {
        return this.lab;
    }

    public void setLab(Lab lab) {
        this.lab = lab;
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
        return Objects.equals(product, stock.product) && quantity == stock.quantity && Objects.equals(lab, stock.lab);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, quantity, lab);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", product='" + getProduct() + "'" +
            ", quantity='" + getQuantity() + "'" +
            ", order='" + getOrder() + "'" +
            "}";
    }

}
