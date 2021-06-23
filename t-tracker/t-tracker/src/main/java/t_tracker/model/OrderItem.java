package t_tracker.model;

import java.util.Objects;
import java.util.UUID;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.beans.factory.annotation.Autowired;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @JsonIgnore
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    @ManyToOne(targetEntity = Order.class)
    private Order order;

    @Column(name="order_id")
    private Integer orderId;

    public OrderItem() {}

    @Autowired
    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public OrderItem(Product product, int quantity, Order order) {
        this.order = order;
        this.orderId = order.getId();
        this.product = product;
        this.quantity = quantity;
    }

    public Integer getId() {
        return this.id;
    }

    public Double getTotalPrice() {
        return product.getPrice() * quantity;
    }

    public Product getProduct() {
        return this.product;
    }

    public int getQuantity() {
        return this.quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof OrderItem)) {
            return false;
        }
        OrderItem orderItem = (OrderItem) o;
        return Objects.equals(product, orderItem.product) && quantity == orderItem.quantity && Objects.equals(order, orderItem.order) && Objects.equals(orderId, orderItem.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, quantity, order, orderId);
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
