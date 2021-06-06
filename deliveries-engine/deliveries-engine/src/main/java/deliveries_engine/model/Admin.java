package deliveries_engine.model;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Table(name = "Admin")
public class Admin extends User {

    // @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    // private int id;

    public Admin(String name, String email, String username, String password, int phoneNumber){
        super(name, email, username, password, phoneNumber);
    }

    @Autowired
    public Admin(String name, String email, String username, String password, int phoneNumber, String address, String city, String zipCode){
        super(name, email, username, password, phoneNumber, address, city, zipCode);
    }

    public int getId() {
        return super.getId();
    }

    public String getUsername(){
        return super.getUsername();
    }

    public String getEmail(){
        return super.getEmail();
    }

    public String getPassword(){
        return super.getPassword();
    }

    public String getName() {
        return super.getName();
    }

    public int getPhoneNumber() {
        return super.getPhoneNumber();
    }

    public String getAddress() {
        return super.getAddress();
    }

    public String getCity() {
        return super.getCity();
    }

    public String getZipCode() {
        return super.getZipCode();
    }

    public void setPassword(String password){
        super.setPassword(password);
    }

}
