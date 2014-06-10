package com.cantwellcode.fitfriend.app.exercise.log;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.cantwellcode.fitfriend.app.R;
import com.cantwellcode.fitfriend.app.exercise.types.Gym;
import com.cantwellcode.fitfriend.app.utils.DBHelper;
import com.cantwellcode.fitfriend.app.utils.DatePickerFragment;
import com.cantwellcode.fitfriend.app.utils.DialogListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Daniel on 4/18/2014.
 */
public class AddGym extends FragmentActivity {

    private AutoCompleteTextView mNameText;
    private Button mDateButton;
    private Spinner mTypeSpinner;
    private Button mAddRoutineButton;
    private ListView mRoutineListView;

    private String mName;
    private String mDate;
    private String mType;

    private NewRoutineListAdapter mListAdapter;

    private List<GymRoutine> mRoutines;

    private Gym mGym = null;
    private Gym mOldGym = null;

    private DBHelper mDatabase;

    private Calendar mCalendar;
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.workout_new_gym);

        mDatabase = new DBHelper(this);
        mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);

        mNameText = (AutoCompleteTextView) findViewById(R.id.name);
        mDateButton = (Button) findViewById(R.id.date);
        mTypeSpinner = (Spinner) findViewById(R.id.type);
        mAddRoutineButton = (Button) findViewById(R.id.addRoutine);
        mRoutineListView = (ListView) findViewById(R.id.routineList);

        /* Setup Spinner Adapter */
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.workout_types, R.layout.spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        /* Set Spinner Adapter */
        mTypeSpinner.setAdapter(spinnerAdapter);

        mRoutines = new ArrayList<GymRoutine>();

        /* Setup Routine List Adapter */
        mListAdapter = new NewRoutineListAdapter(this, R.id.routineList, mRoutines);
        /* Set Routien List Adapter */
        mRoutineListView.setAdapter(mListAdapter);

        /* Initialize Date Button Text */
        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        String formattedDate = df.format(mCalendar.getTime());
        mDateButton.setText(formattedDate);

        /* Handle Date Button Clicks */
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Create Date Fragment */
                DatePickerFragment dateFragment = new DatePickerFragment();
                dateFragment.setDialogListener(new DialogListener() {
                    @Override
                    public void onDialogOK(Bundle bundle) {
                        mYear = bundle.getInt("year");
                        mMonth = bundle.getInt("month");
                        mDay = bundle.getInt("day");

                        mCalendar.set(Calendar.YEAR, mYear);
                        mCalendar.set(Calendar.MONTH, mMonth);
                        mCalendar.set(Calendar.DAY_OF_MONTH, mDay);

                        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
                        String formattedDate = df.format(mCalendar.getTime());

                        mDateButton.setText(formattedDate);
                    }

                    @Override
                    public void onDialogCancel() {

                    }
                });
                dateFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        /* Handle Type Selection */
        mTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mType = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mTypeSpinner.setSelection(0);

        /* Handle Add Routine Button Clicks */
        mAddRoutineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                WorkoutAddRoutineDialog dialog = new WorkoutAddRoutineDialog();
                dialog.show(fm, "routineDialog");
            }
        });

        /* Handle Routine List Item Clicks */
        mRoutineListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                GymRoutine routine = mListAdapter.getItem(position);
                mRoutines.remove(routine);
                updateRoutineList();
                return true;
            }
        });

        /* Check if workout is new or for edit */
        if (getIntent().hasExtra("Edit")) {
            mGym = (Gym) getIntent().getExtras().getSerializable("Edit");
            mOldGym = mGym;

            int spinnerPosition = spinnerAdapter.getPosition(mGym.getType());

            mNameText.setText(mGym.getName());
            mDateButton.setText(mGym.getDate());
            mTypeSpinner.setSelection(spinnerPosition);
            mRoutines.addAll(mGym.getRoutines());
            updateRoutineList();
        }
    }

    private void updateRoutineList() {
        mListAdapter.notifyDataSetChanged();
    }

    private void prepareData() {
        if (!mNameText.getText().toString().isEmpty())
            mName = mNameText.getText().toString();
        else mName = mType;

        mDate = mDateButton.getText().toString();
    }

    private void editWorkout() {
        prepareData();
        mGym = new Gym(mName, mDate, mType, mRoutines);
        mDatabase.updateGym(mOldGym, mGym);
    }

    private void saveWorkout() {
        prepareData();
        mGym = new Gym(mName, mDate, mType, mRoutines);
        mDatabase.store(mGym);
    }

    private void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("New Gym Workout");
    }

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
                if (mGym == null) {
                    saveWorkout();
                }
                else {
                    editWorkout();
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
                    mRoutines.add(new GymRoutine(name.getText().toString(), sets));
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
