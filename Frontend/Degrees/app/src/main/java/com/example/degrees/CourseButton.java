package com.example.degrees;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CourseButton#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseButton extends Fragment {
    // TODO: Rename and change types of parameters
    private String courseName = "Data Structures";
    private String courseShortName = "CS 228";
    private long courseId = 1234;
    private String courseImageSrc = "@drawable/coding";

    public CourseButton() {
        // Required empty public constructor
        super(R.layout.fragment_course_button);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param courseName Parameter 1.
     * @param courseShortName Parameter 2.
     * @return A new instance of fragment CourseButton.
     */
    // TODO: Rename and change types and number of parameters
    public static CourseButton newInstance(String courseName, String courseShortName,
                                           long courseId, String courseImageSrc) {
        CourseButton fragment = new CourseButton();
        Bundle args = new Bundle();
        args.putString("courseName", courseName);
        args.putString("courseShortName", courseShortName);
        args.putLong("courseId", courseId);
        args.putString("courseImageSrc", courseImageSrc);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            courseName = getArguments().getString("courseName");
            courseShortName = getArguments().getString("courseShortName");
            courseId = getArguments().getLong("courseId");
            courseImageSrc = getArguments().getString("courseImageSrc");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course_button, container, false);
    }
}