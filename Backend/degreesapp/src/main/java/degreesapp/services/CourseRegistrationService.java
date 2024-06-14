package degreesapp.services;

import degreesapp.models.CourseRegistration;
import degreesapp.models.CourseSection;
import degreesapp.models.RegistrationId;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CourseRegistrationService {
    public List<CourseSection> getRegisteredSectionsForStudent(Long studentId);

    public List<CourseRegistration> getRegistrationsForStudent(Long studentId);

    public CourseRegistration saveCourseSection(CourseRegistration courseRegistration);

    public void deleteById(RegistrationId id);

    public Optional<CourseRegistration> findById(RegistrationId id);

    public List<CourseRegistration> getRegistrationsByInstructorId(Long instructorId);

    public void updateGrade(RegistrationId id , BigDecimal newGrade);

    public List<CourseRegistration> getRegistrationsBySectionId(Long sectionId);

}
