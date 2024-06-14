package dgreesapp.restAssured;

import io.restassured.http.ContentType;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CourseSectionTest extends BaseTest {
    @Test
    public void testCourseSectionsList() {
        given().when().get("/courseSections")
                .then()
                .assertThat()
                .statusCode(is(between(200, 300)))
                .and().contentType(ContentType.JSON)
                .and().body(is(not(emptyOrNullString())))
                .and().body("", hasSize(greaterThan(0)));
    }

    @Test
    public void testCourseSectionsListFilteredBySemester() {
        String semester = "Spring 2024";

        given().when().queryParam("semester", semester)
                .get("/courseSections")
                .then()
                .assertThat()
                .statusCode(is(between(200, 300)))
                .and().contentType(ContentType.JSON)
                .and().body(is(not(emptyOrNullString())))
                .and().body("", hasSize(greaterThan(0)))
                .and().body("", everyItem(hasEntry("semester", semester)));
    }

    @Test
    public void testCourseSectionOperations() {
        Long courseId = 18L;
        Long instructorId = 50L;
        String daysOfWeek = "MWF";
        String startDate = "2023-01-01";
        String endDate = "2023-05-15";
        String startTime = "10:00:00";
        String endTime = "11:00:00";
        String semester = "Spring 2023";

        Long sectionId = given().body(String.format("""
                {
                    "course": {"courseId": %s},
                    "instructor": {"universityId": %s},
                    "daysOfWeek": "%s",
                    "startDate": "%s",
                    "endDate": "%s",
                    "startTime": "%s",
                    "endTime": "%s",
                    "semester": "%s",
                    "sectionIdentifier": "TEST"
                }
                """, courseId, instructorId, daysOfWeek, startDate, endDate, startTime, endTime, semester))
                .contentType(ContentType.JSON)
                .when().post("/courseSections")
                .then()
                .assertThat().statusCode(between(200, 300))
                .and().contentType(ContentType.JSON)
                .extract().jsonPath().getLong("id");

        given().when().get("/courseSections/" + sectionId)
                .then().assertThat()
                .statusCode(between(200, 300))
                .contentType(ContentType.JSON)
                .and()
                .body("course.courseId", numEqualTo(courseId))
                .body("instructor.universityId", numEqualTo(instructorId))
                .body("daysOfWeek", equalTo(daysOfWeek))
                .body("startDate", equalTo(startDate))
                .body("endDate", equalTo(endDate))
                .body("startTime", equalTo(startTime))
                .body("endTime", equalTo(endTime))
                .body("semester", equalTo(semester));

        given().when().queryParam("semester", semester)
                .get("/courseSections/")
                .then().assertThat()
                .statusCode(between(200, 300))
                .contentType(ContentType.JSON)
                .and()
                .body("", hasItem(
                        hasEntry(equalTo("id"), numEqualTo(sectionId))
                ));

        given().when().get("/course-sections-by-instructor/" + instructorId)
                .then().assertThat()
                .statusCode(between(200, 300))
                .contentType(ContentType.JSON)
                .and()
                .body("", hasItem(
                        hasEntry(equalTo("id"), numEqualTo(sectionId))
                ));

        given().when().get("/courses/" + courseId + "/courseSections/")
                .then().assertThat()
                .statusCode(between(200, 300))
                .contentType(ContentType.JSON)
                .and()
                .body("", hasItem(
                        hasEntry(equalTo("id"), numEqualTo(sectionId))
                ));

        given().when().queryParam("semester", semester)
                .get("/courses/" + courseId + "/courseSections/")
                .then().assertThat()
                .statusCode(between(200, 300))
                .contentType(ContentType.JSON)
                .and()
                .body("", hasItem(
                        hasEntry(equalTo("id"), numEqualTo(sectionId))
                ));

        given().when().delete("/courseSections/" + sectionId)
                .then()
                .assertThat().statusCode(between(200, 300));
    }

    @Test
    public void testCourseSectionErrors() {
        String badSemester = "This is not a semester :)";
        Long badCourseId = (long)(10000 * Math.random());
        Long goodCourseId = 18L;

        given().when().queryParam("semester", badSemester)
                .get("/courseSections/")
                .then()
                .assertThat()
                .statusCode(between(400, 500));

        given().when().queryParam("semester", badSemester)
                .get("/courses/" + badCourseId + "/courseSections")
                .then()
                .assertThat()
                .statusCode(between(400, 500));
    }
}
