package com.example.rovermore.bakingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.rovermore.bakingapp.R;
import com.example.rovermore.bakingapp.fragments.IngredientActivityFragment;
import com.example.rovermore.bakingapp.fragments.RecipeActivityFragment;
import com.example.rovermore.bakingapp.fragments.StepActivityFragment;

public final class RecipeActivity extends AppCompatActivity {

    public static final String LOG_TAG = RecipeActivity.class.getSimpleName();
    public static final String  TWO_PANE_KEY = "two_panel_key";

    private int recipeId;
    private int stepId;
    private boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        //Receiving intent and saving the value of the recipe clicked
        Intent intent = getIntent();
        recipeId = intent.getIntExtra(MainActivity.RECIPE_ID,1);
        stepId = intent.getIntExtra(StepActivity.STEP_ID,-1);


        //Setting up the bundle to pass as an argument to the fragments
        Bundle bundle = new Bundle();
        bundle.putInt(MainActivity.RECIPE_ID,recipeId);


        FragmentManager fragmentManager = getSupportFragmentManager();

        //Checking if we are in a tablet or in a phone
        if(findViewById(R.id.two_pane_linear_layout)!=null){

            //Setting up the variable and adding it to the bundle for tablet
            mTwoPane = true;
            bundle.putBoolean(TWO_PANE_KEY, mTwoPane);

            //Inflates the left side panel
            RecipeActivityFragment recipeActivityFragment = new RecipeActivityFragment();
            recipeActivityFragment.setArguments(bundle);

            //FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_fragment, recipeActivityFragment)
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
            RecipeActivityFragment recipeActivityFragment = new RecipeActivityFragment();
            recipeActivityFragment.setArguments(bundle);
            //FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_fragment, recipeActivityFragment)
                    .commit();

            Log.v(LOG_TAG, "the value of recipeId: " + recipeId);
        }
    }

    public void setIngredientsInRightPanel(Bundle bundle, FragmentManager fragmentManager){
        int recipeId = bundle.getInt(MainActivity.RECIPE_ID);

        //Inflates the right side panel with the ingredient list fragment
        IngredientActivityFragment ingredientActivityFragment = new IngredientActivityFragment();
        ingredientActivityFragment.setArguments(bundle);

        fragmentManager.beginTransaction()
                .add(R.id.right_panel, ingredientActivityFragment)
                .commit();
        Log.v(LOG_TAG,"the value of recipeId: " + recipeId);
    }

    public void setStepInRightPanel(Bundle bundle, FragmentManager fragmentManager){

        StepActivityFragment stepActivityFragment = new StepActivityFragment();
        stepActivityFragment.setArguments(bundle);

        fragmentManager.beginTransaction()
                .add(R.id.right_panel, stepActivityFragment)
                .commit();
    }
}
