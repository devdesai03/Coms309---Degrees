package com.example.degrees;

import static org.hamcrest.number.OrderingComparison.greaterThan;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class FourYearPlannerTest {

    @Rule
    public ActivityScenarioRule<FourYearPlannerActivity> activityScenarioRule =
            new ActivityScenarioRule<>(FourYearPlannerActivity.class);

    @Test
    public void testAddSemesterAndFetchCourses() {
        // Perform click on the "Add Semester" button
        Espresso.onView(ViewMatchers.withId(R.id.addSemesterButton))
                .perform(ViewActions.click());

        // Delay for a short period to observe the result (optional)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Add assertions after adding a semester
        Espresso.onView(ViewMatchers.withText("Semester 1"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Perform click on the added semester to fetch courses
        Espresso.onView(ViewMatchers.withText("Semester 1"))
                .perform(ViewActions.click());

        // Delay for a short period to observe the result (optional)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        // Add assertions after fetching courses
//        // You can check if the RecyclerView (courseTable) has items
//        Espresso.onView(ViewMatchers.withId(R.id.courseTable))
//                .check(RecyclerViewItemCountAssertion.withItemCount(greaterThan(0)));

        // Example: Check if a specific course is displayed
        Espresso.onView(ViewMatchers.withText("CourseName1"))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // You can add more assertions here based on your UI elements and behavior

        // Delay for a short period to observe the result (optional)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}