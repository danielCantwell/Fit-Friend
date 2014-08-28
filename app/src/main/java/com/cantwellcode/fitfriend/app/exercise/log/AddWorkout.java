package com.cantwellcode.fitfriend.app.exercise.log;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cantwellcode.fitfriend.app.R;
import com.cantwellcode.fitfriend.app.exercise.types.BaseRoutine;
import com.cantwellcode.fitfriend.app.exercise.types.BaseSet;
import com.cantwellcode.fitfriend.app.exercise.types.CadenceValue;
import com.cantwellcode.fitfriend.app.exercise.types.RepsValue;
import com.cantwellcode.fitfriend.app.exercise.types.Routine;
import com.cantwellcode.fitfriend.app.exercise.types.Set;
import com.cantwellcode.fitfriend.app.exercise.types.Value;
import com.cantwellcode.fitfriend.app.exercise.types.WeightValue;
import com.cantwellcode.fitfriend.app.utils.DBHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 6/27/2014.
 */
public class AddWorkout extends FragmentActivity implements View.OnClickListener {

    private final static String CADENCE = "rpm";
    private final static String HEART_RATE = "bmp";
    private final static String PACE = "min/mile";
    private final static String REPS = "reps";
    private final static String SPEED = "mph";
    private final static String SECONDS = "s";
    private final static String WEIGHT = "lbs";

    private AutoCompleteTextView mNameText;
    private Spinner mTypeSpinner;
    private Button mDateButton;
    private Spinner mIntensitySpinner;
    private Button mNewRoutineButton;

    private LinearLayout mLayout;

    private DBHelper mDatabase;

    private DialogFragment mDateDialog;

    private List<Routine> mRoutineList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_new);
        mLayout = (LinearLayout) findViewById(R.id.linearLayout);
        mRoutineList = new ArrayList<Routine>();

        mNewRoutineButton = (Button) findViewById(R.id.newRoutine);

        mNewRoutineButton.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("New Workout");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newRoutine:
                final Routine r = new BaseRoutine();
                mRoutineList.add(r);

                RelativeLayout relativeLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.exercise_add_routine, null);
                relativeLayout.setTag(r);

                Button copySet = (Button) relativeLayout.findViewById(R.id.copySet);
                Button newSet = (Button) relativeLayout.findViewById(R.id.newSet);
                final LinearLayout setsLayouts = (LinearLayout) relativeLayout.findViewById(R.id.sets);

                copySet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(AddWorkout.this, "Copy", Toast.LENGTH_SHORT).show();
                    }
                });

                newSet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new SetDialog(setsLayouts, r).show(getSupportFragmentManager(), null);
                    }
                });

                mLayout.addView(relativeLayout);
                new SetDialog(setsLayouts, r).show(getSupportFragmentManager(), null);
                break;
        }
    }

    private class SetDialog extends DialogFragment {

        LinearLayout l;
        Routine r;

        Value primaryValue;
        Value secondaryValue;

        EditText primaryDataText;
        EditText secondaryDataText;
        TextView primaryUnitsText;
        TextView secondaryUnitsText;

        AutoCompleteTextView primaryLabelText;
        AutoCompleteTextView secondaryLabelText;

        ArrayAdapter<CharSequence> labelsAdapter;

        RadioGroup primaryRadioGroup;
        RadioGroup secondaryRadioGroup;

        public SetDialog(LinearLayout l, Routine r) {
            this.l = l;
            this.r = r;

            primaryValue = new WeightValue();
            secondaryValue = new RepsValue();
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View root = inflater.inflate(R.layout.exercise_set_dialog, null);

            primaryDataText = (EditText) root.findViewById(R.id.primaryData);
            secondaryDataText = (EditText) root.findViewById(R.id.secondaryData);
            primaryUnitsText = (TextView) root.findViewById(R.id.primaryUnits);
            secondaryUnitsText = (TextView) root.findViewById(R.id.secondaryUnits);

            primaryRadioGroup = (RadioGroup) root.findViewById(R.id.primaryRadioGroup);
            secondaryRadioGroup = (RadioGroup) root.findViewById(R.id.secondaryRadioGroup);

            primaryRadioGroup.setOnCheckedChangeListener(unitsHandler);
            secondaryRadioGroup.setOnCheckedChangeListener(unitsHandler);

            labelsAdapter = ArrayAdapter.createFromResource(AddWorkout.this, R.array.set_labels, android.R.layout.simple_spinner_item);
            labelsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            primaryLabelText = (AutoCompleteTextView) root.findViewById(R.id.primaryLabel);
            secondaryLabelText = (AutoCompleteTextView) root.findViewById(R.id.secondaryLabel);

            primaryLabelText.setAdapter(labelsAdapter);
            secondaryLabelText.setAdapter(labelsAdapter);

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(root)
                    // Add action buttons
                    .setPositiveButton(R.string.action_save, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            primaryValue.setData(primaryDataText.getText().toString());
                            secondaryValue.setData(secondaryDataText.getText().toString());
                            List<Value> values = new ArrayList<Value>();
                            values.add(primaryValue);
                            values.add(secondaryValue);

                            Set s = new BaseSet(values);
                            l.addView(s.getLayout(getActivity().getLayoutInflater()));
                        }
                    })
                    .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SetDialog.this.getDialog().cancel();
                        }
                    });
            return builder.create();
        }

        RadioGroup.OnCheckedChangeListener unitsHandler = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    /* Primary Units */
                    case R.id.primaryCadence:
                        primaryValue = new CadenceValue();
                        primaryUnitsText.setText(CADENCE);
                        break;
                    case R.id.primaryHeartRate:
                        primaryUnitsText.setText(HEART_RATE);
                        break;
                    case R.id.primaryPace:
                        primaryUnitsText.setText(PACE);
                        break;
                    case R.id.primaryReps:
                        primaryValue = new RepsValue();
                        primaryUnitsText.setText(REPS);
                        break;
                    case R.id.primarySpeed:
                        primaryUnitsText.setText(SPEED);
                        break;
                    case R.id.primaryTime:
                        primaryUnitsText.setText(SECONDS);
                        break;
                    case R.id.primaryWeight:
                        primaryValue = new WeightValue();
                        primaryUnitsText.setText(WEIGHT);
                        break;
                    /* Secondary Units */
                    case R.id.secondaryCadence:
                        secondaryValue = new CadenceValue();
                        secondaryUnitsText.setText(CADENCE);
                        break;
                    case R.id.secondaryHeartRate:
                        secondaryUnitsText.setText(HEART_RATE);
                        break;
                    case R.id.secondaryPace:
                        secondaryUnitsText.setText(PACE);
                        break;
                    case R.id.secondaryReps:
                        secondaryValue = new RepsValue();
                        secondaryUnitsText.setText(REPS);
                        break;
                    case R.id.secondarySpeed:
                        secondaryUnitsText.setText(SPEED);
                        break;
                    case R.id.secondaryTime:
                        secondaryUnitsText.setText(SECONDS);
                        break;
                    case R.id.secondaryWeight:
                        secondaryValue = new WeightValue();
                        secondaryUnitsText.setText(WEIGHT);
                        break;
                }
            }
        };
    }


}
