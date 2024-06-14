package degreesapp.controllers;

import degreesapp.models.*;
import degreesapp.repositories.CourseRatingRepository;
import degreesapp.repositories.CourseRepository;
import degreesapp.repositories.InstructorRepository;
import degreesapp.repositories.StudentRepository;
import degreesapp.services.CourseRatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class CourseRatingController {

    @Autowired
    private CourseRatingService courseRatingService;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private InstructorRepository instructorRepository;
    @Autowired
    private CourseRatingRepository courseRatingRepository;

    @PostMapping ( "/courseratings" )
    public ResponseEntity<CourseRating> createCourseRating(@RequestBody CourseRating courseRating) {
        CourseRating createdRating = courseRatingService.createCourseRating(courseRating);
        return new ResponseEntity<>(createdRating , HttpStatus.CREATED);
    }

    @GetMapping ( "/courseratings/{studentId}/{courseId}" )
    public ResponseEntity<List<CourseRating>> getCourseRating(@PathVariable Long studentId , @PathVariable Long courseId) {
        
        List<CourseRating> courseRatings = courseRatingRepository.findCourseRatingsByStudentAndCourseIds(studentId , courseId);

        if ( !courseRatings.isEmpty() ) {
            // Handle when the entity is found
            return new ResponseEntity<>(courseRatings , HttpStatus.OK);
        } else {
            // Handle when the entity is not found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/courses/{courseId}/ratings")
    public Map<String, Object> getCourseRatingsForCourse(@PathVariable Long courseId) {
        List<CourseRating> ratings = courseRatingRepository.findCourseRatingsByCourseId(courseId);
        OptionalDouble averageRating = ratings.stream().mapToDouble(cr -> cr.getRating()).average();

        HashMap<String, Object> response = new HashMap<>();
        response.put("ratings", ratings);
        response.put("averageRating", averageRating.isPresent() ? Double.valueOf(averageRating.getAsDouble()) : null);

        return response;
    }
}