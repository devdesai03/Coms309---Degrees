package degreesapp.controllers;

import degreesapp.models.Course;
import degreesapp.models.CourseSection;
import degreesapp.models.Semester;
import degreesapp.repositories.CourseSectionRepository;
import degreesapp.services.CourseSectionService;
import degreesapp.services.CourseService;
import degreesapp.services.CourseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
public class CourseSectionController {

    @Autowired
    private CourseSectionRepository sectionRepository;
    @Autowired
    private CourseSectionService courseSectionService;

    @PostMapping ( "/courseSections" )
    public ResponseEntity<?> createCourseSection(@Valid @RequestBody CourseSection courseSection) {
        CourseSection savedCourseSection = sectionRepository.save(courseSection);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCourseSection);
    }

    @GetMapping("/courseSections")
    public ResponseEntity<?> listCourseSections(@RequestParam(name = "semester", required = false) String semesterString) {
        Semester semester;
        if (semesterString == null) {
            semester = null;
        } else {
            semester = Semester.fromString(semesterString);
            if (semester == null) {
                return ResponseEntity.badRequest().body("Invalid semester");
            }
        }
        return ResponseEntity.ok(sectionRepository
                .findCourseSectionsBySemester(semester)
                .stream()
                .sorted(Comparator
                        .comparing(CourseSection::getCourse, Course.courseComparator())
                        .thenComparing(CourseSection::getSemester)
                        .thenComparing(CourseSection::getSectionIdentifier, Course.alphanumericComparator())
                )
                .toList());
    }


    @GetMapping ( "/courseSections/{id}" )
    public ResponseEntity<CourseSection> getCourseSectionById(@PathVariable Long id) {
        Optional<CourseSection> courseSection = sectionRepository.findById(id);
        return courseSection.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Transactional
    @GetMapping ( "/course-sections-by-instructor/{instructorId}" )
    public ResponseEntity<List<CourseSection>> getSectionsByInstructorId(@PathVariable Long instructorId) {
        try {
            List<CourseSection> sections = courseSectionService.findByInstructorId(instructorId);
            if ( sections.isEmpty() ) {
                return ResponseEntity.notFound().build();
            } else {
                return ResponseEntity.ok(sections.stream()
                        .sorted(Comparator.comparing(CourseSection::getCourse, Course.courseComparator())
                                .thenComparing(CourseSection::getSemester)
                                .thenComparing(CourseSection::getSectionIdentifier, Course.alphanumericComparator())
                        ).toList());
            }
        } catch ( Exception e ) {
            // Log or print the exception
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping ( "/courseSections/{id}" )
    public ResponseEntity<?> deleteCourseSection(@PathVariable Long id) {
        Optional<CourseSection> courseSection = sectionRepository.findById(id);
        if ( courseSection.isPresent() ) {
            sectionRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/courses/{courseId}/courseSections")
    public ResponseEntity<?> listCourseSectionsByCourseId(@PathVariable Long courseId,
                                                          @RequestParam(name = "semester", required = false) String semesterString) {
        Semester semester;
        if (semesterString == null) {
            semester = null;
        } else {
            semester = Semester.fromString(semesterString);
            if (semester == null) {
                return ResponseEntity.badRequest().body("Invalid semester");
            }
        }
        List<CourseSection> list =
                sectionRepository.findCourseSectionsByCourseIdAndSemester(courseId, semester)
                .stream()
                .sorted(Comparator
                        .comparing(CourseSection::getCourse, Course.courseComparator())
                        .thenComparing(CourseSection::getSemester)
                        .thenComparing(CourseSection::getSectionIdentifier, Course.alphanumericComparator())
                )
                .toList();
        return ResponseEntity.ok(list);
    }
}
