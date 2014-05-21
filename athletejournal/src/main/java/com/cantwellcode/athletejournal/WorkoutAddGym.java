package com.cantwellcode.athletejournal;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Daniel on 4/18/2014.
 */
public class WorkoutAddGym extends Fragment{

    public static Fragment newInstance() {
        WorkoutAddGym f = new WorkoutAddGym();

        Bundle args = new Bundle();
        f.setArguments(args);

        return f;
    }

    public static Fragment newInstance(Gym gym) {
        WorkoutAddGym f = new WorkoutAddGym();

        Bundle args = new Bundle();
        args.putSerializable("EditGym", gym);
        f.setArguments(args);

        return f;
    }

    private AutoCompleteTextView name;
    private Button date;
    private Spinner type;
    private Button addRoutine;
    private ListView routineList;

    private String _name;
    private String _date;
    private String _type;

    private ArrayAdapter<CharSequence> spinnerAdapter;
    private NewRoutineListAdapter listAdapter;

    private List<GymRoutine> routines;

    private Gym gym = null;
    private Gym oldGym = null;

    private DBHelper db;

    private Calendar c;
    private int year, month, day;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.workout_new_gym, null);

        db = new DBHelper(getActivity());
        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        name = (AutoCompleteTextView) root.findViewById(R.id.w_name);
        date = (Button) root.findViewById(R.id.w_date);
        type = (Spinner) root.findViewById(R.id.w_type);
        addRoutine = (Button) root.findViewById(R.id.addRoutine);
        routineList = (ListView) root.findViewById(R.id.routineList);

        /* Setup Spinner Adapter */
        spinnerAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.workout_types, R.layout.spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        /* Set Spinner Adapter */
        type.setAdapter(spinnerAdapter);

        routines = new ArrayList<GymRoutine>();

        /* Setup Routine List Adapter */
        listAdapter = new NewRoutineListAdapter(getActivity(), R.id.routineList, routines);
        /* Set Routien List Adapter */
        routineList.setAdapter(listAdapter);

        /* Initialize Date Button Text */
        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        String formattedDate = df.format(c.getTime());
        date.setText(formattedDate);

        /* Handle Date Button Clicks */
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Create Date Fragment */
                DatePickerFragment dateFragment = new DatePickerFragment();
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

        /* Handle Type Selection */
        type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                _type = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        type.setSelection(0);

        /* Handle Add Routine Button Clicks */
        addRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                WorkoutAddRoutineDialog dialog = new WorkoutAddRoutineDialog();
                dialog.show(fm, "routineDialog");
            }
        });

        /* Handle Routine List Item Clicks */
        routineList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                GymRoutine routine = listAdapter.getItem(position);
                routines.remove(routine);
                updateRoutineList();
                return true;
            }
        });

        /* Check if workout is new or for edit */
        if (getArguments().containsKey("EditGym")) {
            gym = (Gym) getArguments().getSerializable("EditGym");
            oldGym = gym;

            int spinnerPosition = spinnerAdapter.getPosition(gym.getType());

            name.setText(gym.getName());
            date.setText(gym.getDate());
            type.setSelection(spinnerPosition);
            routines.addAll(gym.getRoutines());
            //listAdapter.addAll(routines);
            updateRoutineList();
        }

        /* Enable Options Menu */
        setHasOptionsMenu(true);

        return root;
    }

    private void updateRoutineList() {
        listAdapter.notifyDataSetChanged();
    }

    private void prepareData() {
        if (!name.getText().toString().isEmpty())
            _name = name.getText().toString();
        else _name = _type;

        _date = date.getText().toString();
    }

    private void editWorkout() {
        prepareData();
        gym = new Gym(_name, _date, _type, routines);
        db.updateGym(oldGym, gym);
    }

    private void saveWorkout() {
        prepareData();
        gym = new Gym(_name, _date, _type, routines);
        db.store(gym);
    }

    private void restoreActionBar() {
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("New Gym Workout");
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
                if (gym == null) {
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
        }
        return super.onOptionsItemSelected(item);
    }

    private class WorkoutAddRoutineDialog extends DialogFragment {

        private AutoCompleteTextView name;
        private EditText reps;
        private EditText weight;
        private Button addSet;
        private ListView setList;

        private RoutineSetsListAdapter setsListAdapter;

        private List<GymSet> sets;
        private View view;

        private AlertDialog.Builder builder;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();

            view = inflater.inflate(R.layout.workout_new_gym_routine, null);

            name = (AutoCompleteTextView) view.findViewById(R.id.name);
            reps = (EditText) view.findViewById(R.id.reps);
            weight = (EditText) view.findViewById(R.id.weight);
            addSet = (Button) view.findViewById(R.id.addSet);
            setList = (ListView) view.findViewById(R.id.setList);

            sets = new ArrayList<GymSet>();

            setsListAdapter = new RoutineSetsListAdapter(getActivity(), R.id.setList, sets);
            setList.setAdapter(setsListAdapter);

            setList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    GymSet set = setsListAdapter.getItem(position);
                    sets.remove(set);
                    setsListAdapter.notifyDataSetChanged();
                }
            });

            addSet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (reps.getText().toString().equals("") || weight.getText().toString().equals("")) {
                        return;
                    }

                    sets.add(new GymSet(Integer.parseInt(reps.getText().toString()), Integer.parseInt(weight.getText().toString())));
                    setsListAdapter.notifyDataSetChanged();
                }
            });

            builder.setView(view);
            builder.setTitle("Add Routine");

            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (name.getText().toString().equals("")) {
                        name.setText("Routine " + sets.size());
                    }
                    routines.add(new GymRoutine(name.getText().toString(), sets));
                    updateRoutineList();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            return builder.create();
        }
    }
}
