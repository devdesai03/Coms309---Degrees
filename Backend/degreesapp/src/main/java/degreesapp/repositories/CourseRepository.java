package degreesapp.repositories;

import degreesapp.models.Course;
import degreesapp.models.Department;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The repository for getting courses.
 * <p>
 * These method names are complicated, but necessary in order
 * to let Spring Boot know what database queries/joins we want to make.
 *
 * @author Ellie CHen
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query ( "SELECT c FROM Course c, Department d WHERE c.courseDepartment = d\n" +
            "AND (:department IS NULL OR :department = d)\n" +
            "AND (:courseNumber IS NULL OR :courseNumber = c.courseNumber)\n" )
    List<Course> findBy_department_courseNumber(
            Department department , String courseNumber);

    @Query ( "SELECT c FROM Course c, Department d WHERE c.courseDepartment = d\n" +
            "AND (:departmentId IS NULL OR :departmentId = d.departmentId)\n" +
            "AND (:departmentCode IS NULL OR :departmentCode = d.departmentCode)\n" +
            "AND (:courseNumber IS NULL OR :courseNumber = c.courseNumber)\n" )
    List<Course> findBy_departmentId_departmentCode_courseNumber(
            Long departmentId , String departmentCode , String courseNumber);

    @Query ( "select course from Course course where lower(course.courseName) = lower(:courseName)" )
    Course fetchByCourseName(String courseName);
}
