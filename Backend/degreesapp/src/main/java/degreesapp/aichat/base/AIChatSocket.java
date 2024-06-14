package degreesapp.aichat.base;

import degreesapp.aichat.activities.MainChatActivity;
import degreesapp.aichat.base.AIChatActivity;
import degreesapp.models.User;
import degreesapp.repositories.CourseSectionRepository;
import degreesapp.services.CourseRegistrationService;
import degreesapp.services.CourseService;
import degreesapp.services.DepartmentService;
import degreesapp.services.UserService;
import lombok.Getter;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.NoSuchElementException;

@Controller
@ServerEndpoint ( "/aiChat/{userId}" )
public class AIChatSocket {
    private AIChatActivity currentActivity;

    private static UserService userService;

    @Autowired
    private void setUserService(UserService service) {
        userService = service;
    }

    static CourseRegistrationService courseRegistrationService;

    @Autowired
    private void setCourseRegistrationService(CourseRegistrationService courseRegistrationService) {
        this.courseRegistrationService = courseRegistrationService;
    }

    static CourseService courseService;

    @Autowired
    private void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    static DepartmentService departmentService;

    @Autowired
    private void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    static CourseSectionRepository courseSectionRepository;

    @Autowired
    public void setCourseSectionRepository(CourseSectionRepository courseSectionRepository) {
        this.courseSectionRepository = courseSectionRepository;
    }

    private Long userId;

    private Session session = null;

    @OnOpen
    public synchronized void onOpen(Session session , @PathParam ( "userId" ) Long userId) {
        this.userId = userId;
        this.session = session;
        try {
            System.out.println(userService.fetchUserById(userId));
        } catch ( NoSuchElementException e ) {
            // Replace this with a better error. We use RuntimeException solely for the sake
            // of a better error message. But NoSuchElementException is not documented to be thrown
            // by fetchUserById, so this is just a fragile hack.
            throw new RuntimeException("User ID " + userId + " does not exist!");
        }

        this.addChildActivity(new MainChatActivity());
    }

    @OnMessage
    public void onMessage(Session session , String message) {
        AIChatActivity currentActivity;
        AIChatActivity.Prompt prompt;
        synchronized ( this ) {
            System.out.println("The message: " + message + " from user " + userId);
            System.out.println(userService.fetchUserById(userId));

            prompt = AIChatActivity.Prompt.fromMessageString(message);
            currentActivity = this.currentActivity;
        }
        currentActivity.onPrompt(prompt);
    }

    @OnClose
    public synchronized void onClose(Session session) {
        // empty
    }

    protected synchronized void sendResponse(AIChatActivity.Response response) {
        try {
            this.session.getBasicRemote().sendText(response.intoMessageString());
        } catch ( IOException e ) {
            e.printStackTrace(System.out);
        } catch ( JSONException e ) {
            throw new RuntimeException(e);
        }
    }

    protected synchronized User fetchUser() {
        return userService.fetchUserById(userId);
    }

    protected void addChildActivity(AIChatActivity childActivity) {
        AIChatActivity oldCurrentActivity;
        synchronized ( this ) {
            oldCurrentActivity = this.currentActivity;
            childActivity.parentActivity = this.currentActivity;
            childActivity.aiChatSocket = this;
            if ( this.currentActivity != null ) {
                this.currentActivity.childActivity = childActivity;
            }
            this.currentActivity = childActivity;
        }
        if ( oldCurrentActivity != null ) {
            oldCurrentActivity.onSuspend();
        }
        childActivity.onStartup();
    }

    public void removeActivity(AIChatActivity aiChatActivity) {
        AIChatActivity resumingActivity = null;
        synchronized ( this ) {
            if ( aiChatActivity.childActivity != null ) {
                aiChatActivity.childActivity.parentActivity = aiChatActivity.parentActivity;
            }
            if ( aiChatActivity.parentActivity != null ) {
                aiChatActivity.parentActivity.childActivity = aiChatActivity.childActivity;
            }
            if ( aiChatActivity == currentActivity ) {
                currentActivity = aiChatActivity.parentActivity;
                resumingActivity = currentActivity.parentActivity;
            }
        }
        if ( resumingActivity != null ) {
            resumingActivity.onResume();
        }
    }

    public void replace(AIChatActivity aiChatActivity , AIChatActivity newActivity) {
        synchronized ( this ) {
            removeActivity(aiChatActivity);
            addChildActivity(newActivity);
        }
    }

    public synchronized AIChatActivity getCurrentActivity() {
        return this.currentActivity;
    }
}


