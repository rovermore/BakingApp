package com.example.rovermore.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.FragmentManager;

import com.example.rovermore.bakingapp.activities.MainActivity;
import com.example.rovermore.bakingapp.activities.RecipeActivity;
import com.example.rovermore.bakingapp.activities.StepActivity;
import com.example.rovermore.bakingapp.fragments.RecipeFragment;
import com.example.rovermore.bakingapp.fragments.StepFragment;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class RecipeFragmentRecyclerViewTest {

    @Rule
    public ActivityTestRule<RecipeActivity> mActivityTestRule =
            new ActivityTestRule<>(RecipeActivity.class);


    @Before
    public void setup() {
        Intent MY_ACTIVITY_INTENT = new Intent();
        mActivityTestRule.launchActivity(MY_ACTIVITY_INTENT);
    }

    @Test
    public void checkIfRecyclerViewDisplays() {

        Bundle bundle = new Bundle();
        bundle.putInt(MainActivity.RECIPE_ID,1);
        bundle.putLong(StepFragment.PLAYBACK_POSITION_KEY,0);
        bundle.putBoolean(StepActivity.PLAY_WHRN_READY,false);
        bundle.putInt(StepActivity.STEP_ID,3);

        FragmentManager fragmentManager = mActivityTestRule.getActivity().getSupportFragmentManager();
        RecipeFragment recipeFragment = new RecipeFragment();
        recipeFragment.setArguments(bundle);

        fragmentManager.beginTransaction()
                .replace(R.id.recipe_fragment, recipeFragment)
                .commit();

        // verify the visibility of recycler view on screen
        onView(withId(R.id.recycler_view_recipe_activity)).check(matches(isDisplayed()));

    }

    @Test
    public void checkIfRecyclerViewIsClickable(){
        Bundle bundle = new Bundle();
        bundle.putInt(MainActivity.RECIPE_ID,1);
        bundle.putLong(StepFragment.PLAYBACK_POSITION_KEY,0);
        bundle.putBoolean(StepActivity.PLAY_WHRN_READY,false);
        bundle.putInt(StepActivity.STEP_ID,3);

        FragmentManager fragmentManager = mActivityTestRule.getActivity().getSupportFragmentManager();
        RecipeFragment recipeFragment = new RecipeFragment();
        recipeFragment.setArguments(bundle);

        fragmentManager.beginTransaction()
                .replace(R.id.recipe_fragment, recipeFragment)
                .commit();

        // perform click on view at 3rd position in RecyclerView
        onView(withId(R.id.recycler_view_recipe_activity))
        .perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));
    }

}
