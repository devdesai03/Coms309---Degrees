package dgreesapp.restAssured;

import io.restassured.http.ContentType;
import org.junit.Test;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class BreakoutRoomControllerTest extends BaseTest {
    @Test
    public void TestListAvailableBreakoutRoomsTest() {
        given().
                when().
                get("/availableBreakoutRooms").
                then().
                assertThat().
                statusCode(200).
                and().
                contentType(ContentType.JSON).
                body("$" , everyItem(isA(String.class)));
    }

    @Test
    public void TestListOnlineUsersTest() {
        given().
                when().
                get("/onlineUsers").
                then().
                assertThat().
                statusCode(200).
                and().
                contentType(ContentType.JSON).
                body("$" , everyItem(isA(String.class)));
    }

    @Test
    public void TestTetChatHistoryTest() {
        given().
                when().
                pathParam("userName" , "ellie").
                get("/chatHistory/{userName}").
                then().
                assertThat().
                statusCode(200).
                and().
                contentType("text/plain;charset=UTF-8").
                body(not(emptyOrNullString()));
    }
}
