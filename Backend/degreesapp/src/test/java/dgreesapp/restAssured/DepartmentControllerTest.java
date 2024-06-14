package dgreesapp.restAssured;

import degreesapp.services.DepartmentService;
import io.restassured.http.ContentType;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class DepartmentControllerTest extends BaseTest {

    @Autowired
    private DepartmentService departmentService;

    @Test
    public void TestGetDepartmentListTest() {
        given().
                when().
                get("/departments").
                then().
                assertThat().
                statusCode(200).
                and().
                contentType(ContentType.JSON);
    }

    @Test
    public void TestGetDepartmentByIdTest() {

        Long departmentId = 10L;
        given().
                when().
                get("/departments/{id}" , departmentId).
                then().assertThat().statusCode(200).
                and().
                contentType(ContentType.JSON);
    }

    @Test
    public void TestPostSaveDepartmentTest() {
        given().
                header("Content-Type" , "application/json").
                body("""
                        {
                            "departmentName": "2322",
                            "departmentAddress": "50014",
                            "departmentCode": "DEP121"
                        }""").
                when().
                post("/departments").
                then().
                assertThat().statusCode(200).and().contentType(ContentType.JSON);
    }

    @Test
    public void TestDeleteDepartmentTest() {
        Long departmentId = departmentService.getDepartmentIdByDepartmentName("2322");
        given().
                header("Content-Type" , "application/json").
                when().
                delete("/departments/{id}" , departmentId).
                then().
                assertThat().statusCode(200).
                body(is
                        (equalTo("Department number "
                                + departmentId
                                + " Deleted Successfully")));

        given().
                header("Content-Type" , "application/json").
                when().
                delete("/departments/{id}" , departmentId).
                then().
                assertThat().statusCode(403).
                body(is
                        (equalTo("Department number "
                                + departmentId +
                                " was not found!")));
    }
}
