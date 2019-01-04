package com.example.rovermore.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.rovermore.bakingapp.R;
import com.example.rovermore.bakingapp.activities.MainActivity;
import com.example.rovermore.bakingapp.datamodel.Recipe;
import com.example.rovermore.bakingapp.utils.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ListProvider implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<Recipe> recipeList;
    private int appWidgetId;

    public ListProvider(Context applicationContext, Intent intent) {
        mContext = applicationContext;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {

    }

    //You can execute asynchronous task inside this method without asynctask needed
    @Override
    public void onDataSetChanged() {

        URL url = NetworkUtils.urlBuilder();

        try {
            String jsonResponse = NetworkUtils.getResponseFromHttpUrl(url);
            recipeList = NetworkUtils.parseJson(jsonResponse);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return recipeList == null ? 0 : recipeList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        Recipe recipe = recipeList.get(position);

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.list_row);
        rv.setTextViewText(R.id.widget_row_tv_ingredient, recipe.getRecipeName() );

        Intent intent=new Intent();
        Bundle extras=new Bundle();

        //Here you set the ID of the item clicked in the list that will be added later
        //in the pending intent without needing any extra
        //so will open directly the selected recipe ingredients list
        extras.putInt(MainActivity.RECIPE_ID, position);
        intent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.widget_row_tv_ingredient, intent);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}