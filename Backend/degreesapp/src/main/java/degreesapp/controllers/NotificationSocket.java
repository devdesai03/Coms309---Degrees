package degreesapp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import degreesapp.models.Notification;
import degreesapp.models.User;
import degreesapp.services.NotificationService;
import degreesapp.services.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Date;
import java.util.NoSuchElementException;

@Controller
@ServerEndpoint("/notifications/{userId}")
public class NotificationSocket {
    private static NotificationService notificationService;
    @Autowired private void setNotificationService(NotificationService notificationService) {
        NotificationSocket.notificationService = notificationService;
    }

    private static UserService userService;
    @Autowired private void setUserService(UserService userService) {
        NotificationSocket.userService = userService;
    }

    private Long userId;
    private User user;
    private NotificationService.Listener listener;
    private Session session;

    @OnOpen
    public synchronized void onOpen(Session session, @PathParam("userId") Long userId) {
        this.userId = userId;
        try {
            this.user = userService.fetchUserById(userId);
        } catch (NoSuchElementException e) {
            // Replace this with a better error. We use RuntimeException solely for the sake
            // of a better error message. But NoSuchElementException is not documented to be thrown
            // by fetchUserById, so this is just a fragile hack.
            throw new RuntimeException("User ID " + userId + " does not exist!");
        }
        this.session = session;
        this.listener = notificationService.registerNotificationListener(this.userId, this::handleNotification);
    }

    @OnMessage
    public synchronized void onMessage(Session session, String message) {

    }

    @OnClose
    public synchronized void onClose(Session session) {
        notificationService.deregisterNotificationListener(userId, listener);
    }

    @Async
    public synchronized void handleNotification(Notification notification) {
        String type = notification.notificationType();
        ObjectNode body = notification.notificationBody();
        body.put("type", type);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String payload = objectMapper.writeValueAsString(body);
            this.session.getBasicRemote().sendText(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}

//@RestController
//class NotificationTest {
//    @Autowired NotificationService notificationService;
//
//    @Getter
//    @AllArgsConstructor
//    static class TestNotification implements Notification {
//        private String text;
//
//        private Date timestamp;
//
//        @Override
//        public String notificationType() {
//            return "testNotification";
//        }
//    }
//
//    @GetMapping("/notificationTest")
//    boolean notificationTest() {
//        TestNotification notification = new TestNotification("Test... ", new Date());
//        notificationService.sendGlobalNotification(notification);
//        return true;
//    }
//
//    @GetMapping("/notificationTest/{userId}")
//    boolean notificationTest(@PathVariable Long userId) {
//        TestNotification notification = new TestNotification("This is for you! " + userId, new Date());
//        notificationService.sendNotificationToUser(notification, userId);
//        return true;
//    }
//}