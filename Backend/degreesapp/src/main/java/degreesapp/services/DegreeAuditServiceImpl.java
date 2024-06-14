package degreesapp.services;

import degreesapp.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DegreeAuditServiceImpl implements DegreeAuditService {
    static private final BigDecimal DEFAULT_MIN_GRADE = BigDecimal.valueOf(10, 1);

    @Autowired
    CourseRegistrationService courseRegistrationService;

    @Autowired
    RequirementService requirementService;

    @Override
    public void writeDegreeAudit(PrintWriter out, DegreeAuditOptions options) {
        Student student = options.student();
        IsuRegistration isuRegistration = student.getIsuRegistration();
        User user = isuRegistration.getUser();
        Semester semester;
        if (options.includeInProgressCourses()) {
            semester = Semester.during(LocalDate.now()).orElseGet(() -> Semester.before(LocalDate.now()));
        } else {
            semester = Semester.before(LocalDate.now());
        }
        boolean isSemesterCurrent = semester.contains(LocalDate.now());

        if (isuRegistration.getFullName() == null) {
            out.printf("Degree Audit (Net-ID: %s)\n", isuRegistration.getNetId());
        } else {
            out.printf("Degree Audit for %s (Net-ID: %s)\n", isuRegistration.getFullName(), isuRegistration.getNetId());
        }
        if (student.getStudentAdvisor() == null) {
            out.printf("Advisor: None\n");
        } else if (student.getStudentAdvisor().getIsuRegistration().getFullName() == null) {
            out.printf("Advisor: %s@iastate.edu\n", student.getStudentAdvisor().getIsuRegistration().getNetId());
        } else {
            out.printf("Advisor: %s\n", student.getStudentAdvisor().getIsuRegistration().getFullName());
            out.printf("         %s@iastate.edu\n", student.getStudentAdvisor().getIsuRegistration().getNetId());
        }

        Map<Long, CourseRegistration> takenCoursesMap = new HashMap<>();
        long passedCoursesCount = 0;
        BigDecimal totalCreditHours = BigDecimal.ZERO;
        BigDecimal totalGradePoints = BigDecimal.ZERO;
        for (CourseRegistration registration :
                courseRegistrationService.getRegistrationsForStudent(student.getUniversityId())) {
            if (registration.getSection().getSemester().compareTo(semester) >= 0 && !options.includeInProgressCourses())
                continue;
            // TODO: only show latest course registration
            takenCoursesMap.put(registration.getSection().getCourse().getCourseId(), registration);
            if (didPassCourse(registration) == CourseStatus.PASSED) {
                passedCoursesCount += 1;
                totalCreditHours = totalCreditHours.add(registration.getSection().getCourse().getCourseCreditHours());
                totalGradePoints = totalGradePoints.add(registration.getGrade());
            }
        }

        out.println("Semester: " + semester + (isSemesterCurrent ? " (ongoing)" : ""));
        out.println("Total credits: " + totalCreditHours);
        if (passedCoursesCount != 0) {
            out.println("Cumulative GPA: " + totalGradePoints.divide(BigDecimal.valueOf(passedCoursesCount), RoundingMode.HALF_DOWN));
        }
        out.println();

        Map<Long, CourseRegistration> unaccountedCoursesMap = new HashMap<>(takenCoursesMap);
        for (Degree major : student.getMajors()) {
            out.println("=======================================================");
            out.printf("Major: %s %s\n", major.getName(), major.getSuffix());
            out.printf("Department: %s (%s)\n", major.getDepartment().getDepartmentName(), major.getDepartment().getDepartmentCode());
            if (major.getDepartment().getDepartmentAddress() != null)
                out.printf("            %s\n", major.getDepartment().getDepartmentAddress());
            out.println();

            int numFulfilled = 0;
            int numTotal = 0;
            for (DegreeRequirement degreeRequirement : requirementService.fetchDegreeRequirementByDegree(major.getId())) {
                RequirementGroup requirementGroup = degreeRequirement.getGroup();
                out.printf("----- Requirement: %s -----\n", requirementGroup.getName());
                List<RequirementFulfillment> requirementFulfillments = requirementService.fetchRequirementFulfillmentByGroup(requirementGroup.getId());
                if (requirementFulfillments.isEmpty()) {
                    out.printf("(empty)\n");
                } else {
                    out.printf("(at least one required to graduate)\n");
                }
                boolean fulfilled = false;
                for (RequirementFulfillment fulfillment : requirementFulfillments) {
                    CourseRegistration registration = takenCoursesMap.get(fulfillment.getCourse().getCourseId());
                    unaccountedCoursesMap.remove(fulfillment.getCourse().getCourseId());
                    Course course = fulfillment.getCourse();
                    BigDecimal minimumGrade = fulfillment.getMinimumGrade();
                    if (minimumGrade == null) {
                        minimumGrade = DEFAULT_MIN_GRADE;
                    }
                    if (registration == null) {
                        out.printf("- %s %s (%s crs) (min grade %s)\n",
                                course.getCourseDepartment().getDepartmentCode(),
                                course.getCourseNumber(),
                                course.getCourseCreditHours(),
                                gradeNumberToLetter(minimumGrade));
                    } else {
                        if (didPassCourse(registration, minimumGrade) == CourseStatus.PASSED) {
                            fulfilled = true;
                        }
                        out.printf("%s %s %s (%s crs) (min grade %s) - %s\n",
                                switch (didPassCourse(registration, minimumGrade)) {
                                    case ONGOING -> "?";
                                    case PASSED -> "+";
                                    case FAILED -> "-";
                                },
                                course.getCourseDepartment().getDepartmentCode(),
                                course.getCourseNumber(),
                                course.getCourseCreditHours(),
                                gradeNumberToLetter(minimumGrade),
                                registration.getGrade() == null ? "(ongoing)" : gradeNumberToLetter(registration.getGrade()));
                    }
                }
                if (fulfilled)
                    numFulfilled++;
                numTotal++;
                out.println();
            }

            out.println("----- Summary -----");
            out.printf("Requirements fulfilled: %s / %s\n", numFulfilled, numTotal);
            out.println();
        }

        if (!unaccountedCoursesMap.isEmpty()) {
            out.println("=======================================================");
            out.println("(courses not required for graduation)");
            for (CourseRegistration registration : unaccountedCoursesMap.values()) {
                Course course = registration.getSection().getCourse();
                out.printf("%s %s %s (%s crs) - %s\n",
                        switch (didPassCourse(registration)) {
                            case ONGOING -> "?";
                            case PASSED -> "+";
                            case FAILED -> "-";
                        },
                        course.getCourseDepartment().getDepartmentCode(),
                        course.getCourseNumber(),
                        course.getCourseCreditHours(),
                        registration.getGrade() == null ? "(ongoing)" : gradeNumberToLetter(registration.getGrade()));
            }
            out.println();
        }

        out.println("***** End of Degree Audit *****");
    }

    private enum CourseStatus {
        PASSED, FAILED, ONGOING
    }
    private CourseStatus didPassCourse(CourseRegistration registration) {
        if (registration.getGrade() == null)
            return CourseStatus.ONGOING;
        if (registration.getGrade().compareTo(DEFAULT_MIN_GRADE) >= 0) {
            return CourseStatus.PASSED;
        } else {
            return CourseStatus.FAILED;
        }
    }

    private CourseStatus didPassCourse(CourseRegistration registration, BigDecimal minimumGrade) {
        if (registration.getGrade() == null)
            return CourseStatus.ONGOING;
        if (registration.getGrade().compareTo(minimumGrade) >= 0) {
            return CourseStatus.PASSED;
        } else {
            return CourseStatus.FAILED;
        }
    }

    private static String gradeNumberToLetter(BigDecimal grade) {
        if (grade.compareTo(BigDecimal.valueOf(40, 1)) >= 0)
            return "A";
        if (grade.compareTo(BigDecimal.valueOf(37, 1)) >= 0)
            return "A-";
        if (grade.compareTo(BigDecimal.valueOf(33, 1)) >= 0)
            return "B+";
        if (grade.compareTo(BigDecimal.valueOf(30, 1)) >= 0)
            return "B";
        if (grade.compareTo(BigDecimal.valueOf(27, 1)) >= 0)
            return "B-";
        if (grade.compareTo(BigDecimal.valueOf(23, 1)) >= 0)
            return "C+";
        if (grade.compareTo(BigDecimal.valueOf(20, 1)) >= 0)
            return "C";
        if (grade.compareTo(BigDecimal.valueOf(17, 1)) >= 0)
            return "C-";
        if (grade.compareTo(BigDecimal.valueOf(13, 1)) >= 0)
            return "D+";
        if (grade.compareTo(BigDecimal.valueOf(10, 1)) >= 0)
            return "D";
        return "F";
    }
}
