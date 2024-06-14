package degreesapp.controllers;

import degreesapp.models.Advisor;
import degreesapp.services.AdvisorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @author Mohammed Abdalgader
 */
@RestController
@Api ( value = "Adviser Endpoint", tags = "advisor" )
public class AdvisorController {

    @Autowired
    private AdvisorService advisorService;

    @ApiOperation ( value = "get list of advisors from the database", tags = "advisor" )
    @ApiResponses ( value = {
            @ApiResponse ( code = 200, message = "Success|Ok" ) ,
            @ApiResponse ( code = 404, message = "not found!" )
    } )
    @GetMapping ( "/advisor" )
    public List<Advisor> fetchAdvisorList() {
        return advisorService.fetchAdvisorList();
    }

    @ApiOperation ( value = "get specific advisor from the database by the Id", tags = "advisor" )
    @ApiResponses ( value = {
            @ApiResponse ( code = 200, message = "Success|Ok" ) ,
            @ApiResponse ( code = 404, message = "not found!" )
    } )
    @GetMapping ( "/advisor/{id}" )
    @ApiIgnore // fix Springfox bug, see https://github.com/springfox/springfox/issues/3482
    public Advisor fetchAdvisorById(@PathVariable ( "id" ) Long advisorId) {
        return advisorService.fetchAdvisorById(advisorId);
    }


    /**
     * This API path also creates Advisor.
     * There is NOT a post mapping because it's impossible to create an advisor
     * without first having a university ID, in which case the put mapping suffices.
     */
    @PutMapping ( "/advisor/{id}" )
    @ApiOperation ( value = "Update the advisor in the database", tags = "advisor" )
    @ApiResponses ( value = {
            @ApiResponse ( code = 200, message = "Success|Ok" ) ,
            @ApiResponse ( code = 404, message = "not found!" )
    } )

    public Advisor updateAdvisor(@PathVariable ( "id" ) Long advisorId , @RequestBody Advisor advisor) {
        try {
            return advisorService.updateAdvisor(advisorId , advisor);
        } catch ( Exception e ) {
            e.printStackTrace();
            throw e;
        }
    }


    @ApiOperation ( value = "delete an advisor from the database", tags = "advisor" )
    @ApiResponses ( value = {
            @ApiResponse ( code = 200, message = "Success|Ok" ) ,
            @ApiResponse ( code = 404, message = "not found!" )
    } )
    @DeleteMapping ( "/advisor/{id}" )

    public ResponseEntity<String> deleteAdvisorById(@PathVariable ( "id" ) Long advisorId) {
        Advisor advisor = advisorService.fetchAdvisorById(advisorId);
        if ( advisor != null ) {
            advisorService.deleteAdvisorById(advisorId);
            return ResponseEntity.ok("Advisor number " + advisorId + " Deleted Successfully");
        } else {
            return ResponseEntity.status(404).body("Advisor number " + advisorId + " Does not Exist");
        }
    }

}
