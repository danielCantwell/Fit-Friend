package com.cantwellcode.athletejournal;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Daniel on 4/15/2014.
 */
public class NutritionPlan extends Fragment {

    public static Fragment newInstance() {
        NutritionPlan f = new NutritionPlan();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.nutrition_plan, null);

        return root;
    }
}
