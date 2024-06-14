package com.example.degrees;

import android.content.Intent;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class VerificationTest {

    @Rule
    public IntentsTestRule<VerificationActivity> activityRule =
            new IntentsTestRule<>(VerificationActivity.class, false, false);

    @Test
    public void testVerificationProcess() {
        // Create an intent with the required email
        Intent intent = new Intent();
        intent.putExtra("email", "test@example.com");
        activityRule.launchActivity(intent);

        // Perform UI interactions with Espresso
        Espresso.onView(ViewMatchers.withId(R.id.editTextVerificationCode))
                .perform(ViewActions.typeText("YOUR_VERIFICATION_CODE"), ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.buttonVerify)).perform(ViewActions.click());

         Espresso.onView(ViewMatchers.withText("Verification failed. Please check your network connection."))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
