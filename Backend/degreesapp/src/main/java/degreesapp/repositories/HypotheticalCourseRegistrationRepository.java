package degreesapp.repositories;

import degreesapp.models.HypotheticalCourseRegistration;
import degreesapp.models.HypotheticalCourseRegistrationKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HypotheticalCourseRegistrationRepository extends JpaRepository<HypotheticalCourseRegistration, HypotheticalCourseRegistrationKey> {

    @Query ( "SELECT hcr FROM HypotheticalCourseRegistration hcr " +
            "WHERE hcr.id.course.courseId = :courseId " +
            "AND hcr.id.student.universityId = :studentId " +
            "AND hcr.semesterNumber = :semesterNumber" )
    List<HypotheticalCourseRegistration> findHypotheticalCourseRegistrations(
            @Param ( "courseId" ) Long courseId ,
            @Param ( "studentId" ) Long studentId ,
            @Param ( "semesterNumber" ) int semesterNumber
    );

    @Query("SELECT hcr FROM HypotheticalCourseRegistration hcr " +
            "WHERE hcr.id.course.courseId = :courseId " +
            "AND hcr.id.student.universityId = :studentId " +
            "AND hcr.semesterNumber = :semesterNumber ")

    HypotheticalCourseRegistration findByCourseIdAndStudentIdAndSemesterNumber( @Param("courseId") Long courseId,
                                                                                @Param("studentId") Long studentId,
                                                                                @Param("semesterNumber") int semesterNumber
    );

    @Query("SELECT hcr FROM HypotheticalCourseRegistration hcr " +
            "WHERE hcr.id.student.universityId = :studentId")
    List<HypotheticalCourseRegistration> findHypotheticalCourseRegistrationsById_Student(@Param("studentId") Long studentId);

    @Query("SELECT hcr FROM HypotheticalCourseRegistration hcr " +
              "WHERE hcr.id.student.universityId = :studentId " + "AND hcr.id.course.courseId = :courseId ")
    HypotheticalCourseRegistration findHypotheticalCourseRegistrationsById_StudentAndCourseId(@Param("courseId") Long courseId,
                                                                                              @Param("studentId") Long studentId);

}
