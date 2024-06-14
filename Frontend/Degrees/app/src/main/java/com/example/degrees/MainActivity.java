package com.example.degrees;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.degrees.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * This activity represents the main login screen of the application.
 */
public class MainActivity extends AppCompatActivity {

    // Class members
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private EditText emailEditText;
    private EditText passwordEditText;

    /**
     * Initializes the activity.
     *
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);

        Button signButton = findViewById(R.id.SignIn);
        // Set OnClickListener for the sign-in button
        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }

    /**
     * Performs the user login process.
     */
    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        loginPostRequest(email, password);
        // Get the user-entered email and password

    }

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(binding.toolbar);

//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

//        binding.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAnchorView(R.id.fab)
//                        .setAction("Action", null).show();
//            }
//        });

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void onCreateNewUserButtonClick(View view) {
        Button signUpButton = findViewById(R.id.CreateNewUser);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch the Sign-Up Activity
                Intent intent = new Intent(MainActivity.this, CreateUserActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Retrieves the base URL for API requests.
     *
     * @return The base URL as a string.
     */
    public static String getBaseURL() {
        return "http://coms-309-036.class.las.iastate.edu:8080/";
    }

    /**
     * Initiates the HTTP POST request to log in the user.
     *
     * @param email The user's email address.
     * @param password The user's password.
     */
    private void loginPostRequest(String email, String password) {
        String url = getBaseURL() + "/login";
        JSONObject body = new JSONObject();
        try {
            body.put("userEmail", email);
            body.put("userPassword", password);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Get the "didSucceed" value from the response
                            boolean didSucceed = response.getBoolean("didSucceed");

                            if (didSucceed) {
                                // Login successful, navigate to the next screen for verification
                                Intent intent = new Intent(MainActivity.this, VerificationActivity.class);
                                intent.putExtra("email", email);
                                startActivity(intent);
                            } else {
                                // Login failed, show an error message
                                Snackbar.make(binding.getRoot(), "Invalid email or password. Please try again.", Snackbar.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error (e.g., network error)
                        System.out.println(error);
                        Snackbar.make(binding.getRoot(), "Login failed. Please check your network connection.", Snackbar.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // Add headers if needed (e.g., authentication token)
                Map<String, String> headers = new HashMap<>();
                // headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
                // headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Add the request to the Volley request queue
        VolleySingleton2.getInstance(getApplicationContext()).addToRequestQueue(jsonRequest);
    }
}



