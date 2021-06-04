package t_tracker.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "type", nullable = false)
    private String type;

    @ManyToMany(mappedBy = "buy_products")
    private List<VaccineBuy> vaccine_buys;

    @ManyToMany(mappedBy = "collect_products")
    private List<VaccineCollection> vaccine_collects;
}
