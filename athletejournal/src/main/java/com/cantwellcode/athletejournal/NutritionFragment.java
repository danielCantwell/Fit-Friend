package com.cantwellcode.athletejournal;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Daniel on 2/8/14.
 */
public class NutritionFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    DialogFragment dateFragment;

    EditText name;
    Button date;
    EditText calories;
    EditText protein;
    EditText carbs;
    EditText fat;

    String _name;
    String _date;
    String _type;
    String _calories;
    String _protein;
    String _carbs;
    String _fat;

    int year;
    int month;
    int day;

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

        type        = (Spinner)  root.findViewById(R.id.n_type);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.meal_types, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        type.setAdapter(adapter);
        type.setOnItemSelectedListener(this);

        name        = (EditText) root.findViewById(R.id.n_name);
        date        = (Button)   root.findViewById(R.id.n_date);
        calories    = (EditText) root.findViewById(R.id.n_calories);
        protein     = (EditText) root.findViewById(R.id.n_protein);
        carbs       = (EditText) root.findViewById(R.id.n_carbs);
        fat         = (EditText) root.findViewById(R.id.n_fat);

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        date.setText(" " + (month + 1) + " / " + day + " / " + year + " ");

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateFragment = new DatePickerFragment();
                dateFragment.show(getActivity().getSupportFragmentManager(), "datPicker");
            }
        });

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

    /* Date Picker Fragment */

    public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

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
            year = i;
            month = i2;
            day = i3;
            date.setText(" " + (month + 1) + " / " + day + " / " + year + " ");
        }
    }

    public void prepareData() {
        if (!name.getText().toString().isEmpty())
            _name = name.getText().toString();
        else _name = _type;

        _date = (month + 1) + "_" + day + "_" + year;

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

    public void saveNutrition(Database db, NutritionViewFragment nView, Menu actionMenu) {
        prepareData();
        Toast.makeText(getActivity(), "Saving Meal", Toast.LENGTH_SHORT).show();
        Log.d("Nutrition", "Name: " + _name + " Date: " + _date + " Type: " + _type + " Calories: " + _calories);
        db.addNutrition(new Nutrition(_name, _date, _type, _calories, _protein, _carbs, _fat));

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

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .replace(R.id.container, nView)
                .commit();
        actionMenu.clear();
        getActivity().getMenuInflater().inflate(R.menu.nutrition, actionMenu);
    }
}

