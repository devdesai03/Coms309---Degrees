package coms309.people;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Provides the Definition/Structure for the people row
 *
 * @author Vivek Bengre
 */

public class Person {
    private static int nextId = 1; // Used to assign unique IDs

    private int id; // Unique identifier for each person

    private String firstName;

    private String lastName;

    private String address;

    private String telephone;
    private String email;
    private static Map<Integer, Person> personMap = new HashMap<>();

    public Person(){
            this.id = getNextId();
        }

    private synchronized int getNextId() {
        int currentID = nextId;
        nextId++;
        return currentID++;
    }

    public Person(String firstName, String lastName, String address, String telephone, String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.telephone = telephone;
        this.email = email;
    }

    public int getId() {
        return id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void save(Person updatePerson) {
        personMap.put(id, this);
    }

    public static Optional<Person> findById(int id) {
        Person person = personMap.get(id);
        return Optional.ofNullable(person);
    }

    @Override
    public String toString() {
        return firstName + " " 
               + lastName + " "
               + address + " "
               + telephone;
    }

    public <Person> Person orElseThrow(Object o) {
        return (Person) Optional.ofNullable(this);
    }
}
