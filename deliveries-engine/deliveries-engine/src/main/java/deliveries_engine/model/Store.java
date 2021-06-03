package deliveries_engine.model;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "Store")
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "store")
    private List<Delivery> deliveries;

    @Column(name = "owner_name", nullable = false)
    private String owner_name;
    
}
