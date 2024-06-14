package degreesapp.controllers;

import degreesapp.models.*;
import degreesapp.repositories.PrerequisiteRepository;
import degreesapp.services.*;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Api(tags = "Flowchart")
@RestController
public class FlowchartController {
    @Autowired
    DegreeService degreeService;

    @Autowired
    StudentService studentService;

    @Autowired
    HypotheticalCourseRegistrationService hypotheticalCourseRegistrationService;

    @Autowired
    PrerequisiteRepository prerequisiteRepository;

    @Operation(description = "With a specific degree and user, return the data of the flowchart associated " +
            "with the degree, along with user-specific information such as the prerequisite and registration " +
            "status for the courses in the flowchart.\n" +
            "The flowchart is organized into different \"semesters\" (rows), which contain \"requirement groups\" " +
            "(boxes), which contain \"requirement fulfillments\" (courses within a box). Refer to the screen sketches " +
            "document for more information about how a flowchart is organized.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "On success, returns object with the following fields:\n" +
                            "* `degree` (object): The Degree object representing the degree that this flowchart is for. Has the following subfields:\n" +
                            "  * `id` (long): The degree's unique ID.\n" +
                            "  * `name` (string): The degree's name, like \"Computer Science\".\n" +
                            "  * `suffix` (string): The degree's suffix, like \"BS\" or \"MA\".\n" +
                            "  * `department` (object): A Department object, with the following field:\n" +
                            "    * `departmentCode` (string): The department code, like \"COM S\".\n" +
                            "* `semesters` (array): A sorted array of semesters. Each semester corresponds to a \"row\" in the flowchart.\n" +
                            "  * `semesterNumber` (int): The semester number, like 1, 2, or 3.\n" +
                            "  * `requirements` (array): An array of requirement objects. A requirement object corresponds to one of the \"boxes\" in the flowchart. Each requirement object has the following fields:\n" +
                            "    * `name` (string): The big name that\n" +
                            "      appears at the top of the \"box\", such as\n" +
                            "      \"COM S 309\" or \"Arts & Humanities\".\n" +
                            "    * `subtitle` (string): The subtitle of the box, such as \"Object-oriented programming - Java\" or \"(tap to view options)\".\n" +
                            "    * `totalCreditHours`: (string or null): A string representing the credit hours or range of credit hours. If `null`, then the number of credit hours is not listed and \"(required)\" should be displayed instead.\n" +
                            "    * `state` (string): The box's \"state\" as described in the screen sketches document. This can be one of the following values: `available`, `selected`, `unavailable`, `unavailableSelected`.\n" +
                            "    * `fulfillments` (array): An array representing the set of courses that the user can take to fulfill this requirement. The user only has to take one of them. It consists of one or more objects with the following fields:\n" +
                            "      * `minimumGrade` (BigDecimal): The minimum grade in the course required to meet the requirement.\n" +
                            "      * `semesterTaking` (int or null): The semester that the user is planning to take this course. If `null`, then the course is not in the user's academic plan. At least one course must be in the academic plan for the requirement to be fulfilled.\n" +
                            "      * `minimumSemester` (int or null): The first semester that the user has all the prerequisites for this course fulfilled. If `null`, then the user can't take this course at all without registering for a prerequisite course.\n" +
                            "      * `course`: Information about the course itself. This is a Course object, with the following fields:\n" +
                            "        * `courseId` (long): A unique identifier for a course.\n" +
                            "        * `courseName` (string): The full name of the course, such as \"Software Development Practices\".\n" +
                            "        * `courseDepartment` (object): A department object.\n" +
                            "          * `departmentId` (long): A number uniquely identifying the course's department, such as \"12\".\n" +
                            "          * `departmentCode` (string): The course's department's abbreviation, such as \"COM S\".\n" +
                            "        * `courseNumber` (string): The course's number, such as \"309\". This is *not* necessarily a valid integer. For example, WISE 201x has the course number \"201x\".\n" +
                            "        * `courseCreditHours` (string): The course's credit hours, such as 3.0. It is guaranteed to be a valid decimal number.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class),
                            examples = { @ExampleObject(
                                    value = "{\n" +
                                            "  \"degree\": {\n" +
                                            "    \"id\": id,\n" +
                                            "    \"name\": name,\n" +
                                            "    \"suffix\": suffix,\n" +
                                            "    \"department\": {\n" +
                                            "      \"departmentCode\": departmentCode\n" +
                                            "    }\n" +
                                            "  },\n" +
                                            "  \"semesters\": [\n" +
                                            "    {\n" +
                                            "      \"semesterNumber\": semesterNumber,\n" +
                                            "      \"requirementGroups\": [\n" +
                                            "        {\n" +
                                            "          \"name\": name,\n" +
                                            "          \"subtitle\": subtitle,\n" +
                                            "          \"totalCreditHours\": totalCreditHours,\n" +
                                            "          \"state\": state,\n" +
                                            "          \"fulfillments\": [\n" +
                                            "            {\n" +
                                            "              \"minimumGrade\": minimumGrade,\n" +
                                            "              \"semesterTaking\": semesterTaking,\n" +
                                            "              \"minimumSemester\": minimumSemester,\n" +
                                            "              \"course\": {\n" +
                                            "                \"courseId\": courseId,\n" +
                                            "                \"courseName\": courseName,\n" +
                                            "                \"courseDepartment\": {\n" +
                                            "                  \"departmentId\": departmentId,\n" +
                                            "                  \"departmentCode\": departmentCode\n" +
                                            "                },\n" +
                                            "                \"courseNumber\": courseNumber,\n" +
                                            "                \"courseDescription\": courseDescription,\n" +
                                            "                \"courseCreditHours\": courseCreditHours\n" +
                                            "              }\n" +
                                            "            }\n" +
                                            "          ]\n" +
                                            "        }\n" +
                                            "      ]\n" +
                                            "    }\n" +
                                            "  ]\n" +
                                            "}"
                            ) }) })})
    @GetMapping(value = "/degrees/{degreeId}/flowchart", produces = "application/json")
    public HashMap<String, Object> getFlowchart(
            @Parameter(description = "The ID of the degree to get the flowchart of.")
                @PathVariable Long degreeId,
            @Parameter(description = "The student's university ID to include data about.")
                @RequestParam Long studentId) {
        try {
            Degree degree = degreeService.fetchDegreeById(degreeId);
            Student student = studentService.fetchStudentById(studentId);

            Map<Integer, ArrayList<Map<String, Object>>> semesters = new HashMap<>();

            List<DegreeRequirement> degreeRequirements = degree.getRequirements();
            for (DegreeRequirement degreeRequirement : degreeRequirements) {
                Integer recommendedSemester = degreeRequirement.getRecommendedSemester();
                RequirementGroup requirementGroup = degreeRequirement.getGroup();
                if (recommendedSemester == null)
                    continue;
                ArrayList<Map<String, Object>> requirementGroupObjects =
                        semesters.computeIfAbsent(recommendedSemester, (k) -> new ArrayList<>());
                List<RequirementFulfillment> requirementFulfillments = requirementGroup.getFulfillments();

                Map<String, Object> requirementGroupObject = new HashMap<>();
                requirementGroupObject.put("name", requirementGroup.getName());

                BigDecimal totalCreditHours = requirementFulfillments.stream()
                        .map(f -> f.getCourse().getCourseCreditHours())
                        .reduce(BigDecimal.valueOf(0), BigDecimal::add);
                if (!totalCreditHours.equals(BigDecimal.valueOf(0))) {
                    requirementGroupObject.put("totalCreditHours", totalCreditHours);
                } else {
                    requirementGroupObject.put("totalCreditHours", null);
                }

                if (requirementFulfillments.size() == 1) {
                    requirementGroupObject.put("subtitle",
                            requirementFulfillments.get(0).getCourse().getCourseDepartment().getDepartmentCode() + " "
                                + requirementFulfillments.get(0).getCourse().getCourseNumber());
                } else {
                    requirementGroupObject.put("subtitle",
                            "(tap to view courses)"
                            ); // TODO: finalize
                }

                ArrayList<Map<String, Object>> fulfillmentObjects = new ArrayList<>();
                for (RequirementFulfillment fulfillment : requirementFulfillments) {
                    Map<String, Object> fulfillmentObject = new HashMap<>();

                    fulfillmentObject.put("minimumGrade", fulfillment.getMinimumGrade());

                    HypotheticalCourseRegistration hcr = hypotheticalCourseRegistrationService
                            .findHypotheticalCourseRegistration(
                                    fulfillment.getCourse(), student);
                    if (hcr != null) {
                        fulfillmentObject.put("semesterTaking", Integer.valueOf(hcr.getSemesterNumber()));
                    } else {
                        fulfillmentObject.put("semesterTaking", null);
                    }

                    fulfillmentObject.put("state", hcr != null ? "selected" : "available"); // TODO: complete

                    Integer minimumSemester = null;
                    for (Prerequisites prerequisite :
                            prerequisiteRepository.findByPostCourse(fulfillment.getCourse().getCourseId())) {
                        HypotheticalCourseRegistration cr = hypotheticalCourseRegistrationService
                                .findHypotheticalCourseRegistration(
                                        fulfillment.getCourse(), student);
                        if (cr != null) {
                            int maybeMinSemester = cr.getSemesterNumber();
                            if (minimumSemester == null || minimumSemester < maybeMinSemester)
                                minimumSemester = maybeMinSemester;
                        }
                    }
                    if (minimumSemester != null)
                        fulfillmentObject.put("minimumSemester", minimumSemester);
                    else {
                        fulfillmentObject.put("minimumSemester", null);
                    }

                    fulfillmentObject.put("course", fulfillment.getCourse());

                    fulfillmentObjects.add(fulfillmentObject);
                }

                requirementGroupObject.put("fulfillments", fulfillmentObjects);

                requirementGroupObjects.add(requirementGroupObject);
            }


            HashMap<String, Object> response = new HashMap<>();
            response.put("degree", degree);

            ArrayList<Map<String, Object>> semesterObjects = new ArrayList<>();
            for (Integer semesterNumber : semesters.keySet().stream().sorted().collect(Collectors.toList())) {
                Map<String, Object> semesterObject = new HashMap<>();
                semesterObject.put("semesterNumber", semesterNumber);
                semesterObject.put("requirementGroups", semesters.get(semesterNumber));
                semesterObjects.add(semesterObject);
            }
            response.put("semesters", semesterObjects);
            return response;
        } catch (Exception e) {
            e.printStackTrace(System.out);
            throw e;
        }
    }
}
