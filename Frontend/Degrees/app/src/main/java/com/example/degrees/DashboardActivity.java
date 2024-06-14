package com.example.degrees;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * DashboardActivity serves as the central point of interaction for users when they navigate through the application's dashboard.
 * Extending {@link AppCompatActivity}, this activity ensures a consistent user experience across different versions of Android,
 */
public class DashboardActivity extends AppCompatActivity {
    View backgroundTextView ;

    String baseUrl() {
        return MainActivity.getBaseURL();
    }
    //tells the rest of the application what server to connect to
//the client is the whole app
    /**
     *  the method sends a request to the server for the background color and night mode setting
     */

    private void getRequest() {

        //the method is asking  the server for the background color and night mode setting
        //When the client receives the response from the server , it updates the view object and nightmode setting such that the changes are visisble

        // Request a string response from the provided URL.
        String url = baseUrl() + "/settings/" + getUserid() + "/userInterface";
        //ssettings is the API we agreed on
        //userInterface is the API
        //baseUrl is the server address(localhost:8080)
        //localhost-IP address (IP address locates which computer to connect to)
        //getUserid is getting the id from the dashboard activity object
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
         //JsonObjectRequest is an object that represents the http request
         //url is the path//
         //null because a GET request does not require a body
         //listenerObject is an object that represents what we do when we receive the response from the server

                new Response.Listener<JSONObject>() {
                    /**
                     * Handles JSON responses for network requests.
                     */
                    @Override
                    public void onResponse(JSONObject response) {
                        //volley framework calls the onresponse method
        //onResponse  is the method of the lIstener that the Volley framework calls when it receives the response
                        System.out.println("Get response is: "+ response);
                        try {
                            if (response.isNull("backgroundColor"))
                                return;
                            String bgColor = response.getString("backgroundColor");
                            String nightMode = response.getString("nightMode");
                            setBackgroundColor(Color.parseColor(bgColor));
                            if (nightMode.equals("followSystem"))
                                setDarkModeState(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                            else if(nightMode.equals("on"))
                                setDarkModeState(AppCompatDelegate.MODE_NIGHT_YES);
                            else if (nightMode.equals("off"))
                                setDarkModeState(AppCompatDelegate.MODE_NIGHT_NO);
                            else
                                throw new RuntimeException("server returned incorrect nightMode: " + nightMode);


                        } catch (JSONException e) {
                            //throw new RuntimeException(e);
                            System.out.println("Wrong!!!! " + e);
                        }
                        // Display the first 500 characters of the response string.
                        // String response can be converted to JSONObject via
                        // JSONObject object = new JSONObject(response);
//                        tvResponse.setText("Response is: "+ response);
                    }
                },
                new Response.ErrorListener() {
                    /**
                     * This method outputs an error
                     * @param error
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        tvResponse.setText("That didn't work!" + error.toString());
                        System.out.println("That didn't work!" + error.toString());
                    }
                }){

            @Override
            //getHeaders method is an method of the request object
            //tells the volley framework what HEADERS we want in our request
            //Volley framework handles the http connection to the server
            //Handles all the requests and all the responses
/**
 * getHeaders method is an method that tells the volley framework what HEADERS we want in our request
 * @return A map of HTTP header field names to their values.
 * @throws AuthFailureError if authentication fails while attempting to retrieve the headers.
 */
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
                //                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            /**
             * This method tells Volley what request parameters we have             *
             */
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //                params.put("param1", "value1");
                //                params.put("param2", "value2");
                return params;
            }

        };
//        System.out.println("Response is: "+ "jjjjj");

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
//VolleySingleton.getInstance(getApplicationContext()) creates an instance of Volley. We had initially created our own object and we had not given it to Volley
    /**
     * putRequest()  updates the background color and night mode setting and it  sends a PUT request for the server to update the changes
     */
    private void putRequest(String color, String nightMode) {
        if (color != null)
        setBackgroundColor(Color.parseColor(color));
        else
            setBackgroundColor(Color.BLUE);
        if (nightMode.equals("followSystem"))
            setDarkModeState(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        else if(nightMode.equals("on"))
            setDarkModeState(AppCompatDelegate.MODE_NIGHT_YES);
        else if (nightMode.equals("off"))
            setDarkModeState(AppCompatDelegate.MODE_NIGHT_NO);
        else assert false;

        // Request a string response from the provided URL.
        String url = baseUrl() + "/settings/" + getUserid() + "/userInterface";

        JSONObject body = new JSONObject();
        try {
            body.put("backgroundColor", color);
            body.put("nightMode", nightMode);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.PUT, url, body,
 new Response.Listener<JSONObject>() {
                                   @Override
                    public void onResponse(JSONObject response) {

                        // Display the first 500 characters of the response string.
                        // String response can be converted to JSONObject via
                        // JSONObject object = new JSONObject(response);
//                        tvResponse.setText("Response is: "+ response);
                        System.out.println("Put response is: "+ response);
                    }
                },
                new Response.ErrorListener() {
                    /**
                     * This method is called when there is an error during the network request.
                     * @param error The error object containing details about what went wrong.
                     * @see VolleyError
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        tvResponse.setText("That didn't work!" + error.toString());
                        System.out.println("That didn't work!" + error.toString());
                    }
                }){

            /**
             * Retrieves the HTTP headers required for making a network request.
             * <p>
             * This method is used to add any custom headers that are required for
             * making a network request. In this implementation, the method returns
             * an empty set of headers, but it can be modified to include necessary
             * headers such as "Authorization" for authentication purposes, and
             * "Content-Type" to specify the content type of the request.
             * </p>
             *
             * @return a map of HTTP header field names to their respective values.
             * @throws AuthFailureError if there is an authentication failure while
             *         attempting to retrieve the headers.
             *
             * @see AuthFailureError
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                //                headers.put("Authorization", "Bearer YOUR_ACCESS_TOKEN");
                //                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                //                params.put("param1", "value1");
                //                params.put("param2", "value2");
                return params;
            }

        };
//        System.out.println("Response is: "+ "jjjjj");

        // Adding request to request queue
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private static long userid = 7;

    private static Long isuRegistrationId = (long)19;

    /**
     * this method returns the userid
     * @return userid
     */
    public static long getUserid(){
        return userid;
    }

    /**
     * This method returns isuRegistrationId
     * @return isuRegistrationId
     */
    public static Long getIsuRegistrationId() { return isuRegistrationId; }

    /**
     * Sets the user ID.
     * This method updates the user ID of the current object with the provided value.
     * @param userid the new user ID to be set.
     */
    public void setUserid(long userid) {
        this.userid = userid;
    }

    /**
     * Indicates whether an item, event, or condition has appeared before.
     */
    public boolean hasAppearedBefore = false;
    private int backgroundColor;
    private int darkModeState;

    private void setBackgroundColor(int backgroundColor) {
        this.backgroundTextView.setBackgroundColor(backgroundColor);
        this.backgroundColor = backgroundColor;
    }
    private int getBackgroundColor() {
        return this.backgroundColor;
    }
    private void setDarkModeState(int darkModeState) {
        AppCompatDelegate.setDefaultNightMode(darkModeState);
        this.darkModeState = darkModeState;
    }
    private int getDarkModeState() {
        return this.darkModeState;
    }
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        //onSaveInstanceStance method is called when Android is about to destroy our dashboard activity object such the screen settings do not change
        // Before object is destroyed, Android sends us a savedInstanceState object which we use to store information that we can read when the object is recreated
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("hasAppearedBefore", hasAppearedBefore);
        savedInstanceState.putInt("backgroundColor", backgroundColor);
        savedInstanceState.putInt("darkModeState", darkModeState);
        System.out.println("Saving instance state: " + savedInstanceState);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//this method is called when Android recreates our object
        super.onRestoreInstanceState(savedInstanceState);
        System.out.println("Restoring instance state: " + savedInstanceState);
        if (savedInstanceState.containsKey("hasAppearedBefore")) {
            setBackgroundColor(savedInstanceState.getInt("backgroundColor"));
            setDarkModeState(savedInstanceState.getInt("darkModeState"));
            hasAppearedBefore = savedInstanceState.getBoolean("hasAppearedBefore");
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //onCreate is called whenever an activity object is created
        // it is used for initialization
        super.onCreate(savedInstanceState);
        setUserid(getIntent().getLongExtra("userId", getUserid()));

        VolleySingleton.getInstance(getApplicationContext())
                .addToRequestQueue(new JsonObjectRequest(
                        baseUrl() + "/users/" + getUserid(),
                        (JSONObject obj) -> {
                            try {
                                JSONObject isuRegistration = obj.getJSONObject("isuRegistration");
                                if (isuRegistration == null) return;
                                this.isuRegistrationId = isuRegistration.getLong("universityId");
                                System.out.println("ISURegistration ID: " + isuRegistrationId);

                                if (!isuRegistration.isNull("student")) {

                                }
                                if (!isuRegistration.isNull("advisor")) {

                                }
                                if (!isuRegistration.isNull("instructor")) {

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        (VolleyError err) -> {
                            err.printStackTrace();
                        }
                ));

        System.out.println(getUserid());
        setContentView(R.layout.activity_dashboard);

        // Initialize TextViews
        TextView CourseRatingsTextView = findViewById(R.id.textViewCourseRatings);
        TextView coursesTextView = findViewById(R.id.textViewCourses);
        TextView advisorsInstructorsTextView = findViewById(R.id.textViewAdvisorsInstructors);
        TextView toDoListTextView = findViewById(R.id.textViewToDoList);
        TextView degreeFlowchartTextView = findViewById(R.id.textViewDegreeFlowchart);
        TextView schedulerTextView = findViewById(R.id.textViewScheduler);

        TextView advisorAppointmentTextView = findViewById(R.id.textAdvisorAppointments);
        TextView courseSectionTextView = findViewById(R.id.textCourseSectionActivity);
        TextView majorMinorTextView = findViewById(R.id.textMajorMinor);



        TextView BookAppointmentTextView = findViewById(R.id.bookAppointment);
        TextView ListUsers = findViewById(R.id.manageUsers);
        TextView AuditDegree= findViewById(R.id.auditDegree);

        View settingsButtonView = findViewById(R.id.settingsButton);
        this.backgroundTextView = findViewById(R.id.background);
//        backgroundTextView.setBackgroundColor(Color.parseColor("#FFA500"));
        if (savedInstanceState != null && savedInstanceState.containsKey("hasAppearedBefore")) {
            this.hasAppearedBefore = savedInstanceState.getBoolean("hasAppearedBefore");
        }
        if (!hasAppearedBefore) {
            System.out.println("onCreate called");
            this.getRequest();
            this.hasAppearedBefore = true;
        }
        findViewById(R.id.bookAppointment).setOnClickListener(view -> {
            startActivity(new Intent(DashboardActivity.this, bookAppointment.class));
        });

        findViewById(R.id.manageUsers).setOnClickListener(view -> {
            startActivity(new Intent(DashboardActivity.this, list_Users.class));
        });
        findViewById(R.id.auditDegree).setOnClickListener(view -> {
            startActivity(new Intent(DashboardActivity.this, Degree_Audit.class));
        });

        findViewById(R.id.fourYearPlanner).setOnClickListener(view -> {
            startActivity(new Intent(DashboardActivity.this, AcademicPlannerActivity.class));
        });

        findViewById(R.id.textAdvisorAppointments).setOnClickListener(view -> {
            startActivity(new Intent(DashboardActivity.this, advisorAppointmentView.class));
        });

        findViewById(R.id.textCourseSectionActivity).setOnClickListener(view -> {
            startActivity(new Intent(DashboardActivity.this, CourseSectionActivity.class));
        });

        findViewById(R.id.textMajorMinor).setOnClickListener(view -> {
            startActivity(new Intent(DashboardActivity.this, MajorMinorActivity.class));
        });

        // Set click listeners for each category
        CourseRatingsTextView.setOnClickListener(new View.OnClickListener() {
            /**
             * This method is invoked when a view associated with this click listener is clicked.             *
             * @param view The view that was clicked.
             */

            @Override
            public void onClick(View view) {
                // Handle Personal Account click
                startActivity(new Intent(DashboardActivity.this, courseRatings.class));
            }
        });

        coursesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle Courses click
                startActivity(new Intent(DashboardActivity.this, courseActivity.class));
            }
        });

        advisorsInstructorsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle Advisors and Instructors click
                startActivity(new Intent(DashboardActivity.this, Chat_Bot.class));
            }
        });

        toDoListTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle To-Do List click
                startActivity(new Intent(DashboardActivity.this, AdvisorChatActivity.class));
            }
        });

        degreeFlowchartTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle Degree Flowchart click
                startActivity(new Intent(DashboardActivity.this, degreeflowchart.class));
            }
        });

            schedulerTextView.setOnClickListener(new View.OnClickListener() {
                /**
                 *
                 * @param view The method is called when a view is clicked.
                 */
                @Override
                public void onClick(View view) {
                    // Handle Scheduler click
                    startActivity(new Intent(DashboardActivity.this, SchedulerActivity.class));
                }
            });

/**
 * Responds to click events on the settings button.
 * <p>
 * This method is called when the settings button is clicked. In response,
 * it displays the settings popup by calling {@link #showSettingsPopup()}.
 * </p>
 *
 * @param view The view that was clicked, which should be the settings button in this context.
 */
        settingsButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onClick() makes sure that when we click the settings button, it takes us to the showPopUp
                // Handle settings button click
                showSettingsPopup();
            }
        });
    }
    /**
     * Displays a popup dialog that allows the user to change settings for the dashboard.
     * The popup features two dropdown menus (Spinners) for the user to select from predefined options.
     * <p>The method sets up an AlertDialog.Builder for creating the settings popup, with
     * the title "Change Dashboard Settings".
     */
    public void showSettingsPopup() {
        final String POPUP_LOGIN_TITLE="Change Dashboard Settings";
//        final String POPUP_LOGIN_TEXT="Please fill in your credentials";

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(POPUP_LOGIN_TITLE);
//        alert.setMessage(POPUP_LOGIN_TEXT);

        // Set an EditText view to get user input
        final Spinner color = new Spinner(this);
        //A spinner is drop-down menu
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"red", "orange", "yellow", "green", "blue"});
        color.setAdapter(spinnerArrayAdapter);
        final Spinner theme = new Spinner(this);
        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"darkMode", "lightMode", "sync with system"});
        theme.setAdapter(spinnerArrayAdapter2);
        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(color);
        layout.addView(theme);
        alert.setView(layout);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            /**
             * Handles click events on the settings popup dialog, applying the selected color and theme settings.
             * @param dialog the dialog interface
             * @param whichButton the button that was clicked
             */


            public void onClick(DialogInterface dialog, int whichButton) {
                String colorName = color.getSelectedItem().toString();
                String themeName = theme.getSelectedItem().toString();
                String theme = null;
                String colorValue = null;

                if (colorName.equals("red") )
                    colorValue = "#FFAAAA";
                else if (colorName.equals("green") )
                    colorValue = "#008000";
                else if (colorName.equals("yellow") )
                    colorValue = "#FFFF00";

                else if  (colorName.equals("orange") )
                    colorValue = "#FFA500";
                else if (colorName.equals("blue"))
                    colorValue = null;
                else assert false;

                if (themeName.equals("darkMode") )
                    theme = "on";
                else if (themeName.equals("lightMode") )
                    theme = "off";
                else if (themeName.equals("sync with system"))
                    theme = "followSystem";
                else
                    assert false;
                // Do something with value!
                putRequest(colorValue, theme);
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
