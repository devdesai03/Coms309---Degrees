package degreesapp.repositories;

import degreesapp.models.Course;
import degreesapp.models.CourseRating;
import degreesapp.models.Instructor;
import degreesapp.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRatingRepository extends JpaRepository<CourseRating, Long> {

    @Query ( "SELECT cr FROM CourseRating cr " +
            "WHERE (:studentId IS NULL OR cr.id.student.universityId = :studentId) " +
            "AND (:courseId IS NULL OR cr.id.course.courseId = :courseId)" )
    List<CourseRating> findCourseRatingsByStudentAndCourseIds(
            @Param ( "studentId" ) Long studentId ,
            @Param ( "courseId" ) Long courseId
    );

    @Query("SELECT cr FROM CourseRating cr WHERE cr.id.course.courseId = :courseId")
    List<CourseRating> findCourseRatingsByCourseId(Long courseId);
}
