package com.cantwellcode.athletejournal;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    public static Fragment newInstance(Context context) {
        JournalFragment f = new JournalFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.journal_fragment, null);

        calories = (TextView) root.findViewById(R.id.n_cal_text);
        protein = (TextView) root.findViewById(R.id.n_protein_text);
        carbs = (TextView) root.findViewById(R.id.n_carbs_text);
        fat = (TextView) root.findViewById(R.id.n_fat_text);

        List<Nutrition> todaysNutrition = db.getTodaysNutrition();

        int calorieCount = 0;
        int proteinCount = 0;
        int carbCount = 0;
        int fatCount = 0;

        for (Nutrition n : todaysNutrition) {
            calorieCount += Integer.parseInt(n.get_calories());
            proteinCount += Integer.parseInt(n.get_protein());
            carbCount += Integer.parseInt(n.get_carbs());
            fatCount += Integer.parseInt(n.get_fat());
        }

        calories.setText(calorieCount + "");
        protein.setText(proteinCount + "");
        carbs.setText(carbCount + "");
        fat.setText(fatCount + "");

        return root;
    }

    public void setDb(Database d) {
        db = d;
    }
}
