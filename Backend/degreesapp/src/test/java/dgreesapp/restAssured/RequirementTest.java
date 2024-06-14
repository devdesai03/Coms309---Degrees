package dgreesapp.restAssured;
import degreesapp.services.DepartmentService;
import io.restassured.http.ContentType;

import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class RequirementTest extends BaseTest {
    @Test
    public void testRequirementGroupsList() {
        given().when().get("/requirementGroups/")
                .then()
                .assertThat()
                .statusCode(between(200, 300))
                .contentType(ContentType.JSON)
                .body("", everyItem(anything()));
    }

    @Test
    public void testRequirementsEdit() {
        String groupId = "testRequirementGroup";
        String groupName = "Test Requirement Group";
        String groupName2 = "Test Updated Requirement Group";
        Long courseId = 12L;
        Long degreeId = 11L;

        requirementGroup: {
            given().when().body(String.format("""
                            {
                                "id": "%s",
                                "name": "%s"
                            }
                            """, groupId, groupName))
                    .contentType(ContentType.JSON)
                    .post("/requirementGroups/")
                    .then()
                    .assertThat()
                    .statusCode(between(200, 300))
                    .contentType(ContentType.JSON);

            given().when()
                    .get("/requirementGroups/" + groupId)
                    .then()
                    .assertThat()
                    .statusCode(between(200, 300))
                    .contentType(ContentType.JSON)
                    .body("id", equalTo(groupId))
                    .body("name", equalTo(groupName));

            given().when().body(String.format("""
                            {
                                "name": "%s"
                            }
                            """, groupName2))
                    .contentType(ContentType.JSON)
                    .put("/requirementGroups/" + groupId)
                    .then()
                    .assertThat()
                    .statusCode(between(200, 300))
                    .contentType(ContentType.JSON);

            given().when()
                    .get("/requirementGroups/" + groupId)
                    .then()
                    .assertThat()
                    .statusCode(between(200, 300))
                    .contentType(ContentType.JSON)
                    .body("id", equalTo(groupId))
                    .body("name", equalTo(groupName2));

            requirementFulfillment: {
                given().when().body(String.format("""
                        {
                            "group": { "id": "%s" },
                            "course": { "courseId": %s },
                            "minimumGrade": "2.0"
                        }
                        """, groupId, courseId))
                        .contentType(ContentType.JSON)
                        .post("/requirementGroups/" + groupId + "/fulfillments/")
                        .then()
                        .assertThat()
                        .statusCode(between(200, 300))
                        .contentType(ContentType.JSON);

                given().when().body("""
                        {
                            "minimumGrade": "1.0"
                        }
                        """)
                        .contentType(ContentType.JSON)
                        .put("/requirementGroups/" + groupId + "/fulfillments/" + courseId)
                        .then()
                        .assertThat()
                        .statusCode(between(200, 300))
                        .contentType(ContentType.JSON);

                given().when()
                        .get("/requirementGroups/" + groupId + "/fulfillments/" + courseId)
                        .then()
                        .assertThat()
                        .statusCode(between(200, 300))
                        .contentType(ContentType.JSON)
                        .body("group.id", equalTo(groupId))
                        .body("course.courseId", numEqualTo(courseId))
                        .body("minimumGrade", numEqualTo("1.00"));

                given().when()
                        .get("/requirementGroups/" + groupId + "/fulfillments/")
                        .then()
                        .assertThat()
                        .statusCode(between(200, 300))
                        .contentType(ContentType.JSON);

                given().when().delete("/requirementGroups/" + groupId + "/fulfillments/" + courseId)
                        .then()
                        .assertThat()
                        .statusCode(between(200, 300));
            }

            degreeRequirement: {
                given().when().body(String.format("""
                        {
                            "group": {"id": "%s"},
                            "degree": {"id": %s},
                            "recommendedSemester": 1
                        }
                        """, groupId, degreeId))
                        .contentType(ContentType.JSON)
                        .post("/degrees/" + degreeId + "/requirements/")
                        .then()
                        .assertThat()
                        .statusCode(between(200, 300));

                given().when().body("""
                        {
                            "recommendedSemester": 2
                        }
                        """)
                        .contentType(ContentType.JSON)
                        .put("/degrees/" + degreeId + "/requirements/" + groupId)
                        .then()
                        .assertThat()
                        .statusCode(between(200, 300));

                given().when()
                        .get("/degrees/" + degreeId + "/requirements/" + groupId)
                        .then()
                        .assertThat()
                        .statusCode(between(200, 300))
                        .contentType(ContentType.JSON)
                        .body("recommendedSemester", numEqualTo(2))
                        .body("group.id", equalTo(groupId))
                        .body("degree.id", numEqualTo(degreeId));

                given().when().get("/degrees/" + degreeId + "/requirements/")
                        .then()
                        .assertThat()
                        .statusCode(between(200, 300))
                        .contentType(ContentType.JSON)
                        .body("", hasSize(greaterThan(0)));

                given().when().delete("/degrees/" + degreeId + "/requirements/" + groupId)
                        .then()
                        .assertThat()
                        .statusCode(between(200, 300));
            }

            given().when().delete("/requirementGroups/" + groupId)
                    .then()
                    .assertThat()
                    .statusCode(between(200, 300));
        }
    }
}
