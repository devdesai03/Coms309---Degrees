package com.example.degrees;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

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
public class AIChatClientActionTest {

    @Rule
    public ActivityScenarioRule<DashboardActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(DashboardActivity.class);

    @Test
    public void aIChatClientActionTest() {
        ViewInteraction materialTextView = onView(
                allOf(withId(R.id.textViewAdvisorsInstructors), withText("AI Chat"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.linearLayout),
                                        5),
                                0)));
        materialTextView.perform(scrollTo(), click());
//
//        ViewInteraction appCompatEditText = onView(
//                allOf(withId(R.id.et1),
//                        childAtPosition(
//                                childAtPosition(
//                                        withId(android.R.id.content),
//                                        0),
//                                1),
//                        isDisplayed()));
//        appCompatEditText.perform(replaceText("7"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.bt1), withText("Connect"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction appCompatEditText7 = onView(
                allOf(withId(R.id.et2),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText7.perform(replaceText("i want to make an academic plan"));

        ViewInteraction appCompatEditText8 = onView(
                allOf(withId(R.id.et2), withText("i want to make an academic plan"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText8.perform(closeSoftKeyboard());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.bt2), withText("Send request"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        materialButton2.perform(click());

        pressBack();

        ViewInteraction button = onView(
                allOf(withText("I want to talk to my advisor"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.messagesContainer),
                                        1),
                                1)));
        button.perform(scrollTo(), click());

        ViewInteraction appCompatEditText9 = onView(
                allOf(withId(R.id.et1),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                0),
                        isDisplayed()));
        appCompatEditText9.perform(replaceText("myAwesome"), closeSoftKeyboard());

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.bt1), withText("Connect"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                1),
                        isDisplayed()));
        materialButton3.perform(click());

        pressBack();

        ViewInteraction textView = onView(
                allOf(withText("I want to talk to my advisor"),
                        withClassName(is(TextView.class.getName())),
                        withParent(withParent(withId(R.id.messagesContainer)))))
                .perform(scrollTo());
        textView.check(matches(allOf(isDisplayed(),
                withText("I want to talk to my advisor"))));
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
