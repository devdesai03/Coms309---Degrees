package degreesapp.services;

import degreesapp.models.Course;
import degreesapp.models.Department;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CourseService {
    public Course saveCourse(Course course);

    public List<Course> fetchCourseList();

    public List<Course> fetchCourseListFiltered(Department department , String courseNumber);

    public List<Course> fetchCourseListFiltered(Long departmentId , String departmentCode , String courseNumber);

    public Course fetchCourseById(Long courseId);

    Course fetchByCourseName(String courseName);

    void delete(Course courseToDelete);
}