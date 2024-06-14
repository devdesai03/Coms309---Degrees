package com.example.degrees;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles the flowchart of a degree and course interactions.
 */
public class degreeflowchart extends AppCompatActivity {
    /**
     * Retrieves the base URL for API requests.
     * @return The base URL for API requests
     */
    public String baseUrl() {
        return MainActivity.getBaseURL();
    }

    private Long studentId = Long.valueOf(18);
    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
    private Long degreeId = Long.valueOf(11);
    public void setDegreeId(Long degreeId) {
        this.degreeId = degreeId;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_degreeflowchart);

        String url = "/degrees/" + degreeId + "/flowchart?studentId=" + studentId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, baseUrl() + url, null,
                (JSONObject response) -> {
                    try {
                        loadDegreeFlowchart(response);
                    } catch (JSONException e) {
                        e.printStackTrace(System.out);
                    }
                },
                (VolleyError error) -> {
                    System.out.println("The error is: " + error);
                }
        );
        // Do something with value!
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);


//        try {
//            JSONObject data = new JSONObject(courseActivity_TEST.Companion.getTEST_FLOWCHART());
//            loadDegreeFlowchart(data);
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }
    }

    /**
     * Loads the degree flowchart data and displays it on the screen.
     * @param data The JSON data representing the degree flowchart
     * @throws JSONException if there's an issue parsing the JSON data
     */
    public void loadDegreeFlowchart(JSONObject data) throws JSONException {
        Context context = getApplicationContext();
        LinearLayout semesterContainer = findViewById(R.id.semesterContainer);

        JSONArray semesters = data.getJSONArray("semesters");
        for (int semesterIdx = 0; semesterIdx < semesters.length(); semesterIdx++) {
            JSONObject semester = semesters.getJSONObject(semesterIdx);

            int semesterNumber = semester.getInt("semesterNumber");
            JSONArray requirementGroups = semester.getJSONArray("requirementGroups");

            LinearLayout requirementGroupContainer = new LinearLayout(context);

            TextView semesterNumberText = new TextView(context);
            semesterNumberText.setText("" + semesterNumber);
            requirementGroupContainer.addView(semesterNumberText);

            for (int requirementGroupIdx = 0; requirementGroupIdx < requirementGroups.length(); requirementGroupIdx++) {
                JSONObject requirementGroup = requirementGroups.getJSONObject(requirementGroupIdx);

                CardView courseView = (CardView) LayoutInflater.from(context).inflate(R.layout.fragment_course_button, null);
                TextView courseTitleText = (TextView)courseView.findViewById(R.id.courseTitleText);
                courseTitleText.setText(requirementGroup.getString("name"));

                courseView.setOnClickListener(view -> {
                    try {
                        showCoursePop(requirementGroup);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });

                requirementGroupContainer.addView(courseView);
            }

            semesterContainer.addView(requirementGroupContainer);
        }
    }

    /**
     * Displays a pop-up for managing courses in the flowchart.
     * @param requirementGroup The JSON data representing a group of course requirements
     * @throws JSONException if there's an issue parsing the JSON data
     */
    public void showCoursePop(JSONObject requirementGroup) throws JSONException {
        final String POPUP_LOGIN_TITLE="Add to Flowchart";
//        final String POPUP_LOGIN_TEXT="Please fill in your credentials";

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(POPUP_LOGIN_TITLE);
//        alert.setMessage(POPUP_LOGIN_TEXT);

        // Set an EditText view to get user input
        final Spinner color = new Spinner(this);
        //A spinner is drop-down menu
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Add to Flowchart", "Remove from Flowchart"});
        color.setAdapter(spinnerArrayAdapter);
        final Spinner semester = new Spinner(this);
        ArrayAdapter<String> semesterArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Semester 1", "Semester 2", "Semester 3",
                "Semester 4", "Semester 5", "Semester 6", "Semester 7",
                "Semester 8"});
        semester.setAdapter(semesterArrayAdapter);
        final Spinner theme = new Spinner(this);
        System.out.println(requirementGroup);
        JSONArray courseArray = requirementGroup.getJSONArray("fulfillments");
        ArrayList<String> courseNameArray = new ArrayList<>();
        Map<String, Long> courseNameToIdMap = new HashMap<>();
        for (int i = 0; i < courseArray.length(); i++) {
            JSONObject course = courseArray.getJSONObject(i).getJSONObject("course");
            String courseString = course.getJSONObject("courseDepartment")
                            .getString("departmentCode") + " "
                            + course.getString("courseNumber");
            courseNameToIdMap.put(courseString, course.getLong("courseId"));
            courseNameArray.add(courseString);
            System.out.println(courseString);
        }
        ArrayAdapter<String> courseArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                courseNameArray.toArray(new String[courseNameArray.size()]));
        theme.setAdapter(courseArrayAdapter);



        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(color);
        layout.addView(theme);
        layout.addView(semester);
        alert.setView(layout);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
                Long courseId = courseNameToIdMap.get(theme.getSelectedItem().toString());
                Integer semesterNum = Integer.valueOf(semester.getSelectedItem().toString().substring("Semester ".length()));

                try {
                    String action = color.getSelectedItem().toString();
                    String baseUrl = MainActivity.getBaseURL();

                    Request request;
                    if (action.equals("Add to Flowchart")) {
                        JSONObject body = new JSONObject();
                        JSONObject id = new JSONObject();
                        JSONObject course = new JSONObject();
                        JSONObject student = new JSONObject();
                        course.put("courseId", courseId);
                        student.put("universityId", studentId);
                        student.put("majors", new JSONArray());
                        student.put("minors", new JSONArray());
                        id.put("course", course);
                        id.put("student", student);
                        body.put("id", id);
                        body.put("semesterNumber", semesterNum);

                        request = new JsonObjectRequest(Request.Method.POST,
                                baseUrl + "/hypothetical-course-registrations/",
                                body,
                                (JSONObject response) -> {
                                    System.out.println("The response is Added to flowchart: " + response);
                                },
                                (VolleyError error) -> {
                                    System.out.println("The error is: " + error);
                                }
                        );
                    } else if (action.equals("Remove from Flowchart")) {
                        request = new StringRequest(Request.Method.DELETE,
                                baseUrl + "/hypothetical-course-registrations/" + courseId + "/" + studentId,
                                (String response) -> {
                                    System.out.println("The response is Removed from Flowchart: " + response);
                                },
                                (VolleyError error) -> {
                                    System.out.println("The error is: " + error);
                                }
                        );
                    } else throw new AssertionError();

                    // Do something with value!
                    VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }
}