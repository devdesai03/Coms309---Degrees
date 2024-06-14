package degreesapp.services;

import degreesapp.models.CourseRating;
import degreesapp.models.CourseRatingKey;
import degreesapp.repositories.CourseRatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CourseRatingServiceImpl implements CourseRatingService {
    @Autowired
    private CourseRatingRepository courseRatingRepository;

    @Override
    public CourseRating createCourseRating(CourseRating courseRating) {
        // Implement the logic to save the course rating to the database
        return courseRatingRepository.save(courseRating);
    }

}
