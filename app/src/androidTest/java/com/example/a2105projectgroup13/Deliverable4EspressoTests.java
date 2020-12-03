package com.example.a2105projectgroup13;

import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class Deliverable4EspressoTests {
    @Rule
    public ActivityTestRule<UserLogin> activityTestRule = new ActivityTestRule<UserLogin>(UserLogin.class);

    @Test
    public void checkViewBranchListButton() {
        //on UserLogin.class
        onView(withId(R.id.editTextEmailAddress)).perform(typeText("customer@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.editTextPassword)).perform(typeText("Password123"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        //on CustomerWelcomeActivity.class
        onView(withId(R.id.viewBranchListButton)).perform(click());
    }
}
