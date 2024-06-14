package degreesapp.aichat.base;

import degreesapp.models.User;
import degreesapp.repositories.CourseSectionRepository;
import degreesapp.repositories.DepartmentRepository;
import degreesapp.services.CourseRegistrationService;
import degreesapp.services.CourseService;
import degreesapp.services.DepartmentService;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AIChatActivity {
    public static final class Prompt {
        @Getter
        private String text;

        public Prompt(String text) {
            this.text = text;
        }

        Prompt() {
        }

        public static Prompt fromMessageString(String message) {
            Prompt inst = new Prompt();
            inst.text = message;
            return inst;
        }
    }

    public static final class Response {
        @Setter
        private String text = null;

        private List<String> clientAction = new ArrayList<>();

        public Response(String text) {
            this.text = text;
        }

        public Response() {
        }

        private List<String> recommendedPrompts = new ArrayList<>();

        public void addRecommendedPrompt(Prompt recommendedPrompt) {
            recommendedPrompts.add(recommendedPrompt.getText());
        }

        ;

        public void addRecommendedPrompt(String recommendedPrompt) {
            addRecommendedPrompt(new Prompt(recommendedPrompt));
        }

        public void setClientAction(String... args) {
            this.clientAction.addAll(Arrays.asList(args));
        }

        public String intoMessageString() throws JSONException {
            JSONObject object = new JSONObject();
            object.put("text" , text);
            object.put("recommendedPrompts" , recommendedPrompts);
            if ( !clientAction.isEmpty() ) {
                object.put("clientAction" , clientAction);
            }
            return object.toString();
        }
    }

    @Getter
    AIChatActivity parentActivity = null;
    @Getter
    AIChatActivity childActivity = null;
    AIChatSocket aiChatSocket = null;

    public final AIChatActivity getCurrentActivity() {
        return this.aiChatSocket.getCurrentActivity();
    }

    public final void addChildActivity(AIChatActivity childActivity) {
        this.aiChatSocket.addChildActivity(childActivity);
    }

    public final void removeSelf() {
        this.aiChatSocket.removeActivity(this);
    }

    public final void replaceWithNewActivity(AIChatActivity newActivity) {
        this.aiChatSocket.replace(this , newActivity);
    }

    public void sendResponse(Response response) {
        aiChatSocket.sendResponse(response);
    }

    public void sendResponse(String response) {
        sendResponse(new Response(response));
    }

    private User user = null;

    public final User getUser() {
        if ( user == null ) {
            user = this.aiChatSocket.fetchUser();
        }
        return user;
    }

    public final void resetCache() {
        user = null;
    }

    public final DepartmentService getDepartmentService() {
        return this.aiChatSocket.departmentService;
    }

    public final CourseService getCourseService() {
        return this.aiChatSocket.courseService;
    }

    public final CourseRegistrationService getCourseRegistrationService() {
        return this.aiChatSocket.courseRegistrationService;
    }

    public final CourseSectionRepository getCourseSectionRepository() {
        return this.aiChatSocket.courseSectionRepository;
    }

    public abstract void onStartup();

    public void onCancel() {
        sendResponse(new Response("Alright. Sorry about that."));
    }

    ;

    public abstract void onPrompt(Prompt prompt);

    public void onResume() {
        this.onStartup();
    }

    public void onSuspend() {
    }
}