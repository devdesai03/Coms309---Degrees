package com.example.degrees;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MajorMinorActivity extends AppCompatActivity {

    private TextView currentMajorTextView;
    private TextView currentMinorTextView;
    private Spinner majorSpinner;
    private Spinner minorSpinner;

    private Button changeButton;

    private TextView majorTextView;

    private TextView minorTextView;
    private MajAdapter majorAdapter;
    private MinAdapter minorAdapter;

    private View initialLayout;
    private View dropdownLayout;

    private List<String> majors = new ArrayList<>();
    private HashMap<String, Long> majorNameToIdMap = new HashMap<>();
    private List<String> minors = new ArrayList<>();
    private Button btnSave;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private static final String BASE_URL = "http://coms-309-036.class.las.iastate.edu:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_major_minor);

        currentMajorTextView = findViewById(R.id.textViewCurrentMajor);
        currentMinorTextView = findViewById(R.id.textViewCurrentMinor);
        changeButton = findViewById(R.id.changeMajorMinorButton);
        majorSpinner = findViewById(R.id.majorSpinner);
        minorSpinner = findViewById(R.id.minorSpinner);
        dropdownLayout = findViewById(R.id.dropdownLayout);
        majorTextView = findViewById(R.id.currentMajor);
        minorTextView = findViewById(R.id.currentMinor);
        btnSave = findViewById(R.id.btnSave);

        // Initially, hide the dropdown layout
        dropdownLayout.setVisibility(View.GONE);

        // Set up spinners
        fetchMajorDataFromBackend();
        fetchMinorDataFromBackend();
        // Fetch degrees when the activity starts
        fetchDegreesFromBackend();

        majorAdapter = new MajAdapter(this, android.R.layout.simple_spinner_item, majors);
        minorAdapter = new MinAdapter(this, android.R.layout.simple_spinner_item, minors);

        majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        minorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        majorSpinner.setAdapter(majorAdapter);
        minorSpinner.setAdapter(minorAdapter);
    }

    private void fetchDegreesFromBackend() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL + "degrees/")
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        // Assuming the JSON response is an array of degrees
                        List<String> degreeList = parseDegreeData(responseBody);

                        // You might want to update the UI on the main thread
                        runOnUiThread(() -> {
                            // Clear existing data and add new degrees
                            majors.clear();
                            majors.addAll(degreeList);
                            minors.clear();
                            minors.addAll(degreeList);
                            majorAdapter.notifyDataSetChanged();
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }
        });
    }

    // Method to parse degree data and extract degree names
    private List<String> parseDegreeData(String responseBody) throws JSONException {
        List<String> degreeList = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(responseBody);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject degreeObject = jsonArray.getJSONObject(i);
            String degreeName = degreeObject.getString("name");
            degreeList.add(degreeName);
            majorNameToIdMap.put(degreeName, degreeObject.getLong("id"));
        }

        return degreeList;
    }
    private void fetchMajorDataFromBackend() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL + "students/" + DashboardActivity.getIsuRegistrationId() + "/major")
                .get()
                .build();
        //ERROR HERE
        Log.d("MajorMinorActivity", "Fetching majors from: " + BASE_URL + "students/" + DashboardActivity.getIsuRegistrationId() + "/major");


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Log.d("MajorMinorActivity", "Major response body: " + responseBody);
                        majors.clear(); // Clear existing data
                        majors.addAll(parseMajorData(responseBody));


                        runOnUiThread(() -> majorAdapter.notifyDataSetChanged());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Failed response: " + response);
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void fetchMinorDataFromBackend() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL + "students/" + DashboardActivity.getIsuRegistrationId() + "/minor")
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        Log.d("MajorMinorActivity", "Minor response body: " + responseBody);
                        minors.clear(); // Clear existing data
                        minors.addAll(parseMinorData(responseBody));

                        runOnUiThread(() -> minorAdapter.notifyDataSetChanged());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }
        });
    }

    private List<String> parseMajorData(String responseBody) throws JSONException {
        List<String> majorList = new ArrayList<>();

        JSONObject majorObject = new JSONObject(responseBody);

        String majorName = majorObject.getJSONObject("degree")
                .getString("name") + " "
                + majorObject.getJSONObject("degree")
                .getString("suffix");
        majorList.add(majorName);
        majorTextView.setText(majorName);

        // Assuming the JSON response is an array of majors
//        JSONArray jsonArray = new JSONArray(responseBody);


//        if (jsonArray.length() == 0) {
//            Log.d("MajorMinorActivity", "No majors found in the response.");
//            // You can add a default value or handle this case as needed
//        } else {
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject majorObject = jsonArray.getJSONObject(i);
//                String majorName = majorObject.getJSONObject("degree")
//                        .getString("name") + " "
//                        + majorObject.getJSONObject("degree")
//                            .getString("suffix");
//                majorList.add(majorName);
//                majorTextView.setText(majorName);
//
//
//            }
//        }

        return majorList;
    }


    private List<String> parseMinorData(String responseBody) throws JSONException {
        List<String> minorList = new ArrayList<>();

        // Assuming the JSON response is an array of minors
        JSONArray jsonArray = new JSONArray(responseBody);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject minorObject = jsonArray.getJSONObject(i);
            String minorName = minorObject.getJSONObject("degree")
                    .getString("name") + " "
                    + minorObject.getJSONObject("degree")
                    .getString("suffix");
            minorList.add(minorName);
            minorTextView.setText(minorName);

        }

        return minorList;
    }

    public void onChangeMajorMinorClick(View view) {
        // Show the dropdown layout and hide the initial layout
        currentMajorTextView.setVisibility(View.GONE);
        currentMinorTextView.setVisibility(View.GONE);
        changeButton.setVisibility(View.GONE);
        majorTextView.setVisibility(View.GONE);
        minorTextView.setVisibility(View.GONE);
        dropdownLayout.setVisibility(View.VISIBLE);

        // Fetch degrees when the dropdown is clicked
        fetchDegreesFromBackend();
    }

    public void onSaveButtonClick(View view) {
        // Get the selected major and minor from the Spinners
        Object majorSelectedItem = majorSpinner.getSelectedItem();
        Object minorSelectedItem = minorSpinner.getSelectedItem();

        // Check if selected items are not null
        String selectedMajor = (majorSelectedItem != null) ? majorSelectedItem.toString() : "";
        String selectedMinor = (minorSelectedItem != null) ? minorSelectedItem.toString() : "";

        // Save the selected major and minor
//    saveSelectedMajorAndMinor(selectedMajor, selectedMinor);

        // Update the TextViews to display the newly selected major and minor
        majorTextView.setText(selectedMajor);
        minorTextView.setText(selectedMinor);

        // After saving, switch visibility back
        dropdownLayout.setVisibility(View.GONE);
        currentMajorTextView.setVisibility(View.VISIBLE);
        currentMinorTextView.setVisibility(View.VISIBLE);
        changeButton.setVisibility(View.VISIBLE);
        majorTextView.setVisibility(View.VISIBLE);
        minorTextView.setVisibility(View.VISIBLE);
        saveSelectedMajorAndMinor(selectedMajor, selectedMinor);
    }

    private void saveSelectedMajorAndMinor(String major, String minor) {
        // Update major registration
        OkHttpClient client = new OkHttpClient();
        Request majorRequest = new Request.Builder()
                .url(BASE_URL + "students/" + DashboardActivity.getIsuRegistrationId() + "/major")
                .put(RequestBody.create(String.format("""
                        { "id": %s }
                        """, majorNameToIdMap.get(major)), MediaType.get("application/json")))  // You might need to provide a proper request body
                .build();
        System.out.println("Saving major ID: " + majorNameToIdMap.get(major));
        // Execute the requests asynchronously
        client.newCall(majorRequest).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                // Handle the response if needed
                System.out.println("Saving major: " + response);
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                // Handle failure (e.g., network error)
                e.printStackTrace();
            }
        });
    }

    private class MajAdapter extends ArrayAdapter<String> {

        public MajAdapter(Context context, int resource, List<String> majors) {
            super(context, resource, majors);
        }

        // You can override other methods if needed

    }

    private class MinAdapter extends ArrayAdapter<String> {

        public MinAdapter(Context context, int resource, List<String> minors) {
            super(context, resource, minors);
        }

        // You can override other methods if needed

    }
}
