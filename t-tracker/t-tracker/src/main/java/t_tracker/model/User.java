package t_tracker.model;

import javax.persistence.*;

@Entity
@Table(name = "User")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "username", nullable = false, unique=true)
    private String username;

    @Column(name = "email", nullable = false, unique=true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone_number")
    private int phoneNumber;

    @OneToOne
    @JoinColumn(name = "coordinates_id")
    private Coordinates homeLocation;

    public User() {}

    public User(String name, String username, String email, String password, int phoneNumber) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public User(String name, String username, String email, String password, Coordinates homeLocation) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.homeLocation = homeLocation;
    }

    public User(String name, String username, String email, String password, int phoneNumber, Coordinates homeLocation) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.homeLocation = homeLocation;
    }

    public Integer getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPhoneNumber() {
        return this.phoneNumber;
    }

    public Coordinates getHomeLocation() {
        return this.homeLocation;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", username='" + getUsername() + "'" +
            ", email='" + getEmail() + "'" +
            ", password='" + getPassword() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", homeLocation='" + getHomeLocation() + "'" +
            "}";
    }

}
