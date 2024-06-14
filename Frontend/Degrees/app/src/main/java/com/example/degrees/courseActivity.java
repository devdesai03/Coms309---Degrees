package com.example.degrees;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * This class displays popular courses at ISU and a sttudent is able to  add/drop courses
 */

public class courseActivity extends AppCompatActivity {

    /**
     * Retrieves the base URL for the application.     *
     * This method calls the static method {@code getBaseURL()} from the {@code MainActivity} class
     * to get the base URL used throughout the application for network requests.     *
     * @return A {@code String} representing the base URL.
     */

    public String baseUrl() {
        return MainActivity.getBaseURL();
    }

    private Long studentId = Long.valueOf(18);

    Spinner semesterSelect;

    /**
     * This method sets the studentId
     * @param studentId
     */
    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        View courseButton = findViewById(R.id.codingCourses);

        semesterSelect = findViewById(R.id.semesterSelect);
        LocalDate currentDate = LocalDate.now();
        String currentSemester;
        String nextSemester;
        if (currentDate.getMonthValue() < 6) {
            currentSemester = "Spring " + currentDate.getYear();
            nextSemester = "Fall " + (currentDate.getYear());
        } else {
            currentSemester = "Fall " + currentDate.getYear();
            nextSemester = "Spring " + (currentDate.getYear() + 1);
        }
        semesterSelect.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{
                        currentSemester, nextSemester
                }));
//        long courseId = 123456;

//        courseButton.setOnClickListener(
//            (View view) -> {
//                showCoursePop(courseId);
//            }
//        );

//        String jsonText = courseActivity_TEST.Companion.getTEST_ARRAY();
//        JSONArray array;
//        try {
//            array = new JSONArray(jsonText);
//        } catch (JSONException e) {
//            throw new AssertionError("Bad JSON", e);
//        }
//
//        generateCourseButtons(array);

        String url = "/courses/";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, baseUrl() + url, null,
                (JSONArray response) -> {
                    generateCourseButtons(response);
                },
                (VolleyError error) -> {
                    System.out.println("The error is: " + error);
                }
        );
        // Do something with value!
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    /**
     * This method delete course buttons
     */

    void deleteCourseButtons() {
        ViewGroup courseListView = findViewById(R.id.courseList);
        courseListView.removeAllViews();
    }

    /**
     * This method generates the buttons automatically
     * @param courseList
     */
    void generateCourseButtons(JSONArray courseList) {
        deleteCourseButtons();

        GridLayout courseListView = findViewById(R.id.courseList);

        try {
            // TODO
            for (int i = 0; i < courseList.length(); i++) {
                JSONObject course = courseList.getJSONObject(i);
                Context context = getApplicationContext();

                long courseId = course.getLong("courseId");

//                CourseButton courseButton = CourseButton.newInstance(
//                        course.getString("courseName"),
//                        course.getJSONObject("courseDepartment")
//                                .getString("departmentCode")
//                                + " " + course.getString("courseNumber"),
//                        courseId,
////                        course.getString("courseImageSrc")
//                        null
//                );

//                View courseView = courseButton.getView();

                CardView courseView = (CardView)LayoutInflater.from(context).inflate(R.layout.fragment_course_button, null);
                TextView courseTitleText = (TextView)courseView.findViewById(R.id.courseTitleText);
                courseTitleText.setText(course.getJSONObject("courseDepartment")
                                .getString("departmentCode")
                                + " " + course.getString("courseNumber"));

                courseView.setOnClickListener(
                        (View view) -> {
                            PopupMenu menu = new PopupMenu(this, view);
                            menu.getMenu().add("Add/Drop Course");
                            menu.getMenu().add("Rate Course");
                            menu.getMenu().add("View Ratings");
                            menu.setOnMenuItemClickListener((MenuItem item) -> {
                                switch (item.getTitle().toString()) {
                                    default -> throw new AssertionError("unreachable");
                                    case "Add/Drop Course" -> {
                                        showCoursePop(courseId);
                                    }
                                    case "Rate Course" -> {
                                        Intent intent = new Intent(courseActivity.this, courseRatings.class);
                                        intent.putExtra("courseId", courseId);
                                        startActivity(intent);
                                    }
                                    case "View Ratings" -> {
                                        Intent intent = new Intent(courseActivity.this, RatingDisplayActivity.class);
                                        intent.putExtra("courseId", courseId);
                                        startActivity(intent);
                                    }
                                }
                                return true;
                            });
                            menu.show();
                        }
                );

                courseListView.setRowCount(i / 2 + 1);
//                GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams)courseView.getLayoutParams();
//                layoutParams.rowSpec = GridLayout.spec(i / 2);
//                layoutParams.columnSpec = GridLayout.spec(i % 2);
                courseListView.addView(courseView/*, layoutParams*/);
            }
        } catch (JSONException jsonException) {
            System.out.println("Error parsing JSON" + jsonException.toString());
        }
    }

    /**
     * Displays a popup dialog that allows a user to add, drop, or waitlist a course.
     * This method initializes an {@code AlertDialog} with a custom title. It presents a
     * {@code Spinner} to the user with options to "Add Course," "Drop Course," or "Waitlist."
     * @param courseId
     */

    public void showCoursePop(long courseId) {
        final String POPUP_LOGIN_TITLE="Add/Drop Courses";
//        final String POPUP_LOGIN_TEXT="Please fill in your credentials";

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(POPUP_LOGIN_TITLE);
//        alert.setMessage(POPUP_LOGIN_TEXT);

        // Set an EditText view to get user input
        final Spinner color = new Spinner(this);
        //A spinner is drop-down menu
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Add Course", "Drop Course", "Waitlist"});
        color.setAdapter(spinnerArrayAdapter);
        final Spinner theme = new Spinner(this);

        getSections: {
            var request = new JsonArrayRequest(
                    Request.Method.GET,
                    baseUrl() + "/courses/" + courseId + "/courseSections/?semester=" +
                            URLEncoder.encode((String)semesterSelect.getSelectedItem()),
                    null,
                    (response) -> {
                        try {
                            ArrayList<JSONObject> sections = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                sections.add(response.getJSONObject(i));
                            }
                            theme.setAdapter(new ArrayAdapter<JSONObject>(this,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    sections) {
                                String convertToString(JSONObject section) {
                                    try {
                                        return "Section " + section.getString("sectionIdentifier");
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                }

                                @Override
                                public View getView(int position, View convertView, ViewGroup parent) {
                                    TextView view = (TextView) super.getView(position, convertView, parent);
                                    JSONObject section = getItem(position);
                                    view.setText(convertToString(section));
                                    return view;
                                }

                                @Override
                                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                    TextView view = (TextView) super.getDropDownView(position, convertView, parent);
                                    JSONObject section = getItem(position);
                                    view.setText(convertToString(section));
                                    return view;
                                }
                            });
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    (error) -> {
                        error.printStackTrace();
                    }
            );
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
        }

        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(color);
        layout.addView(theme);
        alert.setView(layout);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
                try {
                    String action = color.getSelectedItem().toString();
                    String baseUrl = MainActivity.getBaseURL();

                    Long sectionId = ((JSONObject)theme.getSelectedItem()).getLong("id");

                    JsonObjectRequest request;
                    if (action.equals("Add Course")) {
                        request = new JsonObjectRequest(Request.Method.PUT,
                                baseUrl + "/course-section/" + studentId + "/" + sectionId, null,
                                (JSONObject response) -> {
                                    System.out.println("The response is: " + response);
                                },
                                (VolleyError error) -> {
                                    System.out.println("The error is: " + error);
                                }
                        );
                    } else if (action.equals("Drop Course")) {
                        request = new JsonObjectRequest(Request.Method.DELETE,
                                baseUrl + "/drop-registered-course-section/" + studentId + "/" + sectionId,
                                null,
                                (JSONObject response) -> {
                                    System.out.println("The response is: " + response);
                                },
                                (VolleyError error) -> {
                                    System.out.println("The error is: " + error);
                                }
                                );
                    } else if (action.equals("Waitlist")) {
                        throw new AssertionError("Not implemented yet!");
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
