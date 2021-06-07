package t_tracker.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Vaccine_Buy")
public class VaccineBuy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToMany
    @JoinTable(name = "vaccine_products", joinColumns = @JoinColumn(name = "vaccine_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> buy_products;

    @Column(name = "total", nullable = false)
    private Double total;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
}
