package com.example.degrees;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

public class Degree_Audit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_degree_audit);
        TextView degreeAuditTextView = findViewById(R.id.degreeAuditText);
        setStudentId(DashboardActivity.getIsuRegistrationId());
        if (studentId == null) {
            setStudentId(Long.valueOf(19)); // Test student ID
        }
        getRequest(degreeAuditTextView);


    }

    private void getRequest(TextView degreeAuditTextView) {

        // Request a string response from the provided URL.
        String url;
        url = MainActivity.getBaseURL() + "/degreeAudit/" + studentId;



        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    degreeAuditTextView.setText(response);
                },
                error -> {
                    error.printStackTrace();
                    degreeAuditTextView.setText("Error occurred for URL " + url + ":\n"
                    + error.networkResponse.statusCode + new String(error.networkResponse.data));
                }
        );
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
    public Long studentId ;

    public void setStudentId( Long studentId){
        this.studentId = studentId;
    }
}