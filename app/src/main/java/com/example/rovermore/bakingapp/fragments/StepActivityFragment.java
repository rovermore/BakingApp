package com.example.rovermore.bakingapp.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rovermore.bakingapp.R;
import com.example.rovermore.bakingapp.datamodel.Step;
import com.example.rovermore.bakingapp.utils.NetworkUtils;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class StepActivityFragment extends Fragment {

    private Step step;
    private int stepId;
    private int recipeId;

    private TextView shortDescriptionTextView;
    private TextView descriptionTextView;
    private TextView nextStepTextView;
    private TextView previousStepTextView;

    private SimpleExoPlayer mExoPlayer;
    private SimpleExoPlayerView mPlayerView;


    public StepActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_step, container, false);

        mPlayerView = rootView.findViewById(R.id.playerView);
        shortDescriptionTextView = rootView.findViewById(R.id.tv_short_description);
        descriptionTextView = rootView.findViewById((R.id.tv_description));
        //nextStepTextView = rootView.findViewById(R.id.tv_next_step);
        //previousStepTextView = rootView.findViewById(R.id.tv_previous_step);

        new FetchRecipeSteps().execute(recipeId);

        return rootView;
    }

    private class FetchRecipeSteps extends AsyncTask<Integer, Void, List<Step>> {

        @Override
        protected List<Step> doInBackground(Integer... integers) {
            List<Step> stepList = new ArrayList<>();

            URL url = NetworkUtils.urlBuilder();

            try {
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
                stepList = NetworkUtils.parseStepsJson(jsonResponse,integers[0]);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return stepList;
        }

        @Override
        protected void onPostExecute(List<Step> stepList) {
            super.onPostExecute(stepList);

            step = stepList.get(stepId);
            String shortDescription = step.getShortDescription();
            String description = step.getDescription();
            shortDescriptionTextView.setText(shortDescription);
            descriptionTextView.setText(description);
        }
    }

    public void setStepId(int stepId){
        this.stepId = stepId;
    }
}
