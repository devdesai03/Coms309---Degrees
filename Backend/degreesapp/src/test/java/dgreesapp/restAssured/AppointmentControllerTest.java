package dgreesapp.restAssured;

import io.restassured.http.ContentType;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class AppointmentControllerTest extends BaseTest{

    @Test
    public void TestGetAppointmentsByStudentId(){
        given().
                when().
                get("/appointments/student/{studentId}",18L).
                then().
                assertThat().
                statusCode(200).
                and().
                contentType(ContentType.JSON);
    }
    @Test
    public void TestGetAppointmentsByAdvisorIdAndTimeRange(){
        given().
                when().param("startTime" , "2023-11-28T09:00:00").
                param("endTime","2023-11-28T22:00:00").
                get("/appointments/advisor/{advisorId}/timeRange",60L).
                then().
                assertThat().
                statusCode(200).
                and().
                contentType(ContentType.JSON);
    }

    @Test
    public void TestGetAppointmentsByAdvisorId(){
        given().
                when().
                get("/appointments/advisor/{advisorId}",60L).
                then().
                assertThat().
                statusCode(200).
                and().
                contentType(ContentType.JSON);
    }

    @Test
    public void TestScheduleAppointment(){

        LocalDate date = LocalDate.now().plusDays((int)(5000 * Math.random()));
        LocalDateTime startTime = date.atTime(LocalTime.of(12, 00));
        LocalDateTime endTime = date.atTime(LocalTime.of(13, 00));

        given().
                contentType(ContentType.JSON).
                body(String.format("""
                    {
                       "advisor":{
                          "advisorId":60
                       },
                       "startTime":"%s",
                       "endTime":"%s",
                       "description":"Webex meeting"
                    }""", startTime.toString(), endTime.toString())).
                when().
                post("/{studentId}/schedule-appointment",18L).
                then().
                assertThat().
                statusCode(200).
                body(is(equalTo("Appointment scheduled successfully.")));

        given().
                contentType(ContentType.JSON).
                body(String.format("""
                    {
                       "advisor":{
                          "advisorId":60
                       },
                       "startTime":"%s",
                       "endTime":"%s",
                       "description":"Webex meeting"
                    }""", startTime.toString(), endTime.toString())).
                when().
                post("/{studentId}/schedule-appointment",18L).
                then().
                assertThat().
                statusCode(400);
    }

}
