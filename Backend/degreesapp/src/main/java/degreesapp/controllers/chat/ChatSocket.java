package degreesapp.controllers.chat;

import degreesapp.models.Message;
import degreesapp.models.Notification;
import degreesapp.models.User;
import degreesapp.models.notifications.DirectMessageNotification;
import degreesapp.repositories.MessageRepository;
import degreesapp.services.NotificationService;
import degreesapp.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a WebSocket chat server for handling real-time communication
 * between users. Each user connects to the server using their unique
 * username.
 * <p>
 * This class is annotated with Spring's `@ServerEndpoint` and `@Component`
 * annotations, making it a WebSocket endpoint that can handle WebSocket
 * connections at the "/chat/{username}" endpoint.
 * <p>
 * Example URL: ws://localhost:8080/chat/username
 * <p>
 * The server provides functionality for broadcasting messages to all connected
 * users and sending messages to specific users.
 *
 * @author Mohammed Abdalgader
 */
@ServerEndpoint ( "/advisor-chat/{username}" )
@Component
@ComponentScan ( basePackages = "degreesapp.chat" )
public class ChatSocket {
    public static MessageRepository msgRepo;

    @Autowired
    public void setMessageRepository(MessageRepository repo) {
        msgRepo = repo;
    }

    public static UserService userService;

    @Autowired
    public void setUserRepository(UserService service) {
        userService = service;
    }

    public static NotificationService notificationService;

    @Autowired
    public void setNotificationService(NotificationService service) {
        notificationService = service;
    }

    private static final Map<Session, String> sessionUsernameMap = new ConcurrentHashMap<>();
    private static final Map<String, Session> usernameSessionMap = new ConcurrentHashMap<>();
    private static final Set<String> onlineUsers = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public final static Map<String, Set<String>> roomUserMap = new ConcurrentHashMap<>();
    private final Map<String, String> userRoomMap = new ConcurrentHashMap<>();
    private final Logger logger = LoggerFactory.getLogger(ChatSocket.class);

    public static Collection<String> getRooms() {
        return roomUserMap.keySet();
    }


    @OnOpen
    public void onOpen(Session session , @PathParam ( "username" ) String username) throws IOException {
        sessionUsernameMap.put(session , username);
        usernameSessionMap.put(username , session);

        broadcast(username + " has joined the chat");
        onlineUsers.add(username);
    }


    @OnMessage
    public void onMessage(Session session , String message) throws IOException {
        String username = sessionUsernameMap.get(session);
        logger.info("[onMessage] " + username + ": " + message);

        String currentRoom = userRoomMap.get(username);

        if ( message.startsWith("/join-room") ) {
            handleJoinRoom(username , message);
        } else if ( message.startsWith("/leaveroom") ) {
            handleLeaveRoom(username);
        } else if ( message.startsWith("@") ) {
            handleDirectMessage(username , message);
        } else {
            if ( currentRoom != null ) {
                broadcastToRoom(username , currentRoom , username + ": " + message);
            } else {
                broadcast(username + ": " + message);
            }
        }

        msgRepo.save(new Message(username , message));
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        String username = sessionUsernameMap.get(session);
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);

        broadcast(username + " disconnected");
        onlineUsers.remove(username);
    }

    @OnError
    public void onError(Session session , Throwable throwable) {
        logger.error("WebSocket error" , throwable);
    }

    private void sendMessageToParticularUser(String username , String message) {
        Session userSession = usernameSessionMap.get(username);
        if ( userSession != null ) {
            try {
                userSession.getBasicRemote().sendText(message);
            } catch ( IOException e ) {
                logger.error("Failed to send a message to " + username , e);
            }
        }
    }

    private void handleJoinRoom(String username , String message) {
        String[] parts = message.split(" ");
        if ( parts.length == 2 ) {
            String roomName = parts[1];
            userRoomMap.put(username , roomName);
            roomUserMap.computeIfAbsent(roomName , k -> new HashSet<>()).add(username);

            sendMessageToParticularUser(username , "You joined room: " + roomName);
            broadcastToRoom(username , roomName , username + " has joined this room");
        }
    }

    private void handleDirectMessage(String username , String message) {
        String[] splitMsg = message.split("\\s+");
        String destUserName = splitMsg[0].substring(1);
        String actualMessage = message.substring(destUserName.length() + 2);

        sendMessageToParticularUser(destUserName , "[DM from " + username + "]: " + actualMessage);
        sendMessageToParticularUser(username , "[DM to " + destUserName + "]: " + actualMessage);

        sendNotification:
        {
            User destUser = userService.fetchUserByUserName(destUserName);

            if ( destUser == null ) {
                break sendNotification;
            }

            Notification notification = new DirectMessageNotification(username , destUserName , actualMessage);
            notificationService.sendNotificationToUser(notification , destUser.getUserId());
        }
    }

    private void broadcastToRoom(String sender , String roomName , String message) {
        Set<String> usersInRoom = roomUserMap.get(roomName);
        if ( usersInRoom != null ) {
            usersInRoom.forEach(user -> sendMessageToParticularUser(user , message));
        }
    }

    private void broadcast(String message) {
        sessionUsernameMap.keySet().forEach(session -> sendMessageToParticularUser(sessionUsernameMap.get(session) , message));
    }

    private void handleLeaveRoom(String username) {
        String currentRoom = userRoomMap.get(username);
        if ( currentRoom != null ) {
            leaveRoom(username , currentRoom);
        }
    }

    private void leaveRoom(String username , String roomName) {
        userRoomMap.remove(username);
        roomUserMap.get(roomName).remove(username);

        sendMessageToParticularUser(username , "You left room: " + roomName);
        broadcastToRoom(username , roomName , username + " has left this room");
    }

    public String getChatHistory(String requestingUsername) {
        List<Message> messages = msgRepo.findByUserName(requestingUsername);
        StringBuilder history = new StringBuilder();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for ( Message message : messages ) {
            String formattedTimestamp = dateFormat.format(message.getSent());
            history.append(formattedTimestamp).append(" ");
            history.append(message.getUserName()).append(": ");
            history.append(message.getContent()).append("\n");
        }

        return history.toString();
    }

    public static List<String> getOnlineUsersList() {
        return new ArrayList<>(onlineUsers);
    }

}

