package com.example.rovermore.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.rovermore.bakingapp.R;
import com.example.rovermore.bakingapp.activities.IngredientActivity;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetProvider extends AppWidgetProvider {

    public static final String EXTRA_INGREDIENT = "extra_ingredient";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

        for (int i=0; i<appWidgetIds.length; i++){
            Intent widgetServiceIntent=new Intent(context, WidgetService.class);

            RemoteViews widget=new RemoteViews(context.getPackageName(),
                    R.layout.recipe_widget_provider);

            widget.setRemoteAdapter( R.id.listViewWidget,
                    widgetServiceIntent);


            Intent clickIntent=new Intent(context, IngredientActivity.class);
            //clickIntent.putExtra(MainActivity.RECIPE_ID, 1);
            PendingIntent clickPI=PendingIntent
                    .getActivity(context, 0,
                            clickIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);

            widget.setPendingIntentTemplate(R.id.listViewWidget, clickPI);

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds[i], R.id.listViewWidget);

            appWidgetManager.updateAppWidget(appWidgetIds[i], widget);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

