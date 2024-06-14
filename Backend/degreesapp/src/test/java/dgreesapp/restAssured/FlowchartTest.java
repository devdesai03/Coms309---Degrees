package dgreesapp.restAssured;

import io.restassured.http.ContentType;
import org.junit.Test;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class FlowchartTest extends BaseTest {
    @Test
    public void testDegreeFlowchart() {
        testDegreeFlowchart(11L, 18L);
        testDegreeFlowchart(11L, 19L);
    }

    private void testDegreeFlowchart(Long degreeId, Long studentId) {
        given().when()
                .queryParam("studentId", studentId)
                .get("/degrees/" + degreeId + "/flowchart")
                .then()
                .assertThat()
                .statusCode(between(200, 300))
                .contentType(ContentType.JSON)
                .body(is(not(emptyOrNullString())))
                .body("degree.id", numEqualTo(degreeId))
                .body("semesters", hasSize(greaterThan(0)));
    }
}
