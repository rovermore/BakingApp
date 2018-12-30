package com.example.rovermore.bakingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.rovermore.bakingapp.R;
import com.example.rovermore.bakingapp.fragments.RecipeActivityFragment;

public class RecipeActivity extends AppCompatActivity {

    private int recipeId;
    public static final String LOG_TAG = RecipeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent intent = getIntent();
        recipeId = intent.getIntExtra(MainActivity.RECIPE_ID,1);

        Bundle bundle = new Bundle();
        bundle.putInt(MainActivity.RECIPE_ID,recipeId);

        RecipeActivityFragment recipeActivityFragment = new RecipeActivityFragment();
        recipeActivityFragment.setRecipeId(recipeId);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.recipe_fragment, recipeActivityFragment)
                .commit();

        Log.v(LOG_TAG,"the value of recipeId: " + recipeId);
    }

}
