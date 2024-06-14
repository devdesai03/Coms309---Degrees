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

public class GradingActivity extends AppCompatActivity {
    private Long sectionId;
    private List<Student> students;
    private RecyclerView recyclerView;
    private GradingActivityAdapter adapter;

    private OkHttpClient okHttpClient = new OkHttpClient();
    private static final String BASE_URL = "http://coms-309-036.class.las.iastate.edu:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grading);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        students = new ArrayList<>();
        adapter = new GradingActivityAdapter(students);
        recyclerView.setAdapter(adapter);
        sectionId = getIntent().getLongExtra("sectionId", -1);

        // Fetch course sections from the API
        fetchStudentsForCourseSection();
    }

    public long instructorId() {
        //WILL CHANGE THIS LATER
        return 50;
    }

    private void fetchStudentsForCourseSection() {
        Request request = new Request.Builder()
                .url(BASE_URL + "course-registration-instructor/" + instructorId())
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
                    List<Student> aStudents = parseCourseSectionsResponse(responseBody);
                    System.out.println(responseBody);

                    // Update the courseSections list
                    students.clear();
                    students.addAll(aStudents);

                    runOnUiThread(() -> {
                        adapter.notifyDataSetChanged();
                        Log.d("TAG", "Data Set Changed. Number of students: " + students.size());
                    });
                } else {
                    Log.d("TAG", "Server Error in StudentsinCourseSection");
                }
            }
        });
    }

    // Helper method to parse JSON response for course sections
// Helper method to parse JSON response for course sections
    private List<Student> parseCourseSectionsResponse(String response) {
        List<Student> students = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                if (jsonObject.getJSONObject("section").getLong("id") != sectionId) {
                    continue;
                }

                // Accessing student -> isuRegistration -> netId
                JSONObject studentObject = jsonObject.optJSONObject("student");
                if (studentObject != null) {
                    JSONObject isuRegistrationObject = studentObject.optJSONObject("isuRegistration");
                    if (isuRegistrationObject != null) {
                        String netId = isuRegistrationObject.getString("netId");
                        long universityId = isuRegistrationObject.getLong("universityId");
                        String fullName = isuRegistrationObject.getString("fullName");
                        String grade = jsonObject.getString("grade");

                        // Create a Student object with the extracted information
                        Student student = new Student(universityId, netId, fullName, grade);
                        students.add(student);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return students;
    }

    public class GradingActivityAdapter extends RecyclerView.Adapter<GradingActivityAdapter.ViewHolder> {
        private final List<Student> students;

        public GradingActivityAdapter(List<Student> students) {
            this.students = students;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_section, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Student studenting = students.get(position);
            holder.bind(studenting);
        }

        @Override
        public int getItemCount() {
            return students.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView studentNameTextView;
            private TextView gradeIdentifierTextView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                studentNameTextView = itemView.findViewById(R.id.courseNameTextView);
                gradeIdentifierTextView = itemView.findViewById(R.id.sectionIdentifierTextView);
            }

            public void bind(Student student) {
                studentNameTextView.setText("Name: " + student.getFullName());
                gradeIdentifierTextView.setText("ID: " + student.universityId() + "\n"
                        + "NetID: " + student.netId() + "\n"
                        + "Grade: " + student.getGrade());

                // Handle item click, e.g., fetch and display students for this course section
                itemView.setOnClickListener(v -> {
                    startDetailedGradingActivity(student);
                });
            }

            private void startDetailedGradingActivity(Student student) {
                // Create an Intent to start the DetailedGradingActivity
                Intent intent = new Intent(itemView.getContext(), DetailedGradingActivity.class);

                // Pass any necessary data to the DetailedGradingActivity using Intent extras
                intent.putExtra("grade", student.getGrade());
                intent.putExtra("universityId", student.universityId());
                intent.putExtra("sectionId", sectionId);

                // Start the DetailedGradingActivity
                itemView.getContext().startActivity(intent);
            }
        }
    }
}
