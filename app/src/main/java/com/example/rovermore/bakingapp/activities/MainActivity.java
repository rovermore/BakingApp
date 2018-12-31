package com.example.rovermore.bakingapp.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.rovermore.bakingapp.R;
import com.example.rovermore.bakingapp.adapters.MainAdapter;
import com.example.rovermore.bakingapp.datamodel.Recipe;
import com.example.rovermore.bakingapp.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainAdapter.onRecipeClickListener {

    private RecyclerView recyclerView;
    private MainAdapter recipeAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public static final String RECIPE_ID = "recipe_id";
    public static final String RECIPE_NAME = "recipe_name";
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new FetchRecipes().execute(NetworkUtils.RECIPE_URL_STRING);
    }


    @Override
    public void onRecipeClicked(int position, String recipeName) {
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra(RECIPE_ID,position);
        intent.putExtra(RECIPE_NAME,recipeName);
        Log.v(LOG_TAG,"the value of recipeId: " + position);
        startActivity(intent);
    }

    private class FetchRecipes extends AsyncTask<String,Void,List<Recipe>>{

        @Override
        protected List<Recipe> doInBackground(String... string) {
            List<Recipe> recipeList = new ArrayList<>();

            URL url =  NetworkUtils.urlBuilder();

            try {
                String jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);

                recipeList = NetworkUtils.parseJson(jsonResponse);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return recipeList;
        }

        @Override
        protected void onPostExecute(List<Recipe> recipes) {
            super.onPostExecute(recipes);

            createUI(recipes);
        }
    }

    private void createUI(List<Recipe> recipes) {
        recyclerView = findViewById(R.id.recycler_view_main_activity);
        if (findViewById(R.id.tablet_frame_layout) != null) {
            layoutManager = new GridLayoutManager(this, 2);
        } else {
            layoutManager = new LinearLayoutManager(this);
        }
        recipeAdapter = new MainAdapter(this, recipes, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recipeAdapter);
    }
}
