package com.cantwellcode.fitfriend.app.nutrition;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cantwellcode.fitfriend.app.R;
import com.cantwellcode.fitfriend.app.utils.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by Daniel on 2/8/14.
 */
public class AddMeal extends FragmentActivity implements AdapterView.OnItemSelectedListener {

    private DBHelper mDatabase;

    private DialogFragment mDateDialog;

    private AutoCompleteTextView mNameText;
    private Button mDateButton;
    private EditText mCaloriesText;
    private EditText mProteinText;
    private EditText mCarbsText;
    private EditText mFatText;
    private CheckBox mAddToFavorites;

    private String mName;
    private String mDate;
    private String mType;
    private String mCalories;
    private String mProtein;
    private String mCarbs;
    private String mFat;
    private boolean isFavorite = false;

    private int mYear;
    private int mMonth;
    private int mDay;

    private Spinner mTypeSpinner;
    private ArrayAdapter<CharSequence> mAdapter;

    private List<String> mSpinnerFavorites = new ArrayList<String>();
    private List<Meal> mFavoritesList;

    private Meal mMeal = null;
    private Meal mOldMeal = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nutrition_new);

        mDatabase = new DBHelper(this);

        /* Meal Type Spinner */
        mTypeSpinner = (Spinner) findViewById(R.id.n_type);
        // Create an ArrayAdapter using the string array and a default spinner layout
        mAdapter = ArrayAdapter.createFromResource(this,
                R.array.meal_types, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the mAdapter to the spinner
        mTypeSpinner.setAdapter(mAdapter);
        mTypeSpinner.setOnItemSelectedListener(this);

        mFavoritesList = mDatabase.getAllFavorites();
        for (Meal meal : mFavoritesList) {
            mSpinnerFavorites.add(meal.getName());
        }
        Collections.sort(mSpinnerFavorites);
        mSpinnerFavorites.add(0, "Favorite Meals");

        ArrayAdapter<String> favoritesAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mSpinnerFavorites);
        favoritesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mNameText = (AutoCompleteTextView) findViewById(R.id.n_name);
        mDateButton = (Button) findViewById(R.id.n_date);
        mCaloriesText = (EditText) findViewById(R.id.n_calories);
        mProteinText = (EditText) findViewById(R.id.n_protein);
        mCarbsText = (EditText) findViewById(R.id.n_carbs);
        mFatText = (EditText) findViewById(R.id.n_fat);
        mAddToFavorites = (CheckBox) findViewById(R.id.addFavoriteCheck);

        if (getIntent().hasExtra("Edit")) {
            mMeal = (Meal) getIntent().getExtras().getSerializable("Edit");
            mOldMeal = mMeal;

            int spinnerPosition = mAdapter.getPosition(mMeal.getType());

            mNameText.setText(mMeal.getName());
            mTypeSpinner.setSelection(spinnerPosition);
            mCaloriesText.setText(mMeal.getCalories());
            mFatText.setText(mMeal.getFat());
            mCarbsText.setText(mMeal.getCarbs());
            mProteinText.setText(mMeal.getProtein());

            mAddToFavorites.setEnabled(false);
            mAddToFavorites.setVisibility(View.GONE);
        }

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        String formattedDate = df.format(c.getTime());

        mDateButton.setText(formattedDate);

        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDateDialog = new DatePickerFragment();
                mDateDialog.show(getSupportFragmentManager(), "datePicker");
            }
        });

        mNameText.setAdapter(favoritesAdapter);

        mNameText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();

                for (Meal meal : mFavoritesList) {
                    if (meal.getName() == item) {
                        int spinnerPosition = mAdapter.getPosition(meal.getType());

                        mNameText.setText(meal.getName());
                        mTypeSpinner.setSelection(spinnerPosition);
                        mCaloriesText.setText(meal.getCalories());
                        mProteinText.setText(meal.getProtein());
                        mCarbsText.setText(meal.getCarbs());
                        mFatText.setText(meal.getFat());
                    }
                }
            }
        });
    }

    /* Spinner Item Selection */

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        String item = adapterView.getSelectedItem().toString();

        switch (adapterView.getId()) {
            case R.id.n_type:
                mType = item;
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /* Options Menu */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        restoreActionBar();
        getMenuInflater().inflate(R.menu.save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if (mMeal == null) {
                    saveNutrition();
                }
                else {
                    editNutrition();
                }
                setResult(RESULT_OK);
                finish();
                break;
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
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

            return new DatePickerDialog(AddMeal.this, this, y, m, d);
        }

        @Override
        public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
            mYear = i;
            mMonth = i2;
            mDay = i3;

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, mYear);
            cal.set(Calendar.MONTH, mMonth);
            cal.set(Calendar.DAY_OF_MONTH, mDay);

            SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
            String formattedDate = df.format(cal.getTime());

            mDateButton.setText(formattedDate);
        }
    }

    public void prepareData() {
        if (!mNameText.getText().toString().isEmpty())
            mName = mNameText.getText().toString();
        else mName = mType;

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, mYear);
        cal.set(Calendar.MONTH, mMonth);
        cal.set(Calendar.DAY_OF_MONTH, mDay);

        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        String formattedDate = df.format(cal.getTime());

        mDate = formattedDate;

        // mTypeSpinner is already set

        if (!mCaloriesText.getText().toString().isEmpty())
            mCalories = mCaloriesText.getText().toString();
        else mCalories = "0";

        if (!mProteinText.getText().toString().isEmpty())
            mProtein = mProteinText.getText().toString();
        else mProtein = "0";

        if (!mCarbsText.getText().toString().isEmpty())
            mCarbs = mCarbsText.getText().toString();
        else mCarbs = "0";

        if (!mFatText.getText().toString().isEmpty())
            mFat = mFatText.getText().toString();
        else mFat = "0";

        if (mAddToFavorites.isChecked()) {
            isFavorite = true;
        }
    }

    public void saveNutrition() {
        prepareData();
        Toast.makeText(this, "Saving Meal", Toast.LENGTH_SHORT).show();
        Log.d("Nutrition", "Name: " + mName + " Date: " + mDate + " Type: " + mType + " Calories: " + mCalories);

        mMeal = new Meal(mName, mDate, mType, mCalories, mProtein, mCarbs, mFat, isFavorite);

        mDatabase.store(mMeal);
    }

    public void editNutrition() {

        prepareData();
        Toast.makeText(this, "Saving Meal", Toast.LENGTH_SHORT).show();
        Log.d("Nutrition", "Name: " + mName + " Date: " + mDate + " Type: " + mType + " Calories: " + mCalories);
        mMeal = new Meal(mName, mDate, mType, mCalories, mProtein, mCarbs, mFat, isFavorite);

        mDatabase.updateMeal(mMeal, mOldMeal);
    }

    private void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Meal");
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}

