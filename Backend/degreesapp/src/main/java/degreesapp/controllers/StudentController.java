package degreesapp.controllers;

import degreesapp.models.Advisor;
import degreesapp.models.Appointment;
import degreesapp.models.Student;
import degreesapp.services.AdvisorService;
import degreesapp.services.AppointmentService;
import degreesapp.services.StudentService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@Api ( tags = "Student", description = "List, get, update, and delete student information." )
public class StudentController {
    @Autowired
    private StudentService studentService;

    @Autowired
    private AdvisorService advisorService;

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping ( "/students/" )
    @Operation ( summary = "List students", description = "Get a list of all students." )
    public List<Student> getStudentList() {
        return studentService.fetchStudentList();
    }

    @GetMapping ( "/students/{id}" )
    @Operation ( summary = "Get student", description = "Get a student by their university ID." )
    public Student getStudentById(@PathVariable Long id) {
        return studentService.fetchStudentById(id);
    }

    /*
    This API path also creates students.
    There is NOT a post mapping because it's impossible to create a student
    without first having a university ID, in which case the put mapping suffices.
     */
    @PutMapping ( "/students/{id}" )
    @Operation ( summary = "Update student", description = "Update a student by their university ID. " +
            "This means replacing the student's existing information with the information specified " +
            "in the request body." )
    public Student updateStudent(@PathVariable Long id , @RequestBody Student student) {
        return studentService.updateStudent(id , student);
    }

    @DeleteMapping ( "/students/{id}" )
    @Operation ( summary = "Delete student", description = "Delete the student information associated with " +
            "a specific university ID. This effectively means the ISU user is no longer considered to be " +
            "a student by the system." )
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudentById(id);
    }

}
