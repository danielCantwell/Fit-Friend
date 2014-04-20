package com.cantwellcode.athletejournal;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Daniel on 4/18/2014.
 */
public class WorkoutAddRun extends Fragment {

    private enum Type {
        AVG, MAX
    }

    public static Fragment newInstance() {
        WorkoutAddRun f = new WorkoutAddRun();

        Bundle args = new Bundle();
        f.setArguments(args);

        return f;
    }

    public static Fragment newInstance(Run run) {
        WorkoutAddRun f = new WorkoutAddRun();

        Bundle args = new Bundle();
        args.putSerializable("EditRun", run);
        f.setArguments(args);

        return f;
    }

    private AutoCompleteTextView name;
    private Spinner type;
    private Button date;
    private EditText distance;
    private Button time;
    private Button avgPace;
    private Button maxPace;
    private EditText avgHR;
    private EditText maxHR;
    private EditText elevation;
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
    private String _avgHR;
    private String _maxHR;
    private String _elevation;
    private String _caloriesBurned;
    private String _notes;

    private DBHelper db;
    private Calendar c;
    private int year, month, day;
    private DialogFragment dateFragment;

    private ArrayAdapter<CharSequence> adapter;

    private Run run = null;
    private Run oldRun = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        db = new DBHelper(getActivity());

        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.workout_new_run, null);

        name = (AutoCompleteTextView) root.findViewById(R.id.w_name);
        type = (Spinner) root.findViewById(R.id.w_type);
        date = (Button) root.findViewById(R.id.w_date);
        distance = (EditText) root.findViewById(R.id.w_distance);
        time = (Button) root.findViewById(R.id.w_time);
        avgPace = (Button) root.findViewById(R.id.w_avgPace);
        maxPace = (Button) root.findViewById(R.id.w_maxPace);
        avgHR = (EditText) root.findViewById(R.id.w_avgHR);
        maxHR = (EditText) root.findViewById(R.id.w_maxHR);
        elevation = (EditText) root.findViewById(R.id.w_elevation);
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
        //adapter.addAll(getRunTypes());

        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        String formattedDate = df.format(c.getTime());
        date.setText(formattedDate);

        if (getArguments().containsKey("EditRun")) {
            run = (Run) getArguments().getSerializable("EditRun");
            oldRun = run;

            int spinnerPosition = adapter.getPosition(run.getType());

            name.setText(run.getName());
            type.setSelection(spinnerPosition);
            date.setText(run.getDate());
            distance.setText(run.getDistance());
            time.setText(run.getTime());
            avgPace.setText(run.getAvgPace());
            maxPace.setText(run.getMaxPace());
            avgHR.setText(run.getAvgHR());
            maxHR.setText(run.getMaxHR());
            elevation.setText(run.getElevation());
            caloriesBurned.setText(run.getCalBurned());
            notes.setText(run.getNotes());

            addToFavoriteCheck.setEnabled(false);
            addToFavoriteCheck.setVisibility(View.GONE);
        }

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                com.cantwellcode.athletejournal.DatePickerFragment dateFragment = new com.cantwellcode.athletejournal.DatePickerFragment();
                dateFragment.setDialogListener(new DialogListener() {
                    @Override
                    public void onDialogOK(Bundle bundle) {
                        year = bundle.getInt("year");
                        month = bundle.getInt("month");
                        day = bundle.getInt("day");

                        Calendar cal = Calendar.getInstance();
                        cal.set(Calendar.YEAR, year);
                        cal.set(Calendar.MONTH, month);
                        cal.set(Calendar.DAY_OF_MONTH, day);

                        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
                        String formattedDate = df.format(cal.getTime());

                        date.setText(formattedDate);
                    }

                    @Override
                    public void onDialogCancel() {

                    }
                });
                dateFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
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

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog();
            }
        });
        avgPace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPaceDialog(Type.AVG);
            }
        });
        maxPace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPaceDialog(Type.MAX);
            }
        });

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
                if (run == null) {
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
        run = new Run(_name, _date, _type, _notes, _distance, _time, _avgPace, _maxPace, _avgHR, _maxHR, _caloriesBurned, _elevation);
        db.store(run);
    }

    private void editWorkout() {
        prepareData();
        run = new Run(_name, _date, _type, _notes, _distance, _time, _avgPace, _maxPace,  _avgHR, _maxHR, _caloriesBurned, _elevation);
        db.updateRun(oldRun, run);
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

        if (!avgHR.getText().toString().isEmpty())
            _avgHR = avgHR.getText().toString();
        else _avgHR = "0";

        if (!maxHR.getText().toString().isEmpty())
            _maxHR = maxHR.getText().toString();
        else _maxHR = "0";

        if (!elevation.getText().toString().isEmpty())
            _elevation = elevation.getText().toString();
        else _elevation = "0";

        if (!caloriesBurned.getText().toString().isEmpty())
            _caloriesBurned = caloriesBurned.getText().toString();
        else _caloriesBurned = "0";

        if (!notes.getText().toString().isEmpty())
            _notes = notes.getText().toString();
        else _notes = "";
    }

    private List<String> getRunTypes() {
        SharedPreferences sp = getActivity().getSharedPreferences(MainActivity.PREFS, 0);

        String _list = sp.getString("RunTypes", null);

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

    private void addRunType(String type) {
        SharedPreferences sp = getActivity().getSharedPreferences(MainActivity.PREFS, 0);

        List<String> list = getRunTypes();
        StringBuilder stringBuilder = new StringBuilder();

        if (list != null) {
            for (String s : list) {
                stringBuilder.append(s);
                stringBuilder.append(",");
            }
        }

        stringBuilder.append(type);
        stringBuilder.append(",");
        sp.edit().putString("RunTypes", stringBuilder.toString());
    }

    private void showTimeDialog() {
        FragmentManager fm = getFragmentManager();
        TimePickerDialog timePickerDialog = new TimePickerDialog();
        timePickerDialog.setDialogListener(new DialogListener() {
            @Override
            public void onDialogOK(Bundle bundle) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, bundle.getInt("hour"));
                cal.set(Calendar.MINUTE, bundle.getInt("minute"));
                cal.set(Calendar.SECOND, bundle.getInt("second"));

                SimpleDateFormat df = new SimpleDateFormat("H : mm : ss");
                String formattedTime = df.format(cal.getTime());

                time.setText(formattedTime);
            }

            @Override
            public void onDialogCancel() {

            }
        });
        timePickerDialog.show(fm, "timeFragment");
    }

    private void showPaceDialog(final Type type) {
        FragmentManager fm = getFragmentManager();
        PacePickerDialog pacePickerDialog = new PacePickerDialog();
        pacePickerDialog.setDialogListener(new DialogListener() {
            @Override
            public void onDialogOK(Bundle bundle) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.MINUTE, bundle.getInt("minute"));
                cal.set(Calendar.SECOND, bundle.getInt("second"));

                SimpleDateFormat df = new SimpleDateFormat("m : ss");
                String formattedTime = df.format(cal.getTime());

                if (type == Type.AVG)
                    avgPace.setText(formattedTime);
                else if (type == Type.MAX)
                    maxPace.setText(formattedTime);
            }

            @Override
            public void onDialogCancel() {

            }
        });
        pacePickerDialog.show(fm, "paceFragment");
    }

    private void restoreActionBar() {
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("New Run");
    }
}
