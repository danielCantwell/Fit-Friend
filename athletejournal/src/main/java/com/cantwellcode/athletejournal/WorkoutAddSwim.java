package com.cantwellcode.athletejournal;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Daniel on 4/15/2014.
 */
public class WorkoutAddSwim extends Fragment {

    public static Fragment newInstance() {
        WorkoutAddSwim f = new WorkoutAddSwim();

        Bundle args = new Bundle();
        f.setArguments(args);

        return f;
    }

    public static Fragment newInstance(Swim swim) {
        WorkoutAddSwim f = new WorkoutAddSwim();

        Bundle args = new Bundle();
        args.putSerializable("EditSwim", swim);
        f.setArguments(args);

        return f;
    }

    private AutoCompleteTextView name;
    private Spinner type;
    private Button date;
    private EditText distance;
    private EditText time;
    private EditText avgPace;
    private EditText maxPace;
    private EditText strokeRate;
    private EditText caloriesBurned;
    private EditText notes;
    private CheckBox addToFavoriteCheck;

    private String _name;
    private String _type;
    private String _date;
    private String _distance;
    private String _time;
    private String _avgPace;
    private String _maxPace;
    private String _strokeRate;
    private String _caloriesBurned;
    private String _notes;

    private DBHelper db;
    private Calendar c;
    private int year, month, day;
    private DialogFragment dateFragment;

    private ArrayAdapter<CharSequence> adapter;

    private Swim swim = null;
    private Swim oldSwim = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        db = new DBHelper(getActivity());

        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.workout_new_swim, null);

        name = (AutoCompleteTextView) root.findViewById(R.id.w_name);
        type = (Spinner) root.findViewById(R.id.w_type);
        date = (Button) root.findViewById(R.id.w_date);
        distance = (EditText) root.findViewById(R.id.w_distance);
        time = (EditText) root.findViewById(R.id.w_time);
        avgPace = (EditText) root.findViewById(R.id.w_avgPace);
        maxPace = (EditText) root.findViewById(R.id.w_maxPace);
        strokeRate = (EditText) root.findViewById(R.id.w_strokeRate);
        caloriesBurned = (EditText) root.findViewById(R.id.w_calories);
        notes = (EditText) root.findViewById(R.id.w_notes);
        addToFavoriteCheck = (CheckBox) root.findViewById(R.id.addFavoriteCheck);

        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.workout_types, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        type.setAdapter(adapter);
        //adapter.addAll(getSwimTypes());

        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        String formattedDate = df.format(c.getTime());
        date.setText(formattedDate);

        if (getArguments().containsKey("EditSwim")) {
            swim = (Swim) getArguments().getSerializable("EditSwim");
            oldSwim = swim;

            int spinnerPosition = adapter.getPosition(swim.getType());

            name.setText(swim.getName());
            type.setSelection(spinnerPosition);
            date.setText(swim.getDate());
            distance.setText(swim.getDistance());
            time.setText(swim.getType());
            avgPace.setText(swim.getAvgPace());
            maxPace.setText(swim.getMaxPace());
            strokeRate.setText(swim.getStrokeRate());
            caloriesBurned.setText(swim.getCalBurned());
            notes.setText(swim.getNotes());

            addToFavoriteCheck.setEnabled(false);
            addToFavoriteCheck.setVisibility(View.GONE);
        }

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateFragment = new DatePickerFragment();
                dateFragment.show(((FragmentActivity)getActivity()).getSupportFragmentManager(), "datePicker");
            }
        });

        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getSelectedItem().toString();
                _type = item;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        type.setSelection(0);

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        restoreActionBar();
        inflater.inflate(R.menu.workout, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_saveWorkout:
                if (swim == null) {
                    saveWorkout();
                }
                else {
                    editWorkout();
                }
                FragmentManager fm1 = getFragmentManager();
                fm1.beginTransaction()
                        .replace(R.id.container, WorkoutLog.newInstance())
                        .commit();
                break;
//            case R.id.action_addLap:
//                FragmentManager fm2 = getFragmentManager();
//                fm2.beginTransaction()
//                        .replace(R.id.container, NutritionLog.newInstance())
//                        .commit();
//                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveWorkout() {
        prepareData();
        swim = new Swim(_name, _date, _type, _notes, _distance, _time, _avgPace, _maxPace, _caloriesBurned, _strokeRate);
        db.store(swim);
    }

    private void editWorkout() {
        prepareData();
        swim = new Swim(_name, _date, _type, _notes, _distance, _time, _avgPace, _maxPace, _caloriesBurned, _strokeRate);
        db.updateSwim(oldSwim, swim);
    }

    private void prepareData() {
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

        if (!distance.getText().toString().isEmpty())
            _distance = distance.getText().toString();
        else _distance = "0";

        if (!time.getText().toString().isEmpty())
            _time = time.getText().toString();
        else _time = "0";

        if (!avgPace.getText().toString().isEmpty())
            _avgPace = avgPace.getText().toString();
        else _avgPace = "0";

        if (!maxPace.getText().toString().isEmpty())
            _maxPace = maxPace.getText().toString();
        else _maxPace = "0";

        if (!strokeRate.getText().toString().isEmpty())
            _strokeRate = strokeRate.getText().toString();
        else _strokeRate = "0";

        if (!caloriesBurned.getText().toString().isEmpty())
            _caloriesBurned = caloriesBurned.getText().toString();
        else _caloriesBurned = "0";

        if (!notes.getText().toString().isEmpty())
            _notes = notes.getText().toString();
        else _notes = "";
    }

    private List<String> getSwimTypes() {
        SharedPreferences sp = getActivity().getSharedPreferences(MainActivity.PREFS, 0);

        String _list = sp.getString("SwimTypes", null);

        if (_list != null) {
            String[] items = _list.split(",");
            List<String> list = new ArrayList<String>();

            for (int i = 0; i < items.length; i++) {
                list.add(items[i]);
            }

            return list;
        }
        else {
            List<String> list = new ArrayList<String>();
            list.add("Training");
            return list;
        }
    }

    private void addSwimType(String type) {
        SharedPreferences sp = getActivity().getSharedPreferences(MainActivity.PREFS, 0);

        List<String> list = getSwimTypes();
        StringBuilder stringBuilder = new StringBuilder();

        if (list != null) {
            for (String s : list) {
                stringBuilder.append(s);
                stringBuilder.append(",");
            }
        }

        stringBuilder.append(type);
        stringBuilder.append(",");
        sp.edit().putString("SwimTypes", stringBuilder.toString());
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

    private void restoreActionBar() {
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("New Swim");
    }
}
