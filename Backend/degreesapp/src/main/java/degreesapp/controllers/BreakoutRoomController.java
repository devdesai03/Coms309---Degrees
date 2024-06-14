package degreesapp.controllers;

import degreesapp.controllers.chat.ChatSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
public class BreakoutRoomController {
    @Autowired
    private ChatSocket chatSocket; // Inject the ChatSocket

    @GetMapping ( "/availableBreakoutRooms" )
    public ResponseEntity<Set<String>> listAvailableBreakoutRooms() {
        return ResponseEntity.ok(ChatSocket.roomUserMap.keySet());
    }

    @GetMapping ( "/onlineUsers" )
    public ResponseEntity<List<String>> listOnlineUsers() {
        List<String> onlineUsers = ChatSocket.getOnlineUsersList();
        return ResponseEntity.ok(onlineUsers);
    }

    @GetMapping ( "/chatHistory/{userName}" )
    public ResponseEntity<String> getChatHistory(@PathVariable String userName) {
        String chatHistory = chatSocket.getChatHistory(userName);
        return ResponseEntity.ok(chatHistory);
    }
}
