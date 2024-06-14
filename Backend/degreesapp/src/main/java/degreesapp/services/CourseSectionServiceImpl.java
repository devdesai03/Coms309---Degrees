package degreesapp.services;

import degreesapp.models.CourseSection;
import degreesapp.repositories.CourseSectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseSectionServiceImpl implements CourseSectionService {

    @Autowired
    private CourseSectionRepository courseSectionRepository;

    @Override
    public List<CourseSection> findByInstructorId(Long instructorId) {
        return courseSectionRepository.findByInstructorId(instructorId);
    }

}
