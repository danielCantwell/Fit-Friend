package com.cantwellcode.athletejournal;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 2/12/14.
 */
public class NutritionViewFragment extends ListFragment {

    Database db;

    public static Fragment newInstance(Context context) {
        NutritionViewFragment f = new NutritionViewFragment();
        return f;
    }

    private ListView listView;
    private List<Nutrition> meals;
    private NutritionArrayAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.n_day_view, null);

        meals = db.getTodaysNutrition();

        mAdapter = new NutritionArrayAdapter(getActivity(), android.R.id.list, meals);

        listView = (ListView) root.findViewById(android.R.id.list);

        listView.setAdapter(mAdapter);

        return root;
    }

    public void setDB(Database d) { db = d; }
}
