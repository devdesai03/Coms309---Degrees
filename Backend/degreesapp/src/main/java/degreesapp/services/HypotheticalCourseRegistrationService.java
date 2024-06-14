package degreesapp.services;

import degreesapp.models.Course;
import degreesapp.models.HypotheticalCourseRegistration;
import degreesapp.models.Student;

import java.util.List;
import java.util.Optional;

public interface HypotheticalCourseRegistrationService {
     HypotheticalCourseRegistration createHypotheticalCourseRegistration(HypotheticalCourseRegistration hypotheticalCourseRegistration);

     List<HypotheticalCourseRegistration> findHypotheticalCourseRegistrations(Long courseId , Long studentId , int semesterNumber);

    public HypotheticalCourseRegistration findHypotheticalCourseRegistration(Course course , Student student);

     List<HypotheticalCourseRegistration> deleteHypotheticalCourseRegistration(Long courseId , Long studentId , int semesterNumber);

    HypotheticalCourseRegistration findRegistrationById(Long courseId, Long studentId, int semesterNumber);

    HypotheticalCourseRegistration updateHypotheticalCourseRegistration(HypotheticalCourseRegistration updatedRegistration);

    List<HypotheticalCourseRegistration> findHypotheticalCourseRegistrationsById_Student(Long studentId);
    HypotheticalCourseRegistration findHypotheticalCourseRegistrationsById_StudentAndCourseId(Long studentId, Long courseId);
}
