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
    public ActivityTestRule<CustomerBranchSearch> activityTestRule = new ActivityTestRule<CustomerBranchSearch>(CustomerBranchSearch.class);

    //TEST 1
    @Test
    public void checkSearchByMondayHours() {
        onView(withId(R.id.searchByHoursRB)).perform(click());
        onView(withId(R.id.monButton)).perform(click());
        onView(withId(R.id.timeEditText)).perform(typeText("12:00"), closeSoftKeyboard());
        onView(withId(R.id.searchButton)).perform(click());
    }

    //TEST 2
    @Test
    public void checkSearchByTuesdayHours() {
        onView(withId(R.id.searchByHoursRB)).perform(click());
        onView(withId(R.id.tuesButton)).perform(click());
        onView(withId(R.id.timeEditText)).perform(typeText("12:00"), closeSoftKeyboard());
        onView(withId(R.id.searchButton)).perform(click());
    }

    //TEST 3
    @Test
    public void checkSearchByWednesdayHours() {
        onView(withId(R.id.searchByHoursRB)).perform(click());
        onView(withId(R.id.wedButton)).perform(click());
        onView(withId(R.id.timeEditText)).perform(typeText("12:00"), closeSoftKeyboard());
        onView(withId(R.id.searchButton)).perform(click());
    }

    //TEST 4
    @Test
    public void checkSearchByThursdayHours() {
        onView(withId(R.id.searchByHoursRB)).perform(click());
        onView(withId(R.id.thurButton)).perform(click());
        onView(withId(R.id.timeEditText)).perform(typeText("12:00"), closeSoftKeyboard());
        onView(withId(R.id.searchButton)).perform(click());
    }

    //TEST 5
    @Test
    public void checkSearchByFridayHours() {
        onView(withId(R.id.searchByHoursRB)).perform(click());
        onView(withId(R.id.friButton)).perform(click());
        onView(withId(R.id.timeEditText)).perform(typeText("12:00"), closeSoftKeyboard());
        onView(withId(R.id.searchButton)).perform(click());
    }

    //TEST 6
    @Test
    public void checkSearchBySaturdayHours() {
        onView(withId(R.id.searchByHoursRB)).perform(click());
        onView(withId(R.id.satButton)).perform(click());
        onView(withId(R.id.timeEditText)).perform(typeText("12:00"), closeSoftKeyboard());
        onView(withId(R.id.searchButton)).perform(click());
    }


    //TEST 7
    @Test
    public void checkSearchBySundayHours() {
        onView(withId(R.id.searchByHoursRB)).perform(click());
        onView(withId(R.id.sunButton)).perform(click());
        onView(withId(R.id.timeEditText)).perform(typeText("12:00"), closeSoftKeyboard());
        onView(withId(R.id.searchButton)).perform(click());
    }


    //TEST 8
    @Test
    public void checkSearchByAddressCity() {
        onView(withId(R.id.searchByAddressRB)).perform(click());
        onView(withId(R.id.citySearch)).perform(typeText("Nottawa"), closeSoftKeyboard());
        onView(withId(R.id.searchButton)).perform(click());
    }


    //TEST 9
    @Test
    public void checkSearchByAddressStreetAddress() {
        onView(withId(R.id.searchByAddressRB)).perform(click());
        onView(withId(R.id.addressSearch)).perform(typeText("Sesame Street"), closeSoftKeyboard());
        onView(withId(R.id.searchButton)).perform(click());
    }


    //TEST 10
    @Test
    public void checkSearchByService() {
        onView(withId(R.id.searchByServiceRB)).perform(click());
        onView(withId(R.id.searchButton)).perform(click());
    }
}
