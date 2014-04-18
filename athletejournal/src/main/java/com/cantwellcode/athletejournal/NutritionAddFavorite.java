package com.cantwellcode.athletejournal;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Daniel on 3/6/14.
 */
public class NutritionAddFavorite extends Fragment implements AdapterView.OnItemSelectedListener {

    public static enum InstanceType { NewFavorite, EditFavorite };

    private DBHelper db;

    private EditText name;
    private EditText calories;
    private EditText protein;
    private EditText carbs;
    private EditText fat;
    private AutoCompleteTextView category;

    private String _name;
    private String _category;
    private String _type;
    private String _calories;
    private String _protein;
    private String _carbs;
    private String _fat;

    private Spinner type;

    private Favorite favoriteToEdit = null;

    public static Fragment newInstance(InstanceType instanceType) {
        NutritionAddFavorite f = new NutritionAddFavorite();

        Bundle args = new Bundle();
        args.putSerializable("InstanceType", instanceType);
        f.setArguments(args);

        return f;
    }

    public static Fragment newInstance(InstanceType instanceType, Favorite favorite) {
        NutritionAddFavorite f = new NutritionAddFavorite();

        Bundle args = new Bundle();
        args.putSerializable("InstanceType", instanceType);
        args.putSerializable("Favorite", favorite);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.nutrition_new_favorite, null);

        db = new DBHelper(getActivity());

        type = (Spinner)  root.findViewById(R.id.f_type);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.meal_types, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        type.setAdapter(typeAdapter);
        type.setOnItemSelectedListener(this);

        // setup category auto complete
        category    = (AutoCompleteTextView) root.findViewById(R.id.f_category);
        List<String> categoryList = db.getFavoritesCategories();
        ArrayAdapter categoryAdapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_dropdown_item_1line, categoryList);
        category.setAdapter(categoryAdapter);

        name        = (EditText) root.findViewById(R.id.f_name);
        calories    = (EditText) root.findViewById(R.id.f_calories);
        protein     = (EditText) root.findViewById(R.id.f_protein);
        carbs       = (EditText) root.findViewById(R.id.f_carbs);
        fat         = (EditText) root.findViewById(R.id.f_fat);

        if (((InstanceType)(getArguments().getSerializable("InstanceType"))).equals(InstanceType.EditFavorite)) {

            favoriteToEdit = (Favorite) getArguments().getSerializable("Favorite");

            int spinnerPosition = typeAdapter.getPosition(favoriteToEdit.get_type());

            name.setText(favoriteToEdit.get_name());
            category.setText(favoriteToEdit.get_category());
            type.setSelection(spinnerPosition);
            calories.setText(favoriteToEdit.get_calories());
            fat.setText(favoriteToEdit.get_fat());
            carbs.setText(favoriteToEdit.get_carbs());
            protein.setText(favoriteToEdit.get_protein());
        }

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
                InstanceType instanceType = (InstanceType) getArguments().getSerializable("InstanceType");
                if (instanceType == InstanceType.NewFavorite) {
                    saveFavorite();
                }
                else if (instanceType == InstanceType.EditFavorite) {
                    editFavorite();
                }
                FragmentManager fm1 = getFragmentManager();
                fm1.beginTransaction()
                        .replace(R.id.container, NutritionFavoritesView.newInstance(getActivity()))
                        .commit();
                break;
            case R.id.action_cancelNutrition:
                FragmentManager fm2 = getFragmentManager();
                fm2.beginTransaction()
                        .replace(R.id.container, NutritionFavoritesView.newInstance(getActivity()))
                        .commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void prepareData() {
        // type is already set
        _name       = name.getText().toString().isEmpty()       ? _type : name.getText().toString();
        _category   = category.getText().toString().isEmpty()   ? _type : category.getText().toString();
        _calories   = calories.getText().toString().isEmpty()   ? "0"   : calories.getText().toString();
        _protein    = protein.getText().toString().isEmpty()    ? "0"   : protein.getText().toString();
        _carbs      = carbs.getText().toString().isEmpty()      ? "0"   : carbs.getText().toString();
        _fat        = fat.getText().toString().isEmpty()        ? "0"   : fat.getText().toString();
    }

    private void saveFavorite() {
        prepareData();
        Toast.makeText(getActivity(), "Saving Meal", Toast.LENGTH_SHORT).show();
        db.store(new Favorite(_name, _category, _type, _calories, _protein, _carbs, _fat));

        nullify();
    }

    private void editFavorite() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

        prepareData();
        Toast.makeText(getActivity(), "Updating Meal", Toast.LENGTH_SHORT).show();
        db.updateFavorite(favoriteToEdit, new Favorite(_name, _category, _type, _calories, _protein, _carbs, _fat));

        nullify();
    }

    private void restoreActionBar() {
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);

        InstanceType instanceType = (InstanceType) getArguments().getSerializable("InstanceType");

        if (instanceType.equals(InstanceType.NewFavorite)) {
            actionBar.setTitle("New Favorite");
        } else if (instanceType.equals(InstanceType.EditFavorite)) {
            actionBar.setTitle("Edit Favorite");
        }
    }

    private void nullify() {
        name.setText(null);
        category.setText(null);
        calories.setText(null);
        protein.setText(null);
        carbs.setText(null);
        fat.setText(null);
        _name       = null;
        _category   = null;
        _calories   = null;
        _protein    = null;
        _carbs      = null;
        _fat        = null;
    }
}
