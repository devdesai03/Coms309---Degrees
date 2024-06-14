package coms309.people;
import java.util.UUID;


/**
 * Provides the Definition/Structure for the people row
 *
 * @author Vivek Bengre
 */

public class Person {
    private UUID id = null;

    private String firstName;

    private String lastName;

    private String address;

    private String telephone;

    public Person(){
        
    }

    public Person(String firstName, String lastName, String address, String telephone){
        this(null, firstName, lastName, address, telephone);
    }

    public Person(UUID id, String firstName, String lastName, String address, String telephone) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.telephone = telephone;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public UUID getID() {
        return this.id;
    }

    public void setID(UUID id) {
        this.id = id;
    }

    public UUID assignNewIDIfNull() {
        if (this.id == null) {
            this.id = UUID.randomUUID();
        }
        return this.id;
    }

    @Override
    public String toString() {
        return firstName + " " 
               + lastName + " "
               + address + " "
               + telephone;
    }
}
