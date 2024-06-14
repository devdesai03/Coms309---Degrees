package com.example.degrees;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class enables the user to rate a class they have a taken and leave a comment about the class experience
 */
public class courseRatings extends AppCompatActivity {

    TextView rateCount, showRating;
    EditText review;
    Button submit;
    RatingBar ratingBar;
    float rateValue; String temp;


    private Long courseId = Long.valueOf(12);

    /**
     * This method sets the courseId
     * @param courseId
     */
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_ratings);

        if (getIntent().hasExtra("courseId")) {
            setCourseId(getIntent().getLongExtra("courseId", -1));
        }

        rateCount = findViewById(R.id.rateCount);
        ratingBar =findViewById(R.id.ratingBar);
        review =findViewById(R.id.review);
        submit = findViewById(R.id.submitBtn);
        showRating =findViewById(R.id.showRating);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                /**
                 * Callback method to be invoked when the rating of a RatingBar changes.
                 * It provides feedback to the user on the selected rating by categorizing the rating value
                 * into predefined ranges like "Worst", "Bad", "Average", "Good", and "Excellent".
                 */
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rateValue = ratingBar.getRating();

                if(rateValue <= 1 && rateValue > 0)
                    rateCount.setText("Worst " + rateValue +"/5");
                else if(rateValue <= 2 && rateValue > 1)
                    rateCount.setText("Bad " + rateValue +"/5");
                else if(rateValue <= 3 && rateValue > 2)
                    rateCount.setText("Average " + rateValue +"/5");
                else if(rateValue <= 4 && rateValue > 3)
                    rateCount.setText("Good " + rateValue +"/5");
                else if(rateValue <= 5 && rateValue > 4)
                    rateCount.setText("Excellent " + rateValue +"/5");
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {

            /**
             * Handles click events on views that this listener has been assigned to.
             */
            @Override
            public void onClick(View v) {
                try {
                    JSONObject body = new JSONObject();
                    JSONObject id = new JSONObject();
                    JSONObject student = new JSONObject();
                    student.put("universityId", DashboardActivity.getIsuRegistrationId());
                    id.put("student", student);
                    JSONObject course = new JSONObject();
                    course.put("courseId", courseId);
                    id.put("course", course);
                    body.put("id", id);
                    body.put("rating", (int)rateValue);
                    body.put("reviewText", review.getText());
                    VolleySingleton.getInstance(getApplicationContext())
                            .addToRequestQueue(new JsonObjectRequest(
                                    Request.Method.POST,
                                    MainActivity.getBaseURL() + "/courseratings",
                                    body,
                                    (JSONObject response) -> {
                                        System.out.println("Succeeded");
                                    },
                                    (VolleyError error) -> {
                                        error.printStackTrace();
                                    }
                            ));

                    Intent intent = new Intent(courseRatings.this, RatingDisplayActivity.class);
                    intent.putExtra("courseId", courseId);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

               temp = rateCount.getText().toString();
               showRating.setText("Your Rating: \n" + temp + "\n" + review.getText());
               review.setText("");
               ratingBar.setRating(0);
               rateCount.setText("");
            }
        });
    }
}