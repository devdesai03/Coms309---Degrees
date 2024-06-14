package com.example.degrees;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * An activity to display course ratings retrieved from a specific course ID via an HTTP GET request.
 */
public class RatingDisplayActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayAdapter<String> adapter;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private static final String API_URL = "http://coms-309-036.class.las.iastate.edu:8080/";
    private Long courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewing_allratings);

        courseId = getIntent().getLongExtra("courseId", -1);

        recyclerView = findViewById(R.id.ratingRecyclerView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        recyclerView.setAdapter(new CourseRatingsAdapter());

        // Make a GET request to retrieve course ratings
        getCourseRatings();
    }

    /**
     * Performs an HTTP GET request to fetch course ratings for the provided course ID.
     */
    private void getCourseRatings() {
        Request request = new Request.Builder()
                .url(API_URL + "/courses/" + courseId + "/ratings")
                .get()
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(RatingDisplayActivity.this, "Network request failed", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    parseAndDisplayCourseRatings(responseBody);
                } else {
                    runOnUiThread(() -> Toast.makeText(RatingDisplayActivity.this, "Failed to retrieve course ratings", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    /**
     * Parses the received course ratings data and displays it on the view.
     *
     * @param responseBody The JSON data received as a response to the course ratings request.
     */
    private void parseAndDisplayCourseRatings(String responseBody) {
        try {
            JSONObject response = new JSONObject(responseBody);
            JSONArray courseRatings = response.getJSONArray("ratings");
            List<String> courseRatingsList = new ArrayList<>();

            for (int i = 0; i < courseRatings.length(); i++) {
                JSONObject courseRating = courseRatings.getJSONObject(i);

                // Construct the course rating item
                String ratingText = "Student ID: " + courseRating.getJSONObject("id").getJSONObject("student").getLong("universityId") +
                        "\nCourse ID: " + courseRating.getJSONObject("id").getJSONObject("course").getInt("courseId") +
                        "\nRating: " + courseRating.getInt("rating") +
                        "\nReview: " + courseRating.getString("reviewText");

                courseRatingsList.add(ratingText);
            }

            runOnUiThread(() -> {
                TextView disp = findViewById(R.id.courseRatingDisplay);
                disp.setText(String.join("\n\n", courseRatingsList));
//                adapter.clear();
//                adapter.addAll(courseRatingsList);
//                adapter.notifyDataSetChanged();
            });
        } catch (JSONException e) {
            e.printStackTrace();
            runOnUiThread(() -> Toast.makeText(RatingDisplayActivity.this, "Failed to parse JSON response", Toast.LENGTH_SHORT).show());
        }
    }
}
