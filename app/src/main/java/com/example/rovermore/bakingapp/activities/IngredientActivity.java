package com.example.rovermore.bakingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.rovermore.bakingapp.R;
import com.example.rovermore.bakingapp.fragments.IngredientActivityFragment;

public class IngredientActivity extends AppCompatActivity {

    private int recipeId;

    public static final String LOG_TAG = IngredientActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingedient);

        Intent intent = getIntent();
        recipeId = intent.getIntExtra(MainActivity.RECIPE_ID,1);

        Bundle bundle = new Bundle();
        bundle.putInt(MainActivity.RECIPE_ID,recipeId);

        IngredientActivityFragment ingredientActivityFragment = new IngredientActivityFragment();
        ingredientActivityFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fragment_ingredients, ingredientActivityFragment)
                .commit();
        Log.v(LOG_TAG,"the value of recipeId: " + recipeId);
    }

}
