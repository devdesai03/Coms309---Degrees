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
public class AdvisorChatTest {

    @Rule
    public ActivityScenarioRule<AdvisorChatActivity> activityScenarioRule =
            new ActivityScenarioRule<>(AdvisorChatActivity.class);

    @Test
    public void testChatFunctionality() {
        // Type the username in the username EditText
        Espresso.onView(ViewMatchers.withId(R.id.et1))
                .perform(ViewActions.typeText("TestAdvisor"), ViewActions.closeSoftKeyboard());

        // Click the Connect button
        Espresso.onView(ViewMatchers.withId(R.id.bt1))
                .perform(ViewActions.click());

        // Delay for a short period to observe the result (optional)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Test sending a message
        Espresso.onView(ViewMatchers.withId(R.id.et2))
                .perform(ViewActions.typeText("Hello, this is a test message."), ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.bt2))
                .perform(ViewActions.click());

        // Check if the sent message is displayed in the chat area
        Espresso.onView(ViewMatchers.withId(R.id.tx1))
                .check(ViewAssertions.matches(ViewMatchers.withText("Hello, this is a test message.")));

        // Test getting breakout rooms
        Espresso.onView(ViewMatchers.withId(R.id.getBreakoutRoomsBtn))
                .perform(ViewActions.click());

        // Delay for a short period to observe the result (optional)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if the breakout rooms data is displayed in the chat area
        Espresso.onView(ViewMatchers.withId(R.id.tx1))
                .check(ViewAssertions.matches(ViewMatchers.withText("Breakout Rooms Data")));

        // Test getting online users
        Espresso.onView(ViewMatchers.withId(R.id.getOnlineUsersBtn))
                .perform(ViewActions.click());

        // Delay for a short period to observe the result (optional)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if the online users data is displayed in the chat area
        Espresso.onView(ViewMatchers.withId(R.id.tx1))
                .check(ViewAssertions.matches(ViewMatchers.withText("Online Users Data")));

        // Test getting chat history
        Espresso.onView(ViewMatchers.withId(R.id.getChatHistoryBtn))
                .perform(ViewActions.click());

        // Delay for a short period to observe the result (optional)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if the chat history data is displayed in the chat area
        Espresso.onView(ViewMatchers.withId(R.id.tx1))
                .check(ViewAssertions.matches(ViewMatchers.withText("Chat History Data")));
    }
}
