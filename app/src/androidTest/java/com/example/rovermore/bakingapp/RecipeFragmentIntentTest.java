package com.example.rovermore.bakingapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.runner.AndroidJUnitRunner;
import android.support.v4.app.FragmentManager;

import com.example.rovermore.bakingapp.activities.IngredientActivity;
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
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class RecipeFragmentIntentTest extends AndroidJUnitRunner {

    @Rule
    public IntentsTestRule<RecipeActivity> mActivityRule = new IntentsTestRule<>(
            RecipeActivity.class);

    @Before
    public void stubAllExternalIntents() {
        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.
        intending(not(isInternal())).respondWith(new ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void testIntentWhenClickRecyclerViewItem(){

        Bundle bundle = new Bundle();
        bundle.putInt(MainActivity.RECIPE_ID,1);
        bundle.putLong(StepFragment.PLAYBACK_POSITION_KEY,0);
        bundle.putBoolean(StepActivity.PLAY_WHRN_READY,false);
        bundle.putInt(StepActivity.STEP_ID,3);
        bundle.putBoolean(RecipeActivity.TWO_PANE_KEY, false);

        FragmentManager fragmentManager = mActivityRule.getActivity().getSupportFragmentManager();
        RecipeFragment recipeFragment = new RecipeFragment();
        recipeFragment.setArguments(bundle);

        fragmentManager.beginTransaction()
                .replace(R.id.recipe_fragment, recipeFragment)
                .commit();

        onView(withId(R.id.recycler_view_recipe_activity))
                .perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));

        intended(allOf(hasExtra(StepActivity.STEP_ID, 3),
                hasComponent(StepActivity.class.getName())));
    }

    @Test
    public void checkIfTextViewIngredientsClickable(){
        Bundle bundle = new Bundle();
        bundle.putInt(MainActivity.RECIPE_ID,1);
        bundle.putLong(StepFragment.PLAYBACK_POSITION_KEY,0);
        bundle.putBoolean(StepActivity.PLAY_WHRN_READY,false);
        bundle.putInt(StepActivity.STEP_ID,3);
        bundle.putBoolean(RecipeActivity.TWO_PANE_KEY, false);

        FragmentManager fragmentManager = mActivityRule.getActivity().getSupportFragmentManager();
        RecipeFragment recipeFragment = new RecipeFragment();
        recipeFragment.setArguments(bundle);

        fragmentManager.beginTransaction()
                .replace(R.id.recipe_fragment, recipeFragment)
                .commit();

        onView(withId(R.id.tv_ingredients)).perform(ViewActions.click());

        intended(allOf(hasExtra(MainActivity.RECIPE_ID, 1),
                hasComponent(IngredientActivity.class.getName())));
    }
}
