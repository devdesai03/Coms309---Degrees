package degreesapp.controllers;

import degreesapp.models.DegreeRequirement;
import degreesapp.models.RequirementFulfillment;
import degreesapp.models.RequirementGroup;
import degreesapp.services.RequirementService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "Requirements",
        description = "Use these APIs to manage degree requirements tracked by the application.\n" +
                "* The application uses three concepts to track and manage degree requirements:\n" +
                "* Requirement group: Represents a specific requirement, which can consist of one or more courses. For example, an \"arts and humanities\" requirement.\n" +
                "* Degree requirement: Links a degree to a requirement group. For example, if a Computer Science degree has an \"arts and humanities\" requirement, it is represented through a degree requirement object. Each degree can have many degree requirements; for example, a computer science major could require both arts and humanities and International Perspectives.\n" +
                "* Requirement fulfillment: Links a course to a requirement group. For example, if SOC 134 satisfies the \"arts and humanities\" requirement, it is represented through a requirement fulfillment object.")
@RestController
public class RequirementController {
    @Autowired
    RequirementService requirementService;

    // Requirement group
    @GetMapping("/requirementGroups/")
    @Operation(summary = "List requirement groups", description = "Get a list of all requirement groups in the system.")
    public List<RequirementGroup> getRequirementGroupList() {
        return requirementService.fetchRequirementGroupList();
    }

    @PostMapping("/requirementGroups/")
    @Operation(summary = "Create requirement group", description = "Create a new requirement group.")
    public RequirementGroup createRequirementGroup(@RequestBody RequirementGroup requirementGroup) {
        return requirementService.saveRequirementGroup(requirementGroup);
    }

    @GetMapping("/requirementGroups/{groupId}")
    @Operation(summary = "Get requirement group", description = "Get a requirement group by its ID.")
    public RequirementGroup getRequirementGroupById(@PathVariable String groupId) {
        return requirementService.fetchRequirementGroupById(groupId);
    }

    @PutMapping("/requirementGroups/{groupId}")
    @Operation(summary = "Update requirement group", description = "Update a requirement group. This means " +
            "replacing the existing requirement group data with the data specified in the request body.")
    public RequirementGroup updateRequirementGroup(@PathVariable String groupId,
                                                   @RequestBody RequirementGroup requirementGroup) {
        return requirementService.updateRequirementGroup(groupId, requirementGroup);
    }

    @DeleteMapping("/requirementGroups/{groupId}")
    @Operation(summary = "Delete requirement group", description = "Delete a requirement group.")
    public void deleteRequirementGroup(@PathVariable String groupId) {
        requirementService.deleteRequirementGroupById(groupId);
    }

    // Requirement fulfillment
    @GetMapping("/requirementGroups/{groupId}/fulfillments")
    @Operation(summary = "List requirement fulfillments for requirement group",
            description = "Get a list of requirement fulfillment objects for a specific requirement group.")
    public List<RequirementFulfillment> getRequirementFulfillmentsByGroupId(@PathVariable String groupId) {
        return requirementService.fetchRequirementFulfillmentByGroup(groupId);
    }

    @PostMapping("/requirementGroups/{groupId}/fulfillments")
    @Operation(summary = "Create requirement fulfillment for requirement group",
            description = "Create a new requirement fulfillment for a requirement group.")
    public RequirementFulfillment createRequirementFulfillmentInGroup(@PathVariable String groupId,
                                                                      @RequestBody RequirementFulfillment requirementFulfillment) {
        return requirementService.saveRequirementFulfillmentInGroup(groupId, requirementFulfillment);
    }

    @GetMapping("/requirementGroups/{groupId}/fulfillments/{courseId}")
    @Operation(summary = "Get requirement fulfillment",
            description = "Get a requirement fulfillment of a requirement group by the course ID.")
    public RequirementFulfillment getRequirementFulfillmentById(@PathVariable String groupId,
                                                                @PathVariable Long courseId) {
        return requirementService.fetchRequirementFulfillmentById(new RequirementFulfillment.Key(groupId, courseId));
    }

    @PutMapping("/requirementGroups/{groupId}/fulfillments/{courseId}")
    @Operation(summary = "Update requirement fulfillment",
            description = "Update a specific requirement fulfillment.")
    public RequirementFulfillment updateRequirementFulfillment(@PathVariable String groupId,
                                                               @PathVariable Long courseId,
                                                               @RequestBody RequirementFulfillment requirementFulfillment) {
        return requirementService.updateRequirementFulfillment(
                new RequirementFulfillment.Key(groupId, courseId),
                requirementFulfillment);
    }

    @DeleteMapping("/requirementGroups/{groupId}/fulfillments/{courseId}")
    @Operation(summary = "Delete requirement fulfillment",
            description = "Remove a specific requirement fulfillment for a requirement group.")
    public void deleteRequirementFulfillment(@PathVariable String groupId,
                                             @PathVariable Long courseId) {
        requirementService.deleteRequirementFulfillmentById(new RequirementFulfillment.Key(groupId, courseId));
    }

    // Requirement group degree
    @GetMapping("/degrees/{degreeId}/requirements")
    @Operation(summary = "List degree requirements",
            description = "Get a list of all required requirement groups for a specific " +
                    "degree.")
    public List<DegreeRequirement> getDegreeRequirementsList(@PathVariable Long degreeId) {
        return requirementService.fetchDegreeRequirementByDegree(degreeId);
    }

    @PostMapping("/degrees/{degreeId}/requirements")
    @Operation(summary = "Create degree requirement",
            description = "Add a new requirement group for a specific degree.")
    public DegreeRequirement createDegreeRequirementForDegree(@PathVariable Long degreeId,
                                                              @RequestBody DegreeRequirement degreeRequirement) {
        return requirementService.saveDegreeRequirementForDegree(degreeId, degreeRequirement);
    }

    @GetMapping("/degrees/{degreeId}/requirements/{groupId}")
    @Operation(summary = "Get degree requirement",
            description = "Get a specific degree requirement.")
    public DegreeRequirement getDegreeRequirementById(@PathVariable Long degreeId, @PathVariable String groupId) {
        return requirementService.fetchDegreeRequirementById(new DegreeRequirement.Key(groupId, degreeId));
    }

    @PutMapping("/degrees/{degreeId}/requirements/{groupId}")
    @Operation(summary = "Update degree requirement",
            description = "Update a specific degree requirement.")
    public DegreeRequirement updateDegreeRequirement(@PathVariable Long degreeId, @PathVariable String groupId,
                                                     @RequestBody DegreeRequirement degreeRequirement) {
        return requirementService.updateDegreeRequirement(
                new DegreeRequirement.Key(groupId, degreeId),
                degreeRequirement);
    }

    @DeleteMapping("/degrees/{degreeId}/requirements/{groupId}")
    @Operation(summary = "Delete degree requirement",
            description = "Delete a specific degree requirement.")
    public void deleteDegreeRequirement(@PathVariable Long degreeId, @PathVariable String groupId) {
        requirementService.deleteDegreeRequirementById(new DegreeRequirement.Key(groupId, degreeId));
    }
}
