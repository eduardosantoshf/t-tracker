package t_tracker.model;

import java.util.ArrayList;
import java.util.List;

public class ClientDTO {

    private String name;
    private String username;
    private String email;
    private String password;
    private int phoneNumber;
    private Coordinates homeLocation;
    private List<Order> orderlist;
    
    public ClientDTO(String name, String username, String email, String password, int phoneNumber, Coordinates homeLocation) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.homeLocation = homeLocation;
        this.orderlist = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public int getPhoneNumber() {
        return this.phoneNumber;
    }

    public Coordinates getHomeLocation() {
        return this.homeLocation;
    }

    public List<Order> getOrderlist() {
        return this.orderlist;
    }

}
