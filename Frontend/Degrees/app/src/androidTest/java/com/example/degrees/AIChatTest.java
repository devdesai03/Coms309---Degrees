package com.example.degrees;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AIChatTest {

    @Rule
    public ActivityScenarioRule<DashboardActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(DashboardActivity.class);

    @Test
    public void aIChatTest() {
        ViewInteraction materialTextView = onView(
                allOf(withId(R.id.textViewAdvisorsInstructors), withText("AI Chat"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.linearLayout),
                                        5),
                                0)));
        materialTextView.perform(scrollTo(), click());

//        ViewInteraction appCompatEditText = onView(
//                allOf(withId(R.id.et1),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                1),
//                        isDisplayed()));
//        appCompatEditText.perform(replaceText("7"), closeSoftKeyboard());
//
//        ViewInteraction appCompatEditText2 = onView(
//                allOf(withId(R.id.et1), withText("7"),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                1),
//                        isDisplayed()));
//        appCompatEditText2.perform(click());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.bt1), withText("Connect"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction button = onView(
                allOf(withText("Register me for a course"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.messagesContainer),
                                        1),
                                2)));
        button.perform(scrollTo(), click());

        ViewInteraction button2 = onView(
                allOf(withText("Computer Science"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.messagesContainer),
                                        3),
                                1)));
        button2.perform(scrollTo(), click());

        ViewInteraction button3 = onView(
                allOf(withText("Introduction to Computer Programming - Python"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.messagesContainer),
                                        5),
                                2)));
        button3.perform(scrollTo(), click());

        ViewInteraction button4 = onView(
                allOf(withText("Section 1"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.messagesContainer),
                                        7),
                                1)));
        button4.perform(scrollTo(), click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            //
        }

        ViewInteraction textView = onView(
                allOf(withText("Alright. Added you to the course section!"),
                        withParent(withParent(withId(R.id.messagesContainer)))
                        )).perform(scrollTo());
        textView.check(matches(allOf(isDisplayed(), withText("Alright. Added you to the course section!"))));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
