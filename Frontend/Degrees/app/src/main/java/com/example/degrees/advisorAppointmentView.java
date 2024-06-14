package com.example.degrees;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class advisorAppointmentView extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private static String advisorId = "60"; // Set the advisorId
    private static final String API_URL = "http://coms-309-036.class.las.iastate.edu:8080/appointments/advisor/" + advisorId + "/timeRange";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advisor_screen_to_see_studentappointments);

        listView = findViewById(R.id.listViewStudentAppointments);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        Button showButton = findViewById(R.id.btnShowAppointments);
        showButton.setOnClickListener(v -> getAppointments());

        // Make a GET request to retrieve appointments
        getAppointments();
    }

    // GET request to retrieve appointments
    private void getAppointments() {
        Request request = new Request.Builder()
                .url(API_URL)
                .get()
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(advisorAppointmentView.this, "Network request failed", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    parseAndDisplayAppointments(responseBody);
                } else {
                    System.out.println(response);
                    runOnUiThread(() -> Toast.makeText(advisorAppointmentView.this, "Failed to retrieve appointments", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private void parseAndDisplayAppointments(String responseBody) {
        try {
            JSONArray appointmentsArray = new JSONArray(responseBody);
            List<String> appointmentDetails = new ArrayList<>();

            for (int i = 0; i < appointmentsArray.length(); i++) {
                JSONObject appointmentObject = appointmentsArray.getJSONObject(i);

                int appointmentId = appointmentObject.getInt("id");

                // Parsing student details
                JSONObject studentObject = appointmentObject.getJSONObject("student");
                JSONObject isuRegistrationObject = studentObject.getJSONObject("isuRegistration");
                JSONObject userObject = isuRegistrationObject.getJSONObject("user");

                String studentName = userObject.getString("userName");
                String studentEmail = userObject.getString("userEmail");
                String studentAddress = userObject.getString("userAddress");
                String studentPhoneNumber = userObject.getString("phoneNumber");

                // Parsing advisor details
                JSONObject advisorObject = appointmentObject.getJSONObject("advisor");
                JSONObject advisorIsuRegistrationObject = advisorObject.getJSONObject("isuRegistration");
                JSONObject advisorUserObject = advisorIsuRegistrationObject.getJSONObject("user");

                String advisorName = advisorUserObject.isNull("userName") ? "" : advisorUserObject.getString("userName");
                String advisorEmail = advisorUserObject.getString("userEmail");
                String advisorAddress = advisorUserObject.getString("userAddress");
                String advisorPhoneNumber = advisorUserObject.getString("phoneNumber");

                String startTime = appointmentObject.getString("startTime");
                String endTime = appointmentObject.getString("endTime");
                String description = appointmentObject.getString("description");

                // Construct the appointment details string
                String appointmentDetailsString =
                        "Appointment ID: " + appointmentId +
                                "\nStudent Name: " + studentName +
                                "\nStudent Email: " + studentEmail +
                                "\nStudent Address: " + studentAddress +
                                "\nStudent Phone Number: " + studentPhoneNumber +
                                "\nAdvisor Name: " + advisorName +
                                "\nAdvisor Email: " + advisorEmail +
                                "\nAdvisor Address: " + advisorAddress +
                                "\nAdvisor Phone Number: " + advisorPhoneNumber +
                                "\nStart Time: " + startTime +
                                "\nEnd Time: " + endTime +
                                "\nDescription: " + description;

                appointmentDetails.add(appointmentDetailsString);
            }

            // Update UI on the main thread
            runOnUiThread(() -> {
                adapter.clear();
                adapter.addAll(appointmentDetails);
                adapter.notifyDataSetChanged();
            });
        } catch (JSONException e) {
            e.printStackTrace();
            runOnUiThread(() -> Toast.makeText(advisorAppointmentView.this, "Failed to parse JSON response", Toast.LENGTH_SHORT).show());
        }
    }
}
