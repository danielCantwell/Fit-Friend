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

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Daniel on 2/8/14.
 */
public class JournalFragment extends Fragment {

    Database db;

    SmallDecimalTextView calories;
    SmallDecimalTextView protein;
    SmallDecimalTextView carbs;
    SmallDecimalTextView fat;

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

        calories = (SmallDecimalTextView) root.findViewById(R.id.n_cal_text);
        protein = (SmallDecimalTextView) root.findViewById(R.id.n_protein_text);
        carbs = (SmallDecimalTextView) root.findViewById(R.id.n_carbs_text);
        fat = (SmallDecimalTextView) root.findViewById(R.id.n_fat_text);

        goalCalories = (TextView) root.findViewById(R.id.n_cal_desired_text);
        goalProtein = (TextView) root.findViewById(R.id.n_protein_desired_text);
        goalCarbs = (TextView) root.findViewById(R.id.n_carbs_desired_text);
        goalFat = (TextView) root.findViewById(R.id.n_fat_desired_text);

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);

        List<Nutrition> todaysNutrition = db.getNutritionList(Database.NutritionListType.Day, month, day, year);

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
            calories.setText(calorieCount.toString().substring(0, calorieCount.toString().indexOf(".")));
        } else {
            calories.setText(calorieCount.toString());
        }

        if (proteinCount.toString().endsWith(".0")) {
            protein.setText(proteinCount.toString().substring(0, proteinCount.toString().indexOf(".")));
        } else {
            protein.setText(proteinCount.toString());
        }

        if (carbCount.toString().endsWith(".0")) {
            carbs.setText(carbCount.toString().substring(0, carbCount.toString().indexOf(".")));
        } else {
            carbs.setText(carbCount.toString());
        }

        if (fatCount.toString().endsWith(".0")) {
            fat.setText(fatCount.toString().substring(0, fatCount.toString().indexOf(".")));
        } else {
            fat.setText(fatCount.toString());
        }

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
