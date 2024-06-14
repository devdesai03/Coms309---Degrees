package degreesapp.controllers;

import degreesapp.models.Degree;
import degreesapp.models.MajorRegistration;
import degreesapp.services.MajorRegistrationService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(tags = "Major Registration",
        description = "List, get, set, and delete major registrations for students.")
public class MajorRegistrationController {
    @Autowired
    MajorRegistrationService majorRegistrationService;

    @GetMapping("/majorRegistrations")
    @Operation(summary = "List major registrations",
                description = "Use this endpoint to get a list of " +
                        "all major registrations, for all majors and for all students.")
    public List<MajorRegistration> getMajorRegistrationsList() {
        return majorRegistrationService.fetchMajorRegistrationList();
    }

    @PostMapping("/majorRegistrations")
    @Operation(summary = "Create major registration from body",
                description = "Use this endpoint to create a new major registration object " +
                        "directly from a JSON representation in the request body.")
    public MajorRegistration createMajorRegistration(@RequestBody MajorRegistration majorRegistration) {
        return majorRegistrationService.saveMajorRegistration(majorRegistration);
    }

    @GetMapping("/students/{studentId}/majorRegistrations")
    @Operation(summary = "List major registrations for student",
                description = "Use this endpoint to get a list of " +
                        "major registrations for a specific student.")
    public List<MajorRegistration> getMajorRegistrationsListForStudent(@PathVariable Long studentId) {
        return majorRegistrationService.fetchMajorRegistrationListForStudent(studentId);
    }

    @PostMapping("/students/{studentId}/majorRegistrations")
    @Operation(summary = "Create major registration for student",
                description = "Use this endpoint to create a new major registration object " +
                        "while specifying the student ID in the path.")
    public MajorRegistration createMajorRegistrationForStudent(@PathVariable Long studentId,
                                                               @RequestBody MajorRegistration majorRegistration) {
        return majorRegistrationService.addMajorRegistrationForStudent(studentId, majorRegistration);
    }

    @GetMapping("/students/{studentId}/majorRegistrations/{degreeId}")
    @Operation(summary = "Get major registration",
            description = "Use this endpoint to get a specific major registration. " +
                    "This requires both the student ID and degree ID to uniquely identify " +
                    "a major registration."
    )
    public MajorRegistration getMajorRegistrationById(@PathVariable Long studentId, @PathVariable Long degreeId) {
        return majorRegistrationService.fetchMajorRegistrationById(new MajorRegistration.Key(studentId, degreeId));
    }

    @PutMapping("/students/{studentId}/majorRegistrations/{degreeId}")
    @Operation(summary = "Update major registration",
        description = "Use this endpoint to update a major registration. " +
                "This means replacing the major registration data with the data provided in the request body. " +
                "This endpoint can also be used, with or without a body, to create a new major registration " +
                "with the given student ID and degree ID.")
    public MajorRegistration updateMajorRegistration(@PathVariable Long studentId, @PathVariable Long degreeId,
                                                     @RequestBody(required = false) MajorRegistration majorRegistration) {
        return majorRegistrationService.updateMajorRegistration(new MajorRegistration.Key(studentId, degreeId),
                (majorRegistration != null) ? majorRegistration : new MajorRegistration());
    }

    @DeleteMapping("/students/{studentId}/majorRegistrations/{degreeId}")
    @Operation(summary = "Delete major registration",
        description = "Use this endpoint to delete a major registration. " +
                "This effectively unregisters a major for a student.")
    public void deleteMajorRegistration(@PathVariable Long studentId, @PathVariable Long degreeId) {
        majorRegistrationService.deleteMajorRegistration(new MajorRegistration.Key(studentId, degreeId));
    }

    @GetMapping("/students/{studentId}/majors")
    public List<Degree> getMajorsForStudent(@PathVariable Long studentId) {
        return majorRegistrationService.fetchMajorRegistrationListForStudent(studentId)
                .stream().map((MajorRegistration m) -> m.getDegree()).collect(Collectors.toList());
    }

    @PostMapping("/students/{studentId}/majors")
    public Degree addMajorForStudent(@PathVariable Long studentId, @RequestBody Degree major) {
        MajorRegistration newMajorRegistration = majorRegistrationService.addMajorRegistrationForStudent(studentId, major);
        return newMajorRegistration.getDegree();
    }

    @Operation(
            summary = "Get major for a student",
            description = "Use this endpoint if you want to pretend that students only have a single major. " +
                    "If the student has no majors, or more than one major, responds with a 404 error.")
    @GetMapping("/students/{studentId}/major")
    public ResponseEntity<Degree> getSingleMajorForStudent(@PathVariable Long studentId) {
        List<MajorRegistration> majorRegistrations = majorRegistrationService.fetchMajorRegistrationListForStudent(studentId);
        switch (majorRegistrations.size()) {
            case 1: return new ResponseEntity<>(majorRegistrations.get(0).getDegree(), HttpStatus.OK);
            default: return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Set major for a student",
            description = "Use this endpoint if you want to pretend that students only have a single major. " +
                    "This deletes all of the student's majors and replaces them with the one you specify."
    )
    @PutMapping("/students/{studentId}/major")
    public Degree setSingleMajorForStudent(@PathVariable Long studentId, @RequestBody Degree degree) {
        return majorRegistrationService.setMajorForStudent(studentId, degree).getDegree();
    }
}
