package t_tracker.model;

public class OrderDTO {

    private int productId;
    private int quantity;

    public OrderDTO() {
    }

    public OrderDTO(int productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getProductId() {
        return this.productId;
    }

    public int getQuantity() {
        return this.quantity;
    }


    @Override
    public String toString() {
        return "{" +
            " productId='" + getProductId() + "'" +
            ", quantity='" + getQuantity() + "'" +
            "}";
    }

    
}
