package dgreesapp.restAssured;

import degreesapp.models.User;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;


public class UserControllerTests extends BaseTest {
    @Test
    public void TestGetUserTest() {
        given().
                header("Content-Type" , "application/json").
                when().
                get("/users").
                then().assertThat().statusCode(200).
                and().
                contentType(ContentType.JSON);
    }

    @Test
    public void TestGetUserByIdTest() {

        given().
                header("Content-Type" , "application/json").
                when().
                get("/users/{id}" , 60).
                then().assertThat().statusCode(200).
                and().
                contentType(ContentType.JSON);
    }

    @Test
    public void testUserLifecycle() {
        // Step 1: Create a new user
        User user = new User(null, "Test", "Test123", "Test@Test", "test", "00000");
        Response response = given()
                .header("Content-Type", "application/json")
                .body(user)
                .when()
                .post("/users")
                .then()
                .assertThat().statusCode(200).and().contentType(ContentType.JSON)
                .extract().response();

        // Ensure the user is created successfully
        User savedUser = response.as(User.class);
        assertNotNull(savedUser.getUserId());

        // Step 2: Update the user's address
        savedUser.setUserAddress("test2");
        given()
                .header("Content-Type", "application/json")
                .when()
                .body(savedUser)
                .put("/users/{id}", savedUser.getUserId())
                .then()
                .assertThat().statusCode(200)
                .body("userAddress",equalTo("test2"));
        given()
                .header("Content-Type", "application/json")
                .when()
                .body(savedUser)
                .post("/login/verify")
                .then()
                .assertThat().statusCode(200);
        given()
                .header("Content-Type", "application/json")
                .when()
                .body(savedUser)
                .get("/verificationCode/{userEmail}",savedUser.getUserEmail())
                .then()
                .assertThat().statusCode(200);



        // Step 3: Delete the user
        given()
                .header("Content-Type", "application/json")
                .when()
                .delete("/users/{id}", savedUser.getUserId())
                .then()
                .assertThat().statusCode(200)
                .body(is(equalTo("User number " + savedUser.getUserId() + " Deleted Successfully")));
    }

}
