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

import com.example.degrees.databinding.ActivitySignUpBinding;


import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Activity responsible for creating a new user through a sign-up process.
 */
public class CreateUserActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;

    /**
     * Initializes the SignUpActivity.
     * @param savedInstanceState The saved state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        View signUpButton = findViewById(R.id.btnSignUp);
        signUpButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                createUser();
            }
        });


    }

    /**
     * Navigates back to the sign-in screen from the sign-up screen.
     * @param view The current view
     */
    public void GoBackSignInScreen(View view) {
        Button goBackToSignInFromSignUp = findViewById(R.id.CreateNewUser);
        goBackToSignInFromSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch the Sign-Up Activity
                Intent intent = new Intent(CreateUserActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Retrieves the base URL.
     * @return The base URL
     */
    public String getBaseURL() {
        return MainActivity.getBaseURL();
    }

    /**
     * Handles the creation of a new user after validating the input data.
     */
    private void createUser() {
        System.out.println("createUser called");
        EditText userName = findViewById(R.id.edtSignUpFullName);
        EditText userEmail = findViewById(R.id.edtSignUpEmail);
        EditText userPhoneNumber = findViewById(R.id.edtSignUpEmail);
        EditText editTextPassword = findViewById(R.id.edtSignUpPassword);
        EditText editTextConfirmPassword = findViewById(R.id.edtSignUpConfirmPassword);

        String userNameText = userName.getText().toString();
        String userEmailText = userEmail.getText().toString();
        String userPhoneNumberText = userPhoneNumber.getText().toString();
        String userSignUpPassword = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();

        // Check if the passwords match
        if (!userSignUpPassword.equals(confirmPassword)) {
            // Passwords don't match, display an error message
            Snackbar.make(binding.getRoot(), "Passwords do not match", Snackbar.LENGTH_LONG).show();
            return; // Exit the method
        }

        // Request a JSON response from the provided URL.
        String url = getBaseURL() + "/users";

        // Create a JSON object to send as the request body.
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("userName", userNameText);
            requestBody.put("userPassword", userSignUpPassword);
            requestBody.put("userAddress", "");
            requestBody.put("userEmail", userEmailText);
            requestBody.put("phoneNumber", userPhoneNumberText);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Check if the passwords match

        // Request a JSON response from the provided URL.

        // Create a JSON object to send as the request body.
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("User created successfully!");
                        Intent intent = new Intent(CreateUserActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("User creation failed: " + error.toString());
                        // Handle the error here, e.g., display an error message.
                        Snackbar.make(binding.getRoot(), "User creation failed", Snackbar.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                // Add headers if needed, e.g., authorization token.
                // headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
                // headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Adding the request to the Volley request queue.
        VolleySingleton2.getInstance(getApplicationContext()).addToRequestQueue(jsonRequest);
    }

}
