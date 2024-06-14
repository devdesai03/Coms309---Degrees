package degreesapp.controllers;

import degreesapp.models.Degree;
import degreesapp.services.DegreeService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Degrees", description = "APIs for managing degrees.")
@RestController
public class DegreeController {
    @Autowired
    DegreeService degreeService;

    @GetMapping("/degrees")
    @Operation(summary = "List degrees", description = "Get a list of all degrees registered in the system.")
    public List<Degree> getDegreeList() {
        return degreeService.fetchDegreeList();
    }

    @PostMapping("/degrees")
    @Operation(summary = "Create degree", description = "Register a new degree into the system.")
    public Degree createDegree(@RequestBody Degree degree) {
        return degreeService.saveDegree(degree);
    }

    @GetMapping("/degrees/{degreeId}")
    @Operation(summary = "Get degree", description = "Get a degree by its ID.")
    public Degree getDegreeById(@PathVariable Long degreeId) {
        return degreeService.fetchDegreeById(degreeId);
    }

    @PutMapping("/degrees/{degreeId}")
    @Operation(summary = "Update degree", description = "Update a degree by its ID. This means the " +
            "degree object's data is replaced with the data in the request body.")
    public Degree updateDegree(@PathVariable Long degreeId, @RequestBody Degree degree) {
        return degreeService.updateDegree(degreeId, degree);
    }

    @DeleteMapping("/degrees/{degreeId}")
    @Operation(summary = "Delete degree", description = "Deregister a degree from the system by its ID.")
    public void deleteDegree(@PathVariable Long degreeId) {
        degreeService.deleteDegreeById(degreeId);
    }
}
