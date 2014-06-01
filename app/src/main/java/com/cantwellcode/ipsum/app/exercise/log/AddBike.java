package com.cantwellcode.ipsum.app.exercise.log;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.Spinner;

import com.cantwellcode.ipsum.app.R;
import com.cantwellcode.ipsum.app.exercise.types.Bike;
import com.cantwellcode.ipsum.app.utils.DBHelper;
import com.cantwellcode.ipsum.app.utils.DatePickerFragment;
import com.cantwellcode.ipsum.app.utils.DialogListener;
import com.cantwellcode.ipsum.app.utils.TimePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Daniel on 4/18/2014.
 */
public class AddBike extends FragmentActivity {

    private enum Type {
        AVG, MAX
    }

    private AutoCompleteTextView mNameText;
    private Spinner mTypeSpinner;
    private Button mDateButton;
    private EditText mDistanceText;
    private Button mTimeButton;
    private EditText mAvgSpeedText;
    private EditText mMaxSpeedText;
    private EditText mAvgHRText;
    private EditText mMaxHRText;
    private EditText mAvgCadenceText;
    private EditText mMaxCadenceText;
    private EditText mElevationText;
    private EditText mCaloriesBurnedText;
    private EditText mNotesText;
    private CheckBox mAddToFavoritesCheck;

    private String mName;
    private String mType;
    private String mDate;
    private String mDistance;
    private String mTime;
    private String mAvgSpeed;
    private String mMaxSpeed;
    private String mAvgHR;
    private String mMaxHR;
    private String mAvgCadence;
    private String mMaxCadence;
    private String mElevation;
    private String mCaloriesBurned;
    private String mNotes;

    private DBHelper mDatabase;
    private Calendar mCalendar;
    private int mYear, mMonth, mDay;

    private Bike mBike = null;
    private Bike mOldBike = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_new_bike);

        mDatabase = new DBHelper(this);

        mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH);
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);

        mNameText = (AutoCompleteTextView) findViewById(R.id.name);
        mTypeSpinner = (Spinner) findViewById(R.id.type);
        mDateButton = (Button) findViewById(R.id.date);
        mDistanceText = (EditText) findViewById(R.id.distance);
        mTimeButton = (Button) findViewById(R.id.time);
        mAvgSpeedText = (EditText) findViewById(R.id.avgSpeed);
        mMaxSpeedText = (EditText) findViewById(R.id.maxSpeed);
        mAvgHRText = (EditText) findViewById(R.id.avgHR);
        mMaxHRText = (EditText) findViewById(R.id.maxHR);
        mAvgCadenceText = (EditText) findViewById(R.id.avgCadence);
        mMaxCadenceText = (EditText) findViewById(R.id.maxCadence);
        mElevationText = (EditText) findViewById(R.id.elevation);
        mCaloriesBurnedText = (EditText) findViewById(R.id.calories);
        mNotesText = (EditText) findViewById(R.id.notes);
        mAddToFavoritesCheck = (CheckBox) findViewById(R.id.addFavoriteCheck);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.workout_types, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mTypeSpinner.setAdapter(adapter);
        //adapter.addAll(getBikeTypes());

        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        String formattedDate = df.format(mCalendar.getTime());
        mDateButton.setText(formattedDate);

        if (getIntent().hasExtra("Edit")) {
            mBike = (Bike) getIntent().getExtras().getSerializable("Edit");
            mOldBike = mBike;

            int spinnerPosition = adapter.getPosition(mBike.getType());

            mNameText.setText(mBike.getName());
            mTypeSpinner.setSelection(spinnerPosition);
            mDateButton.setText(mBike.getDate());
            mDistanceText.setText(mBike.getDistance());
            mTimeButton.setText(mBike.getTime());
            mAvgSpeedText.setText(mBike.getAvgSpeed());
            mMaxSpeedText.setText(mBike.getMaxSpeed());
            mAvgHRText.setText(mBike.getAvgHR());
            mMaxHRText.setText(mBike.getMaxHR());
            mAvgCadenceText.setText(mBike.getAvgCadence());
            mMaxCadenceText.setText(mBike.getMaxCadence());
            mElevationText.setText(mBike.getElevation());
            mCaloriesBurnedText.setText(mBike.getCalBurned());
            mNotesText.setText(mBike.getNotes());

            mAddToFavoritesCheck.setEnabled(false);
            mAddToFavoritesCheck.setVisibility(View.GONE);
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
                String item = parent.getSelectedItem().toString();
                mType = item;
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
                if (mBike == null) {
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

    private void saveWorkout() {
        prepareData();
        mBike = new Bike(mName, mDate, mType, mNotes, mDistance, mTime, mAvgSpeed, mMaxSpeed, mAvgCadence, mMaxCadence, mAvgHR, mMaxHR, mCaloriesBurned, mElevation);
        mDatabase.store(mBike);
    }

    private void editWorkout() {
        prepareData();
        mBike = new Bike(mName, mDate, mType, mNotes, mDistance, mTime, mAvgSpeed, mMaxSpeed, mAvgCadence, mMaxCadence, mAvgHR, mMaxHR, mCaloriesBurned, mElevation);
        mDatabase.updateBike(mOldBike, mBike);
    }

    private void prepareData() {
        if (!mNameText.getText().toString().isEmpty())
            mName = mNameText.getText().toString();
        else mName = mType;

        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        mDate = df.format(mCalendar.getTime());

        // mTypeSpinner is already set

        if (!mDistanceText.getText().toString().isEmpty())
            mDistance = mDistanceText.getText().toString();
        else mDistance = "0";

        if (!mTimeButton.getText().toString().isEmpty())
            mTime = mTimeButton.getText().toString();
        else mTime = "0";

        if (!mAvgSpeedText.getText().toString().isEmpty())
            mAvgSpeed = mAvgSpeedText.getText().toString();
        else mAvgSpeed = "0";

        if (!mMaxSpeedText.getText().toString().isEmpty())
            mMaxSpeed = mMaxSpeedText.getText().toString();
        else mMaxSpeed = "0";

        if (!mAvgCadenceText.getText().toString().isEmpty())
            mAvgCadence = mAvgCadenceText.getText().toString();
        else mAvgCadence = "0";

        if (!mMaxSpeedText.getText().toString().isEmpty())
            mMaxCadence = mMaxCadenceText.getText().toString();
        else mMaxCadence = "0";

        if (!mAvgHRText.getText().toString().isEmpty())
            mAvgHR = mAvgHRText.getText().toString();
        else mAvgHR = "0";

        if (!mMaxHRText.getText().toString().isEmpty())
            mMaxHR = mMaxHRText.getText().toString();
        else mMaxHR = "0";

        if (!mElevationText.getText().toString().isEmpty())
            mElevation = mElevationText.getText().toString();
        else mElevation = "0";

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

    private void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("New Bike");
    }
}
