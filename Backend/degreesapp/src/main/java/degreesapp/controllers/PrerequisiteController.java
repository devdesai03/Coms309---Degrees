package degreesapp.controllers;

import degreesapp.models.Course;
import degreesapp.models.Prerequisites;
import degreesapp.repositories.CourseRepository;
import degreesapp.repositories.PrerequisiteRepository;
import degreesapp.services.PrerequisiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping ( "/prerequisites" )
public class PrerequisiteController {
    @Autowired
    private PrerequisiteRepository prerequisiteRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private PrerequisiteService prerequisiteService;

    @PostMapping ( "/prerequisites" )
    public ResponseEntity<?> createPrerequisite(@RequestBody Prerequisites prerequisite) {
        if ( prerequisiteService.hasCyclicDependency(prerequisite) ) {
            return ResponseEntity.badRequest().body("Cyclic dependency detected");
        }

        return ResponseEntity.ok(prerequisiteRepository.save(prerequisite));
    }


    @GetMapping ( "/prerequisites/{courseId}" )
    public List<Prerequisites> getPrerequisitesForCourse(@PathVariable Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        return prerequisiteRepository.findByPostCourse(course.getCourseId());
    }
}
