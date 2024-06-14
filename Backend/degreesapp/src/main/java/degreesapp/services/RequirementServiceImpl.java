package degreesapp.services;

import degreesapp.models.DegreeRequirement;
import degreesapp.models.RequirementFulfillment;
import degreesapp.models.RequirementGroup;
import degreesapp.repositories.DegreeRequirementRepository;
import degreesapp.repositories.RequirementFulfillmentRepository;
import degreesapp.repositories.RequirementGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequirementServiceImpl implements RequirementService {
    // Requirement group
    @Autowired
    RequirementGroupRepository requirementGroupRepository;

    @Override
    public List<RequirementGroup> fetchRequirementGroupList() {
        return requirementGroupRepository.findAll();
    }

    @Override
    public RequirementGroup fetchRequirementGroupById(String id) {
        return requirementGroupRepository.findById(id).orElse(null);
    }

    @Override
    public RequirementGroup saveRequirementGroup(RequirementGroup requirementGroup) {
        return requirementGroupRepository.save(requirementGroup);
    }

    @Override
    public RequirementGroup updateRequirementGroup(String id, RequirementGroup requirementGroup) {
        requirementGroup.setId(id);
        return requirementGroupRepository.save(requirementGroup);
    }

    @Override
    public void deleteRequirementGroupById(String id) {
        requirementGroupRepository.deleteById(id);
    }

    // Requirement group fulfillment
    @Autowired
    RequirementFulfillmentRepository requirementFulfillmentRepository;

    @Override
    public List<RequirementFulfillment> fetchRequirementFulfillmentList() {
        return requirementFulfillmentRepository.findAll();
    }

    @Override
    public List<RequirementFulfillment> fetchRequirementFulfillmentByGroup(String groupId) {
        return requirementFulfillmentRepository.findByGroupId(groupId);
    }

    @Override
    public RequirementFulfillment fetchRequirementFulfillmentById(RequirementFulfillment.Key id) {
        return requirementFulfillmentRepository.findById(id).orElse(null);
    }

    @Override
    public RequirementFulfillment saveRequirementFulfillment(RequirementFulfillment requirementFulfillment) {
        return requirementFulfillmentRepository.save(requirementFulfillment);
    }

    @Override
    public RequirementFulfillment saveRequirementFulfillmentInGroup(String group, RequirementFulfillment requirementFulfillment) {
        requirementFulfillment.setId(new RequirementFulfillment.Key(group, requirementFulfillment.getId().getCourseId()));
        System.out.println(requirementFulfillment);
        return requirementFulfillmentRepository.save(requirementFulfillment);
    }

    @Override
    public RequirementFulfillment updateRequirementFulfillment(RequirementFulfillment.Key id, RequirementFulfillment requirementFulfillment) {
        requirementFulfillment.setId(id);
        return requirementFulfillmentRepository.save(requirementFulfillment);
    }

    @Override
    public void deleteRequirementFulfillmentById(RequirementFulfillment.Key id) {
        requirementFulfillmentRepository.deleteById(id);
    }

    // Requirement group degree
    @Autowired
    DegreeRequirementRepository degreeRequirementRepository;

    @Override
    public List<DegreeRequirement> fetchDegreeRequirementList() {
        return degreeRequirementRepository.findAll();
    }

    @Override
    public List<DegreeRequirement> fetchDegreeRequirementByDegree(Long degreeId) {
        return degreeRequirementRepository.findByDegreeId(degreeId);
    }

    @Override
    public DegreeRequirement fetchDegreeRequirementById(DegreeRequirement.Key id) {
        return degreeRequirementRepository.findById(id).orElse(null);
    }

    @Override
    public DegreeRequirement saveDegreeRequirement(DegreeRequirement degreeRequirement) {
        return degreeRequirementRepository.save(degreeRequirement);
    }

    @Override
    public DegreeRequirement saveDegreeRequirementForDegree(Long degreeId, DegreeRequirement degreeRequirement) {
        degreeRequirement.setId(new DegreeRequirement.Key(degreeRequirement.getId().getGroupId(), degreeId));
        return degreeRequirementRepository.save(degreeRequirement);
    }

    @Override
    public DegreeRequirement updateDegreeRequirement(DegreeRequirement.Key id, DegreeRequirement degreeRequirement) {
        degreeRequirement.setId(id);
        return degreeRequirementRepository.save(degreeRequirement);
    }

    @Override
    public void deleteDegreeRequirementById(DegreeRequirement.Key id) {
        degreeRequirementRepository.deleteById(id);
    }
}
