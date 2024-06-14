package degreesapp.controllers;

import degreesapp.models.IsuRegistration;
import degreesapp.services.IsuRegistrationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Mohammed Abdalgader
 */

@RestController
@Api ( value = "REST APIs for IsuRegistration Entity", tags = "isuRegistration" )
public class IsuRegistrationController {
    @Autowired
    private IsuRegistrationService isuRegistrationService;

    @ApiOperation ( value = "create an IsuRegistration in the database", tags = "isuRegistration" )
    @ApiResponses ( value = {
            @ApiResponse ( code = 200, message = "Success|Ok" ) ,
            @ApiResponse ( code = 404, message = "not found!" )
    } )
    @PostMapping ( "/isuRegistration" )
    public IsuRegistration saveIsuRegistration(@RequestBody IsuRegistration isuRegistration) {
        return isuRegistrationService.saveIsuRegistration(isuRegistration);
    }

    @ApiOperation ( value = "get a list of all IsuRegistration form the database", tags = "isuRegistration" )
    @ApiResponses ( value = {
            @ApiResponse ( code = 200, message = "Success|Ok" ) ,
            @ApiResponse ( code = 404, message = "not found!" )
    } )
    @GetMapping ( "/isuRegistration" )
    public List<IsuRegistration> fetchIsuRegistrationList() {
        return isuRegistrationService.fetchIsuRegistrationList();
    }

    @ApiOperation ( value = "get an IsuRegistration from the database", tags = "isuRegistration" )
    @ApiResponses ( value = {
            @ApiResponse ( code = 200, message = "Success|Ok" ) ,
            @ApiResponse ( code = 404, message = "not found!" )
    } )
    @GetMapping ( "/isuRegistration/{id}" )
    public IsuRegistration fetchIsuRegistrationUserById(@PathVariable ( "id" ) Long IsuRegistrationId) {
        return isuRegistrationService.fetchIsuRegistrationUserById(IsuRegistrationId);
    }

    @ApiOperation ( value = "delete an IsuRegistration from the database", tags = "isuRegistration" )
    @ApiResponses ( value = {
            @ApiResponse ( code = 200, message = "Success|Ok" ) ,
            @ApiResponse ( code = 404, message = "not found!" )
    } )
    @DeleteMapping ( "/isuRegistration/{id}" )
    public String deleteIsuRegistrationUserById(@PathVariable ( "id" ) Long isuRegistrationId) {
        isuRegistrationService.deleteIsuRegistrationUserById(isuRegistrationId);

        return "Isu Registration number " + isuRegistrationId + " Deleted Successfully";
    }

    @ApiOperation ( value = "Update the IsuRegistration in the database", tags = "isuRegistration" )
    @ApiResponses ( value = {
            @ApiResponse ( code = 200, message = "Success|Ok" ) ,
            @ApiResponse ( code = 404, message = "not found!" )
    } )
    @PutMapping ( "/isuRegistration/{id}" )
    public IsuRegistration updateIsuRegistrationUser(@PathVariable ( "id" ) Long userId , @RequestBody IsuRegistration isuRegistration) {
        return isuRegistrationService.updateIsuRegistrationUser(userId , isuRegistration);
    }
}
