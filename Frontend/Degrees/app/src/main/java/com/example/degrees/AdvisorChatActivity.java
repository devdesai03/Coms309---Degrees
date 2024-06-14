package com.example.degrees;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;

import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * The AdvisorChatActivity manages the advisor's chat interface and functionalities,
 * including WebSocket connections, message handling, and API interactions.
 */
public class AdvisorChatActivity extends AppCompatActivity implements WebSocketListener {

    // UI elements
    private Button connectBtn, sendBtn, getBreakoutRoomsBtn, getOnlineUsersBtn, getChatHistoryBtn;
    private EditText usernameEtx, msgEtx;
    private TextView msgTv;
    private List<String> chatHistory = new ArrayList<>();


    /**
     * Initializes the advisor chat interface, sets UI elements, and configures listeners for buttons.
     * Manages WebSocket connections and various API interactions to handle chat functionalities.
     * @param savedInstanceState The saved state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advisor_chat);

        // Initialize UI elements
        connectBtn = findViewById(R.id.bt1);
        usernameEtx = findViewById(R.id.et1);
        getOnlineUsersBtn = findViewById(R.id.getOnlineUsersBtn);
        getBreakoutRoomsBtn = findViewById(R.id.getBreakoutRoomsBtn);
        getChatHistoryBtn = findViewById(R.id.getChatHistoryBtn);
        sendBtn = findViewById(R.id.bt2);
        msgEtx = findViewById(R.id.et2);
        msgTv = findViewById(R.id.tx1);

        // Initially hide room-related elements
        getOnlineUsersBtn.setVisibility(View.GONE);
        getBreakoutRoomsBtn.setVisibility(View.GONE);
        getChatHistoryBtn.setVisibility(View.GONE);
        sendBtn.setVisibility(View.GONE);
        msgEtx.setVisibility(View.GONE);
        msgTv.setVisibility(View.GONE);


        // Connect button listener
        connectBtn.setOnClickListener(view -> {
            String username = usernameEtx.getText().toString();

            if (!username.isEmpty()) {
                // Save the username
                saveUsername(username);

                // Hide username EditText, roomName EditText, and the Connect button
                usernameEtx.setVisibility(View.GONE);
                connectBtn.setVisibility(View.GONE);

                //Show the other buttons
                getOnlineUsersBtn.setVisibility(View.VISIBLE);
                getBreakoutRoomsBtn.setVisibility(View.VISIBLE);
                getChatHistoryBtn.setVisibility(View.VISIBLE);
                sendBtn.setVisibility(View.VISIBLE);
                msgEtx.setVisibility(View.VISIBLE);
                msgTv.setVisibility(View.VISIBLE);

                // Establish WebSocket connection with the username
                String serverUrl = "ws://coms-309-036.class.las.iastate.edu:8080/advisor-chat/" + getSavedUsername();
                WebSocketManager.getInstance().connectWebSocket(serverUrl);
                WebSocketManager.getInstance().setWebSocketListener(AdvisorChatActivity.this);
            }
        });

        sendBtn.setOnClickListener(v -> {
            String message = msgEtx.getText().toString();

            if (!message.isEmpty()) {
                if (message.startsWith("/join-room")) {
                    // Extract the room name from the message
                    String roomName = message.substring("/join-room".length());

                    // Send a join room message to the backend via WebSocket
                    String joinRoomMessage = "/join-room " + roomName;
                    WebSocketManager.getInstance().sendMessage(joinRoomMessage);

                    // Add the message to the chat history
                    chatHistory.add("Joining breakout room: " + roomName);
                } else {
                    // If the message doesn't start with "/join-room ", it's a regular chat message
                    // Send the message to the backend via WebSocket
                    WebSocketManager.getInstance().sendMessage(message);

                    // Add the message to the chat history
                }

                // Update the msgTv with the entire chat history
                updateChatHistory();

                // Clear the input field
                msgEtx.setText("");
            }
        });

        // Get breakout rooms button listener
        getBreakoutRoomsBtn.setOnClickListener(v -> {
            // Send HTTP GET request to get a list of active rooms
            sendHttpGetRequest("/availableBreakoutRooms");
        });

        // Get online users button listener
        getOnlineUsersBtn.setOnClickListener(v -> {
            // Send HTTP GET request to get a list of active users
            sendHttpGetRequest("/onlineUsers");
        });

        // Get chat history button listener
        getChatHistoryBtn.setOnClickListener(v -> {
            // Send HTTP GET request to get chat history
            sendHttpGetRequest("/chatHistory/" + getSavedUsername());
        });
    }


    /**
     * Updates the chat history displayed in the message TextView.
     * Runs on the UI thread.
     */
    private void updateChatHistory() {
        StringBuilder chatText = new StringBuilder();
        for (String message : chatHistory) {
            chatText.append(message).append("\n");
        }

        runOnUiThread(() -> {
            msgTv.setText(chatText.toString());
        });
    }

    /**
     * Saves the username in SharedPreferences.
     * @param username The username to be saved
     */
    private void saveUsername(String username) {
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.apply();
    }

    /**
     * Retrieves the saved username from SharedPreferences.
     * @return The saved username, or an empty string if not found
     */
    private String getSavedUsername() {
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        return preferences.getString("username",""); // "" is the default value if the username is not found
    }

    // Append a message to the chat area
    private void appendMessageToChat(String message) {
        runOnUiThread(() -> {
            String currentText = msgTv.getText().toString();
            //ADD NEW LINE IN THE MIDDLE OF "" TO SHOW MULIPLE MESSAGES
            msgTv.setText(currentText + "" + message);
        });
    }

    private void fetchChatHistory() {
        OkHttpClient client = new OkHttpClient();
        String baseUrl = "http://coms-309-036.class.las.iastate.edu:8080"; // Replace with your API URL

        Request request = new Request.Builder()
                .url(baseUrl + "/chatHistory/" + getSavedUsername())
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    // Handle network errors here
                    // You can update the UI to show an error message
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    runOnUiThread(() -> {
                        // Handle a successful response, e.g., update the UI with chat history data
                        msgTv.setText(responseData);
                    });
                } else {
                    runOnUiThread(() -> {
                        // Handle errors, e.g., show an error message
                        msgTv.setText("Error: " + response.message());
                    });
                }
            }
        });
    }

    private void fetchOnlineUsers() {
        OkHttpClient client = new OkHttpClient();
        String baseUrl = "http://coms-309-036.class.las.iastate.edu:8080"; // Replace with your API URL

        Request request = new Request.Builder()
                .url(baseUrl + "/onlineUsers")
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    // Handle network errors here
                    // You can update the UI to show an error message
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    runOnUiThread(() -> {
                        // Handle a successful response, e.g., update the UI with online users data
                        msgTv.setText(responseData);
                    });
                } else {
                    runOnUiThread(() -> {
                        // Handle errors, e.g., show an error message
                        msgTv.setText("Error: " + response.message());
                    });
                }
            }
        });
    }

    private void fetchBreakoutRooms() {
        OkHttpClient client = new OkHttpClient();
        String baseUrl = "http://coms-309-036.class.las.iastate.edu:8080"; // Replace with your API URL

        Request request = new Request.Builder()
                .url(baseUrl + "/availableBreakoutRooms")
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    // Handle network errors here
                    // You can update the UI to show an error message
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    runOnUiThread(() -> {
                        // Handle a successful response, e.g., update the UI with breakout room data
                        msgTv.setText(responseData);
                    });
                } else {
                    runOnUiThread(() -> {
                        // Handle errors, e.g., show an error message
                        msgTv.setText("Error: " + response.message());
                    });
                }
            }
        });
    }

    private void sendHttpGetRequest(String endpoint) {
        // Replace with your actual server URL
        String baseUrl = "http://coms-309-036.class.las.iastate.edu:8080";

        // Create an OkHttpClient instance
        OkHttpClient client = new OkHttpClient();

        // Build the request
        Request request = new Request.Builder()
                .url(baseUrl + endpoint)
                .get()
                .build();

        // Send the request asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle network errors here
                e.printStackTrace();
                runOnUiThread(() -> {
                    msgTv.setText("Network error: " + e.getMessage());
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    runOnUiThread(() -> {
                        // Handle a successful response, e.g., display data in the UI
                        msgTv.setText(responseData);
                    });
                } else {
                    runOnUiThread(() -> {
                        // Handle errors, e.g., show an error message
                        msgTv.setText("Error: " + response.message());
                    });
                }
            }
        });
    }

    /**
     * Handles the message received via WebSocket and updates the chat area.
     * Runs on the UI thread.
     * @param message The message received via WebSocket
     */
    @Override
    public void onWebSocketMessage(String message) {
        runOnUiThread(() -> {
            String s = msgTv.getText().toString();
            msgTv.setText(s + "\n" + message);
        });
    }


    /**
     * Handles the WebSocket close event, updating the UI with the reason and closure initiator.
     * Runs on the UI thread.
     * @param code The code indicating the reason for closure
     * @param reason The reason for closure
     * @param remote Indicates if the closure was initiated remotely
     */
    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        String closedBy = remote ? "server" : "local";
        runOnUiThread(() -> {
            String s = msgTv.getText().toString();
            msgTv.setText(s + "---\nconnection closed by " + closedBy + "\nreason: " + reason);
        });
    }

    /**
     * Handles the WebSocket open event.
     * If a room is specified, joins the room via WebSocket.
     * @param handshakedata Information related to the handshake
     */
    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        // Handle WebSocket open event
        String room = this.getIntent().getStringExtra("goToRoom");
        if (room != null) {
            WebSocketManager.getInstance().sendMessage("/join-room " + room);
        }
    }

    /**
     * Handles WebSocket errors if they occur.
     * @param ex The encountered exception
     */
    @Override
    public void onWebSocketError(Exception ex) {
        // Handle WebSocket error
    }
}
