package com.cantwellcode.fitfriend.nutrition;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.cantwellcode.fitfriend.R;
import com.cantwellcode.fitfriend.startup.DrawerItem;
import com.cantwellcode.fitfriend.utils.ConfirmationDialog;
import com.cantwellcode.fitfriend.utils.ConfirmationListener;
import com.cantwellcode.fitfriend.utils.Statics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Daniel on 2/8/14.
 */
public class CustomFoodFragment extends Fragment implements AdapterView.OnItemSelectedListener, ConfirmationListener {

    private AutoCompleteTextView mNameText;
    private EditText mCaloriesText;
    private EditText mProteinText;
    private EditText mCarbsText;
    private EditText mFatText;
    private CheckBox mAddToFavorites;
    private Button mSaveButton;

    private String mName;
    private String mType;
    private int mCalories;
    private double mProtein;
    private double mCarbs;
    private double mFat;
    private boolean isFavorite = false;
    private Date mDate;

    private Spinner mTypeSpinner;
    private ArrayAdapter<CharSequence> mAdapter;

    private List<String> mSpinnerFavorites = new ArrayList<String>();
    private List<Food> mFavoritesList;

    private Food mFood = null;

    public static CustomFoodFragment newInstance() {
        return new CustomFoodFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_food_custom, null);

        /* Widget Initialization*/
        mNameText = (AutoCompleteTextView) root.findViewById(R.id.n_name);
        mCaloriesText = (EditText) root.findViewById(R.id.n_calories);
        mProteinText = (EditText) root.findViewById(R.id.n_protein);
        mCarbsText = (EditText) root.findViewById(R.id.n_carbs);
        mFatText = (EditText) root.findViewById(R.id.n_fat);
        mAddToFavorites = (CheckBox) root.findViewById(R.id.addFavoriteCheck);
        mSaveButton = (Button) root.findViewById(R.id.save);

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

        ParseQuery<Food> favoritesQuery = Food.getQuery();
        favoritesQuery.fromPin(Statics.PIN_NUTRITION_FAVORITES);

        try {
            mFavoritesList = favoritesQuery.find();
            for (Food food : mFavoritesList) {
                mSpinnerFavorites.add(food.getName());
            }
            Collections.sort(mSpinnerFavorites);
            mSpinnerFavorites.add(0, "Favorite Meals");

            ArrayAdapter<String> favoritesAdapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_spinner_item, mSpinnerFavorites);
            favoritesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            mNameText.setAdapter(favoritesAdapter);
        } catch (ParseException e) {
            e.printStackTrace();
        }


//        if (getActivity().getIntent().hasExtra("Edit")) {
//            mMeal = (Meal) getActivity().getIntent().getExtras().getSerializable("Edit");
//            mOldMeal = mMeal;
//
//            int spinnerPosition = mAdapter.getPosition(mMeal.getType());
//
//            mNameText.setText(mMeal.getName());
//            mTypeSpinner.setSelection(spinnerPosition);
//            mCaloriesText.setText(mMeal.getCalories());
//            mFatText.setText(mMeal.getFat());
//            mCarbsText.setText(mMeal.getCarbs());
//            mProteinText.setText(mMeal.getProtein());
//
//            mAddToFavorites.setEnabled(false);
//            mAddToFavorites.setVisibility(View.GONE);
//        }

        mDate = Calendar.getInstance().getTime();

        /* Name Autocomplete item clicks */
        mNameText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();

                for (Food food : mFavoritesList) {
                    if (food.getName() == item) {
                        int spinnerPosition = mAdapter.getPosition(food.getType());

                        mNameText.setText(food.getName());
                        mTypeSpinner.setSelection(spinnerPosition);
                        mCaloriesText.setText(String.format("%d", food.getCalories()));
                        mProteinText.setText(String.format("%.1f", food.getProtein()));
                        mCarbsText.setText(String.format("%.1f", food.getCarbs()));
                        mFatText.setText(String.format("%.1f", food.getFat()));

                        mAddToFavorites.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNutrition();
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

    public void prepareData() {
        if (!mNameText.getText().toString().isEmpty())
            mName = mNameText.getText().toString();
        else mName = mType;

        if (!mCaloriesText.getText().toString().isEmpty())
            mCalories = Integer.valueOf(mCaloriesText.getText().toString());
        else mCalories = 0;

        if (!mProteinText.getText().toString().isEmpty())
            mProtein = Double.valueOf(mProteinText.getText().toString());
        else mProtein = 0;

        if (!mCarbsText.getText().toString().isEmpty())
            mCarbs = Double.valueOf(mCarbsText.getText().toString());
        else mCarbs = 0;

        if (!mFatText.getText().toString().isEmpty())
            mFat = Double.valueOf(mFatText.getText().toString());
        else mFat = 0;

        if (mAddToFavorites.isChecked()) {
            isFavorite = true;
        }
    }

    public void saveNutrition() {
        prepareData();

        mFood = new Food();
        mFood.setUserToCurrent();
        mFood.setName(mName);
        mFood.setDate(mDate);
        mFood.setType(mType);
        mFood.setCalories(mCalories);
        mFood.setFat(mFat);
        mFood.setCarbs(mCarbs);
        mFood.setProtein(mProtein);

        if (isFavorite) {
            mFood.pinInBackground(Statics.PIN_NUTRITION_FAVORITES);
        }

        // If the user is an anonymous user
//        if (!ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
//            ConfirmationDialog dialog = ConfirmationDialog.newInstance("Would you like to sign up or log in?");
//            dialog.show(getFragmentManager(), "Confirmation Dialog");
//        }

        mFood.pinInBackground(Statics.PIN_NUTRITION_LOG, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                getActivity().setResult(getActivity().RESULT_OK);
                getActivity().finish();
            }
        });
        mFood.saveInBackground();
    }

//    public void editNutrition() {
//
//        prepareData();
//        Toast.makeText(getActivity(), "Saving Meal", Toast.LENGTH_SHORT).show();
//        Log.d("Nutrition", "Name: " + mName + " Date: " + mDate + " Type: " + mType + " Calories: " + mCalories);
//        mMeal = new Meal(mName, mDate, mType, mCalories, mProtein, mCarbs, mFat);
//
//        mDatabase.updateMeal(mMeal, mOldMeal);
//    }


    public void setDetails(String name, String cal, String fat, String carbs, String prot) {
        mNameText.setText(name);
        mCaloriesText.setText(cal);
        mFatText.setText(fat);
        mCarbsText.setText(carbs);
        mProteinText.setText(prot);
    }

    public void setDetails(Food food) {
        mNameText.setText(food.getName());
        mCaloriesText.setText(String.format("%d", food.getCalories()));
        mFatText.setText(String.format("%.1f", food.getFat()));
        mCarbsText.setText(String.format("%.1f", food.getCarbs()));
        mProteinText.setText(String.format("%.1f", food.getProtein()));

        int spinnerPosition = mAdapter.getPosition(food.getType());
        mTypeSpinner.setSelection(spinnerPosition);

        mAddToFavorites.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onNo(String msg) {

    }

    @Override
    public void onYes(String msg) {

    }
}

