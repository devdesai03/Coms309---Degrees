package degreesapp.controllers;

import degreesapp.models.CourseRegistration;
import degreesapp.models.CourseSection;
import degreesapp.models.RegistrationId;
import degreesapp.models.Student;
import degreesapp.services.CourseRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
public class CourseRegistrationController {

    @Autowired
    private CourseRegistrationService courseRegistrationService;

    @GetMapping ( "/students/{studentId}/registered-course-sections" )
    public ResponseEntity<List<CourseSection>> getRegisteredCourseSections(@PathVariable Long studentId) {
        // Query the database to retrieve registered course sections for the student
        List<CourseSection> registeredSections = courseRegistrationService.getRegisteredSectionsForStudent(studentId);
        return ResponseEntity.ok(registeredSections);
    }

    @PostMapping ( "/register-course-section" )
    public CourseRegistration registerCourseSection(@RequestBody CourseRegistration courseRegistration) {
        // Save the course registration to the database
        return courseRegistrationService.saveCourseSection(courseRegistration);
    }

    @PutMapping ( "/course-section/{universityId}/{courseId}" )
    public CourseRegistration saveCourseSection(@PathVariable Long universityId , @PathVariable Long courseId) {
        // Save the course registration to the database
        CourseRegistration courseRegistration = new CourseRegistration();
        courseRegistration.setId(new RegistrationId(universityId , courseId));
        Student student = new Student();
        student.setUniversityId(universityId);
        courseRegistration.setStudent(student);
        CourseSection section = new CourseSection();
        section.setId(courseId);
        courseRegistration.setSection(section);
        return courseRegistrationService.saveCourseSection(courseRegistration);
    }

    @Transactional
    @DeleteMapping ( "/drop-registered-course-section/{universityId}/{sectionId}" )
    public ResponseEntity<?> dropRegisteredCourseSection(@PathVariable Long universityId , @PathVariable Long sectionId) {
        // Use the provided RegistrationId to identify the registration to be dropped
        RegistrationId id = new RegistrationId();
        id.setUniversityId(universityId);
        id.setSectionId(sectionId);

        Optional<CourseRegistration> courseRegistration = courseRegistrationService.findById(id);

        if ( courseRegistration.isPresent() ) {
            courseRegistrationService.deleteById(id);
            return ResponseEntity.ok(courseRegistration);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping ( "/course-registration-instructor/{instructorId}" )
    public ResponseEntity<List<CourseRegistration>> getRegistrationsByInstructorId(@PathVariable Long instructorId) {
        List<CourseRegistration> registrations = courseRegistrationService.getRegistrationsByInstructorId(instructorId);
        return ResponseEntity.ok(registrations);
    }

    @Transactional
    @PutMapping ( "/updateGrade/{universityId}/{sectionId}" )
    public ResponseEntity<String> updateGrade(
            @PathVariable Long universityId ,
            @PathVariable Long sectionId ,
            @RequestParam BigDecimal newGrade) {

        RegistrationId id = new RegistrationId(universityId , sectionId);

        try {
            courseRegistrationService.updateGrade(id , newGrade);
            return ResponseEntity.ok("Grade updated successfully");
        } catch ( EntityNotFoundException e ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CourseRegistration not found");
        }
    }
}


