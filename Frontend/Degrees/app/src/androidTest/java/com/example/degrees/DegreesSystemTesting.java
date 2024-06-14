package com.example.degrees;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.matcher.ViewMatchers.withId;


import static org.hamcrest.Matchers.*;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RatingBar;
import android.widget.TimePicker;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.example.degrees.DashboardActivity;
import com.example.degrees.MainActivity;
import com.example.degrees.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest   // large execution time


public class DegreesSystemTesting {
    private static final int SIMULATED_DELAY_MS = 500;

    LocalDate date =
            LocalDate.now().plusDays((int) (Math.random() * 4000));
    LocalTime startTime = LocalTime.of((int) (Math.random() * 24), (int) (Math.random() * 12));
    LocalTime endTime = startTime.plusMinutes(5);

    String[] backgroundColors = {"red", "orange", "yellow", "green", "blue"};
    String[] darkModes = {"darkMode", "lightMode", "sync with system"};

    @Rule
    public ActivityScenarioRule<DashboardActivity> activityScenarioRule = new ActivityScenarioRule<>(DashboardActivity.class);
    @Test
    public void testBookAppointment() {

        onView(withId(R.id.bookAppointment)).perform(click());
        onView(withId(R.id.dateButtonView)).perform(click());
        // Example: Check if a text view is now visible after clicking the booking button
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName())))
                .perform(PickerActions.setDate(date.getYear(), date.getMonthValue(), date.getDayOfMonth()));
        onView(withText("OK")).perform(click());
        String datetext = date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL));
        onView(withId(R.id.dateButtonView)).check(matches(withText(datetext)));

        //test for start time
        onView(withId(R.id.startTimeButton)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime(startTime.getHour(), startTime.getMinute()));
        onView(withText("OK")).perform(click());
        String starttimetext = startTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
        onView(withId(R.id.startTimeButton)).check(matches(withText(starttimetext)));


        //test for end time
        onView(withId(R.id.endTimeButton)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName())))
                .perform(PickerActions.setTime(endTime.getHour(), endTime.getMinute()));
        onView(withText("OK")).perform(click());
        String endtimetext = endTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
        onView(withId(R.id.endTimeButton)).check(matches(withText(endtimetext)));

        //test for book button
        onView(withId(R.id.book)).perform(click());

        //final
        onView(withText("Successfully booked appointment!")).check(matches(isDisplayed()));

        // wait until snackbar disappears
        sleep(1000 * 5);

        //test for overlapping appointment failure
        onView(withId(R.id.book)).perform(click());

        onView(withText(startsWith("Error"))).check(matches(isDisplayed()));


        // Put thread to sleep to allow volley to handle the request
//        try {
//            Thread.sleep(SIMULATED_DELAY_MS);
//        } catch (InterruptedException e) {}
//
//        // Verify that volley returned the correct value
//        onView(withId(R.id.myTextView)).check(matches(withText(endsWith(resultString))));


    }

    @Test
    public void testListUsers() {
        Intents.init();

        onView(withId(R.id.manageUsers)).perform(click());

        // Type in testString and send request
        onView(withId(R.id.search_button)).perform(typeText("Blessing"), pressImeActionButton());
        sleep();
        onView(allOf(withText("Edit"), hasSibling(withText("Blessing")), isDisplayed()))
                .perform(click());

        intended(hasComponent(manageusers.class.getName()));

        onView(allOf(withText("Add"), hasSibling(withText("ISU Registration")), isDisplayed()))
                .perform(click());

        onView(allOf(withId(R.id.isuNetId), withEffectiveVisibility(Visibility.VISIBLE)))
                .perform(scrollTo(), typeText("blessingNetId"), closeSoftKeyboard());
        onView(withId(R.id.isuNetId)).check(matches(withText("blessingNetId")));
        onView(allOf(withId(R.id.isuGivenName), withEffectiveVisibility(Visibility.VISIBLE)))
                .perform(scrollTo(), typeText("Blessing"), closeSoftKeyboard());
        onView(withId(R.id.isuGivenName)).check(matches(withText("Blessing")));
        onView(allOf(withId(R.id.isuMiddleName), withEffectiveVisibility(Visibility.VISIBLE)))
                .perform(scrollTo(), typeText("Apple"), closeSoftKeyboard());
        onView(withId(R.id.isuMiddleName)).check(matches(withText("Apple")));
        onView(allOf(withId(R.id.isuSurname), withEffectiveVisibility(Visibility.VISIBLE)))
                .perform(scrollTo(), typeText("Banana"), closeSoftKeyboard());
        onView(withId(R.id.isuSurname)).check(matches(withText("Banana")));


        onView(allOf(withText("Delete"), hasSibling(withText("ISU Registration")), isDisplayed()))
                .perform(click());

        onView(withId(R.id.Save)).perform(click());

        onView(allOf(withId(com.google.android.material.R.id.snackbar_text),
                isDisplayed()))
                .check(matches(withText(containsStringIgnoringCase("success"))));


        // Put thread to sleep to allow volley to handle the request
//        try {
//            Thread.sleep(SIMULATED_DELAY_MS);
//        } catch (InterruptedException e) {}
//
//        // Verify that volley returned the correct value
//        onView(withId(R.id.myTextView)).check(matches(withText(endsWith(resultString))));


    }

    @Test
    public void testAuditDegree() {

        onView(withId(R.id.auditDegree)).perform(click());
        sleep(1000 * 5);
       onView(withId(R.id.degreeAuditText))
              .check(matches(withText(endsWith("***** End of Degree Audit *****\n"))));

        // Put thread to sleep to allow volley to handle the request
//        try {
//            Thread.sleep(SIMULATED_DELAY_MS);
//        } catch (InterruptedException e) {}
//
//        // Verify that volley returned the correct value
//        onView(withId(R.id.myTextView)).check(matches(withText(endsWith(resultString))));


    }

    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            //
        }
    }

    private void sleep() {
        sleep(SIMULATED_DELAY_MS);
    }

    @Test
    public void testDashSettings() {
        for (String bgColor : backgroundColors) {
            onView(withId(R.id.settingsButton)).perform(click());

            onView(withSpinnerText("red"))
                    .perform(click());

            onData(is(bgColor))
                    .inRoot(isPlatformPopup())
                    .perform(click());

            onView(withText("OK"))
                    .perform(click());

            sleep();
        }

        for (String darkMode : darkModes) {
            onView(withId(R.id.settingsButton)).perform(click());

            onView(withText("darkMode"))
                    .perform(click());

            onData(is(darkMode))
                    .inRoot(isPlatformPopup())
                    .perform(click());

            onView(withText("OK"))
                    .perform(click());

            sleep();
        }
    }

    @Test
    public void testCourseRatings() {
        try (var scenario = ActivityScenario.launch(courseRatings.class)) {
            for (int ratingVar = 1; ratingVar <= 5; ratingVar++) {
                final float rating = ratingVar;

                // Find the RatingBar by its ID
                onView(withId(R.id.ratingBar))
                        // Perform an action, for example, set the rating to 3.5
                        .perform(new ViewAction() {
                            @Override
                            public String getDescription() {
                                return "Set rating";
                            }

                            @Override
                            public Matcher<View> getConstraints() {
                                return isAssignableFrom(RatingBar.class);
                            }

                            @Override
                            public void perform(UiController uiController, View view) {
                                ((RatingBar) view).setRating(rating);
                            }
                        })

                        .check(matches(new BoundedMatcher<View, RatingBar>(RatingBar.class) {
                            @Override
                            public void describeTo(Description description) {
                                description.appendText("Checking the matcher on received view: ");
                                description.appendText("with expectedRating=" + rating);
                            }

                            @Override
                            protected boolean matchesSafely(RatingBar item) {
                                return item.getRating() == rating;
                            }
                        }));
                sleep();
            }
            onView(withId(R.id.review))
                    .perform(click(),
                            typeText("I took this course."),
                            closeSoftKeyboard());
            onView(withId(R.id.submitBtn))
                    .perform(click());
            sleep();
        }
    }
}
