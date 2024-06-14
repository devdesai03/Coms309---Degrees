package dgreesapp.restAssured;

import io.restassured.http.ContentType;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserInterfaceSettingsTest extends BaseTest {

    @Test
    public void uiSettingsTest() {
        Long userId = 7L;

        // test get
        given().when().get("/settings/" + userId + "/userInterface")
                .then()
                .statusCode(between(200, 300))
                .contentType(ContentType.JSON)
                .and()
                .body(is(not(emptyOrNullString())));

        // test update
        given().when().body("""
                        {
                            "nightMode": "on",
                            "backgroundColor": "#FFFFFF"
                        }
                        """)
                .contentType(ContentType.JSON)
                .put("/settings/" + userId + "/userInterface")
                .then()
                .statusCode(between(200, 300))
                .contentType(ContentType.JSON)
                .and()
                .body("nightMode", is("on"))
                .body("backgroundColor", is("#FFFFFF"));

        given().when()
                .get("/settings/" + userId + "/userInterface")
                .then()
                .statusCode(between(200, 300))
                .contentType(ContentType.JSON)
                .and()
                .body("nightMode", is("on"))
                .body("backgroundColor", is("#FFFFFF"));

        // reset to default
        given().when().body("""
                        {
                            "nightMode": "followSystem",
                            "backgroundColor": null
                        }
                        """)
                .contentType(ContentType.JSON)
                .put("/settings/" + userId + "/userInterface")
                .then()
                .statusCode(between(200, 300))
                .contentType(ContentType.JSON)
                .and()
                .body("nightMode", is("followSystem"))
                .body("backgroundColor", is(equalTo(null)));
    }

    @Test
    public void uiSettingsDefaultTest() {
        // create new user
        Long userId = given().when()
                .body("""
                        {
                            "userName": "someTestUsernameDoesntMatter",
                            "userAddress": "testAddress",
                            "userPassword": "weakPassword",
                            "userEmail": "someTestEmail@example.com",
                            "phoneNumber": "555-555-5555"
                        }
                        """)
                .contentType(ContentType.JSON)
                .post("/users/")
                .then()
                .statusCode(between(200, 300))
                .contentType(ContentType.JSON)
                .extract().jsonPath().getLong("userId");

        // test defaults
        given().when()
                .get("/settings/" + userId + "/userInterface")
                .then()
                .statusCode(between(200, 300))
                .contentType(ContentType.JSON)
                .and()
                .body("nightMode", is("followSystem"))
                .body("backgroundColor", is(equalTo(null)));

        // delete user
        given().when()
                .delete("/users/" + userId)
                .then()
                .statusCode(between(200, 300));
    }
}
