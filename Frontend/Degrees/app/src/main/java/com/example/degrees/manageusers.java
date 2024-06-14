package com.example.degrees;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

public class manageusers extends AppCompatActivity {

    public abstract class CustomArrayAdapter extends ArrayAdapter<Optional<JSONObject>> {
        public CustomArrayAdapter(@NonNull Context context, @NonNull List<Optional<JSONObject>> objects) {
            super(context, android.R.layout.simple_spinner_dropdown_item, objects);
        }

        abstract String convertToString(JSONObject object) throws JSONException;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            JSONObject object = super.getItem(position).orElse(null);
            try {
                view.setText(convertToString(object));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            return view;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getDropDownView(position, convertView, parent);
            JSONObject object = super.getItem(position).orElse(null);
            try {
                view.setText(convertToString(object));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            return view;
        }
    }

    JSONObject TEST_exampleMajor;
//    JSONArray degrees_list;

    List<Optional<JSONObject>> departmentsList;
    Map<Long, Integer> departmentsMap;
    List<Optional<JSONObject>> advisorsList;
    Map<Long, Integer> advisorsMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manageusers);
        Button Savebtn =  findViewById(R.id.Save);
        Savebtn.setOnClickListener(btn -> {
            this.putRequest(this.userInfo);
        });

        Long userId;
        if (getIntent().hasExtra("editingUserId")) {
            userId = getIntent().getLongExtra("editingUserId", -1);
        } else {
            userId = (long)7;
        }

//        loadDegrees(response->loadUser(userId));

        loadData("/departments/", (list, map) -> {
            departmentsList = list;
            departmentsMap = map;
            if (advisorsList != null)
                loadUser(userId);
        }, dept -> dept.getLong("departmentId"));
        loadData("/advisor/", (list, map) -> {
            advisorsList = list;
            advisorsMap = map;
            if (departmentsList != null)
                loadUser(userId);
        }, adv -> adv.getLong("advisorId"));

//        try {
//            String dummyDataString = "{\n" +
//                    "  \"isuRegistration\": {\n" +
//                    "    \"advisor\": {\n" +
//                    "      \"advisorDepartment\": \"Department of Computer Science\"\n" +
//                    "    },\n" +
//                    "    \"netId\": \"testNetID\",\n" +
//                    "    \"student\": {\n" +
//                    "      \"major\": {\n" +
//                    "        \"department\": {\n" +
//                    "          \"departmentAddress\": \"4444 Osborn Dr\",\n" +
//                    "          \"departmentCode\": \"COM S\",\n" +
//                    "          \"departmentId\": 15,\n" +
//                    "          \"departmentName\": \"Computer Science\"\n" +
//                    "        },\n" +
//                    "        \"id\": 934,\n" +
//                    "        \"name\": \"Computer Science\",\n" +
//                    "        \"suffix\": \"BA\"\n" +
//                    "      },\n" +
//                    "      \"universityId\": 0\n" +
//                    "    },\n" +
//                    "    \"universityId\": 0\n" +
//                    "  },\n" +
//                    "  \"phoneNumber\": \"5155555555\",\n" +
//                    "  \"userAddress\": \"Friley Hall\",\n" +
//                    "  \"userEmail\": \"testUser@testEmail.com\",\n" +
//                    "  \"userId\": 3242,\n" +
//                    "  \"userName\": \"testUserName\"\n" +
//                    "}";
//            JSONObject dummyData = new JSONObject(dummyDataString);
//            TEST_exampleMajor = dummyData.getJSONObject("isuRegistration").getJSONObject("student").getJSONObject("major");
//            displayUserInformation(dummyData);
////        } catch (IOException e) {
////            throw new RuntimeException(e);
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }
    }

    private JSONObject userInfo;


    public void loadUser(Long userId) {
        String url = "/users/" + userId;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                MainActivity.getBaseURL() + url, null,
                (JSONObject response) -> {
                    try {
                        displayUserInformation(response);
                    } catch (JSONException e) {
                        e.printStackTrace(System.out);
                    }
                },
                (VolleyError error) -> {
                    snackbarMsg(findViewById(R.id.Save), "Error loading user!");
                    System.out.println("The error is: " + error);
                }
        );
        // Do something with value!
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);

    }

    public void displayUserInformation(JSONObject userInfo) throws JSONException {
        this.userInfo = userInfo;
        TextView userID = findViewById(R.id.userID);
        TextView userAddress = findViewById(R.id.userAddress);
        TextView userEmail = findViewById(R.id.userEmail);
        TextView userName = findViewById(R.id.userName);
        TextView userMobile = findViewById(R.id.userMobile);


        userID.setText(userInfo.optLong("userId") + "");
        userAddress.setText(userInfo.optString("userAddress"));
        userEmail.setText(userInfo.optString("userEmail"));
        userName.setText(userInfo.optString("userName"));
        userMobile.setText(userInfo.optString("phoneNumber"));

        setOnChangeListener(userID, (changed) -> {
            userInfo.put("userId", Long.valueOf(changed.toString()));
        });
        setOnChangeListener(userAddress, (changed) -> {
            userInfo.put("userAddress", changed.toString());
        });
        setOnChangeListener(userEmail, (changed) -> {
            userInfo.put("userEmail", changed.toString());
        });
        setOnChangeListener(userName, (changed) -> {
            userInfo.put("userName", changed.toString());
        });
        setOnChangeListener(userMobile, (changed) -> {
            userInfo.put("phoneNumber", changed.toString());
        });


        View isuRegistrationView = findViewById(R.id.isuRegistrationSection);
        Button isuRegBtn = findViewById(R.id.isuRegistrationToggle);
        if (userInfo.isNull("isuRegistration")) {
            isuRegBtn.setText("Add");
            isuRegBtn.setOnClickListener((view) -> {
                try {
                    this.onClickIsuRegistration();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
            isuRegistrationView.setVisibility(View.GONE);
        } else {
            isuRegBtn.setText("Delete");
            isuRegBtn.setOnClickListener((view) -> {
                try {
                    this.userInfo.put("isuRegistration", null);
                    this.displayUserInformation(this.userInfo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
            isuRegistrationView.setVisibility(View.VISIBLE);
            JSONObject isuRegistration = userInfo.getJSONObject("isuRegistration");
            TextView universityId = findViewById(R.id.isuUniversityId);
            TextView netId = findViewById(R.id.isuNetId);
            TextView givenName = findViewById(R.id.isuGivenName);
            TextView middleName = findViewById(R.id.isuMiddleName);
            TextView surname = findViewById(R.id.isuSurname);
            if (!isuRegistration.isNull("universityId")) {
                universityId.setText(isuRegistration.getLong("universityId") + "");
            } else {
                universityId.setText("");
            }
            netId.setText(isuRegistration.optString("netId"));

            if (!isuRegistration.isNull("givenName")) {
                givenName.setText(isuRegistration.getString("givenName"));
            } else {
                givenName.setText("");
            }
            if (!isuRegistration.isNull("middleName")) {
                middleName.setText(isuRegistration.getString("middleName"));
            } else {
                middleName.setText("");
            }
            if (!isuRegistration.isNull("surname")) {
                surname.setText(isuRegistration.getString("surname"));
            } else {
                surname.setText("");
            }

            setOnChangeListener(universityId, (changed) -> {
                if (changed != null && !changed.equals("")) {
                    isuRegistration.put("universityId", Long.valueOf(changed));
                } else {
                    isuRegistration.put("universityId", null);
                }
            });
            setOnChangeListener(netId, (changed) -> {
                isuRegistration.put("netId", changed);
            });
            setOnChangeListener(givenName, (changed) -> {
                if (changed != null && !changed.equals("")) {
                    isuRegistration.put("givenName", changed);
                } else {
                    isuRegistration.put("givenName", null);
                }
            });
            setOnChangeListener(middleName, (changed) -> {
                if (changed != null && !changed.equals("")) {
                    isuRegistration.put("middleName", changed);
                } else {
                    isuRegistration.put("middleName", null);
                }
            });
            setOnChangeListener(surname, (changed) -> {
                if (changed != null && !changed.equals("")) {
                    isuRegistration.put("surname", changed);
                } else {
                    isuRegistration.put("surname", null);
                }
            });

            View advisorView = findViewById(R.id.advisorSection);
            Button advisorButton = findViewById(R.id.advisorToggle);
            if (isuRegistration.isNull("advisor")) {
                advisorButton.setText("Add");
                advisorButton.setOnClickListener((view) -> {
                    try {
                        JSONObject advisor = new JSONObject();
                        advisor.put("advisorDepartment", null);
                        isuRegistration.put("advisor", advisor);
                        this.displayUserInformation(this.userInfo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
                advisorView.setVisibility(View.GONE);
            } else {
                advisorButton.setText("Delete");
                advisorButton.setOnClickListener((view) -> {
                    try {
                        isuRegistration.put("advisor", null);
                        this.displayUserInformation(this.userInfo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
                advisorView.setVisibility(View.VISIBLE);
                JSONObject advisor = isuRegistration.getJSONObject("advisor");
                Spinner advisorDepartment = findViewById(R.id.advisorDepartment);
                advisorDepartment.setAdapter(new CustomArrayAdapter(this, departmentsList) {
                    @Override
                    String convertToString(JSONObject department) throws JSONException {
                        if (department == null)
                            return "No Department";
                        return String.format("%s (%s)",
                                department.getString("departmentName"),
                                department.getString("departmentCode"));
                    }
                });
                if (!advisor.isNull("advisorDepartment")) {
                    Long id = advisor.getJSONObject("advisorDepartment")
                            .getLong("departmentId");
                    if (departmentsMap.containsKey(id)) {
                        advisorDepartment.setSelection(departmentsMap.get(id));
                    }
                }
                setOnChangeListener(advisorDepartment, newValue -> {
                    if (newValue == null) {
                        advisor.put("advisorDepartment", null);
                    } else {
                        JSONObject dept = new JSONObject();
                        dept.put("departmentId", newValue.getLong("departmentId"));
                        advisor.put("advisorDepartment", dept);
                    }
                });
            }

            View instructorView = findViewById(R.id.instructorSection);
            Button instructorButton = findViewById(R.id.instructorToggle);
            if (isuRegistration.isNull("instructor")) {
                instructorButton.setText("Add");
                instructorButton.setOnClickListener((view) -> {
                    try {
                        JSONObject instructor = new JSONObject();
                        instructor.put("instructorDepartment", null);
                        isuRegistration.put("instructor", instructor);
                        this.displayUserInformation(this.userInfo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
                instructorView.setVisibility(View.GONE);
            } else {
                instructorButton.setText("Delete");
                instructorButton.setOnClickListener((view) -> {
                    try {
                        isuRegistration.put("instructor", null);
                        this.displayUserInformation(this.userInfo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
                instructorView.setVisibility(View.VISIBLE);
                JSONObject instructor = isuRegistration.getJSONObject("instructor");
                Spinner instructorDepartment = findViewById(R.id.instructorDepartment);
                instructorDepartment.setAdapter(new CustomArrayAdapter(this, departmentsList) {
                    @Override
                    String convertToString(JSONObject department) throws JSONException {
                        if (department == null)
                            return "No Department";
                        return String.format("%s (%s)",
                                department.getString("departmentName"),
                                department.getString("departmentCode"));
                    }
                });
                if (!instructor.isNull("instructorDepartment")) {
                    Long id = instructor.getJSONObject("instructorDepartment")
                            .getLong("departmentId");
                    if (departmentsMap.containsKey(id)) {
                        instructorDepartment.setSelection(departmentsMap.get(id));
                    }
                }
                setOnChangeListener(instructorDepartment, newValue -> {
                    if (newValue == null) {
                        instructor.put("instructorDepartment", null);
                    } else {
                        JSONObject dept = new JSONObject();
                        dept.put("departmentId", newValue.getLong("departmentId"));
                        instructor.put("instructorDepartment", dept);
                    }
                });

            }
            View studentView = findViewById(R.id.studentSection);
            Button studentButton = findViewById(R.id.studentToggle);
            if (isuRegistration.isNull("student")) {
                studentButton.setText("Add");
                studentButton.setOnClickListener((view) -> {
                    try {
                        JSONObject student = new JSONObject();
                        student.put("major", null);
                        isuRegistration.put("student", student);
                        this.displayUserInformation(this.userInfo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
                studentView.setVisibility(View.GONE);
            } else {
                studentButton.setText("Delete");
                studentButton.setOnClickListener((view) -> {
                    try {
                        isuRegistration.put("student", null);
                        this.displayUserInformation(this.userInfo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });
                studentView.setVisibility(View.VISIBLE);
                JSONObject student = isuRegistration.getJSONObject("student");
                Spinner studentAdvisor = findViewById(R.id.studentAdvisor);
                studentAdvisor.setAdapter(new CustomArrayAdapter(this, advisorsList) {
                    @Override
                    String convertToString(JSONObject advisor) throws JSONException {
                        if (advisor == null)
                            return "No Advisor";
                        JSONObject isuRegistration = advisor.getJSONObject("isuRegistration");
                        if (isuRegistration.isNull("fullName")) {
                            return isuRegistration.getString("netId");
                        } else {
                            return isuRegistration.getString("fullName");
                        }
                    }
                });
                if (!student.isNull("studentAdvisor")) {
                    Long studentAdvisorId = student.getJSONObject("studentAdvisor")
                            .getLong("advisorId");
                    if (advisorsMap.containsKey(studentAdvisorId)) {
                        studentAdvisor.setSelection(advisorsMap.get(studentAdvisorId));
                    }
                }
                setOnChangeListener(studentAdvisor, newValue -> {
                    if (newValue == null) {
                        student.put("studentAdvisor", null);
                    } else {
                        JSONObject advisor = new JSONObject();
                        advisor.put("advisorId", newValue.getLong("advisorId"));
                        student.put("studentAdvisor", advisor);
                    }
                });

//                Spinner studentMajor = findViewById(R.id.Major);
//
//                List<JSONObject> possibleMajors = new ArrayList<>();
//                possibleMajors.add(null);
//                for (int i = 0; i < degrees_list.length(); i++) {
//                    possibleMajors.add(degrees_list.getJSONObject(i));
//                }
//                populateSpinner(studentMajor,
//                        student.isNull("major") ? null : student.getJSONObject("major"),
//                        possibleMajors,
//                        (major) -> {
//                            if (major == null) {
//                                return "No Major";
//                            }
//                            try {
//                                return major.getString("name") + " " + major.getString("suffix");
//                            } catch (JSONException e) {
//                                throw new RuntimeException(e);
//                            }
//                        },
//                        (selectedMajor) -> {
//                            try {
//                                student.put("major", selectedMajor);
//                            } catch (JSONException e) {
//                                throw new RuntimeException(e);
//                            }
//                        });
            }
        }


    }

    private interface ThrowingFunction<T, E> {
        E apply(T t) throws JSONException;
    }
    public <Id> void loadData(String endpoint,
                         BiConsumer<? super List<Optional<JSONObject>>, ? super Map<Id, Integer>> callback,
                         ThrowingFunction<JSONObject, Id> objToIdFunc) {

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                MainActivity.getBaseURL() + endpoint, null,
                (JSONArray response) -> {
                    try {
                        ArrayList<Optional<JSONObject>> list = new ArrayList<>();
                        HashMap<Id, Integer> map = new HashMap<>();
                        list.add(Optional.empty());
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            list.add(Optional.of(obj));
                            map.put(objToIdFunc.apply(obj), i + 1);
                        }
                        callback.accept(list, map);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                },
                (VolleyError error) -> {
                    snackbarMsg(findViewById(R.id.Save), "Error loading data " + endpoint);
                    System.out.println("The error is: " + error.networkResponse);
                }
        );
        // Do something with value!
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

//    public void loadDegrees(Consumer<JSONArray> callback) {
//        String url = "/degrees/" ;
//
//        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
//                MainActivity.getBaseURL() + url, null,
//                (JSONArray response) -> {
//
//                        degrees_list = response;
//                        callback.accept(response);
//
//                },
//                (VolleyError error) -> {
//                    snackbarMsg(findViewById(R.id.Save), "Error loading degrees");
//                    System.out.println("The error is: " + error);
//                }
//        );
//        // Do something with value!
//        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
//
//    }




    public void onClickIsuRegistration() throws JSONException {
        JSONObject isuRegistration = new JSONObject();
        isuRegistration.put("universityId", null);
        isuRegistration.put("netId", null);
        this.userInfo.put("isuRegistration", isuRegistration);
         displayUserInformation(this.userInfo);

    }
    private interface ThrowingConsumer<T> {
        void accept(T e) throws JSONException;
    }
    private void setOnChangeListener(TextView userInfo, ThrowingConsumer<String> changed){
        userInfo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    changed.accept(s.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setOnChangeListener(Spinner spinner, ThrowingConsumer<JSONObject> onChangeListener) {
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                @SuppressWarnings("unchecked")
                var item = ((Optional<JSONObject>)parent.getItemAtPosition(position)).orElse(null);
                try {
                    onChangeListener.accept(item);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                try {
                    onChangeListener.accept(null);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
//
//    private <Item> void populateSpinner(Spinner spinner,
//                                        Item currentlySelectedItem,
//                                        List<? extends Item> items,
//                                        Function<Item, String> itemNameFunction,
//                                        Consumer<Item> onChangeListener) {
//        HashMap<String, Item> nameToItemMap = new HashMap<>();
//        ArrayList<String> itemNames = new ArrayList<String>();
//        ArrayList<Item> itemsArray = new ArrayList<Item>();
//        int initialPosition = -1;
//
//        String currentlySelectedItemName
//                = itemNameFunction.apply(currentlySelectedItem);
//
//        for (int i = 0; i < items.size(); i++) {
//            Item item = items.get(i);
//            String itemName = itemNameFunction.apply(item);
//            nameToItemMap.put(itemName, item);
//            itemNames.add(itemName);
//            itemsArray.add(item);
//            if (currentlySelectedItem != null && currentlySelectedItemName.equals(itemName)) {
//                initialPosition = i;
//            }
//        }
//
//        //A spinner is drop-down menu
//        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_dropdown_item,
//                itemNames);
//        spinner.setAdapter(spinnerArrayAdapter);
//
//        if (currentlySelectedItem != null) {
//            if (initialPosition == -1)
//                throw new RuntimeException("Selected item nonexistent in items list");
//            spinner.setSelection(initialPosition);
//        }
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Item item = itemsArray.get(position);
//                onChangeListener.accept(item);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                onChangeListener.accept(null);
//            }
//        });
//    }

    private void putRequest(JSONObject userInfo) {

        // Request a string response from the provided URL.
        String url;
        Long userId;
        try {
            userId = userInfo.getLong("userId");
            url = MainActivity.getBaseURL() + "/users/" + userId;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.PUT, url, userInfo,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        // Display the first 500 characters of the response string.
                        // String response can be converted to JSONObject via
                        // JSONObject object = new JSONObject(response);
//                        tvResponse.setText("Response is: "+ response);
                        snackbarMsg(findViewById(R.id.Save), "Successfully saved user information!");
                        System.out.println("Put response is: " + response);
                        loadUser(userId);
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        snackbarMsg(findViewById(R.id.Save), "Error saving user");
                       System.out.println(error);
                    }
                }
        );

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public void snackbarMsg(View parent, String message) {
        Snackbar snackbar =
                Snackbar.make(parent, message,
                        Snackbar.LENGTH_LONG );
        snackbar.show();
    };
}