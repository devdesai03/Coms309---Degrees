package degreesapp.controllers;

import degreesapp.models.Department;
import degreesapp.services.DepartmentService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Mohammed Abdalgader
 */

@RestController
@Api ( tags = "Department", description = "Create, list, view, update, and delete departments." )
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @PostMapping ( "/departments" )
    @Operation ( summary = "Create department", description = "Create a new department into the system." )
    public Department saveDepartment(@RequestBody Department department) {
        return departmentService.saveDepartment(department);
    }

    @GetMapping ( "/departments" )
    @Operation ( summary = "List departments", description = "Fetch a list of all departments registered in the system." )
    public List<Department> fetchDepartmentList() {
        return departmentService.fetchDepartmentList();
    }

    @GetMapping ( "/departments/{id}" )
    @Operation ( summary = "Get department", description = "Fetch a specific department by its ID." )
    public Department fetchDepartmentById(@PathVariable ( "id" ) Long departmentId) {
        return departmentService.fetchDepartmentById(departmentId);
    }

    @DeleteMapping ( "/departments/{id}" )
    @Operation ( summary = "Delete department", description = "Delete a department by its ID." )
    @OnDelete ( action = OnDeleteAction.CASCADE )
    public ResponseEntity<String> deleteDepartmentById(@PathVariable ( "id" ) Long departmentId) {
        Department department = departmentService.fetchDepartmentById(departmentId);
        if ( department != null ) {
            departmentService.deleteDepartmentById(departmentId);
            return ResponseEntity.ok("Department number " + departmentId + " Deleted Successfully");
        } else {
            return ResponseEntity.status(403)
                    .body("Department number " + departmentId + " was not found!");
        }
    }

    @PutMapping ( "/departments/{id}" )
    @Operation ( summary = "Update department", description = "Update a department. This means " +
            "replacing the existing data of the department with the data of the request body." )
    public Department updateDepartment(@PathVariable ( "id" ) Long departmentId , @RequestBody Department department) {
        return departmentService.updateDepartment(departmentId , department);
    }

}
