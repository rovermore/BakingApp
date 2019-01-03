package com.example.rovermore.bakingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.rovermore.bakingapp.R;
import com.example.rovermore.bakingapp.fragments.IngredientFragment;
import com.example.rovermore.bakingapp.fragments.RecipeFragment;
import com.example.rovermore.bakingapp.fragments.StepFragment;

public final class RecipeActivity extends AppCompatActivity implements StepFragment.OnDataPass {

    public static final String LOG_TAG = RecipeActivity.class.getSimpleName();
    public static final String  TWO_PANE_KEY = "two_panel_key";

    private int recipeId;
    private int stepId;
    private String recipeName;
    private boolean mTwoPane;

    private long playbackPosition;
    private boolean playWhenReady;

    private StepFragment stepFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        if(savedInstanceState!=null){
            stepId = savedInstanceState.getInt(StepActivity.STEP_ID);
            playbackPosition = savedInstanceState.getLong(StepFragment.PLAYBACK_POSITION_KEY);
            playWhenReady = savedInstanceState.getBoolean(StepActivity.PLAY_WHRN_READY);
        }

        //Receiving intent and saving the value of the recipe clicked
        Intent intent = getIntent();
        recipeId = intent.getIntExtra(MainActivity.RECIPE_ID,1);
        if(savedInstanceState==null)stepId = intent.getIntExtra(StepActivity.STEP_ID,-1);
        recipeName = intent.getStringExtra(MainActivity.RECIPE_NAME);

        //Setting up the bundle to pass as an argument to the fragments
        Bundle bundle = new Bundle();
        bundle.putInt(MainActivity.RECIPE_ID,recipeId);
        bundle.putString(MainActivity.RECIPE_NAME,recipeName);
        bundle.putLong(StepFragment.PLAYBACK_POSITION_KEY,playbackPosition);
        bundle.putBoolean(StepActivity.PLAY_WHRN_READY,playWhenReady);
        bundle.putInt(StepActivity.STEP_ID,stepId);

        fragmentManager = getSupportFragmentManager();

        //Checking if we are in a tablet or in a phone
        if(findViewById(R.id.two_pane_linear_layout)!=null){

            //Setting up the variable and adding it to the bundle for tablet
            mTwoPane = true;
            bundle.putBoolean(TWO_PANE_KEY, mTwoPane);

            //Inflates the left side panel
            RecipeFragment recipeFragment = new RecipeFragment();
            recipeFragment.setArguments(bundle);

            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_fragment, recipeFragment)
                    .commit();

            if(stepId==-1) {
                //Inflates the right side panel with the ingredient list fragment
                setIngredientsInRightPanel(bundle, fragmentManager);
            }else{

                bundle.putInt(StepActivity.STEP_ID,stepId);
                //Inflates the right side panel with the step in the stepId
                setStepInRightPanel(bundle, fragmentManager);
            }

        }else {

            //Setting up the variable and adding it to the bundle for phone
            mTwoPane = false;
            bundle.putBoolean(TWO_PANE_KEY, mTwoPane);

            //Inflates the fragment for the activity in phone
            RecipeFragment recipeFragment = new RecipeFragment();
            recipeFragment.setArguments(bundle);


            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_fragment, recipeFragment)
                    .commit();

            Log.v(LOG_TAG, "the value of recipeId: " + recipeId);
        }
    }

    public void setIngredientsInRightPanel(Bundle bundle, FragmentManager fragmentManager){
        int recipeId = bundle.getInt(MainActivity.RECIPE_ID);

        //Inflates the right side panel with the ingredient list fragment
        IngredientFragment ingredientFragment = new IngredientFragment();
        ingredientFragment.setArguments(bundle);

        fragmentManager.beginTransaction()
                .replace(R.id.right_panel, ingredientFragment)
                .commit();
        Log.v(LOG_TAG,"the value of recipeId: " + recipeId);
    }

    public void setStepInRightPanel(Bundle bundle, FragmentManager fragmentManager){

        stepFragment = new StepFragment();

        //Saving the fragment if was instanced anytime
        Fragment fragment =  fragmentManager.findFragmentByTag(String.valueOf(stepId));

        if(fragment!=null) {
            //as the fragment was already instanced we cast it in the specific fragment type (StepFragment)
            StepFragment stepFragment = (StepFragment) fragment;
            stepFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.right_panel, stepFragment, String.valueOf(stepId))
                    .commit();
        } else {
            stepFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.right_panel, stepFragment, String.valueOf(stepId))
                    .commit();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        //Saving the fragment if was instanced anytime
        Fragment fragment =  fragmentManager.findFragmentByTag(String.valueOf(stepId));

        if(fragment!=null) {
            //as the fragment was already instanced we cast it in the specific fragment type (StepFragment)
            StepFragment stepFragment = (StepFragment) fragment;
            stepFragment.releasePlayer(false);
        }
    }

    @Override
    public void onDataPass(long currentPlayPosition, int id, boolean playWhenReady) {
        playbackPosition = currentPlayPosition;
        stepId = id;
        this.playWhenReady = playWhenReady;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(StepFragment.PLAYBACK_POSITION_KEY,playbackPosition);
        outState.putInt(StepActivity.STEP_ID,stepId);
        outState.putBoolean(StepActivity.PLAY_WHRN_READY, playWhenReady);
    }
}
