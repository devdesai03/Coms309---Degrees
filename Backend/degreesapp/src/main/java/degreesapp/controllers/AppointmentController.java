package degreesapp.controllers;

import degreesapp.models.Advisor;
import degreesapp.models.Appointment;
import degreesapp.models.Student;
import degreesapp.services.AdvisorService;
import degreesapp.services.AppointmentService;
import degreesapp.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private AdvisorService advisorService;

    // Endpoint for a student to schedule an appointment
    @Transactional
    @PostMapping ( "/{studentId}/schedule-appointment" )
    public ResponseEntity<String> scheduleAppointment(@PathVariable Long studentId , @RequestBody Appointment appointmentReq) {

        // Retrieve student and advisor entities
        Student student = studentService.fetchStudentById(studentId);
        Advisor advisor = advisorService.fetchAdvisorById(appointmentReq.getAdvisor().getAdvisorId());

        // Parse date-time strings to LocalDateTime objects
        LocalDateTime startDateTime = appointmentReq.getStartTime();
        LocalDateTime endDateTime = appointmentReq.getEndTime();

        String description = appointmentReq.getDescription();

        if ( startDateTime.isAfter(endDateTime) ) {
            return ResponseEntity.badRequest().body("Invalid time range");
        }
        //Check advisor availability
        List<Appointment> advisorAppointments = appointmentService.getAppointmentsByAdvisorAndDateRange(advisor.getAdvisorId() , startDateTime , endDateTime);
        if ( !advisorAppointments.isEmpty() ) {
            return ResponseEntity.badRequest().body("Advisor is not available at the specified time range.");
        }

        // Check student availability
        List<Appointment> studentAppointments = appointmentService.getAppointmentsByStudentAndDateRange(studentId , startDateTime , endDateTime);
        if ( !studentAppointments.isEmpty() ) {
            return ResponseEntity.badRequest().body("You already have an appointment at the specified time range.");
        }

        // Schedule the appointment
        Appointment appointment = new Appointment(null , student , advisor , startDateTime , endDateTime , description);
        appointmentService.saveAppointment(appointment);

        return ResponseEntity.ok("Appointment scheduled successfully.");
    }


    //Api to list all the appointment for advisor by advisorId
    @GetMapping ( "/appointments/advisor/{advisorId}" )
    public ResponseEntity<List<Appointment>> getAppointmentsByAdvisorId(@PathVariable Long advisorId) {
        Advisor advisor = advisorService.fetchAdvisorById(advisorId);

        if ( advisor == null ) return ResponseEntity.notFound().build();

        List<Appointment> appointments = appointmentService.getAppointmentsByAdvisor(advisor);
        return ResponseEntity.ok(appointments);
    }

    //Api to list all the appointment for advisor by advisorId and startTime to endTime
    @GetMapping ( "/appointments/advisor/{advisorId}/timeRange" )
    public ResponseEntity<List<Appointment>> getAppointmentsByAdvisorIdAndTimeRange(@PathVariable Long advisorId ,
                                                                                    @RequestParam @DateTimeFormat ( iso = DateTimeFormat.ISO.DATE_TIME ) LocalDateTime startTime ,
                                                                                    @RequestParam @DateTimeFormat ( iso = DateTimeFormat.ISO.DATE_TIME ) LocalDateTime endTime) {

        Advisor advisor = advisorService.fetchAdvisorById(advisorId);

        if ( advisor == null ) return ResponseEntity.notFound().build();

        List<Appointment> appointmentList = appointmentService.getAppointmentsByAdvisorAndDateRange(advisorId , startTime , endTime);
        return ResponseEntity.ok(appointmentList);

    }


    //Api to list all the appointment for student by studentId
    @GetMapping ( "/appointments/student/{studentId}" )
    public ResponseEntity<List<Appointment>> getAppointmentsByStudentId(@PathVariable Long studentId) {

        Student student = studentService.fetchStudentById(studentId);

        if ( student == null ) return ResponseEntity.notFound().build();

        List<Appointment> appointmentList = appointmentService.getAppointmentsByStudent(studentId);

        return ResponseEntity.ok(appointmentList);
    }

}
