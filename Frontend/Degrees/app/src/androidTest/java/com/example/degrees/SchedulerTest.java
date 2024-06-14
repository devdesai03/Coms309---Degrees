package com.example.degrees;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SchedulerTest {

    @Rule
    public ActivityScenarioRule<SchedulerActivity> activityScenarioRule =
            new ActivityScenarioRule<>(SchedulerActivity.class);

    @Test
    public void testSchedulerFunctionality() {
        // Delay for a short period to observe the result (optional)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if the ListView is displayed
        Espresso.onView(ViewMatchers.withId(R.id.listViewClassSchedule))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Check if the "Show Courses" button is displayed
        Espresso.onView(ViewMatchers.withId(R.id.btnShowCourses))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Perform click on the "Show Courses" button
        Espresso.onView(ViewMatchers.withId(R.id.btnShowCourses))
                .perform(ViewActions.click());

        // Delay for a short period to observe the result (optional)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if the ListView is updated with course sections
        Espresso.onView(ViewMatchers.withId(R.id.listViewClassSchedule))
                .check(ViewAssertions.matches(ViewMatchers.hasMinimumChildCount(1)));

        // Perform a click on an item in the ListView (if available)
        // Note: This depends on the actual data retrieved and may need adjustment
        Espresso.onView(ViewMatchers.withId(R.id.listViewClassSchedule))
                .perform(ViewActions.click());

        // Delay for a short period to observe the result (optional)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Perform a click on the "Show Courses" button to create a course section
        Espresso.onView(ViewMatchers.withId(R.id.btnShowCourses))
                .perform(ViewActions.click());

        // Delay for a short period to observe the result (optional)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if the ListView is updated after creating a course section
        Espresso.onView(ViewMatchers.withId(R.id.listViewClassSchedule))
                .check(ViewAssertions.matches(ViewMatchers.hasMinimumChildCount(2)));

        // Test creating a course section with empty fields (unsuccessful scenario)
        Espresso.onView(ViewMatchers.withId(R.id.btnShowCourses))
                .perform(ViewActions.click());

        // Delay for a short period to observe the result (optional)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if an error message is displayed (this depends on your actual implementation)
        Espresso.onView(ViewMatchers.withText("Failed to create course section"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.btnShowCourses))
                .perform(ViewActions.click());

        // Delay for a short period to observe the result (optional)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if the ListView is updated after creating a course section
        Espresso.onView(ViewMatchers.withId(R.id.listViewClassSchedule))
                .check(ViewAssertions.matches(ViewMatchers.hasMinimumChildCount(3)));
    }
}
