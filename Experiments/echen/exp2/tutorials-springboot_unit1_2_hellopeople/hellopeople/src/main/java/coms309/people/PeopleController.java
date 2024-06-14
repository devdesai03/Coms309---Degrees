package coms309.people;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.HashMap;
import java.util.UUID;
import java.util.Collection;

/**
 * Controller used to showcase Create and Read from a LIST
 *
 * @author Vivek Bengre
 */

@RestController
public class PeopleController {

    // Note that there is only ONE instance of PeopleController in 
    // Springboot system.
    HashMap<UUID, Person> peopleMap = new HashMap<>();

    //CRUDL (create/read/update/delete/list)
    // use POST, GET, PUT, DELETE, GET methods for CRUDL

    // THIS IS THE LIST OPERATION
    // gets all the people in the list and returns it in JSON format
    // This controller takes no input. 
    // Springboot automatically converts the list to JSON format 
    // in this case because of @ResponseBody
    // Note: To LIST, we use the GET method
    @GetMapping("/people")
    public @ResponseBody Collection<Person> getAllPersons() {
        return peopleMap.values();
    }

    // THIS IS THE CREATE OPERATION
    // springboot automatically converts JSON input into a person object and 
    // the method below enters it into the list.
    // It returns a string message in THIS example.
    // in this case because of @ResponseBody
    // Note: To CREATE we use POST method
    @PostMapping("/people")
    public @ResponseBody String createPerson(@RequestBody Person person) {
        System.out.println(person);
        UUID id = person.assignNewIDIfNull();
        if (peopleMap.containsKey(id)) {
            throw new ResponseStatusException(400,
                    String.format("Person with ID %s already exists", id),
                    null);
        }
        peopleMap.put(id, person);
        return "New person with ID " + id + " Saved";
    }

    // THIS IS THE READ OPERATION
    // Springboot gets the PATHVARIABLE from the URL
    // We extract the person from the HashMap.
    // springboot automatically converts Person to JSON format when we return it
    // in this case because of @ResponseBody
    // Note: To READ we use GET method
    @GetMapping("/people/{id}")
    public @ResponseBody Person getPerson(@PathVariable UUID id) {
        Person p = peopleMap.get(id);
        return p;
    }

    // THIS IS THE UPDATE OPERATION
    // We extract the person from the HashMap and modify it.
    // Springboot automatically converts the Person to JSON format
    // Springboot gets the PATHVARIABLE from the URL
    // Here we are returning what we sent to the method
    // in this case because of @ResponseBody
    // Note: To UPDATE we use PUT method
    @PutMapping("/people/{id}")
    public @ResponseBody Person updatePerson(@PathVariable UUID id, @RequestBody Person p) {
        p.setID(id);
        peopleMap.put(id, p);
        return peopleMap.get(id);
    }

    @PatchMapping("/people/{id}")
    public @ResponseBody Person partialUpdatePerson(@PathVariable UUID id, @RequestBody Person updates) {
        updates.setID(null);
        Person person = peopleMap.get(id);
        if (person == null) {
            throw new ResponseStatusException(404, "Person with ID " + id + " not found", null);
        };
        if (updates.getFirstName() != null) {
            person.setFirstName(updates.getFirstName());
        }
        if (updates.getLastName() != null) {
            person.setLastName(updates.getLastName());
        }
        if (updates.getAddress() != null) {
            person.setAddress(updates.getAddress());
        }
        if (updates.getTelephone() != null) {
            person.setTelephone(updates.getTelephone());
        };
        return person;
    }

    // THIS IS THE DELETE OPERATION
    // Springboot gets the PATHVARIABLE from the URL
    // We return the entire list -- converted to JSON
    // in this case because of @ResponseBody
    // Note: To DELETE we use delete method
    
    @DeleteMapping("/people/{id}")
    public @ResponseBody Person deletePerson(@PathVariable UUID id) {
        return peopleMap.remove(id);
    }
}

