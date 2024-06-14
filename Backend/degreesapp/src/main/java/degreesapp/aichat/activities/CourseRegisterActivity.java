package degreesapp.aichat.activities;

import degreesapp.aichat.base.AIChatActivity;
import degreesapp.models.*;
import degreesapp.repositories.CourseSectionRepository;
import degreesapp.services.CourseRegistrationService;
import degreesapp.services.CourseService;
import degreesapp.services.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;

public class CourseRegisterActivity extends AIChatActivity {
    @Override
    public void onStartup() {
        if (getUser().getIsuRegistration() == null) {
            sendResponse("Sorry, looks like you're logged in as a guest!");
            removeSelf();
            return;
        } else if (getUser().getIsuRegistration().getStudent() == null) {
            sendResponse("Sorry, looks like you're not a student!");
            removeSelf();
            return;
        }
        Response response = new Response();
        response.setText("Okay. First things first. What kind of course do you want?");
        for (Department department : getDepartmentService().fetchDepartmentList()) {
            response.addRecommendedPrompt(department.getDepartmentName());
        }
        sendResponse(response);
    }

    @Override
    public void onPrompt(Prompt prompt) {
        String deptString = prompt.getText().toLowerCase();
        Department chosenDepartment = null;

        for (Department department : getDepartmentService().fetchDepartmentList()) {
            if (department.getDepartmentName().toLowerCase().equals(deptString)) {
                chosenDepartment = department;
                break;
            } else if (department.getDepartmentCode().toLowerCase().equals(deptString)) {
                chosenDepartment = department;
                break;
            }
        }

        if (chosenDepartment == null) {
            getParentActivity().onPrompt(prompt);
            return;
        }

        final Department department = chosenDepartment;

        this.replaceWithNewActivity(new AIChatActivity() {
            @Override
            public void onStartup() {
                Response response = new Response();
                response.setText("Okay. Now, which course do you want to register for?");
                for (Course course : getCourseService().fetchCourseListFiltered(department, null)) {
                    response.addRecommendedPrompt(course.getCourseName());
                }
                sendResponse(response);
            }

            @Override
            public void onPrompt(Prompt prompt) {
                for (Course course : getCourseService().fetchCourseListFiltered(department, null)) {
                    if (course.getCourseName().equalsIgnoreCase(prompt.getText())) {
                        this.replaceWithNewActivity(new CourseSectionChoiceActivity(getCourseRegistrationService(), course));
                        return;
                    } else if (course.getCourseNumber().equalsIgnoreCase(prompt.getText())) {
                        this.replaceWithNewActivity(new CourseSectionChoiceActivity(getCourseRegistrationService(), course));
                        return;
                    }
                }
                getParentActivity().onPrompt(prompt);
            }
        });
    }
}

class CourseSectionChoiceActivity extends AIChatActivity {

    CourseRegistrationService courseRegistrationService;

    private Course course;

    public CourseSectionChoiceActivity(CourseRegistrationService courseRegistrationService, Course course) {
        this.course = course;
        this.courseRegistrationService = courseRegistrationService;
    }

    @Override
    public void onStartup() {
        Response response = new Response("Okay. Finally, what course section do you want?");
        getCourseSectionRepository().findAll().stream()
                .filter(courseSection -> courseSection.getCourse().getCourseId().equals(this.course.getCourseId()))
                .forEach(courseSection -> {
                    response.addRecommendedPrompt("Section " + courseSection.getSectionIdentifier());
                });
        sendResponse(response);
    }

    @Override
    public void onPrompt(Prompt prompt) {
        String promptText = prompt.getText();
        if (promptText.toLowerCase().startsWith("section ")) {
            promptText = promptText.substring("section ".length());
        }

        CourseSection chosenSection = getCourseSectionRepository().findAll().stream()
                .filter(courseSection -> courseSection.getCourse().getCourseId().equals(this.course.getCourseId()))
                .findFirst()
                .orElse(null);

        if (chosenSection == null) {
            getParentActivity().onPrompt(prompt);
            return;
        }
        CourseRegistration courseRegistration = new CourseRegistration();
        courseRegistration.setId(new RegistrationId(getUser().getIsuRegistration().getUniversityId(), chosenSection.getId()));
        Student student = new Student();
        student.setUniversityId(getUser().getIsuRegistration().getUniversityId());
        courseRegistration.setStudent(student);
        courseRegistration.setSection(chosenSection);
        courseRegistrationService.saveCourseSection(courseRegistration);
        sendResponse("Alright. Added you to the course section!");

        removeSelf();
    }
}