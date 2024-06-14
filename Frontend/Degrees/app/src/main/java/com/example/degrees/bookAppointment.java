package com.example.degrees;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class bookAppointment extends AppCompatActivity {

    Long studentId;

    Long advisorId = null;

    LocalDate selecteddate;
    LocalTime selectedStartTime;
    LocalTime selectedEndTime;

    Button dateButtonView;
    Button startTimeButton;
    Button endTimeButton;
    TextView descriptionView;
    TextView advisorNameView;

    public void snackbarMsg(View parent, String message) {
        Snackbar snackbar =
                Snackbar.make(parent, message,
                        Snackbar.LENGTH_LONG );
        snackbar.show();
    };

    public void setSelectedDate( LocalDate selecteddate){
        this.selecteddate = selecteddate;
        String text = selecteddate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL));
        dateButtonView.setText(text);
    }

    public void setSelectedTimes(LocalTime startTime, LocalTime endTime) {
        if (startTime != null) {
            this.selectedStartTime = startTime;
            String text = startTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
            startTimeButton.setText(text);
            if (endTime == null) {
                endTime = startTime.plusMinutes(30);
            }
        }
        if (endTime != null) {
            this.selectedEndTime = endTime;
            String text = endTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
            endTimeButton.setText(text);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        dateButtonView = findViewById(R.id.dateButtonView);
        startTimeButton = findViewById(R.id.startTimeButton);
        endTimeButton = findViewById(R.id.endTimeButton);
        descriptionView = findViewById(R.id.descriptionView);
        advisorNameView = findViewById(R.id.advisorNameView);


        setSelectedDate(LocalDate.now());
        setSelectedTimes(LocalTime.now(), null);

        studentId = DashboardActivity.getIsuRegistrationId();

        dateButtonView.setOnClickListener(view -> {
            DatePickerDialog dialog = new DatePickerDialog(this,
                    (view2, selectedYear,selectedMonth, selectedDay) ->{
                       setSelectedDate(LocalDate.of(selectedYear, selectedMonth + 1, selectedDay));
                    },
                    selecteddate.getYear(), selecteddate.getMonthValue() - 1, selecteddate.getDayOfMonth());
            dialog.show();
        });

        startTimeButton.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(this,
                    (view2, hourOfDay, minute) -> {
                        setSelectedTimes(LocalTime.of(hourOfDay, minute), null);
                        },
                selectedStartTime.getHour(), selectedStartTime.getMinute(),
                false);
            dialog.show();
        });

        endTimeButton.setOnClickListener(view -> {
            TimePickerDialog dialog = new TimePickerDialog(this,
                    (view2, hourOfDay, minute) -> {
                        setSelectedTimes(null, LocalTime.of(hourOfDay, minute));
                    },
                    selectedEndTime.getHour(), selectedEndTime.getMinute(),
                    false);
            dialog.show();
        });

        Button bookBtn = findViewById(R.id.book);

        bookBtn.setOnClickListener(view -> {
            var body = new JSONObject();
            try {
                if (advisorId == null) {
                    snackbarMsg(view, "Missing advisor ID");
                    return;
                }

                var student = new JSONObject();
                student.put("universityId", studentId);
                body.put("student", student);

                var advisor = new JSONObject();
                advisor.put("advisorId", advisorId);
                body.put("advisor", advisor);

                body.put("description", descriptionView.getText());

                body.put("startTime", selecteddate.atTime(selectedStartTime));
                body.put("endTime", selecteddate.atTime(selectedEndTime));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            var request = new StringRequest(
                    Request.Method.POST,
                    MainActivity.getBaseURL() + "/" + studentId + "/schedule-appointment" ,
                    (String response) -> {
                        snackbarMsg(view, "Successfully booked appointment!");
                    },
                    (VolleyError error) -> {
                        String resp;
                        if (error.networkResponse.data != null && error.networkResponse.data.length != 0) {
                            resp = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        } else {
                            resp = null;
                        }
                        if (resp == null) {
                            snackbarMsg(view, "Error");
                        } else {
                            snackbarMsg(view, "Error: " + resp);
                        }
                        System.out.println("error" + error + " " + resp + '\n' + body);
                    }) {
                @Override
                public byte[] getBody() {
                    return body.toString().getBytes();
                }
                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
            };
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
        });


        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                MainActivity.getBaseURL() + "/students/" + studentId,
                null,
                (JSONObject response) -> {
                    try {
                        if (response.isNull("studentAdvisor")) {
                            snackbarMsg(advisorNameView, "You have no advisor!");
                        }
                        JSONObject advisor = response.getJSONObject("studentAdvisor");
                        JSONObject advisorIsu = advisor.getJSONObject("isuRegistration");
                        advisorNameView.setText(advisorIsu.getString("fullNameFriendly"));
                        advisorId = advisor.getLong("advisorId");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                (VolleyError error) -> {
                    System.out.println(error);
                    snackbarMsg(advisorNameView, "Error retrieving student information.");
                });
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

}