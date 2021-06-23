package t_tracker.model;

public class StockDTO {

    private Integer id;
    private Product product;
    private int quantity;

    public StockDTO() {}

    public StockDTO(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Integer getId() {
        return this.id;
    }

    public Product getProduct() {
        return this.product;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public Double getTotalPrice() {
        return this.getProduct().getPrice() * this.getQuantity();
    }

}
