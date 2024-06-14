package dgreesapp.restAssured;

import degreesapp.Main;
import io.restassured.http.ContentType;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.given;

@SpringBootTest ( webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT )
@RunWith ( SpringRunner.class )
@ContextConfiguration ( classes = Main.class )
public class DegreeTest extends BaseTest {

    @Test
    public void listDegreesTest() {
        given().
                when().
                get("/degrees/").
                then().assertThat().statusCode(200).
                and().
                contentType(ContentType.JSON);
    }

    @Test
    public void getSingleDegreeTest() {
        Long degreeId = 11L;
        given().when().get("/degrees/{degreeId}", degreeId)
        .then().assertThat().statusCode(200)
                .and()
                .contentType(ContentType.JSON)
                .body(not(emptyOrNullString()));
    }

    @Test
    public void modifyDegreesTest() {
        Long departmentId = 10L;

        long degreeId = given().body(String.format("""
                {
                  "department": {
                    "departmentId": %s
                  },
                  "name": "Test Degree",
                  "suffix": "BA"
                }
                        """, departmentId))
                .contentType(ContentType.JSON)
                .when().post("/degrees/")
                .then().assertThat().statusCode(200)
                .and()
                .contentType(ContentType.JSON)
                .body(not(emptyOrNullString()))
                .extract()
                .jsonPath()
                .getLong("id");

        given().body(String.format("""
                {
                  "department": {
                    "departmentId": %s
                  },
                  "name": "Another Test Degree",
                  "suffix": "PhD"
                }
                """, departmentId))
                .contentType(ContentType.JSON)
                .when().put("/degrees/" + degreeId)
                .then()
                .assertThat().statusCode(200)
                .and()
                .contentType(ContentType.JSON)
                .body("id", numEqualTo(degreeId))
                .body("name", equalTo("Another Test Degree"))
                .body("suffix", equalTo("PhD"));

        given().when().delete("/degrees/" + degreeId)
                .then()
                .assertThat().statusCode(200);
    }
}

