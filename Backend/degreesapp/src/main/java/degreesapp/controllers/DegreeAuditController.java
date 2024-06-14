package degreesapp.controllers;

import degreesapp.models.Student;
import degreesapp.services.DegreeAuditService;
import degreesapp.services.StudentService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.PrintWriter;
import java.io.Writer;

@Api(tags = "Degree Audit", description = "APIs for the degree audit service")
@RestController
public class DegreeAuditController {
    @Autowired
    DegreeAuditService degreeAuditService;

    @Autowired
    StudentService studentService;

    @GetMapping(value = "/degreeAudit/{studentId}", produces = MediaType.TEXT_PLAIN_VALUE)
    public void getDegreeAuditString(
        Writer writer,
        @PathVariable Long studentId,
        @RequestParam(required = false) Boolean includeInProgressCourses
    ) {
        PrintWriter printWriter = new PrintWriter(writer);
        try {
            Student student = studentService.fetchStudentById(studentId);
            degreeAuditService.writeDegreeAudit(printWriter, new DegreeAuditService.DegreeAuditOptions() {
                public Student student() {
                    return student;
                }

                public boolean includeInProgressCourses() {
                    if (includeInProgressCourses != null)
                        return includeInProgressCourses;
                    else
                        return super.includeInProgressCourses();
                }
            });
        } catch (Exception e) {
            printWriter.println("\n==================\nAn error occurred.");
            e.printStackTrace();
        };
    }
}
