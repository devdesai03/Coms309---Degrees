package coms309;

import coms309.people.Person;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Simple Hello World Controller to display the string returned
 *
 * @author Vivek Bengre
 */


import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PeopleControllerTestApplication {

    public static void main(String[] args) {
        // Create a RestTemplate to make HTTP requests to your controller
        RestTemplate restTemplate = new RestTemplate();

        // Define the URL of your controller
        String baseUrl = "http://localhost:8080"; // Change the port if needed

        // Create a sample Person object to send in the POST request
        Person newPerson = new Person();
        newPerson.setFirstName("Ee");
        newPerson.setLastName("Tn");
        newPerson.setEmail("M@iastate.edu");
        newPerson.setAddress("West street Unit 306, Ames, IA, 50014 ");
        newPerson.setTelephone("641-244-2064");

        // Set the headers for the POST request
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // Create an HttpEntity with the Person object and headers
        HttpEntity<Person> requestEntity = new HttpEntity<>(newPerson, headers);

        // Make a POST request to create a new person
        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl + "/people", requestEntity, String.class);

        // Check the response
        if (response.getStatusCode() == HttpStatus.OK) {
            String responseBody = response.getBody();
            System.out.println("Response: " + responseBody);
        } else {
            System.err.println("Failed to create person. Status code: " + response.getStatusCode());
        }


    }
}

