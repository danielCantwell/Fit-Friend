package com.cantwellcode.athletejournal;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Daniel on 3/25/2014.
 */
public class WidgetProvider extends AppWidgetProvider {

    Database db;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        db = new Database(context, Database.DATABASE_NAME, null, Database.DATABASE_VERSION);

        // Get all ids
        ComponentName thisWidget = new ComponentName(context,
                WidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);


        for (int widgetId : allWidgetIds) {

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_journal_layout);

            // set texts

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH) + 1;
            int day = c.get(Calendar.DAY_OF_MONTH);

            List<Nutrition> todaysNutrition = db.getNutritionList(Database.ListType.Day, month, day, year);

            BigDecimal calorieCount = new BigDecimal(0);
            BigDecimal proteinCount = new BigDecimal(0);
            BigDecimal carbCount = new BigDecimal(0);
            BigDecimal fatCount = new BigDecimal(0);

            for (Nutrition n : todaysNutrition) {
                calorieCount = calorieCount.add(BigDecimal.valueOf(Double.parseDouble(n.get_calories())));
                proteinCount = proteinCount.add(BigDecimal.valueOf(Double.parseDouble(n.get_protein())));
                carbCount = carbCount.add(BigDecimal.valueOf(Double.parseDouble(n.get_carbs())));
                fatCount = fatCount.add(BigDecimal.valueOf(Double.parseDouble(n.get_fat())));
            }

            if (calorieCount.toString().endsWith(".0")) {
                remoteViews.setTextViewText(R.id.w_cal_text,
                        calorieCount.toString().substring(0, calorieCount.toString().indexOf(".")));
            } else {
                remoteViews.setTextViewText(R.id.w_cal_text, calorieCount.toString());
            }

            if (proteinCount.toString().endsWith(".0")) {
                remoteViews.setTextViewText(R.id.w_protein_text,
                        proteinCount.toString().substring(0, proteinCount.toString().indexOf(".")));
            } else {
                remoteViews.setTextViewText(R.id.w_protein_text, proteinCount.toString());
            }

            if (carbCount.toString().endsWith(".0")) {
                remoteViews.setTextViewText(R.id.w_carbs_text,
                        carbCount.toString().substring(0, carbCount.toString().indexOf(".")));
            } else {
                remoteViews.setTextViewText(R.id.w_carbs_text, carbCount.toString());
            }

            if (fatCount.toString().endsWith(".0")) {
                remoteViews.setTextViewText(R.id.w_fat_text,
                        fatCount.toString().substring(0, fatCount.toString().indexOf(".")));
            } else {
                remoteViews.setTextViewText(R.id.w_fat_text, fatCount.toString());
            }


            // goal texts
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            remoteViews.setTextViewText(R.id.w_cal_desired_text, sp.getString(ProfileFragment.GOAL_CALORIES, "defaultCal"));
            remoteViews.setTextViewText(R.id.w_protein_desired_text, sp.getString(ProfileFragment.GOAL_PROTEIN, "defaultPro"));
            remoteViews.setTextViewText(R.id.w_carbs_desired_text, sp.getString(ProfileFragment.GOAL_CARBS, "defaultCarb"));
            remoteViews.setTextViewText(R.id.w_fat_desired_text, sp.getString(ProfileFragment.GOAL_FAT, "defaultFat"));


            // Register an onClickListener
            Intent activityIntent = new Intent(context, MainActivity.class);
            activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activityIntent.putExtra("FROM_NUTRITION_WIDGET", true);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, activityIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.widget_layout_id, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }
}
