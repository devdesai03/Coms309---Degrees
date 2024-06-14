package com.example.degrees;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class schedules classes as per location, start-time, end-time, section and days of the week
 */

public class SchedulerActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private static final String API_URL = "http://coms-309-036.class.las.iastate.edu:8080/course-sections";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_schedule);

        listView = findViewById(R.id.listViewClassSchedule);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        Button showButton = findViewById(R.id.btnShowCourses);
        showButton.setVisibility(View.GONE);

        // Make a GET request to retrieve course sections
        getCourseSections();
    }

    // GET request to retrieve course sections
    private void getCourseSections() {
        Request request = new Request.Builder()
                .url("http://coms-309-036.class.las.iastate.edu:8080/students/" + DashboardActivity.getIsuRegistrationId() + "/registered-course-sections")
                .get()
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(SchedulerActivity.this, "Network request failed", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    parseAndDisplayCourseSections(responseBody);
                } else {
                    System.out.println(response);
                    runOnUiThread(() -> Toast.makeText(SchedulerActivity.this, "Failed to retrieve course sections", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    // Helper method to parse and display course sections
    private void parseAndDisplayCourseSections(String responseBody) {
        try {
            JSONArray courseSections = new JSONArray(responseBody);
            System.out.println(courseSections);
            int courseCount = courseSections.length();
            List<String> classSchedule = new ArrayList<>();

            for (int i = 0; i < courseCount; i++) {
                JSONObject courseSection = courseSections.getJSONObject(i);
                JSONObject course = courseSection.getJSONObject("course");
                String courseShortName = course.getJSONObject("courseDepartment")
                        .getString("departmentCode") + " " +
                        course.getString("courseNumber");
                String courseName = course.getString("courseName");
                String sectionIdentifier = courseSection.getString("sectionIdentifier");
                String location = courseSection.getString("location");
                String startDate = courseSection.getString("startDate");
                String endDate = courseSection.getString("endDate");
                String daysOfWeek = courseSection.getString("daysOfWeek");
                String startTime = courseSection.getString("startTime");
                String endTime = courseSection.getString("endTime");

                // Construct the course schedule item
                String scheduleItem = courseShortName + " - " + courseName + " - " + sectionIdentifier + " - " +
                        location + " - " + startDate + " to " + endDate + " - " + daysOfWeek + " - " +
                        startTime + " to " + endTime;

                classSchedule.add(scheduleItem);
            }

            runOnUiThread(() -> {
                adapter.clear();
                adapter.addAll(classSchedule);
                adapter.notifyDataSetChanged();
            });
        } catch (JSONException e) {
            e.printStackTrace();
            runOnUiThread(() -> Toast.makeText(SchedulerActivity.this, "Failed to parse JSON response", Toast.LENGTH_SHORT).show());
        }
    }

    // Method to create a course section
    private void createCourseSection() {

        String course = "";
        String semesterAndYear = "";
        String sectionIdentifier = "";
        String location = "";
        String startDate = "";
        String endDate = "";
        String daysOfWeek = "";
        String startTime = "";
        String endTime = "";

        RequestBody requestBody = new FormBody.Builder()
                .add("course", course)
                .add("semesterAndYear", semesterAndYear)
                .add("sectionIdentifier", sectionIdentifier)
                .add("location", location)
                .add("startDate", startDate)
                .add("endDate", endDate)
                .add("daysOfWeek", daysOfWeek)
                .add("startTime", startTime)
                .add("endTime", endTime)
                .build();

        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            /**
             * This Callback method that is invoked when a network request fails.
             */
                 @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(SchedulerActivity.this, "Failed to create course section", Toast.LENGTH_SHORT).show());
            }

            /**
             * Callback method that is invoked upon receiving a response to a network request.
             * If the network request was successful, a {@link Toast} message is displayed on the UI thread
             * to inform the user that the course section was created successfully. Optionally, a method to
             * refresh the course sections may also be called.
             */
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Course section created successfully
                    runOnUiThread(() -> {
                        Toast.makeText(SchedulerActivity.this, "Course section created", Toast.LENGTH_SHORT).show();
                        // Refresh the course sections (optional)
                        getCourseSections();
                    });
                } else {
                    System.out.println(response);
                    runOnUiThread(() -> Toast.makeText(SchedulerActivity.this, "Failed to create course section", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }
}
