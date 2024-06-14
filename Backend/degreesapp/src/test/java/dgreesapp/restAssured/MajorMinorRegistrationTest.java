package dgreesapp.restAssured;

import degreesapp.services.DepartmentService;
import io.restassured.http.ContentType;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Consumer;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class MajorMinorRegistrationTest extends BaseTest {
    private enum MajorMinor {
        MAJOR, MINOR;

        public String mRegs() {
            return switch (this) {
                case MAJOR -> "majorRegistrations";
                case MINOR -> "minorRegistrations";
            };
        }

        public String ms() {
            return switch (this) {
                case MAJOR -> "majors";
                case MINOR -> "minors";
            };
        }

        public String m() {
            return switch (this) {
                case MAJOR -> "major";
                case MINOR -> "minor";
            };
        }

        public static void forEach(Consumer<MajorMinor> consumer) {
            consumer.accept(MAJOR);
            consumer.accept(MINOR);
        }
    }

    @Test
    public void listRegTest() {
        MajorMinor.forEach(m -> {
            given().get("/" + m.mRegs() + "/")
                    .then()
                    .statusCode(between(200, 300))
                    .contentType(ContentType.JSON)
                    .body("", everyItem(anything()));
        });
    }

    @Test
    public void listRegForStudentTest() {
        Long studentId = 19L;

        MajorMinor.forEach(m -> {
            given().get("/students/" + studentId + "/" + m.mRegs() + "/")
                    .then()
                    .statusCode(between(200, 300))
                    .contentType(ContentType.JSON)
                    .body("", everyItem(anything()));
        });
    }

    @Test
    public void listModelForStudentTest() {
        Long studentId = 19L;

        MajorMinor.forEach(m -> {
            given().get("/students/" + studentId + "/" + m.ms() + "/")
                    .then()
                    .statusCode(between(200, 300))
                    .contentType(ContentType.JSON)
                    .body("", everyItem(anything()));
        });
    }

    @Test
    public void getSingleMajorForStudentTest() {
        Long studentId = 19L;

        given().get("/students/" + studentId + "/major")
                .then()
                .assertThat()
                .statusCode(either(between(200, 300)).or(equalTo(404)));
    }

    @Test
    public void testRegistrationEdit() {
        Long studentId = 19L;
        Long degreeId = 620L;

        MajorMinor.forEach(m -> {
            given()
                    .body(String.format("""
                            {
                                "student": {"universityId": %s},
                                "degree": {"id": %s}
                            }
                            """, studentId, degreeId, studentId, degreeId))
                    .contentType(ContentType.JSON)
                    .post("/" + m.mRegs() + "/")
                    .then()
                    .statusCode(between(200, 300))
                    .contentType(ContentType.JSON);

            given()
                    .get("/" + m.mRegs() + "/")
                    .then()
                    .statusCode(between(200, 300))
                    .contentType(ContentType.JSON)
                    .body("", hasItem(
                            allOf(
                                    hasEntry(equalTo("student"), hasEntry(equalTo("universityId"), numEqualTo(studentId))),
                                    hasEntry(equalTo("degree"), hasEntry(equalTo("id"), numEqualTo(degreeId)))
                            )
                    ));

            given()
                    .delete("/students/" + studentId + "/" + m.mRegs() + "/" + degreeId)
                    .then()
                    .statusCode(between(200, 300));
        });
    }

    @Test
    public void testRegistrationForStudentEdit() {
        Long studentId = 19L;
        Long degreeId = 620L;

        MajorMinor.forEach(m -> {
            given()
                    .body(String.format("""
                            {
                                "degree": {"id": %s}
                            }
                            """, degreeId))
                    .contentType(ContentType.JSON)
                    .post("/students/" + studentId + "/" + m.mRegs() + "/")
                    .then()
                    .statusCode(between(200, 300))
                    .contentType(ContentType.JSON);

            given()
                    .get("/students/" + studentId + "/" + m.mRegs() + "/" + degreeId)
                    .then()
                    .statusCode(between(200, 300))
                    .contentType(ContentType.JSON)
                    .body("student.universityId", numEqualTo(studentId))
                    .body("degree.id", numEqualTo(degreeId));

            given()
                    .get("/students/" + studentId + "/" + m.mRegs())
                    .then()
                    .statusCode(between(200, 300))
                    .contentType(ContentType.JSON)
                    .body("", hasItem(hasEntry(equalTo("degree"), hasEntry(equalTo("id"), numEqualTo(degreeId)))));

            given()
                    .delete("/students/" + studentId + "/" + m.mRegs() + "/" + degreeId)
                    .then()
                    .statusCode(between(200, 300));
        });
    }

    @Test
    public void testRegistrationByIdEdit() {
        Long studentId = 19L;
        Long degreeId = 620L;

        MajorMinor.forEach(m -> {
            given()
                    .body(String.format("""
                            {}
                            """, studentId, degreeId))
                    .contentType(ContentType.JSON)
                    .put("/students/" + studentId + "/" + m.mRegs() + "/" + degreeId)
                    .then()
                    .statusCode(between(200, 300))
                    .contentType(ContentType.JSON);

            given()
                    .get("/students/" + studentId + "/" + m.mRegs() + "/" + degreeId)
                    .then()
                    .statusCode(between(200, 300))
                    .contentType(ContentType.JSON)
                    .body("student.universityId", numEqualTo(studentId))
                    .body("degree.id", numEqualTo(degreeId));

            given()
                    .delete("/students/" + studentId + "/" + m.mRegs() + "/" + degreeId)
                    .then()
                    .statusCode(between(200, 300));
        });
    }

    @Test
    public void testDegForStudentEdit() {
        Long studentId = 19L;
        Long degreeId = 620L;

        MajorMinor.forEach(m -> {
            given()
                    .body(String.format("""
                            {
                                "id": %s
                            }
                            """, degreeId))
                    .contentType(ContentType.JSON)
                    .post("/students/" + studentId + "/" + m.ms())
                    .then()
                    .statusCode(between(200, 300))
                    .contentType(ContentType.JSON);

            given()
                    .get("/students/" + studentId + "/" + m.ms())
                    .then()
                    .statusCode(between(200, 300))
                    .contentType(ContentType.JSON)
                    .body("", hasItem(hasEntry(equalTo("id"), numEqualTo(degreeId))));

            given()
                    .delete("/students/" + studentId + "/" + m.mRegs() + "/" + degreeId)
                    .then()
                    .statusCode(between(200, 300));
        });
    }

    @Test
    public void testSingleMajorForStudentEdit() {
        Long studentId = 19L;
        Long degreeId = 620L;

        var m = MajorMinor.MAJOR;

        given()
                .body(String.format("""
                        {
                            "id": %s
                        }
                        """, degreeId))
                .contentType(ContentType.JSON)
                .put("/students/" + studentId + "/" + m.m())
                .then()
                .statusCode(between(200, 300))
                .contentType(ContentType.JSON);

        given()
                .get("/students/" + studentId + "/" + m.m())
                .then()
                .statusCode(between(200, 300))
                .contentType(ContentType.JSON)
                .body("id", numEqualTo(degreeId));

        given()
                .get("/students/" + studentId + "/" + m.ms())
                .then()
                .statusCode(between(200, 300))
                .contentType(ContentType.JSON)
                .body("", contains(hasEntry(equalTo("id"), numEqualTo(degreeId.intValue()))));

        given()
                .delete("/students/" + studentId + "/" + m.mRegs() + "/" + degreeId)
                .then()
                .statusCode(between(200, 300));
    }
}
