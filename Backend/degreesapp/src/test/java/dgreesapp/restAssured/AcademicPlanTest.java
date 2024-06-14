package dgreesapp.restAssured;

import io.restassured.http.ContentType;
import org.junit.Test;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class AcademicPlanTest extends BaseTest {
    @Test
    public void getAcademicPlanTest() {
        Long studentId = 18L;
        given().when().get("/academic-planner/" + studentId)
                .then()
                .assertThat()
                .contentType(ContentType.JSON)
                .and().statusCode(between(200, 300))
                .and().body(is(not(emptyOrNullString())))
        ;
    }
}
