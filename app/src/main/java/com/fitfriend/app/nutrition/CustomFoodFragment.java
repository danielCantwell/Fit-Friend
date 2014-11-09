package com.fitfriend.app.nutrition;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
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

import com.fitfriend.app.R;
import com.fitfriend.app.utils.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by Daniel on 2/8/14.
 */
public class CustomFoodFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private DBHelper mDatabase;

    private DialogFragment mDateDialog;

    private AutoCompleteTextView mNameText;
    private Button mDateButton;
    private EditText mCaloriesText;
    private EditText mProteinText;
    private EditText mCarbsText;
    private EditText mFatText;
    private CheckBox mAddToFavorites;
    private Button mSaveButton;

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
    private List<FavoriteMeal> mFavoritesList;

    private Meal mMeal = null;
    private Meal mOldMeal = null;

    public static CustomFoodFragment newInstance() {
        return new CustomFoodFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_food_custom, null);

        mDatabase = new DBHelper(getActivity());

        /* Meal Type Spinner */
        mTypeSpinner = (Spinner) root.findViewById(R.id.n_type);
        // Create an ArrayAdapter using the string array and a default spinner layout
        mAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.meal_types, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the mAdapter to the spinner
        mTypeSpinner.setAdapter(mAdapter);
        mTypeSpinner.setOnItemSelectedListener(this);

        mFavoritesList = mDatabase.getAllFavorites();
        for (FavoriteMeal meal : mFavoritesList) {
            mSpinnerFavorites.add(meal.getName());
        }
        Collections.sort(mSpinnerFavorites);
        mSpinnerFavorites.add(0, "Favorite Meals");

        ArrayAdapter<String> favoritesAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, mSpinnerFavorites);
        favoritesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mNameText = (AutoCompleteTextView) root.findViewById(R.id.n_name);
        mDateButton = (Button) root.findViewById(R.id.n_date);
        mCaloriesText = (EditText) root.findViewById(R.id.n_calories);
        mProteinText = (EditText) root.findViewById(R.id.n_protein);
        mCarbsText = (EditText) root.findViewById(R.id.n_carbs);
        mFatText = (EditText) root.findViewById(R.id.n_fat);
        mAddToFavorites = (CheckBox) root.findViewById(R.id.addFavoriteCheck);
        mSaveButton = (Button) root.findViewById(R.id.save);

        if (getActivity().getIntent().hasExtra("Edit")) {
            mMeal = (Meal) getActivity().getIntent().getExtras().getSerializable("Edit");
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

//        mDateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mDateDialog = new DatePickerFragment();
//                mDateDialog.show(getSupportFragmentManager(), "datePicker");
//            }
//        });

        mNameText.setAdapter(favoritesAdapter);

        mNameText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();

                for (FavoriteMeal meal : mFavoritesList) {
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

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNutrition();
                getActivity().setResult(getActivity().RESULT_OK);
                getActivity().finish();
            }
        });

        return root;
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

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

            return new DatePickerDialog(getActivity(), this, y, m, d);
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
        Toast.makeText(getActivity(), "Saving Meal", Toast.LENGTH_SHORT).show();
        Log.d("Nutrition", "Name: " + mName + " Date: " + mDate + " Type: " + mType + " Calories: " + mCalories);

        mMeal = new Meal(mName, mDate, mType, mCalories, mProtein, mCarbs, mFat);

        if (isFavorite) {
            mDatabase.store(new FavoriteMeal(mName, mType, mCalories, mProtein, mCarbs, mFat));
        }

        mDatabase.store(mMeal);
    }

    public void editNutrition() {

        prepareData();
        Toast.makeText(getActivity(), "Saving Meal", Toast.LENGTH_SHORT).show();
        Log.d("Nutrition", "Name: " + mName + " Date: " + mDate + " Type: " + mType + " Calories: " + mCalories);
        mMeal = new Meal(mName, mDate, mType, mCalories, mProtein, mCarbs, mFat);

        mDatabase.updateMeal(mMeal, mOldMeal);
    }



    public void setDetails(String name, String cal, String fat, String carbs, String prot) {
        mNameText.setText(name);
        mCaloriesText.setText(cal);
        mFatText.setText(fat);
        mCarbsText.setText(carbs);
        mProteinText.setText(prot);
    }

    public void setDetails(FavoriteMeal food) {
        mNameText.setText(food.getName());
        mCaloriesText.setText(food.getCalories());
        mFatText.setText(food.getFat());
        mCarbsText.setText(food.getCarbs());
        mProteinText.setText(food.getProtein());

        int spinnerPosition = mAdapter.getPosition(food.getType());
        mTypeSpinner.setSelection(spinnerPosition);

        mAddToFavorites.setVisibility(View.INVISIBLE);
    }
}

