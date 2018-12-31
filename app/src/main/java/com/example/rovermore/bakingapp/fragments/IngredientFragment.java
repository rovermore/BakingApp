package com.example.rovermore.bakingapp.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rovermore.bakingapp.R;
import com.example.rovermore.bakingapp.activities.MainActivity;
import com.example.rovermore.bakingapp.adapters.IngredientAdapter;
import com.example.rovermore.bakingapp.datamodel.Ingredient;
import com.example.rovermore.bakingapp.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class IngredientFragment extends Fragment {

    private int recipeId;
    private String recipeName;
    private IngredientAdapter ingredientAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;

    public IngredientFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_ingedient, container, false);

        if(getArguments()!=null){
            recipeId = getArguments().getInt(MainActivity.RECIPE_ID);
            recipeName = getArguments().getString(MainActivity.RECIPE_NAME);
        }

        getActivity().setTitle(recipeName);

        recyclerView = rootView.findViewById(R.id.recycler_view_ingredients);
        layoutManager = new LinearLayoutManager(rootView.getContext());
        ingredientAdapter = new IngredientAdapter(rootView.getContext(),null);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(ingredientAdapter);

        new FetchIngredients().execute(recipeId);
        Log.v("IngredientsFragment","the value of recipeId: " + recipeId);

        return rootView;
    }

    private class FetchIngredients extends AsyncTask<Integer, Void, List<Ingredient>>{

        @Override
        protected List<Ingredient> doInBackground(Integer... integers) {
            List<Ingredient> ingredientList = new ArrayList<>();

            URL url = NetworkUtils.urlBuilder();

            try {
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
                ingredientList = NetworkUtils.parseIngredientsJson(jsonResponse,integers[0]);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return ingredientList;
        }

        @Override
        protected void onPostExecute(List<Ingredient> ingredients) {
            super.onPostExecute(ingredients);
            if(ingredientAdapter!=null) ingredientAdapter.clearIngredientList();
            ingredientAdapter.setIngredientList(ingredients);
        }
    }

}
