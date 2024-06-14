package dgreesapp.restAssured;

import io.restassured.http.ContentType;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.testng.AssertJUnit.fail;

public class UserTypeTest extends BaseTest {
    @Test
    public void userTypeTest() {
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

        isuRegistration: {
            // create ISU registration
            Long universityId = given().when()
                    .body(String.format("""
                            {
                                "user": { "userId": %s },
                                "netId": "isuUserTestNetId",
                                "givenName": "Iowa State User",
                                "middleName": "Cy",
                                "surname": "Test"
                            }
                            """, userId))
                    .contentType(ContentType.JSON)
                    .post("/isuRegistration")
                    .then()
                    .statusCode(between(200, 300))
                    .extract()
                    .jsonPath().getLong("universityId");

            instructor: for (String endpoint : new String[]{"instructors", "students", "departmentHeads"}) {
                try {
                    // create/update
                    given().when()
                            .body(String.format("""
                                    {}
                                    """))
                            .contentType(ContentType.JSON)
                            .put("/" + endpoint + "/" + universityId)
                            .then()
                            .statusCode(between(200, 300));

                    // get list
                    given().when().get("/" + endpoint + "/")
                            .then()
                            .statusCode(between(200, 300))
                            .contentType(ContentType.JSON)
                            .body("",
                                hasItem(
                                    hasEntry(
                                        equalTo("isuRegistration"),
                                        hasEntry(
                                            equalTo("universityId"),
                                            numEqualTo(universityId)
                                        )
                                    )
                                )
                            );

                    // get
                    given().when().get("/" + endpoint + "/" + universityId)
                            .then()
                            .statusCode(between(200, 300))
                            .contentType(ContentType.JSON)
                            .body("isuRegistration.universityId", numEqualTo(universityId));

                    // delete
                    given().when().delete("/" + endpoint + "/" + universityId)
                            .then()
                            .statusCode(between(200, 300));

                    // test truly deleted
                    var response = given().when().get("/" + endpoint + "/" + universityId)
                            .then()
                            .extract();
                    deleteChecks: {
                        if (!(200 <= response.statusCode()))
                            break deleteChecks;
                        if (!(response.statusCode() < 300))
                            break deleteChecks;
                        if (response.body().asString().isBlank())
                            break deleteChecks;
                        fail("Entity may not have been truly deleted");
                    }
                } catch (AssertionError e) {
                    System.out.println("Assertion error for " + endpoint + " at ID " + universityId);
                    throw e;
                }
            }

            // delete ISU registration
            given().when().delete("/isuRegistration/" + universityId)
                    .then()
                    .statusCode(between(200, 300));
        }

        // delete user
        given().when()
                .delete("/users/" + userId)
                .then()
                .statusCode(between(200, 300));
    }
}
