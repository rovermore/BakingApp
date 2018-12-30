package com.example.rovermore.bakingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.rovermore.bakingapp.R;
import com.example.rovermore.bakingapp.fragments.StepActivityFragment;

public class StepActivity extends AppCompatActivity {

    public static final String STEP_ID = "step_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        Intent intent = getIntent();

        int stepId = intent.getIntExtra(STEP_ID,1);
        int recipeId = intent.getIntExtra(MainActivity.RECIPE_ID,1);

        Bundle bundle = new Bundle();
        bundle.putInt(STEP_ID,stepId);
        bundle.putInt(MainActivity.RECIPE_ID,recipeId);

        StepActivityFragment stepActivityFragment = new StepActivityFragment();
        stepActivityFragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fragment_step, stepActivityFragment)
                .commit();

    }

}
