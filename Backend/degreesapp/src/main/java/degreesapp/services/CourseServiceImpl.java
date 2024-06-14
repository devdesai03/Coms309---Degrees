package degreesapp.services;

import degreesapp.models.Course;
import degreesapp.models.Department;
import degreesapp.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseRepository courseRepository;

    @Transactional
    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    public List<Course> fetchCourseList() {
        return courseRepository.findAll();
    }

    @Override
    public List<Course> fetchCourseListFiltered(Department department , String courseNumber) {
        return courseRepository.findBy_department_courseNumber(department , courseNumber);
    }

    @Override
    public List<Course> fetchCourseListFiltered(Long departmentId , String departmentCode , String courseNumber) {
        return courseRepository.
                findBy_departmentId_departmentCode_courseNumber(
                        departmentId , departmentCode , courseNumber)
                .stream().sorted(Course.courseComparator()).toList();
    }

    public Course fetchCourseById(Long courseId) {
        return courseRepository.findById(courseId).get();
    }

    @Override
    public Course fetchByCourseName(String courseName) {

        return courseRepository.fetchByCourseName(courseName);
    }

    @Override
    public void delete(Course courseToDelete) {
        courseRepository.delete(courseToDelete);
    }
}
