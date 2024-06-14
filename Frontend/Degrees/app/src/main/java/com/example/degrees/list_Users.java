package com.example.degrees;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class list_Users extends AppCompatActivity {
    LinearLayout usersView;
    TextView search_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);
        usersView = findViewById(R.id.linearLayout);
        Button refresh = findViewById(R.id.refreshButton);
        search_button = findViewById(R.id.search_button);
        refreshUserList();
        refresh.setOnClickListener(view-> {
            refreshUserList();
        });
        search_button.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
            System.out.println("Action: " + actionId);
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                refreshUserList();
                return true;
            }
            return false;
        });
    }

    public boolean shouldDisplayUser(JSONObject user) throws JSONException {
        String searchText = search_button.getText().toString();
        System.out.println(user + " " + searchText);
        if (searchText.equals("")) {
            return true;
        } else {
            String userName = user.getString("userName");
            if (userName.toLowerCase().contains(searchText.toLowerCase())) {
                return true;
            }
            return false;
        }
    }

    public void refreshUserList() {
        loadUsers_List(userList -> {
            try {
                ArrayList<JSONObject> list = new ArrayList<>();
                for (int i = 0; i < userList.length(); i++) {
                    if (shouldDisplayUser(userList.getJSONObject(i))) {
                        list.add(userList.getJSONObject(i));
                    }
                }
                displayUserList(list);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void displayUserList(List<JSONObject> users) throws JSONException {
        usersView.removeAllViews();
        for (JSONObject user : users) {
            View listEntryView = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.user_list_entry, null);
            TextView userNameView = listEntryView.findViewById(R.id.userName);
            View editButton = listEntryView.findViewById(R.id.editButton);
            View deleteButton = listEntryView.findViewById(R.id.deleteButton);

            Long userId = user.getLong("userId");
            String userName = user.getString("userName");

            userNameView.setText(userName);
            editButton.setOnClickListener(view -> {
                Intent intent = new Intent(list_Users.this, manageusers.class);
                intent.putExtra("editingUserId", userId);
                startActivity(intent);
            });
            deleteButton.setOnClickListener(view -> {
                StringRequest request = new StringRequest(
                        Request.Method.DELETE,
                        MainActivity.getBaseURL() + "/users/" + userId,
                        response -> {
                            refreshUserList();
                        },
                        error -> {
                            error.printStackTrace();
                        }
                );
                VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
            });

            usersView.addView(listEntryView);
        }
    }

    public void loadUsers_List(Consumer<JSONArray> callback){

            // Request a string response from the provided URL.
            String url;
            url = MainActivity.getBaseURL() + "/users/";



            JsonArrayRequest stringRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                    response -> {
                        callback.accept(response);
                    },
                    error -> {
                        error.printStackTrace();
                    }
            );
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        }



}