package com.example.rovermore.bakingapp.fragments;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rovermore.bakingapp.R;
import com.example.rovermore.bakingapp.activities.MainActivity;
import com.example.rovermore.bakingapp.activities.StepActivity;
import com.example.rovermore.bakingapp.datamodel.Step;
import com.example.rovermore.bakingapp.utils.NetworkUtils;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class StepActivityFragment extends Fragment implements ExoPlayer.EventListener {

    private Step step;
    private int stepId;
    private int recipeId;
    private int listsize;

    private TextView shortDescriptionTextView;
    private TextView descriptionTextView;
    private TextView nextStepTextView;
    private TextView previousStepTextView;

    private SimpleExoPlayer player;
    private SimpleExoPlayerView mPlayerView;
    private long playbackPosition;
    private int currentWindow;
    private boolean playWhenReady = true;
    private String videoURL;

    public StepActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_step, container, false);

        if(getArguments()!=null){

            recipeId = getArguments().getInt(MainActivity.RECIPE_ID);
            stepId = getArguments().getInt(StepActivity.STEP_ID);
        }

        mPlayerView = rootView.findViewById(R.id.playerView);
        // Load the question mark as the background image until the user answers the question.
        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource
                (getResources(), R.drawable.accesorios_cocina));
        shortDescriptionTextView = rootView.findViewById(R.id.tv_short_description);
        descriptionTextView = rootView.findViewById((R.id.tv_description));

        nextStepTextView = rootView.findViewById(R.id.tv_next_step);
        nextStepTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stepId==listsize -1){
                    releasePlayer();
                    playbackPosition = 0;
                    stepId = 0;
                    new FetchRecipeSteps().execute(recipeId);
                }else{
                    releasePlayer();
                    playbackPosition = 0;
                    stepId++;
                    new FetchRecipeSteps().execute(recipeId);
                }
            }
        });
        previousStepTextView = rootView.findViewById(R.id.tv_previous_step);
        previousStepTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(stepId==0){
                    Toast.makeText(getActivity().getApplication(),"You are at the first step",Toast.LENGTH_SHORT).show();
                }else{
                    releasePlayer();
                    playbackPosition = 0;
                    stepId--;
                    new FetchRecipeSteps().execute(recipeId);
                }
            }
        });

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
            createUI(stepList);
        }
    }

    private void createUI(List<Step> stepList){
        listsize = stepList.size();
        step = stepList.get(stepId);
        String shortDescription = step.getShortDescription();
        String description = step.getDescription();
        shortDescriptionTextView.setText(shortDescription);
        descriptionTextView.setText(description);
        videoURL = step.getVideoURL();
        initializePlayer(videoURL);
    }

    private void initializePlayer(String videoURL) {
        if(player==null && videoURL!=null) {
            Context context = getActivity().getApplicationContext();
            player = ExoPlayerFactory.newSimpleInstance(context,
                    new DefaultTrackSelector(), new DefaultLoadControl());

            mPlayerView.setPlayer(player);

            player.setPlayWhenReady(playWhenReady);
            player.seekTo(currentWindow, playbackPosition);

            player.addListener(this);

            Uri uri = Uri.parse(videoURL);
            MediaSource mediaSource = buildMediaSource(uri);
            player.prepare(mediaSource);
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        MediaSource mediaSource = new ExtractorMediaSource(
                uri, new DefaultHttpDataSourceFactory("exoplayer-codelab"),
                new DefaultExtractorsFactory(),null, null);
        return mediaSource;
    }



    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer(videoURL);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer(videoURL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

}
