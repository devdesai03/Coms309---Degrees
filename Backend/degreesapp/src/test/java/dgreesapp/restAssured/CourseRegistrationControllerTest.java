package dgreesapp.restAssured;

import io.restassured.http.ContentType;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class CourseRegistrationControllerTest extends BaseTest {
    @Test
    public void TestGetRegisteredCourseSectionsTest() {
        Long studentId = 18L;
        given().
                when().
                get("/students/{studentId}/registered-course-sections" , studentId).
                then().assertThat().statusCode(200).
                and().
                contentType(ContentType.JSON);
    }

    @Test
    public void TestPutSaveCourseSectionTest() {
        // Define path variables
        Long universityId = 18L;
        Long courseId = 112233L;
        // Send the request
        given().
                pathParam("universityId" , universityId).
                pathParam("courseId" , courseId).
                header("Content-Type" , "application/json").
                when().
                put("/course-section/{universityId}/{courseId}").
                then().
                assertThat().statusCode(200).and().contentType(ContentType.JSON);
    }

    @Test
    public void TestDropRegisteredCourseSectionTest() {
        // Define path variables
        Long universityId = 18L;
        Long courseId = 112233L;
        // Send the request
        given().
                pathParam("universityId" , universityId).
                pathParam("courseId" , courseId).
                header("Content-Type" , "application/json").
                when().
                delete("/drop-registered-course-section/{universityId}/{courseId}").
                then().
                assertThat().statusCode(200).and().contentType(ContentType.JSON);
    }

}

