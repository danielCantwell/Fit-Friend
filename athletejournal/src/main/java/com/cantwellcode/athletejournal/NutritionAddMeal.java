package com.cantwellcode.athletejournal;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by Daniel on 2/8/14.
 */
public class NutritionAddMeal extends Fragment implements AdapterView.OnItemSelectedListener {

    public static enum InstanceType { NewMeal, EditMeal };

    private Activity activity;

    private DBHelper db;

    private DialogFragment dateFragment;

    private AutoCompleteTextView name;
    private Button date;
    private EditText calories;
    private EditText protein;
    private EditText carbs;
    private EditText fat;
    private CheckBox addToFavorites;

    private String _name;
    private String _date;
    private String _type;
    private String _calories;
    private String _protein;
    private String _carbs;
    private String _fat;

    private int year;
    private int month;
    private int day;

    private Spinner type;
    private ArrayAdapter<CharSequence> adapter;

    private List<String> spinnerFavorites = new ArrayList<String>();
    private List<Favorite> favoritesList;

    private Nutrition mealToEdit = null;

    public static Fragment newInstance(InstanceType instanceType) {
        NutritionAddMeal f = new NutritionAddMeal();

        Bundle args = new Bundle();
        args.putSerializable("InstanceType", instanceType);
        f.setArguments(args);

        return f;
    }

    public static Fragment newInstance(InstanceType instanceType, Nutrition meal) {
        NutritionAddMeal f = new NutritionAddMeal();

        Bundle args = new Bundle();
        args.putSerializable("InstanceType", instanceType);
        args.putSerializable("Meal", meal);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.nutrition_new, null);

        //db = new Database(activity, Database.DATABASE_NAME, null, Database.DATABASE_VERSION);
        db = new DBHelper(getActivity());

        /* Meal Type Spinner */
        type = (Spinner) root.findViewById(R.id.n_type);
        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(activity,
                R.array.meal_types, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        type.setAdapter(adapter);
        type.setOnItemSelectedListener(this);

        favoritesList = db.getAllFavorites();
        for (Favorite meal : favoritesList) {
            spinnerFavorites.add(meal.get_name());
        }
        Collections.sort(spinnerFavorites);
        spinnerFavorites.add(0, "Favorite Meals");

        ArrayAdapter<String> favoritesAdapter = new ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_item, spinnerFavorites);
        favoritesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        name        = (AutoCompleteTextView) root.findViewById(R.id.n_name);
        date        = (Button)   root.findViewById(R.id.n_date);
        calories    = (EditText) root.findViewById(R.id.n_calories);
        protein     = (EditText) root.findViewById(R.id.n_protein);
        carbs       = (EditText) root.findViewById(R.id.n_carbs);
        fat         = (EditText) root.findViewById(R.id.n_fat);
        addToFavorites = (CheckBox) root.findViewById(R.id.addFavoriteCheck);

        if (((InstanceType)(getArguments().getSerializable("InstanceType"))).equals(InstanceType.EditMeal)) {
            mealToEdit = (Nutrition) getArguments().getSerializable("Meal");

            int spinnerPosition = adapter.getPosition(mealToEdit.get_type());

            name.setText(mealToEdit.get_name());
            type.setSelection(spinnerPosition);
            calories.setText(mealToEdit.get_calories());
            fat.setText(mealToEdit.get_fat());
            carbs.setText(mealToEdit.get_carbs());
            protein.setText(mealToEdit.get_protein());

            addToFavorites.setEnabled(false);
            addToFavorites.setVisibility(View.GONE);
        }

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        String formattedDate = df.format(c.getTime());

        date.setText(formattedDate);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateFragment = new DatePickerFragment();
                dateFragment.show(((FragmentActivity)activity).getSupportFragmentManager(), "datePicker");
            }
        });

        name.setAdapter(favoritesAdapter);

        name.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();

                for (Favorite meal : favoritesList) {
                    if (meal.get_name() == item) {
                        int spinnerPosition = adapter.getPosition(meal.get_type());

                        name.setText(meal.get_name());
                        type.setSelection(spinnerPosition);
                        calories.setText(meal.get_calories());
                        protein.setText(meal.get_protein());
                        carbs.setText(meal.get_carbs());
                        fat.setText(meal.get_fat());
                    }
                }
            }
        });

        setHasOptionsMenu(true);

        return root;
    }

    /* Spinner Item Selection */

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        String item = adapterView.getSelectedItem().toString();

        switch (adapterView.getId()) {
            case R.id.n_type:
                _type = item;
                break;
        }
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
                if (instanceType == InstanceType.NewMeal) {
                    saveNutrition();
                }
                else if (instanceType == InstanceType.EditMeal) {
                    editNutrition();
                }
                FragmentManager fm1 = getFragmentManager();
                fm1.beginTransaction()
                        .replace(R.id.container, NutritionLog.newInstance())
                        .commit();
                break;
            case R.id.action_cancelNutrition:
                FragmentManager fm2 = getFragmentManager();
                fm2.beginTransaction()
                        .replace(R.id.container, NutritionLog.newInstance())
                        .commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /* Date Picker Fragment */

    private class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int y = c.get(Calendar.YEAR);
            int m = c.get(Calendar.MONTH);
            int d = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(activity, this, y, m, d);
        }

        @Override
        public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
            year = i;
            month = i2;
            day = i3;

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, day);

            SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
            String formattedDate = df.format(cal.getTime());

            date.setText(formattedDate);
        }
    }

    public void prepareData() {
        if (!name.getText().toString().isEmpty())
            _name = name.getText().toString();
        else _name = _type;

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);

        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        String formattedDate = df.format(cal.getTime());

        _date = formattedDate;

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

    public void saveNutrition() {
        prepareData();
        Toast.makeText(activity, "Saving Meal", Toast.LENGTH_SHORT).show();
        Log.d("Nutrition", "Name: " + _name + " Date: " + _date + " Type: " + _type + " Calories: " + _calories);

        Nutrition meal = new Nutrition(_name, _date, _type, _calories, _protein, _carbs, _fat);

        if (addToFavorites.isChecked()) {
            DialogFragment categoryDialog = new CategoryDialog(meal, db);
            categoryDialog.show(getFragmentManager(), "categoryDialog");
        }

        db.storeMeal(meal);

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

    public void editNutrition() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);

        prepareData();
        Toast.makeText(activity, "Saving Meal", Toast.LENGTH_SHORT).show();
        Log.d("Nutrition", "Name: " + _name + " Date: " + _date + " Type: " + _type + " Calories: " + _calories);

        db.updateMeal(mealToEdit, new Nutrition(_name, _date, _type, _calories, _protein, _carbs, _fat));

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
        ActionBar actionBar = activity.getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("New Meal");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }
}

