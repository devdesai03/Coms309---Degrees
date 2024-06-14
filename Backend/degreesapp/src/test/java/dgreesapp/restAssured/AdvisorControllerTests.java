package dgreesapp.restAssured;

import degreesapp.models.*;
import degreesapp.services.AdvisorService;
import io.restassured.http.ContentType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class AdvisorControllerTests extends BaseTest {

    @Test
    public void TestGetAdvisorTest() {
        given().
                header("Content-Type" , "application/json").
                when().
                get("/advisor").
                then().assertThat().statusCode(200).
                and().
                contentType(ContentType.JSON);
    }

    @Test
    public void TestGetAdvisorByIdTest() {

        given().
                header("Content-Type" , "application/json").
                when().
                get("/advisor/{id}" , 60).
                then().assertThat().statusCode(200).
                and().
                contentType(ContentType.JSON);
    }

    @Test
    public void TestPutSaveAdvisorTest() {

        Long advisorId = 63L;
        Department department = new Department();
        department.setDepartmentId(10L);
        department.setDepartmentName("Com s");
        IsuRegistration isuRegistration = new IsuRegistration();
        isuRegistration.setUniversityId(advisorId);
        Advisor advisor = new Advisor();
        advisor.setIsuRegistration(isuRegistration);
        advisor.setAdvisorId(advisorId);
        advisor.setAdvisorDepartment(department);
        given().
                header("Content-Type" , "application/json").
                body(advisor).
                when().
                put("/advisor/{id}" , advisorId).
                then().
                assertThat().statusCode(200).and().contentType(ContentType.JSON);
    }

    @Test
    public void TestDeleteAdvisorTest() {
        long advisorId = 1083095478L;
        given().
                header("Content-Type" , "application/json").
                when().
                delete("/advisor/{id}" , advisorId).
                then().
                assertThat().statusCode(404).
                body(is(equalTo("Advisor number " + advisorId + " Does not Exist")));
    }
}
