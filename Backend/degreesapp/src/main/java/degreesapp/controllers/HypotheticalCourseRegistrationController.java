package degreesapp.controllers;

import degreesapp.models.HypotheticalCourseRegistration;
import degreesapp.services.HypotheticalCourseRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
public class HypotheticalCourseRegistrationController {

    @Autowired
    private HypotheticalCourseRegistrationService hypotheticalCourseRegistrationService;


    @PostMapping ( "/hypothetical-course-registrations" )
    public ResponseEntity<HypotheticalCourseRegistration> createHypotheticalCourseRegistration(@RequestBody HypotheticalCourseRegistration hypotheticalCourseRegistration) {
        HypotheticalCourseRegistration createdRegistration = hypotheticalCourseRegistrationService.createHypotheticalCourseRegistration(hypotheticalCourseRegistration);
        return new ResponseEntity<>(createdRegistration , HttpStatus.CREATED);
    }

    @GetMapping ( "/hypothetical-course-registrations/{studentId}" )
    public ResponseEntity<List<HypotheticalCourseRegistration>> getHypotheticalCourseRegistrations(
            @PathVariable Long studentId
    ) {
        List<HypotheticalCourseRegistration> registrations = hypotheticalCourseRegistrationService.findHypotheticalCourseRegistrationsById_Student(studentId);

        if ( !registrations.isEmpty() ) {
            return new ResponseEntity<>(registrations , HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/hypothetical-course-registrations/{courseId}/{studentId}")
    public ResponseEntity<HypotheticalCourseRegistration> updateHypotheticalCourseRegistration(
            @PathVariable Long courseId,
            @PathVariable Long studentId,
            @RequestBody HypotheticalCourseRegistration updatedRegistration
    ) {
        // Check if the registration exists, and if it does, update it
        HypotheticalCourseRegistration existingRegistration = hypotheticalCourseRegistrationService.findHypotheticalCourseRegistrationsById_StudentAndCourseId(studentId,courseId);

        if (existingRegistration != null) {
            updatedRegistration.setId(existingRegistration.getId()); // Make sure the ID stays the same
            HypotheticalCourseRegistration updatedRegistrationResult = hypotheticalCourseRegistrationService.updateHypotheticalCourseRegistration(updatedRegistration);
            return new ResponseEntity<>(updatedRegistrationResult, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping ( "/hypothetical-course-registrations/{courseId}/{studentId}" )
    public ResponseEntity<?> deleteHypotheticalCourseRegistration(
            @PathVariable Long courseId ,
            @PathVariable Long studentId

    ) {
        // Check if the course registration exists
        HypotheticalCourseRegistration registrationToDelete = hypotheticalCourseRegistrationService.findHypotheticalCourseRegistrationsById_StudentAndCourseId(courseId, studentId);

        hypotheticalCourseRegistrationService.deleteHypotheticalCourseRegistration(courseId, studentId, registrationToDelete.getSemesterNumber());

        if ( registrationToDelete != null ) {
            return new ResponseEntity<>(registrationToDelete , HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
