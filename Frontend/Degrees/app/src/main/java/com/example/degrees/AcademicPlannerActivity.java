package com.example.degrees;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
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

/**
 * Manages the display of a four-year planner, fetching and displaying courses for each semester.
 */
public class AcademicPlannerActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://coms-309-036.class.las.iastate.edu:8080/academic-planner/";
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.four_year_planner_activity);

        listView = findViewById(R.id.listViewAcademicPlanner);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        // Get academic planner data when the activity is created
        getFourYearPlan();
    }

    private int isuRegisterationID() {
        return 18;
    }

    private void getFourYearPlan() {
        Request request = new Request.Builder()
                .url(BASE_URL + isuRegisterationID())
                .get()
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> showToast("Network request failed"));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Log.d("TAG", "API Response: " + responseBody); // Add this line for debugging
                    parseAndDisplayAcademicPlanner(responseBody);
                } else {
                    runOnUiThread(() -> showToast("Failed to retrieve academic planner"));
                }
            }
        });
    }

    private void parseAndDisplayAcademicPlanner(String responseBody) {
        try {
            JSONArray academicPlanArray = new JSONArray(responseBody);

            List<String> academicPlanList = new ArrayList<>();

            // Create a list to store courses and their semester numbers
            List<CourseInfo> courses = new ArrayList<>();

            // Parse courses and add them to the list
            for (int i = 0; i < academicPlanArray.length(); i++) {
                JSONObject academicPlanObject = academicPlanArray.getJSONObject(i);

                JSONObject course = academicPlanObject.getJSONObject("course");
                String courseCreditHours = course.getString("courseCreditHours");
                String departmentCode = course.getJSONObject("courseDepartment").getString("departmentCode");
                String courseName = course.getString("courseName");
                String courseNumber = course.getString("courseNumber");

                int semesterNumber = academicPlanObject.getInt("semesterNumber");

                // Create a CourseInfo object to store course information and semester number
                CourseInfo courseInfo = new CourseInfo(courseName, courseNumber, departmentCode, courseCreditHours, semesterNumber);

                courses.add(courseInfo);
            }

            // Sort the courses by semester number
            courses.sort((course1, course2) -> Integer.compare(course1.semesterNumber, course2.semesterNumber));

            // Construct course information strings and add them to the list
            for (CourseInfo courseInfo : courses) {
                String courseString = "Semester " + courseInfo.semesterNumber + ": " +
                        "Course: " + courseInfo.courseName + " (" + courseInfo.courseNumber + "), " +
                        "Department: " + courseInfo.departmentCode + ", " +
                        "Credit Hours: " + courseInfo.courseCreditHours;
                Log.d("TAG", "Course Info: " + courseString);
                academicPlanList.add(courseString);
            }

            runOnUiThread(() -> {
                adapter.clear();
                adapter.addAll(academicPlanList);
                adapter.notifyDataSetChanged();
            });
        } catch (JSONException e) {
            e.printStackTrace();
            runOnUiThread(() -> showToast("Failed to parse JSON response"));
        }
    }

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(AcademicPlannerActivity.this, message, Toast.LENGTH_SHORT).show());
    }

    // Class to store course information along with semester number
    private static class CourseInfo {
        String courseName;
        String courseNumber;
        String departmentCode;
        String courseCreditHours;
        int semesterNumber;

        public CourseInfo(String courseName, String courseNumber, String departmentCode, String courseCreditHours, int semesterNumber) {
            this.courseName = courseName;
            this.courseNumber = courseNumber;
            this.departmentCode = departmentCode;
            this.courseCreditHours = courseCreditHours;
            this.semesterNumber = semesterNumber;
        }
    }
}