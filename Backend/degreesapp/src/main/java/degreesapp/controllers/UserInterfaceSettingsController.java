package degreesapp.controllers;

import degreesapp.models.UserInterfaceSettings;
import degreesapp.services.UserInterfaceSettingsService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@Api(tags = "Settings", description = "Get the settings of a user. " +
        "The settings are divided based on their general type.")
public class UserInterfaceSettingsController {
    @Autowired
    private UserInterfaceSettingsService userInterfaceSettingsService;

    @GetMapping("/settings/{userId}/userInterface")
    @Operation(summary = "Get user interface settings",
            description = "Get the user's user interface personalization settings. " +
                    "This will only return a 404 if the user ID does not exist. A user " +
                    "who hasn't customized their user interface settings will receive the " +
                    "default user interface settings as the response.")
    public UserInterfaceSettings getUserInterfaceSettings(@Parameter(description = "The user ID of the user.")
                                                              @PathVariable(required = true) Long userId) {
        return userInterfaceSettingsService.fetchUserInterfaceSettingsById(userId);
    }

    @PutMapping("/settings/{userId}/userInterface")
    @Operation(summary = "Update user interface settings",
            description = "Update the user's user interface personalization settings. This means " +
                    "replacing the current user interface settings with the ones specified in the request body.")
    public UserInterfaceSettings updateUserInterfaceSettings(
            @Parameter(description = "The user ID of the user.")
            @PathVariable(required = true) Long userId,
            @RequestBody UserInterfaceSettings userInterfaceSettings
    ) {
        return userInterfaceSettingsService.updateUserInterfaceSettings(userId, userInterfaceSettings);
    }
}
