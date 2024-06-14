package com.example.degrees;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.DataInteraction;
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
public class ManageUsersTest {

    @Rule
    public ActivityScenarioRule<DashboardActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(DashboardActivity.class);

    @Test
    public void manageUsersTest() {
        ViewInteraction materialTextView = onView(
                allOf(withId(R.id.manageUsers), withText("List Users"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.linearLayout),
                                        1),
                                0)));
        materialTextView.perform(scrollTo(), click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.search_button),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("Blessing"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.search_button), withText("Blessing"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText2.perform(pressImeActionButton());

        ViewInteraction materialButton = onView(
                allOf(withId(R.id.refreshButton), withText("Refresh"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction button = onView(
                allOf(withId(R.id.editButton), withText("Edit"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.cardview.widget.CardView")),
                                        0),
                                1),
                        isDisplayed()));
        button.perform(click());

        ViewInteraction textInputEditText = onView(
                allOf(withId(R.id.userName), withText("Blessing"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0)));
        textInputEditText.perform(scrollTo(), replaceText("Blessing2"));

        ViewInteraction textInputEditText2 = onView(
                allOf(withId(R.id.userName), withText("Blessing2"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0))).perform(scrollTo());
        textInputEditText2.perform(closeSoftKeyboard());

        ViewInteraction textInputEditText3 = onView(
                allOf(withId(R.id.userMobile), withText("5155555555"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0))).perform(scrollTo());
        textInputEditText3.perform(replaceText("5155555556"));

        ViewInteraction textInputEditText4 = onView(
                allOf(withId(R.id.userMobile), withText("5155555556"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0))).perform(scrollTo());
        textInputEditText4.perform(closeSoftKeyboard());

        ViewInteraction textInputEditText5 = onView(
                allOf(withId(R.id.userEmail), withText("monim@gmail.com"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0)));
        textInputEditText5.perform(scrollTo(), replaceText("monim2@gmail.com"));

        ViewInteraction textInputEditText6 = onView(
                allOf(withId(R.id.userEmail), withText("monim2@gmail.com"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0))).perform(scrollTo());
        textInputEditText6.perform(closeSoftKeyboard());

        ViewInteraction textInputEditText7 = onView(
                allOf(withId(R.id.userAddress),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0)));
        textInputEditText7.perform(scrollTo(), replaceText("1"), closeSoftKeyboard());

        ViewInteraction textInputEditText8 = onView(
                allOf(withId(R.id.userAddress), withText("1"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0)));
        textInputEditText8.perform(scrollTo(), click());

        ViewInteraction textInputEditText9 = onView(
                allOf(withId(R.id.userAddress), withText("1"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0)));
        textInputEditText9.perform(scrollTo(), replaceText(""));

        ViewInteraction textInputEditText10 = onView(
                allOf(withId(R.id.userAddress),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0))).perform(scrollTo());
        textInputEditText10.perform(closeSoftKeyboard());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.isuRegistrationToggle), withText("Add"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        7),
                                1)));
        materialButton2.perform(scrollTo(), click());

        ViewInteraction textInputEditText11 = onView(
                allOf(withId(R.id.isuGivenName),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0))).perform(scrollTo());
        textInputEditText11.perform(replaceText("Blessing"), closeSoftKeyboard());

        ViewInteraction textInputEditText12 = onView(
                allOf(withId(R.id.isuMiddleName),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0))).perform(scrollTo());
        textInputEditText12.perform(replaceText("Banana"), closeSoftKeyboard());

        ViewInteraction textInputEditText13 = onView(
                allOf(withId(R.id.isuSurname),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0)
                        )).perform(scrollTo());
        textInputEditText13.perform(replaceText("Apple"), closeSoftKeyboard());

        ViewInteraction textInputEditTextNetId = onView(
                allOf(withId(R.id.isuNetId),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0)
                )).perform(scrollTo());
        textInputEditTextNetId.perform(replaceText("blessingNetId"), closeSoftKeyboard());


        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.instructorToggle), withText("Add"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.isuRegistrationSection),
                                        5),
                                1)));
        materialButton3.perform(scrollTo(), click());

        ViewInteraction appCompatSpinner = onView(
                allOf(withId(R.id.instructorDepartment),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.instructorSection),
                                        0),
                                1))).perform(scrollTo());
        appCompatSpinner.perform(click());

        DataInteraction appCompatCheckedTextView = onData(anything())
                .inRoot(isPlatformPopup())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(1);
        appCompatCheckedTextView.perform(click());

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.advisorToggle), withText("Add"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.isuRegistrationSection),
                                        7),
                                1)));
        materialButton4.perform(scrollTo(), click());

        ViewInteraction appCompatSpinner2 = onView(
                allOf(withId(R.id.advisorDepartment),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.advisorSection),
                                        0),
                                1))).perform(scrollTo());
        appCompatSpinner2.perform(click());

        DataInteraction appCompatCheckedTextView2 = onData(anything())
                .inRoot(isPlatformPopup())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(1);
        appCompatCheckedTextView2.perform(click());

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.studentToggle), withText("Add"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.isuRegistrationSection),
                                        9),
                                1)));
        materialButton5.perform(scrollTo(), click());

        ViewInteraction appCompatSpinner3 = onView(
                allOf(withId(R.id.studentAdvisor),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.studentSection),
                                        0),
                                1))).perform(scrollTo());
        appCompatSpinner3.perform(click());

        DataInteraction appCompatCheckedTextView4 = onData(anything())
                .inRoot(isPlatformPopup())
                .inAdapterView(childAtPosition(
                        withClassName(is("android.widget.PopupWindow$PopupBackgroundView")),
                        0))
                .atPosition(1);
        appCompatCheckedTextView4.perform(click());

        ViewInteraction materialButton6 = onView(
                allOf(withId(R.id.Save), withText("Save"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                9)));
        materialButton6.perform(scrollTo(), click());

        ViewInteraction materialButton7 = onView(
                allOf(withId(R.id.instructorToggle), withText("Delete"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.isuRegistrationSection),
                                        5),
                                1)));
        materialButton7.perform(scrollTo(), click());

        ViewInteraction materialButton8 = onView(
                allOf(withId(R.id.advisorToggle), withText("Delete"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.isuRegistrationSection),
                                        7),
                                1)));
        materialButton8.perform(scrollTo(), click());

        ViewInteraction materialButton9 = onView(
                allOf(withId(R.id.studentToggle), withText("Delete"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.isuRegistrationSection),
                                        9),
                                1)));
        materialButton9.perform(scrollTo(), click());

        ViewInteraction materialButton10 = onView(
                allOf(withId(R.id.isuRegistrationToggle), withText("Delete"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        7),
                                1)));
        materialButton10.perform(scrollTo(), click());

        ViewInteraction textInputEditText14 = onView(
                allOf(withId(R.id.userName), withText("Blessing2"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0)));
        textInputEditText14.perform(scrollTo(), replaceText("Blessing"));

        ViewInteraction textInputEditText15 = onView(
                allOf(withId(R.id.userName), withText("Blessing"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0))).perform(scrollTo());
        textInputEditText15.perform(closeSoftKeyboard());

        ViewInteraction textInputEditText16 = onView(
                allOf(withId(R.id.userEmail), withText("monim2@gmail.com"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0)));
        textInputEditText16.perform(scrollTo(), replaceText("monim@gmail.com"));

        ViewInteraction textInputEditText17 = onView(
                allOf(withId(R.id.userEmail), withText("monim@gmail.com"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0))).perform(scrollTo());
        textInputEditText17.perform(closeSoftKeyboard());

        ViewInteraction textInputEditText18 = onView(
                allOf(withId(R.id.userMobile), withText("5155555556"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0))).perform(scrollTo());
        textInputEditText18.perform(replaceText("5155555555"));

        ViewInteraction textInputEditText19 = onView(
                allOf(withId(R.id.userMobile), withText("5155555555"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("com.google.android.material.textfield.TextInputLayout")),
                                        0),
                                0))).perform(scrollTo());
        textInputEditText19.perform(closeSoftKeyboard());

        ViewInteraction materialButton11 = onView(
                allOf(withId(R.id.Save), withText("Save"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                9)));
        materialButton11.perform(scrollTo(), click());
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
