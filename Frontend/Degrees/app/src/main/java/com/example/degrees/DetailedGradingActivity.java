package com.example.degrees;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

public class DetailedGradingActivity extends AppCompatActivity {

    private TextView currentGradeTextView;
    private EditText gradeEditText;

    private static final String BASE_URL = "http://coms-309-036.class.las.iastate.edu:8080/";

    private OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_grading);

        currentGradeTextView = findViewById(R.id.currentGradeTextView);
        gradeEditText = findViewById(R.id.gradeEditText);

        // Retrieve the current grade from the intent
        String currentGrade = getIntent().getStringExtra("grade");
        currentGradeTextView.setText(currentGrade);

        findViewById(R.id.assignGradeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignNewGrade();
            }
        });
    }

    private void assignNewGrade() {
        String newGrade = gradeEditText.getText().toString().trim();

        // Validate the new grade (add your validation logic)
        if (newGrade.isEmpty()) {
            Toast.makeText(this, "Please enter a new grade", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get universityId and sectionId from your data source (Intent, SharedPreferences, etc.)
        long universityId = getUniversityId();
        long sectionId = getSectionId();

        // Prepare the PUT request body
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        String requestBodyJson = "{ \"grade\": \"" + newGrade + "\" }";
        RequestBody requestBody = RequestBody.create(mediaType, requestBodyJson);

        // Create the PUT request
        Request request = new Request.Builder()
                .url(BASE_URL + "updateGrade/" + universityId + "/" + sectionId + "?newGrade= " + newGrade)
                .put(requestBody)
                .build();
        Log.d("DetailedGradingActivity", "UniversityId: " + universityId + ", SectionId: " + sectionId);

        // Execute the PUT request
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> showToast("Failed to update grade"));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(() -> {
                        showToast("Grade updated successfully");
                        // You may finish the activity or perform other actions
                        finish();
                    });
                } else {
                    runOnUiThread(() -> showToast("Failed to update grade. Server Error: " + response.code()));
                }
            }
        });
    }

    private long getUniversityId() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("universityId")) {
            return intent.getLongExtra("universityId", -1);
        }
        return -1;
    }

    private long getSectionId() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("sectionId")) {
            return intent.getLongExtra("sectionId", -1);
        }
        return -1;
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
