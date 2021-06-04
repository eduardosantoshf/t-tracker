package t_tracker.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "current_location", nullable = false)
    private String currentLocation;

    @OneToMany(mappedBy = "client")
    private List<VaccineBuy> vaccine_buys;

    @OneToMany(mappedBy = "client")
    private List<VaccineCollection> vaccine_collects;
}
