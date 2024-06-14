package degreesapp.controllers;


import degreesapp.models.User;
import degreesapp.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * @author Mohammed Abdalgader
 */

@Api ( value = "REST APIs related to User Entity", tags = "user" )
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender javaMailSender;

    @ApiOperation ( value = "create the user in the database", tags = "user" )
    @ApiResponses ( value = {
            @ApiResponse ( code = 200, message = "Success|Ok" ) ,
            @ApiResponse ( code = 404, message = "not found!" )
    } )
    @PostMapping ( "/users" )
    public ResponseEntity<User> saveUser(@Valid @RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.OK);
    }

    @ApiOperation ( value = "get a list of Users from the database", tags = "user" )
    @ApiResponses ( value = {
            @ApiResponse ( code = 200, message = "Success|Ok" ) ,
            @ApiResponse ( code = 404, message = "not found!" )
    } )
    @GetMapping ( "/users" )
    public List<User> fetchUserList() {
        return userService.fetchUserList();
    }

    @ApiOperation ( value = "get specific user from the database by Id", tags = "user" )
    @ApiResponses ( value = {
            @ApiResponse ( code = 200, message = "Success|Ok" ) ,
            @ApiResponse ( code = 404, message = "not found!" )
    } )
    @GetMapping ( "/users/{id}" )
    public User fetchUserById(@PathVariable ( "id" ) Long userId) {
        return userService.fetchUserById(userId);
    }


    @ApiOperation ( value = "delete user from the database", tags = "user" )
    @ApiResponses ( value = {
            @ApiResponse ( code = 200, message = "Success|Ok" ) ,
            @ApiResponse ( code = 404, message = "not found!" )
    } )
    @DeleteMapping ( "/users/{id}" )

    public String deleteUserById(@PathVariable ( "id" ) Long userId) {

        userService.deleteUserById(userId);

        return "User number " + userId + " Deleted Successfully";
    }

    @ApiOperation ( value = "update user in the database", tags = "user" )
    @ApiResponses ( value = {
            @ApiResponse ( code = 200, message = "Success|Ok" ) ,
            @ApiResponse ( code = 404, message = "not found!" )
    } )
    @PutMapping ( "/users/{id}" )
    public User updateUser(@PathVariable ( "id" ) Long userId , @RequestBody User user) {
        return userService.updateUser(userId , user);
    }


    @ApiOperation ( value = "Login EndPoint ", tags = "user" )
    @ApiResponses ( value = {
            @ApiResponse ( code = 200, message = "Success|Ok" ) ,
            @ApiResponse ( code = 403, message = "Forbidden!" )
    } )
    @PostMapping ( "/login" )
    @Transactional
    public HashMap<String, Boolean> response(@RequestBody User user) {
        boolean didSucceed;

        User loginUser = userService.login(user.getUserEmail() , user.getUserPassword());

        didSucceed = loginUser != null;
        HashMap<String, Boolean> response = new HashMap<>();
        response.put("didSucceed" , didSucceed);
        if ( didSucceed ) {
            loginUser.sendVerificationCodeAndSet(loginUser.getUserEmail() , javaMailSender);
//            userService.saveUser(user); // Save the updated user
        }
        return response;
    }


    @ApiOperation ( value = "create verifyCode in the database", tags = "user" )
    @ApiResponses ( value = {
            @ApiResponse ( code = 200, message = "Success|Ok" ) ,
            @ApiResponse ( code = 404, message = "not found!" )
    } )
    @PostMapping ( "/login/verify" )
    public HashMap<String, Object> verifyCode(@RequestBody User user) {
        // Get the stored verification code for the user from your database
        String storedCode = userService.getStoredVerificationCode(user.getUserEmail());

        String token;
        System.out.println(user.getUserEmail());
        System.out.println(storedCode + " " + user.getVerificationCode());
        if ( storedCode != null && storedCode.equals(user.getVerificationCode()) ) {
            // Verification successful
            token = "someToken";
        } else {
            // Verification failed
            token = null;
        }
        HashMap<String, Object> response = new HashMap<>();
        response.put("userId" , userService.fetchUserByEmail(user.getUserEmail()).getUserId());
        response.put("accessToken" , token);
        return response;
    }

    @ApiOperation ( value = "get the verification code with your email", tags = "getVerifyCodeHack" )
    @ApiResponses ( value = {
            @ApiResponse ( code = 200, message = "Success|Ok" ) ,
            @ApiResponse ( code = 404, message = "not found!" )
    } )
    @GetMapping ( "/verificationCode/{userEmail}" )
    public String getVerifyCodeHack(@PathVariable String userEmail) {
        return userService.getStoredVerificationCode(userEmail);
    }
}
