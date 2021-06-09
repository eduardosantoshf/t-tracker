package t_tracker.model;

import javax.persistence.*;

@Entity
@Table(name = "User")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique=true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone_number", unique=true)
    private int phoneNumber;

    @Column(name = "home_location")
    private Coordinates homeLocation;

    public User(String name, String email, String password, int phoneNumber) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public User(String name, String email, String password, Coordinates homeLocation) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.homeLocation = homeLocation;
    }

    public User(String name, String email, String password, int phoneNumber, Coordinates homeLocation) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.homeLocation = homeLocation;
    }


}
