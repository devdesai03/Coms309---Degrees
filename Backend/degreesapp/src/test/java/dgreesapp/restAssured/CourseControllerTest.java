package dgreesapp.restAssured;

import degreesapp.models.Course;
import degreesapp.models.Department;
import io.restassured.http.ContentType;
import org.junit.Test;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class CourseControllerTest extends BaseTest {
    @Test
    public void TestGetCourseTest() {
        given().
                header("Content-Type" , "application/json").
                when().
                get("/courses/").
                then().assertThat().statusCode(200).
                and().
                contentType(ContentType.JSON);
    }

    @Test
    public void TestGetCourseByIdTest() {

        given().
                header("Content-Type" , "application/json").
                when().
                get("/courses/{courseId}" , 19).
                then().assertThat().statusCode(200).
                and().
                contentType(ContentType.JSON);
    }

    @Test
    public void TestPutSaveCourseTest() {
        Department department = new Department();
        department.setDepartmentId(10L);
        Course course = new Course(null , "Test course" ,
                department , "Test name" ,
                "test Description" , new BigDecimal(3.0));
        given().
                header("Content-Type" , "application/json").
                body(course).
                when().
                post("/courses/course").
                then().
                assertThat().statusCode(200).and().contentType(ContentType.JSON);
    }

    @Test
    public void TestDeleteCourseTest() {
        given().
                header("Content-Type" , "application/json").
                when().
                delete("/course/{courseName}" , "Test Course").
                then().
                assertThat().statusCode(200).
                body(is(equalTo("Course deleted successfully")));

        given().
                header("Content-Type" , "application/json").
                when().
                delete("/course/{courseName}" , "Test Course").
                then().
                assertThat().statusCode(403).
                body(is(equalTo("Course not found")));
    }
}
