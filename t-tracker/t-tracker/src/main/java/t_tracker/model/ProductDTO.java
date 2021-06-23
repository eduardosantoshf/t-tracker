package t_tracker.model;

public class ProductDTO {

    private Integer id;
    private String name;
    private Double price;
    private String type;
    private String description;
    private String foto;

    public ProductDTO() {}

    public ProductDTO(String name, Double price, String type, String description) {
        this.name = name;
        this.price = price;
        this.type = type;
        this.description = description;
    }

    public ProductDTO(String name, Double price, String type, String description, String foto) {
        this.name = name;
        this.price = price;
        this.type = type;
        this.description = description;
        this.foto=foto;
    }

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Double getPrice() {
        return this.price;
    }

    public String getType() {
        return this.type;
    }

    public String getDescription() {
        return this.description;
    }

    public String getFoto() {
        return this.foto;
    }

}
