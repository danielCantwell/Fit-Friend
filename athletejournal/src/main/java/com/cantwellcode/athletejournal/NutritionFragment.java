package com.cantwellcode.athletejournal;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Daniel on 2/8/14.
 */
public class NutritionFragment extends Fragment {

    public static Fragment newInstance(Context context) {
        NutritionFragment f = new NutritionFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.nutrition_fragment, null);
        return root;
    }
}

