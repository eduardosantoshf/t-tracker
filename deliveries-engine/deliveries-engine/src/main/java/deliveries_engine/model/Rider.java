package deliveries_engine.model;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "Rider")
public class Rider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "status", nullable = false)
    private boolean status;

    @OneToMany(mappedBy = "rider")
    private List<Delivery> deliveries;

    

    
}
