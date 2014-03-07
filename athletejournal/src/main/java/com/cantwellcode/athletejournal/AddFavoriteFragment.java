package com.cantwellcode.athletejournal;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Daniel on 3/6/14.
 */
public class AddFavoriteFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private Database db;

    private EditText name;
    private EditText calories;
    private EditText protein;
    private EditText carbs;
    private EditText fat;

    private String _name;
    private String _type;
    private String _calories;
    private String _protein;
    private String _carbs;
    private String _fat;

    private Spinner type;

    public static Fragment newInstance(Context context) {
        AddFavoriteFragment f = new AddFavoriteFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.new_favorite_fragment, null);

        db = new Database(getActivity(), Database.DATABASE_NAME, null, Database.DATABASE_VERSION);

        type        = (Spinner)  root.findViewById(R.id.f_type);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.meal_types, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        type.setAdapter(adapter);
        type.setOnItemSelectedListener(this);

        name        = (EditText) root.findViewById(R.id.f_name);
        calories    = (EditText) root.findViewById(R.id.f_calories);
        protein     = (EditText) root.findViewById(R.id.f_protein);
        carbs       = (EditText) root.findViewById(R.id.f_carbs);
        fat         = (EditText) root.findViewById(R.id.f_fat);

        setHasOptionsMenu(true);

        return root;
    }

    /* Spinner Item Selection */

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getSelectedItem().toString();
        _type = item;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /* Options Menu */

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        restoreActionBar();
        inflater.inflate(R.menu.nutrition, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_saveNutrition:
                saveFavorite();
                FragmentManager fm1 = getFragmentManager();
                fm1.beginTransaction()
                        .replace(R.id.container, FavoritesViewFragment.newInstance(getActivity()))
                        .commit();
                break;
            case R.id.action_cancelNutrition:
                FragmentManager fm2 = getFragmentManager();
                fm2.beginTransaction()
                        .replace(R.id.container, FavoritesViewFragment.newInstance(getActivity()))
                        .commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void prepareData() {
        if (!name.getText().toString().isEmpty())
            _name = name.getText().toString();
        else _name = _type;

        // type is already set

        if (!calories.getText().toString().isEmpty())
            _calories = calories.getText().toString();
        else _calories = "0";

        if (!protein.getText().toString().isEmpty())
            _protein = protein.getText().toString();
        else _protein = "0";

        if (!carbs.getText().toString().isEmpty())
            _carbs = carbs.getText().toString();
        else _carbs = "0";

        if (!fat.getText().toString().isEmpty())
            _fat = fat.getText().toString();
        else _fat = "0";
    }

    public void saveFavorite() {
        prepareData();
        Toast.makeText(getActivity(), "Saving Meal", Toast.LENGTH_SHORT).show();
        Log.d("Favorite", "Name: " + _name + " Type: " + _type + " Calories: " + _calories);
        db.addFavorite(new Nutrition(_name, "", _type, _calories, _protein, _carbs, _fat));

        name.setText(null);
        calories.setText(null);
        protein.setText(null);
        carbs.setText(null);
        fat.setText(null);
        _name       = null;
        _calories   = null;
        _protein    = null;
        _carbs      = null;
        _fat        = null;
    }

    private void restoreActionBar() {
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("New Favorite");
    }
}
