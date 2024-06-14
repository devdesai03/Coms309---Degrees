package degreesapp.services;

import degreesapp.models.CourseSection;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseSectionService {
    List<CourseSection> findByInstructorId(Long instructorId);

}
