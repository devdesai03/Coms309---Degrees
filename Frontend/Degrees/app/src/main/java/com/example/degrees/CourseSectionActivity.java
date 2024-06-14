package com.example.degrees;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
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

public class CourseSectionActivity extends AppCompatActivity {
    private List<CourseSection> courseSections;
    private RecyclerView recyclerView;
    private CourseSectionAdapter adapter;

    private OkHttpClient okHttpClient = new OkHttpClient();
    private static final String API_URL = "http://coms-309-036.class.las.iastate.edu:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_sections);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseSections = new ArrayList<>();
        adapter = new CourseSectionAdapter(courseSections);
        recyclerView.setAdapter(adapter);

        // Fetch course sections from the API
        fetchCourseSections();
    }

    public long instructorId(){
        //WILL CHANGE THIS LATER
        return 50;
    }

    private void fetchCourseSections() {
        Request request = new Request.Builder()
                .url(API_URL + "/course-sections-by-instructor/" + instructorId())
                .get()
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                // Handle failure, show an error message, etc.
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseBody = response.body().string();
                    Log.d("TAG", "Response: " + responseBody);

                    // Parse the JSON response and update UI with course sections
                    List<CourseSection> sections = parseCourseSectionsResponse(responseBody);

                    // Update the courseSections list
                    courseSections.clear();
                    courseSections.addAll(sections);

                    runOnUiThread(() -> {
                        adapter.notifyDataSetChanged();
                        Log.d("TAG", "Data Set Changed. Number of sections: " + courseSections.size());
                    });
                } else {
                    Log.d("TAG", "Server Error in CourseSections");
                }
            }
        });
    }

    // Helper method to parse JSON response for course sections
    private List<CourseSection> parseCourseSectionsResponse(String response) {
        List<CourseSection> courseSections = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                // Parse the JSON object to create CourseSection objects
                // ...

                // Example: (you need to adapt this based on your JSON structure)
                long id = jsonObject.getLong("id");
                String courseShortName = jsonObject.getJSONObject("course")
                        .getJSONObject("courseDepartment").getString("departmentCode")
                        + " " + jsonObject.getJSONObject("course").getString("courseNumber");
                String courseLongName = jsonObject.getJSONObject("course").getString("courseName");
                String semester = jsonObject.getString("semester");
                String sectionIdentifier = jsonObject.getString("sectionIdentifier");
                String location = jsonObject.getString("location");
                String startDate = jsonObject.getString("startDate");
                String endDate = jsonObject.getString("endDate");
                String daysOfWeek = jsonObject.getString("daysOfWeek");
                String startTime = jsonObject.getString("startTime");
                String endTime = jsonObject.getString("endTime");

                CourseSection courseSection = new CourseSection(id, courseShortName, courseLongName, semester,
                        sectionIdentifier, location, startDate, endDate, daysOfWeek, startTime, endTime);
                courseSections.add(courseSection);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return courseSections;
    }

    // Define your CourseSection and Student classes based on the API response structure
    // ...

    public class CourseSectionAdapter extends RecyclerView.Adapter<CourseSectionActivity.CourseSectionAdapter.ViewHolder> {
        private final List<CourseSection> courseSections;

        public CourseSectionAdapter(List<CourseSection> courseSections) {
            this.courseSections = courseSections;
        }

        @NonNull
        @Override
        public CourseSectionActivity.CourseSectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_section, parent, false);
            return new CourseSectionActivity.CourseSectionAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CourseSectionActivity.CourseSectionAdapter.ViewHolder holder, int position) {
            CourseSection courseSection = courseSections.get(position);
            holder.bind(courseSection);
        }

        @Override
        public int getItemCount() {
            return courseSections.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView courseNameTextView;
            private TextView sectionIdentifierTextView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                courseNameTextView = itemView.findViewById(R.id.courseNameTextView);
                sectionIdentifierTextView = itemView.findViewById(R.id.sectionIdentifierTextView);
            }

            public void bind(CourseSection courseSection) {
                courseNameTextView.setText(courseSection.getCourseShortName() + " - " + courseSection.getCourseLongName());
                sectionIdentifierTextView.setText("Section" + " : " + courseSection.getSectionIdentifier());

                // Handle item click, e.g., fetch and display students for this course section
                    itemView.setOnClickListener(v -> {
                        startGradingActivity((Long) courseSection.getId());
                    });
                };
            }
        }
        private void startGradingActivity(long sectionId) {
            // Create an Intent to start the GradingActivity
            Intent intent = new Intent(this, GradingActivity.class);

            // Pass any necessary data to the GradingActivity using Intent extras
            intent.putExtra("sectionId", sectionId);

            // Start the GradingActivity
            startActivity(intent);
        }
    }

