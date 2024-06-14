package com.example.degrees;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.degrees.R;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class CreateUserTest {

    @Rule
    public ActivityScenarioRule<CreateUserActivity> activityScenarioRule =
            new ActivityScenarioRule<>(CreateUserActivity.class);

    @Test
    public void testUserCreation() {
        // Delay for a short period to observe the result (optional)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if the "Sign Up" button is displayed
        Espresso.onView(ViewMatchers.withId(R.id.btnSignUp))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        // Enter valid user data
        Espresso.onView(ViewMatchers.withId(R.id.edtSignUpFullName))
                .perform(ViewActions.typeText("John Doe"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.edtSignUpEmail))
                .perform(ViewActions.typeText("john.doe@example.com"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.edtSignUpMobile))
                .perform(ViewActions.typeText("1234567890"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.edtSignUpPassword))
                .perform(ViewActions.typeText("password123"), ViewActions.closeSoftKeyboard());
        Espresso.onView(ViewMatchers.withId(R.id.edtSignUpConfirmPassword))
                .perform(ViewActions.typeText("password123"), ViewActions.closeSoftKeyboard());

        // Perform click on the "Sign Up" button
        Espresso.onView(ViewMatchers.withId(R.id.btnSignUp))
                .perform(ViewActions.click());

        // Delay for a short period to observe the result (optional)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if the user is navigated to the MainActivity after successful user creation
//        Espresso.onView(ViewMatchers.withId(R.id.activity_main))
//                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

    }
}
