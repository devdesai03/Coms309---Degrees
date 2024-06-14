package degreesapp.repositories;

import degreesapp.models.CourseRegistration;
import degreesapp.models.RegistrationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRegistrationRepository extends JpaRepository<CourseRegistration, RegistrationId> {

    @Query ( "select r FROM CourseRegistration r WHERE r.student.universityId = :studentId " )
    List<CourseRegistration> findByStudentId(Long studentId);

    Optional<CourseRegistration> findById(RegistrationId id);

    // Inside CourseRegistrationRepository
    @Query ( "SELECT cr FROM CourseRegistration cr WHERE cr.section.instructor.universityId = :instructorId" )
    List<CourseRegistration> findByInstructorId(Long instructorId);

    @Query ( "select c from CourseRegistration c where c.section.id = :sectionId" )
    List<CourseRegistration> findBySectionId(Long sectionId);

    @Query ( "SELECT cr FROM CourseRegistration cr " +
            "WHERE cr.id.universityId = :universityId AND cr.id.sectionId = :sectionId" )
    Optional<Object> findByUniversityIdAndSectionId(Long universityId , Long sectionId);
}

