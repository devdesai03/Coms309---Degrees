package com.example.degrees;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.degrees.VolleySingleton2;
import com.example.degrees.databinding.ActivityVerificationBinding;
import com.google.android.material.snackbar.Snackbar;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

 /**
 *This activity is responsible for verifying the user based on a code sent to their email.
 * It inflates its UI from the {@code ActivityVerificationBinding} class, which provides
 * a direct reference to all the views. This activity expects an email passed as an extra
 * from the previous activity, which it uses along with the verification code entered by
 * the user to perform the verification process.
 */
public class VerificationActivity extends AppCompatActivity {

    private ActivityVerificationBinding binding;
    private EditText verificationCodeEditText;
    private String email; // Store the email received from the previous activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerificationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Retrieve the email passed from the previous activity
        email = getIntent().getStringExtra("email");

        verificationCodeEditText = findViewById(R.id.editTextVerificationCode);

        Button verifyButton = findViewById(R.id.buttonVerify);
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyCode();
            }
        });
    }

    /**
     * Initiates the verification process upon button click.
     */
    private void verifyCode() {
        // Get the user-entered verification code
        String verificationCode = verificationCodeEditText.getText().toString().trim();

        // Create a JSON object for the verification request
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("verificationCode", verificationCode);
            requestBody.put("userEmail", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /**
        *Define the URL for the verification endpoint
        */
        String verifyUrl = MainActivity.getBaseURL() + "/login/verify";

        // Create a POST request with Volley
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, verifyUrl, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String accessToken = null;
                        long userId = response.optLong("userId", -1);

                        if (!response.isNull("accessToken")) {
                            try {
                                accessToken = response.getString("accessToken");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                            // Verification successful, navigate to the dashboard
                            Intent intent = new Intent(VerificationActivity.this, DashboardActivity.class);
                            intent.putExtra("userId", userId);
                            startActivity(intent);
                        } else {
                            // Verification failed, show an error message
                            Snackbar.make(binding.getRoot(), "Invalid verification code. Please try again.", Snackbar.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    /**
                     * This methos outputs the error when verification fails
                     * @param error
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle the error (e.g., network error)
                        Snackbar.make(binding.getRoot(), "Verification failed. Please check your network connection.", Snackbar.LENGTH_LONG).show();
                    }
                }) {
        /**
         * This method retrieves a map of request headers to send with the network request.
         */
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
