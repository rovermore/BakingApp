package com.example.rovermore.bakingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.rovermore.bakingapp.R;
import com.example.rovermore.bakingapp.fragments.StepFragment;

public class StepActivity extends AppCompatActivity implements StepFragment.OnDataPass{

    public static final String STEP_ID = "step_id";
    public static final String PLAY_WHRN_READY = "play_when_ready";

    private long playbackPosition;
    private int stepId;
    private boolean playWhenReady;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        if(savedInstanceState!=null){
            playbackPosition = savedInstanceState.getLong(StepFragment.PLAYBACK_POSITION_KEY);
            stepId = savedInstanceState.getInt(STEP_ID);
            playWhenReady = savedInstanceState.getBoolean(PLAY_WHRN_READY);
        }

        Intent intent = getIntent();
        if(savedInstanceState==null) stepId = intent.getIntExtra(STEP_ID,1);
        int recipeId = intent.getIntExtra(MainActivity.RECIPE_ID,1);

        Bundle bundle = new Bundle();
        bundle.putInt(STEP_ID,stepId);
        bundle.putInt(MainActivity.RECIPE_ID,recipeId);
        bundle.putLong(StepFragment.PLAYBACK_POSITION_KEY,playbackPosition);
        bundle.putBoolean(PLAY_WHRN_READY, playWhenReady);

        fragmentManager = getSupportFragmentManager();

        //Saving the fragment if was instanced anytime
        Fragment fragment =  fragmentManager.findFragmentByTag(String.valueOf(stepId));
        //checks if the fragment was instanced
        if(fragment!=null) {
            //as the fragment was already instanced we cast it in the specific fragment type (StepFragment)
            StepFragment stepFragment = (StepFragment) fragment;
            //adding the parameters
            stepFragment.setArguments(bundle);
            //replacing it
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_step, stepFragment, String.valueOf(stepId))
                    .commit();
        } else {
            //the fragment was never instanced so we instance it as new
            StepFragment stepFragment = new StepFragment();
            stepFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_step, stepFragment, String.valueOf(stepId))
                    .commit();
        }
    }

    @Override
    public void onDataPass(long currentPlayPosition, int id, boolean playWhenReady) {
        playbackPosition = currentPlayPosition;
        stepId = id;
        this.playWhenReady = playWhenReady;
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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(StepFragment.PLAYBACK_POSITION_KEY,playbackPosition);
        outState.putInt(STEP_ID,stepId);
        outState.putBoolean(PLAY_WHRN_READY, playWhenReady);
    }
}
