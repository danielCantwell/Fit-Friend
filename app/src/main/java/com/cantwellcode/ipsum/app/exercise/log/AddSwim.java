package com.cantwellcode.ipsum.app.exercise.log;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.cantwellcode.ipsum.app.R;
import com.cantwellcode.ipsum.app.exercise.types.Swim;
import com.cantwellcode.ipsum.app.utils.DBHelper;
import com.cantwellcode.ipsum.app.utils.DatePickerFragment;
import com.cantwellcode.ipsum.app.utils.DialogListener;
import com.cantwellcode.ipsum.app.utils.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Daniel on 4/15/2014.
 */
public class AddSwim extends FragmentActivity {

    private static enum Type {
        AVG, MAX
    }

    // Layout Widgets
    private AutoCompleteTextView mNameText;
    private Spinner mTypeSpinner;
    private Button mDateButton;
    private EditText mDistanceText;
    private Button mTimeButton;
    private Button mAvgPaceButton;
    private Button mMaxPaceButton;
    private EditText mStrokeRateText;
    private EditText mCaloriesBurnedText;
    private EditText mNotesText;
    private CheckBox mAddToFavoriteCheck;

    // Variables for the swim object
    private String mName;
    private String mType;
    private String mDate;
    private String mDistance;
    private String mTime;
    private String mAvgPace;
    private String mMaxPace;
    private String mStrokeRate;
    private String mCaloriesBurned;
    private String mNotes;

    private DBHelper mDatabase;
    private Calendar mCalendar;
    private int mYear, mMonth, mDay;

    private Swim mSwim = null;
    private Swim mOldSwim = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_new_swim);

        mDatabase = new DBHelper(this);

        mCalendar = Calendar.getInstance();

        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);

        // Setup layout
        mNameText = (AutoCompleteTextView) findViewById(R.id.name);
        mTypeSpinner = (Spinner) findViewById(R.id.type);
        mDateButton = (Button) findViewById(R.id.date);
        mDistanceText = (EditText) findViewById(R.id.distance);
        mTimeButton = (Button) findViewById(R.id.time);
        mAvgPaceButton = (Button) findViewById(R.id.avgPace);
        mMaxPaceButton = (Button) findViewById(R.id.maxPace);
        mStrokeRateText = (EditText) findViewById(R.id.strokeRate);
        mCaloriesBurnedText = (EditText) findViewById(R.id.calories);
        mNotesText = (EditText) findViewById(R.id.notes);
        mAddToFavoriteCheck = (CheckBox) findViewById(R.id.addFavoriteCheck);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.workout_types, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mTypeSpinner.setAdapter(adapter);

        // Set date button text to current date
        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        String formattedDate = df.format(mCalendar.getTime());
        mDateButton.setText(formattedDate);

        // if the activity is being used to edit an existing swim
        if (getIntent().hasExtra("Edit")) {
            mSwim = (Swim) getIntent().getExtras().getSerializable("Edit");
            // old swim data is kept for database updating
            mOldSwim = mSwim;

            int spinnerPosition = adapter.getPosition(mSwim.getType());

            // update layout widgets
            mNameText.setText(mSwim.getName());
            mTypeSpinner.setSelection(spinnerPosition);
            mDateButton.setText(mSwim.getDate());
            mDistanceText.setText(mSwim.getDistance());
            mTimeButton.setText(mSwim.getTime());
            mAvgPaceButton.setText(mSwim.getAvgPace());
            mMaxPaceButton.setText(mSwim.getMaxPace());
            mStrokeRateText.setText(mSwim.getStrokeRate());
            mCaloriesBurnedText.setText(mSwim.getCalBurned());
            mNotesText.setText(mSwim.getNotes());

            mAddToFavoriteCheck.setEnabled(false);
            mAddToFavoriteCheck.setVisibility(View.GONE);
        }

        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog();
            }
        });
        mAvgPaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPaceDialog(Type.AVG);
            }
        });
        mMaxPaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPaceDialog(Type.MAX);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        restoreActionBar();
        getMenuInflater().inflate(R.menu.save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if (mSwim == null) {
                    saveWorkout();
                }
                else {
                    editWorkout();
                }
                setResult(RESULT_CANCELED);
                finish();
                break;
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveWorkout() {
        prepareData();
        mSwim = new Swim(mName, mDate, mType, mNotes, mDistance, mTime, mAvgPace, mMaxPace, mCaloriesBurned, mStrokeRate);
        mDatabase.store(mSwim);
    }

    private void editWorkout() {
        prepareData();
        mSwim = new Swim(mName, mDate, mType, mNotes, mDistance, mTime, mAvgPace, mMaxPace, mCaloriesBurned, mStrokeRate);
        mDatabase.updateSwim(mOldSwim, mSwim);
    }

    private void prepareData() {
        if (!mNameText.getText().toString().isEmpty())
            mName = mNameText.getText().toString();
        else mName = mType;

        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        mDate = df.format(mCalendar.getTime());

        // type is already set

        if (!mDistanceText.getText().toString().isEmpty())
            mDistance = mDistanceText.getText().toString();
        else mDistance = "0";

        if (!mTimeButton.getText().toString().isEmpty())
            mTime = mTimeButton.getText().toString();
        else mTime = "0";

        if (!mAvgPaceButton.getText().toString().isEmpty())
            mAvgPace = mAvgPaceButton.getText().toString();
        else mAvgPace = "0";

        if (!mMaxPaceButton.getText().toString().isEmpty())
            mMaxPace = mMaxPaceButton.getText().toString();
        else mMaxPace = "0";

        if (!mStrokeRateText.getText().toString().isEmpty())
            mStrokeRate = mStrokeRateText.getText().toString();
        else mStrokeRate = "0";

        if (!mCaloriesBurnedText.getText().toString().isEmpty())
            mCaloriesBurned = mCaloriesBurnedText.getText().toString();
        else mCaloriesBurned = "0";

        if (!mNotesText.getText().toString().isEmpty())
            mNotes = mNotesText.getText().toString();
        else mNotes = "";
    }

    private void showTimeDialog() {
        FragmentManager fm = getSupportFragmentManager();
        TimePickerDialog timePickerDialog = new TimePickerDialog();
        timePickerDialog.setDialogListener(new DialogListener() {
            @Override
            public void onDialogOK(Bundle bundle) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, bundle.getInt("hour"));
                cal.set(Calendar.MINUTE, bundle.getInt("minute"));
                cal.set(Calendar.SECOND, bundle.getInt("second"));

                SimpleDateFormat df = new SimpleDateFormat("H:mm:ss");
                String formattedTime = df.format(cal.getTime());

                mTimeButton.setText(formattedTime);
            }

            @Override
            public void onDialogCancel() {

            }
        });
        timePickerDialog.show(fm, "timeFragment");
    }

    private void showPaceDialog(final Type type) {
        FragmentManager fm = getSupportFragmentManager();
        PacePickerDialog pacePickerDialog = new PacePickerDialog();
        pacePickerDialog.setDialogListener(new DialogListener() {
            @Override
            public void onDialogOK(Bundle bundle) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.MINUTE, bundle.getInt("minute"));
                cal.set(Calendar.SECOND, bundle.getInt("second"));

                SimpleDateFormat df = new SimpleDateFormat("m:ss");
                String formattedTime = df.format(cal.getTime());

                if (type == Type.AVG)
                    mAvgPaceButton.setText(formattedTime);
                else if (type == Type.MAX)
                    mMaxPaceButton.setText(formattedTime);
            }

            @Override
            public void onDialogCancel() {

            }
        });
        pacePickerDialog.show(fm, "paceFragment");
    }

    private void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("New Swim");
    }
}
