package com.example.degrees;

import android.content.Intent;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.example.degrees.R;
import com.example.degrees.RatingDisplayActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RatingDisplayTest {

    @Rule
    public ActivityTestRule<RatingDisplayActivity> activityRule =
            new ActivityTestRule<>(RatingDisplayActivity.class, false, false);

    @Test
    public void testCourseRatingsDisplay() {
        // Launch the activity with a specific course ID
        Intent intent = new Intent();
        intent.putExtra("courseId", 123); // Replace with an actual course ID
        activityRule.launchActivity(intent);

        // Check if the toolbar is displayed
        Espresso.onView(ViewMatchers.withId(R.id.toolbar))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Check if the title for the ratings section is displayed
        Espresso.onView(ViewMatchers.withText("All Ratings"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Check if the courseRatingDisplay TextView is displayed
        Espresso.onView(ViewMatchers.withId(R.id.courseRatingDisplay))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Check if the RecyclerView is displayed
        Espresso.onView(ViewMatchers.withId(R.id.ratingRecyclerView))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
