package t_tracker.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Lab")
public class Lab {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", nullable = false)
    private String address;

    @OneToMany(mappedBy = "lab")
    private List<VaccineCollection> vaccine_collections;

}
