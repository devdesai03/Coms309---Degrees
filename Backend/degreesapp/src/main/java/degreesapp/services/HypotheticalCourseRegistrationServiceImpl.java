package degreesapp.services;

import degreesapp.models.Course;
import degreesapp.models.HypotheticalCourseRegistration;
import degreesapp.models.HypotheticalCourseRegistrationKey;
import degreesapp.models.Student;
import degreesapp.repositories.HypotheticalCourseRegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HypotheticalCourseRegistrationServiceImpl implements HypotheticalCourseRegistrationService {

    @Autowired
    private HypotheticalCourseRegistrationRepository hypotheticalCourseRegistrationRepository;

    @Override
    public HypotheticalCourseRegistration createHypotheticalCourseRegistration(HypotheticalCourseRegistration hypotheticalCourseRegistration) {
        return hypotheticalCourseRegistrationRepository.save(hypotheticalCourseRegistration);
    }

    @Override
    public List<HypotheticalCourseRegistration> findHypotheticalCourseRegistrations(Long courseId , Long studentId , int semesterNumber) {
        return hypotheticalCourseRegistrationRepository.findHypotheticalCourseRegistrations(courseId , studentId , semesterNumber);
    }

    @Override
    public HypotheticalCourseRegistration findHypotheticalCourseRegistration(Course course , Student student) {
        return hypotheticalCourseRegistrationRepository.findById(
            new HypotheticalCourseRegistrationKey(course, student)
        ).orElse(null);
    }

    @Override
    public List<HypotheticalCourseRegistration> deleteHypotheticalCourseRegistration(Long courseId , Long studentId , int semesterNumber) {
        List<HypotheticalCourseRegistration> registrations = findHypotheticalCourseRegistrations(courseId , studentId , semesterNumber);

        if ( !registrations.isEmpty() ) {
            hypotheticalCourseRegistrationRepository.deleteAll(registrations);
        }
        return registrations;
    }

    @Override
    public HypotheticalCourseRegistration findRegistrationById(Long courseId, Long studentId, int semesterNumber) {
        return hypotheticalCourseRegistrationRepository.findByCourseIdAndStudentIdAndSemesterNumber(courseId, studentId, semesterNumber);
    }

    @Override
    public HypotheticalCourseRegistration updateHypotheticalCourseRegistration(HypotheticalCourseRegistration updatedRegistration) {
        Long courseId = updatedRegistration.getId().getCourse().getCourseId();
        Long studentId = updatedRegistration.getId().getStudent().getUniversityId();
        int semesterNumber = updatedRegistration.getSemesterNumber();

        // Check if the registration exists
        HypotheticalCourseRegistration existingRegistration = findRegistrationById(courseId, studentId, semesterNumber);

        if (existingRegistration != null) {

            existingRegistration.getId().setCourse(updatedRegistration.getId().getCourse());
            existingRegistration.setSemesterNumber(updatedRegistration.getSemesterNumber());

            // Save the updated registration
            return hypotheticalCourseRegistrationRepository.save(existingRegistration);
        }

        return null; // Return null if the registration doesn't exist
    }

    @Override
    public List<HypotheticalCourseRegistration> findHypotheticalCourseRegistrationsById_Student(Long studentId) {
        return hypotheticalCourseRegistrationRepository.findHypotheticalCourseRegistrationsById_Student(studentId);
    }

    @Override
    public HypotheticalCourseRegistration findHypotheticalCourseRegistrationsById_StudentAndCourseId(Long studentId, Long courseId) {
        return hypotheticalCourseRegistrationRepository.findHypotheticalCourseRegistrationsById_StudentAndCourseId(studentId, courseId);
    }

}
