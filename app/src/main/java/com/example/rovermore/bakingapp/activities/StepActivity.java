package com.example.rovermore.bakingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.rovermore.bakingapp.R;
import com.example.rovermore.bakingapp.fragments.StepActivityFragment;

public class StepActivity extends AppCompatActivity {

    public static final String RECIPE_ID = "recipe_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        Intent intent = getIntent();

        int stepId = intent.getIntExtra(RECIPE_ID,1);

        StepActivityFragment stepActivityFragment = new StepActivityFragment();
        stepActivityFragment.setStepId(stepId);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fragment_step, stepActivityFragment)
                .commit();

    }

}
