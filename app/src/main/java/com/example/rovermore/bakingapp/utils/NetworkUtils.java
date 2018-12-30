package com.example.rovermore.bakingapp.utils;

import android.net.Uri;
import android.util.Log;

import com.example.rovermore.bakingapp.datamodel.Ingredient;
import com.example.rovermore.bakingapp.datamodel.Recipe;
import com.example.rovermore.bakingapp.datamodel.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    public static final String RECIPE_URL_STRING =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";


    /**
     * Builds the URL used to talk to the MovieDB server.
     */
    public static URL urlBuilder() {

        //Implement the Url builder
        Uri buildUri = Uri.parse(RECIPE_URL_STRING).buildUpon().build();

        URL url = null;

        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    //Parses the list of the recipes from the jason
    public static List<Recipe> parseJson (String json) throws JSONException {
        List<Recipe> recipeList = new ArrayList<>();
        JSONArray resultJsonArray = null;
        try {
            resultJsonArray = new JSONArray(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i = 0;i<resultJsonArray.length();i++){

            JSONObject jsonRecipe = resultJsonArray.getJSONObject(i);
            int id = jsonRecipe.optInt("id");
            String name = jsonRecipe.optString("name");

            Recipe recipe = new Recipe(id, name);

            Log.v(TAG, "id: " + id);
            Log.v(TAG, "Recipename: " + name);

            recipeList.add(recipe);
        }

        return recipeList;
    }

    //parses the list of ingredients of each recipe
    public static List<Ingredient> parseIngredientsJson(String json, int recipeId) throws JSONException {
        List<Ingredient> ingredientList = new ArrayList<>();

        JSONArray resultJsonArray = null;
        try {
            resultJsonArray = new JSONArray(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = resultJsonArray.optJSONObject(recipeId);
        JSONArray ingredientsArray = jsonObject.optJSONArray("ingredients");
        for (int z = 0; z < ingredientsArray.length(); z++) {
            JSONObject ingredientJsonObject = ingredientsArray.optJSONObject(z);
            int quantity = ingredientJsonObject.optInt("quantity");
            String measure = ingredientJsonObject.optString("measure");
            String ingredient = ingredientJsonObject.optString("ingredient");

            Ingredient ingredients = new Ingredient(quantity, measure, ingredient);

            ingredientList.add(ingredients);
        }

        return ingredientList;
    }

    //parses the list of steps of each recipe
    public static List<Step> parseStepsJson(String json, int recipeId) throws JSONException {
        List<Step> stepList = new ArrayList<>();

        JSONArray resultJsonArray = null;
        try {
            resultJsonArray = new JSONArray(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = resultJsonArray.optJSONObject(recipeId);
        JSONArray stepsArray = jsonObject.optJSONArray("steps");
        for (int z = 0; z < stepsArray.length(); z++) {
            JSONObject stepJsonObject = stepsArray.optJSONObject(z);
            int id = stepJsonObject.optInt("id");
            String shortDescription = stepJsonObject.optString("shortDescription");
            String description = stepJsonObject.optString("description");
            String videoURL = stepJsonObject.optString("videoURL");
            String thumbnailURL = stepJsonObject.optString("thumbnailURL");

            Step step = new Step(id, shortDescription,description, videoURL, thumbnailURL);

            stepList.add(step);
        }

        return stepList;
    }

}
