package degreesapp.controllers;

import degreesapp.models.Degree;
import degreesapp.models.MinorRegistration;
import degreesapp.services.MinorRegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class MinorRegistrationController {
    @Autowired
    MinorRegistrationService minorRegistrationService;

    @GetMapping("/minorRegistrations")
    public List<MinorRegistration> getMinorRegistrationsList() {
        return minorRegistrationService.fetchMinorRegistrationList();
    }

    @PostMapping("/minorRegistrations")
    public MinorRegistration createMinorRegistration(@RequestBody MinorRegistration minorRegistration) {
        return minorRegistrationService.saveMinorRegistration(minorRegistration);
    }

    @GetMapping("/students/{studentId}/minorRegistrations")
    public List<MinorRegistration> getMinorRegistrationsListForStudent(@PathVariable Long studentId) {
        return minorRegistrationService.fetchMinorRegistrationListForStudent(studentId);
    }

    @PostMapping("/students/{studentId}/minorRegistrations")
    public MinorRegistration createMinorRegistrationForStudent(@PathVariable Long studentId,
                                                               @RequestBody MinorRegistration minorRegistration) {
        return minorRegistrationService.addMinorRegistrationForStudent(studentId, minorRegistration);
    }

    @GetMapping("/students/{studentId}/minorRegistrations/{degreeId}")
    public MinorRegistration getMinorRegistrationById(@PathVariable Long studentId, @PathVariable Long degreeId) {
        return minorRegistrationService.fetchMinorRegistrationById(new MinorRegistration.Key(studentId, degreeId));
    }

    @PutMapping("/students/{studentId}/minorRegistrations/{degreeId}")
    public MinorRegistration updateMinorRegistration(@PathVariable Long studentId, @PathVariable Long degreeId,
                                                     @RequestBody(required = false) MinorRegistration minorRegistration) {
        return minorRegistrationService.updateMinorRegistration(new MinorRegistration.Key(studentId, degreeId),
                (minorRegistration != null) ? minorRegistration : new MinorRegistration());
    }

    @DeleteMapping("/students/{studentId}/minorRegistrations/{degreeId}")
    public void deleteMinorRegistration(@PathVariable Long studentId, @PathVariable Long degreeId) {
        minorRegistrationService.deleteMinorRegistration(new MinorRegistration.Key(studentId, degreeId));
    }

    @GetMapping("/students/{studentId}/minors")
    public List<Degree> getMinorsForStudent(@PathVariable Long studentId) {
        return minorRegistrationService.fetchMinorRegistrationListForStudent(studentId)
                .stream().map((MinorRegistration m) -> m.getDegree()).collect(Collectors.toList());
    }

    @PostMapping("/students/{studentId}/minors")
    public Degree addMinorForStudent(@PathVariable Long studentId, @RequestBody Degree minor) {
        MinorRegistration newMinorRegistration = minorRegistrationService.addMinorRegistrationForStudent(studentId, minor);
        return newMinorRegistration.getDegree();
    }
}
