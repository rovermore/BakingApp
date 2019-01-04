package com.example.rovermore.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import com.example.rovermore.bakingapp.activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

@RunWith(AndroidJUnit4.class)
public class MainActivityRecyclerViewTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void checkIfRecyclerViewDisplays(){

        // verify the visibility of recycler view on screen
        onView(withId(R.id.recycler_view_main_activity)).check(matches(isDisplayed()));
        // perform click on view at 3rd position in RecyclerView
        onView(withId(R.id.recycler_view_main_activity))
                .perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));
    }
}
