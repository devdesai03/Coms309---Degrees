package degreesapp.controllers;

import degreesapp.models.Instructor;
import degreesapp.services.InstructorService;
import degreesapp.services.IsuRegistrationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api ( value = "REST APIs related to Instructor Entity", tags = "instructor" )
public class InstructorController {
    @Autowired
    private InstructorService instructorService;

    @ApiOperation ( value = "get list of all Instructors from the database", tags = "instructor" )
    @ApiResponses ( value = {
            @ApiResponse ( code = 200, message = "Success|Ok" ) ,
            @ApiResponse ( code = 404, message = "not found!" )
    } )
    @GetMapping ( "/instructors/" )
    public List<Instructor> getInstructorList() {
        return instructorService.fetchInstructorList();
    }

    @ApiOperation ( value = "get specific Instructor form the database", tags = "instructor" )
    @ApiResponses ( value = {
            @ApiResponse ( code = 200, message = "Success|Ok" ) ,
            @ApiResponse ( code = 404, message = "not found!" )
    } )
    @GetMapping ( "/instructors/{id}" )
    public Instructor getInstructorById(@PathVariable Long id) {
        return instructorService.fetchInstructorById(id);
    }

    /*
    This API path also creates instructors.
    There is NOT a post mapping because it's impossible to create an instructor
    without first having a university ID, in which case the put mapping suffices.
     */
    @ApiOperation ( value = "Update the Instructor in the database", tags = "instructor" )
    @ApiResponses ( value = {
            @ApiResponse ( code = 200, message = "Success|Ok" ) ,
            @ApiResponse ( code = 404, message = "not found!" )
    } )
    @PutMapping ( "/instructors/{id}" )
    public Instructor updateInstructor(@PathVariable Long id , @RequestBody Instructor instructor) {
        return instructorService.updateInstructor(id , instructor);
    }

    @ApiOperation ( value = "delete Instructor from the database by Id", tags = "instructor" )
    @ApiResponses ( value = {
            @ApiResponse ( code = 200, message = "Success|Ok" ) ,
            @ApiResponse ( code = 404, message = "not found!" )
    } )
    @DeleteMapping ( "/instructors/{id}" )
    public void deleteInstructor(@PathVariable Long id) {
        instructorService.deleteInstructorById(id);
    }
}
