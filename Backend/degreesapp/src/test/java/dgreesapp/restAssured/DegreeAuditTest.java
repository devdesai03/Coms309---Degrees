package dgreesapp.restAssured;

import io.restassured.http.ContentType;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.given;

public class DegreeAuditTest extends BaseTest {

    @Test
    public void degreeAuditTest() {
        for (Long studentId : new long[]{19L, 18L, 31L}) {
            test1:
            {
                String degreeAudit = given().when()
                        .get("/degreeAudit/" + studentId)
                        .then()
                        .statusCode(between(200, 300))
                        .extract()
                        .asString();
                Assert.assertTrue(degreeAudit.contains("End of Degree Audit"));
            }

            test2:
            {
                String degreeAudit = given().when()
                        .queryParam("includeInProgressCourses", false)
                        .get("/degreeAudit/" + studentId)
                        .then()
                        .statusCode(between(200, 300))
                        .extract()
                        .asString();
                Assert.assertTrue(degreeAudit.contains("End of Degree Audit"));
            }
        }
    }
}

