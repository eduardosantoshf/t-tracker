package t_tracker.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Store")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "owner_name", nullable = false)
    private String owner_name;

    @OneToMany(mappedBy = "store")
    private List<VaccineBuy> vaccine_buys;
}
