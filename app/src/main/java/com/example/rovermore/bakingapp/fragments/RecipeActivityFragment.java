package com.example.rovermore.bakingapp.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rovermore.bakingapp.R;
import com.example.rovermore.bakingapp.activities.IngredientActivity;
import com.example.rovermore.bakingapp.activities.MainActivity;
import com.example.rovermore.bakingapp.activities.StepActivity;
import com.example.rovermore.bakingapp.adapters.StepListAdapter;
import com.example.rovermore.bakingapp.datamodel.Step;
import com.example.rovermore.bakingapp.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class RecipeActivityFragment extends Fragment implements StepListAdapter.onStepClickHandler {


    private RecyclerView stepRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private StepListAdapter stepListAdapter;
    private int recipeId;

    public static final String LOG_TAG = "RecipeActivityFragment";

    public RecipeActivityFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);

        if(getArguments()!=null) recipeId = getArguments().getInt(MainActivity.RECIPE_ID);

        Log.v("RecipeFragment","the value of recipeId: " + recipeId);

        TextView ingredientsTextView = rootView.findViewById(R.id.tv_ingredients);
        ingredientsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SET INTENT TO START INGREDIENTS ACTIVITY
                Intent intent = new Intent(getActivity().getApplication(), IngredientActivity.class);
                intent.putExtra(MainActivity.RECIPE_ID,recipeId);
                Log.v("RecipeFragment","the value of recipeId: " + recipeId);
                startActivity(intent);
            }
        });

        stepRecyclerView = rootView.findViewById(R.id.recycler_view_recipe_activity);
        layoutManager = new LinearLayoutManager(rootView.getContext());
        stepListAdapter = new StepListAdapter(rootView.getContext(), null, this);
        stepRecyclerView.setLayoutManager(layoutManager);
        stepRecyclerView.setHasFixedSize(true);
        stepRecyclerView.setAdapter(stepListAdapter);

        new FetchRecipeSteps().execute(recipeId);
        Log.v("RecipeFragment","the value of recipeId: " + recipeId);
        return rootView;
    }

    @Override
    public void onStepClicked(Step step) {
        int stepId = step.getId();

        Intent intent = new Intent(getActivity().getApplication(), StepActivity.class);
        intent.putExtra(StepActivity.STEP_ID,stepId);
        intent.putExtra(MainActivity.RECIPE_ID,recipeId);
        startActivity(intent);
    }

    private class FetchRecipeSteps extends AsyncTask<Integer, Void, List<Step>>{

        @Override
        protected List<Step> doInBackground(Integer... integers) {
            List<Step> stepList = new ArrayList<>();

            URL url = NetworkUtils.urlBuilder();
            Log.v("RecipeFragment","Do in background the value of recipeId: " + integers[0]);

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
            if(stepListAdapter!=null) stepListAdapter.clearStepListAdapter();
            stepListAdapter.setStepList(stepList);
            //stepRecyclerView.setAdapter(stepListAdapter);
        }
    }

    public void setRecipeId(int recipeId){
        this.recipeId = recipeId;
    }
}
