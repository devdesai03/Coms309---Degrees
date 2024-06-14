package com.example.degrees;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.degrees.Websockets.WebSocketListener;
import com.example.degrees.Websockets.WebSocketManager;

import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
/**
* This class is an AI chat which uses a web socket.
 */

public class Chat_Bot extends AppCompatActivity implements WebSocketListener {

    private String BASE_URL = "ws://coms-309-036.class.las.iastate.edu:8080/aiChat/";

    private Button connectBtn, sendBtn;
    private EditText msgEtx;
    private TextView msgTv;
    private LinearLayout messagesContainer;

    private ScrollView messagesScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //onCreate is where all the initialization happens
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

        /* initialize UI elements */
        connectBtn = (Button) findViewById(R.id.bt1);
        sendBtn = (Button) findViewById(R.id.bt2);
        msgEtx = (EditText) findViewById(R.id.et2);
        msgTv = (TextView) findViewById(R.id.tx1);
        messagesContainer = findViewById(R.id.messagesContainer);
        messagesScrollView = findViewById(R.id.messagesScrollView);

        /* connect button listener */
        connectBtn.setOnClickListener(view -> {
            String serverUrl = BASE_URL + DashboardActivity.getUserid();

            // Establish WebSocket connection and set listener
            WebSocketManager.getInstance().connectWebSocket(serverUrl);
            WebSocketManager.getInstance().setWebSocketListener(Chat_Bot.this);
        });

        /* send button listener */
        sendBtn.setOnClickListener(v -> {
            try {

                // send message
                WebSocketManager.getInstance().sendMessage(msgEtx.getText().toString());
                displayOwnMessage(msgEtx.getText().toString());
            } catch (Exception e) {
                Log.d("ExceptionSendMessage:", e.getMessage().toString());
            }
        });
    }

    private void displayOwnMessage(String message) {
        runOnUiThread(() -> {
            Context context = getApplicationContext();

            LinearLayout ownMessageBox = new LinearLayout(context);
            ownMessageBox.setOrientation(LinearLayout.VERTICAL);

            TextView ownMessageText = new TextView(context);
            ownMessageText.setText(message);
            ownMessageText.setTextColor(ContextCompat.getColor(context, R.color.WhiteSmoke));

            ownMessageBox.addView(ownMessageText);

            ownMessageBox.setBackgroundColor(ContextCompat.getColor(context, R.color.Black));
            messagesContainer.addView(ownMessageBox);

            messagesScrollView.post(() -> messagesScrollView.fullScroll(View.FOCUS_DOWN));
        });
    }


    /**
     * Handles the incoming WebSocket messages.The 'runOnUiThread' method is used to post a runnable
     * to the UI thread's message queue, allowing UI updates to occur safely from a background
     * or non-UI thread.
     */

    @Override
    public void onWebSocketMessage(String message) {
        runOnUiThread(() -> {
            try {
                Context context = getApplicationContext();


//            String s = msgTv.getText().toString();
                JSONObject response = new JSONObject(message);
                if (!response.isNull("text")) {
                    LinearLayout messageBox = new LinearLayout(context);
                    messageBox.setOrientation(LinearLayout.VERTICAL);

                    messageBox.setBackgroundColor(ContextCompat.getColor(context, R.color.WhiteSmoke)); // Set the background color

//                messageBox.setVisibility(View.VISIBLE);


                    TextView messageText = new TextView(context);
                    messageText.setText(response.getString("text"));
                    messageText.setTextColor(ContextCompat.getColor(context, R.color.Black));
                    messageBox.addView(messageText);

                    JSONArray recommendedPrompts = response.getJSONArray("recommendedPrompts");
                    for (int i = 0; i < recommendedPrompts.length(); i++) {
                        String buttonText = recommendedPrompts.getString(i);
                        Button button = new Button(context);
                        button.setText(buttonText);

                        button.setOnClickListener((view) -> {
                            WebSocketManager.getInstance().sendMessage(buttonText);
                            displayOwnMessage(buttonText);
                        });

                        messageBox.addView(button);
                    }

                    messagesContainer.addView(messageBox);

                    messagesScrollView.post(() -> messagesScrollView.fullScroll(View.FOCUS_DOWN));
                }

                if (!response.isNull("clientAction")) {
                    JSONArray clientAction = response.getJSONArray("clientAction");
                    if (clientAction.getString(0).equals("toAdvisorChat")) {
                        Intent intent = new Intent(Chat_Bot.this, AdvisorChatActivity.class);
                        if (clientAction.length() > 1) {
                            intent.putExtra("goToRoom", clientAction.getString(1));
                        }
                        startActivity(intent);
                    } else if (clientAction.getString(0).equals("goToAcademicPlan")) {
                        Intent intent = new Intent(Chat_Bot.this, AcademicPlannerActivity.class);
                        startActivity(intent);
                    }
                }

//            msgTv.setText(s + "\n"+message);
            } catch (JSONException e) {
                e.printStackTrace(System.out);
            }
        });
    }
/**
 * Callback method invoked when the WebSocket connection has been closed.
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
 * Callback method invoked when the WebSocket connection has been established.
 */
    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {}

/**
 * Callback method that is invoked when a WebSocket error occurs.
 */
    @Override
    public void onWebSocketError(Exception ex) {}
}