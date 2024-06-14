package degreesapp.controllers;

import degreesapp.models.Course;
import degreesapp.models.HypotheticalCourseRegistration;
import degreesapp.models.PlannedCourse;
import degreesapp.models.Prerequisites;
import degreesapp.repositories.PrerequisiteRepository;
import degreesapp.services.HypotheticalCourseRegistrationService;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AcademicPlanController {
    @Autowired
    private HypotheticalCourseRegistrationService hypotheticalCourseRegistrationService;

    @Autowired
    private PrerequisiteRepository prerequisiteRepository;


    @GetMapping ( "/academic-planner/{studentId}" )
    public ResponseEntity<List<PlannedCourse>> generateAcademicPlanner(
            @PathVariable Long studentId
    ) {
        try {
            // Retrieve the student's hypothetical course registrations for the specified student
            List<HypotheticalCourseRegistration> registrations = hypotheticalCourseRegistrationService.findHypotheticalCourseRegistrationsById_Student(studentId);

            List<PlannedCourse> academicPlanner = new ArrayList<>();

            // Iterate through the registrations
            for ( HypotheticalCourseRegistration registration : registrations ) {
                Course course = registration.getId().getCourse();

                List<Prerequisites> missingPrerequisites = checkMissingPrerequisites(course , registration.getId().getStudent().getUniversityId(), registration.getSemesterNumber());

                // Create a PlannedCourse object with course information and warnings
                PlannedCourse plannedCourse = new PlannedCourse(
                        course ,
                        missingPrerequisites,
                        registration.getSemesterNumber()
                );

                academicPlanner.add(plannedCourse);
            }

            return new ResponseEntity<>(academicPlanner , HttpStatus.OK);
        } catch ( Exception e ) {
            // Handle the exception and return an error response
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<Prerequisites> checkMissingPrerequisites(Course course , Long studentId , int semesterNumber) {
        List<Prerequisites> coursePrerequisites = prerequisiteRepository.findByPostCourse(course.getCourseId());
        List<Prerequisites> missingPrerequisites = new ArrayList<>();

        for ( Prerequisites prerequisite : coursePrerequisites ) {
            if ( !hasTakenPrerequisite(prerequisite , studentId , semesterNumber) ) {
                missingPrerequisites.add(prerequisite);
            }
        }

        return missingPrerequisites;
    }

    private boolean hasTakenPrerequisite(Prerequisites prerequisite , Long studentId , int currentSemester) {

        // Iterate through previous semesters and check if the student has taken the prerequisite
        for ( int semester = currentSemester - 1; semester >= 1; semester-- ) {
            List<HypotheticalCourseRegistration> previousSemesterRegistrations = hypotheticalCourseRegistrationService.findHypotheticalCourseRegistrations(prerequisite.getPreCourse().getCourseId() , studentId , semester);

            for ( HypotheticalCourseRegistration registration : previousSemesterRegistrations ) {
                if ( registration.getId().getCourse().getCourseId().equals(prerequisite.getPreCourse().getCourseId()) ) {
                    return true; // Student has taken the prerequisite in a previous semester
                }
            }
        }

        return false; // Student hasn't taken the prerequisite in any previous semester
    }
}
