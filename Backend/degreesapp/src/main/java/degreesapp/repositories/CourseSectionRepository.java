package degreesapp.repositories;

import degreesapp.models.CourseSection;
import degreesapp.models.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseSectionRepository extends JpaRepository<CourseSection, Long> {
    @Query ( "SELECT section FROM CourseSection section WHERE (:semester IS NULL OR section.semester = :semester) AND section.course.courseId = :courseId" )
    List<CourseSection> findCourseSectionsByCourseIdAndSemester(Long courseId , Semester semester);

    @Query ( "SELECT section FROM CourseSection section WHERE (:semester IS NULL OR section.semester = :semester)" )
    List<CourseSection> findCourseSectionsBySemester(Semester semester);

    @Query ( "select sections from CourseSection sections where sections.instructor.universityId = :instructorId" )
    List<CourseSection> findByInstructorId(Long instructorId);
}
