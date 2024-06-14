package degreesapp.services;

import degreesapp.models.RequirementFulfillment;
import degreesapp.models.RequirementGroup;
import degreesapp.models.DegreeRequirement;

import java.util.List;

public interface RequirementService {
    // Requirement group
    List<RequirementGroup> fetchRequirementGroupList();

    RequirementGroup fetchRequirementGroupById(String id);

    RequirementGroup saveRequirementGroup(RequirementGroup requirementGroup);

    RequirementGroup updateRequirementGroup(String id, RequirementGroup requirementGroup);

    void deleteRequirementGroupById(String id);

    // Requirement fulfillment
    List<RequirementFulfillment> fetchRequirementFulfillmentList();

    List<RequirementFulfillment> fetchRequirementFulfillmentByGroup(String groupId);

    RequirementFulfillment fetchRequirementFulfillmentById(RequirementFulfillment.Key id);

    RequirementFulfillment saveRequirementFulfillment(RequirementFulfillment requirementFulfillment);

    RequirementFulfillment saveRequirementFulfillmentInGroup(String group, RequirementFulfillment requirementFulfillment);

    RequirementFulfillment updateRequirementFulfillment(RequirementFulfillment.Key id, RequirementFulfillment requirementFulfillment);

    void deleteRequirementFulfillmentById(RequirementFulfillment.Key id);

    // Requirement group degree
    List<DegreeRequirement> fetchDegreeRequirementList();

    List<DegreeRequirement> fetchDegreeRequirementByDegree(Long degreeId);

    DegreeRequirement fetchDegreeRequirementById(DegreeRequirement.Key id);

    DegreeRequirement saveDegreeRequirement(DegreeRequirement degreeRequirement);

    DegreeRequirement saveDegreeRequirementForDegree(Long degreeId, DegreeRequirement degreeRequirement);

    DegreeRequirement updateDegreeRequirement(DegreeRequirement.Key id, DegreeRequirement degreeRequirement);

    void deleteDegreeRequirementById(DegreeRequirement.Key id);
}
