package com.cantwellcode.athletejournal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Daniel on 2/8/14.
 */
public class JournalFragment extends Fragment {

    Database db;

    TextView calories;
    TextView protein;
    TextView carbs;
    TextView fat;

    TextView goalCalories;
    TextView goalProtein;
    TextView goalCarbs;
    TextView goalFat;

    public static Fragment newInstance(Context context) {
        JournalFragment f = new JournalFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(false);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.journal_fragment, null);

        calories = (TextView) root.findViewById(R.id.n_cal_text);
        protein = (TextView) root.findViewById(R.id.n_protein_text);
        carbs = (TextView) root.findViewById(R.id.n_carbs_text);
        fat = (TextView) root.findViewById(R.id.n_fat_text);

        goalCalories = (TextView) root.findViewById(R.id.n_cal_desired_text);
        goalProtein = (TextView) root.findViewById(R.id.n_protein_desired_text);
        goalCarbs = (TextView) root.findViewById(R.id.n_carbs_desired_text);
        goalFat = (TextView) root.findViewById(R.id.n_fat_desired_text);

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);

        List<Nutrition> todaysNutrition = db.getNutritionList(Database.NutritionListType.Day, month, day, year);

        float calorieCount = 0;
        float proteinCount = 0;
        float carbCount = 0;
        float fatCount = 0;

        for (Nutrition n : todaysNutrition) {
            calorieCount += Float.parseFloat(n.get_calories());
            proteinCount += Float.parseFloat(n.get_protein());
            carbCount += Float.parseFloat(n.get_carbs());
            fatCount += Float.parseFloat(n.get_fat());
        }

        calories.setText(calorieCount + "");
        protein.setText(proteinCount + "");
        carbs.setText(carbCount + "");
        fat.setText(fatCount + "");

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        goalCalories.setText(" / " + sp.getString(ProfileFragment.GOAL_CALORIES, "2000"));
        goalProtein.setText(" / " + sp.getString(ProfileFragment.GOAL_PROTEIN, "80"));
        goalCarbs.setText(" / " + sp.getString(ProfileFragment.GOAL_CARBS, "300"));
        goalFat.setText(" / " + sp.getString(ProfileFragment.GOAL_FAT, "40"));
    }

    public void setDb(Database d) {
        db = d;
    }
}
