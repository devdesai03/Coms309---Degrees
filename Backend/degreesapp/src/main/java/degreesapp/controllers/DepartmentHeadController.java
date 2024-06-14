package degreesapp.controllers;

import degreesapp.models.Department;
import degreesapp.models.DepartmentHead;
import degreesapp.services.DepartmentHeadService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "Department Head", description = "List, get, update, create, and delete department heads.")
public class DepartmentHeadController {
    @Autowired
    DepartmentHeadService departmentHeadService;

    @GetMapping("/departmentHeads")
    @Operation(summary = "List department heads", description = "Get a list of all department heads.")
    List<DepartmentHead> getDepartmentHeadList() {
        return departmentHeadService.fetchDepartmentHeadList();
    }

    @GetMapping("/departmentHeads/{id}")
    @Operation(summary = "Get department head", description = "Get a department head by ID.")
    DepartmentHead getDepartmentHeadList(@PathVariable Long id) {
        return departmentHeadService.fetchDepartmentHeadById(id);
    }

    // No POST mapping because it's impossible to create a new department head
    // without knowing an ID first.
    @PutMapping("/departmentHeads/{id}")
    @Operation(summary = "Create/update department head", description = "Use this endpoint to assign a department " +
            "entry and status to a specific university ID. This can either update or create a department " +
            "head entry, depending on whether or not one already existed.")
    DepartmentHead putDepartmentHead(@RequestBody DepartmentHead departmentHead, @PathVariable Long id) {
        return departmentHeadService.updateDepartmentHead(id, departmentHead);
    }

    @DeleteMapping("/departmentHeads/{id}")
    @Operation(summary = "Delete department head", description = "Use this endpoint to delete a department head " +
            "entry. This effectively revokes department head status from a user.")
    void deleteDepartmentHead(@PathVariable Long id) {
        departmentHeadService.deleteDepartmentHeadById(id);
    }
}
