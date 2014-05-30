package com.cantwellcode.ipsum.Nutrition.Analyze;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cantwellcode.ipsum.R;

/**
 * Created by Daniel on 4/15/2014.
 */
public class NutritionStats extends Fragment {

    public static Fragment newInstance() {
        NutritionStats f = new NutritionStats();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.nutrition_stats, null);

        return root;
    }
}
