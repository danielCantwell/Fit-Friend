package com.cantwellcode.athletejournal;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by Daniel on 2/8/14.
 */
public class NutritionFragment extends Fragment {

    EditText name;
    EditText date;
    EditText calories;
    EditText protein;
    EditText carbs;
    EditText fat;

    Spinner type;

    public static Fragment newInstance(Context context) {
        NutritionFragment f = new NutritionFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.nutrition_fragment, null);

        name        = (EditText) root.findViewById(R.id.n_name);
        date        = (EditText) root.findViewById(R.id.n_date);
        calories    = (EditText) root.findViewById(R.id.n_calories);
        protein     = (EditText) root.findViewById(R.id.n_protein);
        carbs       = (EditText) root.findViewById(R.id.n_carbs);
        fat         = (EditText) root.findViewById(R.id.n_fat);

        return root;
    }

}

