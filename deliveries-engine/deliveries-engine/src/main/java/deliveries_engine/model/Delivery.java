package deliveries_engine.model;

import javax.persistence.*;

@Entity
@Table(name = "Delivery")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "comission", nullable = false)
    private double commission;

    @Column(name = "status", nullable = false)
    private boolean status;

    @ManyToOne
    @JoinColumn(name="rider_id")
    private Rider rider;

    @ManyToOne
    @JoinColumn(name="store_id")
    private Store store;

    @Column(name = "pickup_location", nullable = false)
    private String pickup_location;

    @Column(name = "delivery_location", nullable = false)
    private String delivery_location;

    @Column(name = "duration", nullable = false)
    private double duration;

    
}
