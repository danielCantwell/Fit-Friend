package com.fitfriend.app.plan;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.fitfriend.app.R;
import com.fitfriend.app.startup.MainActivity;
import com.fitfriend.app.utils.Statics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Daniel on 6/2/2014.
 */
public class AddEvent extends FragmentActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private EditText mTitleText;
    private Button mDateButton;
    private Button mTimeButton;
    private EditText mLocationText;
    private EditText mDescriptionText;
    private Spinner mTypeSpinner;

    private int mYear;
    private int mMonth;
    private int mDay;

    private int mHour;
    private int mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_add_event);

        mTitleText = (EditText) findViewById(R.id.title);
        mDateButton = (Button) findViewById(R.id.date);
        mTimeButton = (Button) findViewById(R.id.time);
        mLocationText = (EditText) findViewById(R.id.location);
        mDescriptionText = (EditText) findViewById(R.id.description);
        mTypeSpinner = (Spinner) findViewById(R.id.type);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.event_types, android.R.layout.simple_spinner_dropdown_item);
        mTypeSpinner.setAdapter(adapter);

        Calendar c = Calendar.getInstance();
        final int year = c.get(Calendar.YEAR);
        final int month = c.get(Calendar.MONTH);
        final int day = c.get(Calendar.DAY_OF_MONTH);
        final int hour = c.get(Calendar.HOUR_OF_DAY);
        final int minute = c.get(Calendar.MINUTE);

        mYear = year;
        mMonth = month;
        mDay = day;
        mHour = hour;
        mMinute = minute;

        DateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy");
        DateFormat timeFormat = new SimpleDateFormat("h:mm a");
        String dateButtonText = dateFormat.format(c.getTime());
        String timeButtonText = timeFormat.format(c.getTime());

        mDateButton.setText(dateButtonText);
        mTimeButton.setText(timeButtonText);

        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddEvent.this, AddEvent.this, year, month, day);
                datePickerDialog.show();
            }
        });

        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddEvent.this, AddEvent.this, hour, minute, false);
                timePickerDialog.show();
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
                save();
                setResult(RESULT_OK);
                finish();
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        mYear = year;
        mMonth = month;
        mDay = day;

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, mYear);
        c.set(Calendar.MONTH, mMonth);
        c.set(Calendar.DAY_OF_MONTH, mDay);

        DateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy");
        String dateButtonText = dateFormat.format(c.getTime());
        mDateButton.setText(dateButtonText);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mHour = hourOfDay;
        mMinute = minute;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, mHour);
        c.set(Calendar.MINUTE, mMinute);

        DateFormat timeFormat = new SimpleDateFormat("h:mm a");
        String timeButtonText = timeFormat.format(c.getTime());
        mTimeButton.setText(timeButtonText);
    }

    private void save() {
        String title = mTitleText.getText().toString();
        String type = (String) mTypeSpinner.getSelectedItem();
        String location = mLocationText.getText().toString();
        String description = mDescriptionText.getText().toString();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, mYear);
        calendar.set(Calendar.MONTH, mMonth);
        calendar.set(Calendar.DAY_OF_MONTH, mDay);
        calendar.set(Calendar.HOUR_OF_DAY, mHour);
        calendar.set(Calendar.MINUTE, mMinute);

        DateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy");
        DateFormat timeFormat = new SimpleDateFormat("h:mm a");
        String date = dateFormat.format(calendar.getTime());
        String time = timeFormat.format(calendar.getTime());

        final Event event = new Event();
        event.setUser(ParseUser.getCurrentUser());
        event.setTitle(title);
        event.setDate(date);
        event.setTime(time);
        event.setLocation(location);
        event.setDescription(description);
        event.setType(type);
        event.addAttendee(ParseUser.getCurrentUser());

        event.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                                       /* Subscribe to push notifications */
                PushService.subscribe(AddEvent.this, Statics.EVENT_CHANNEL_ + event.getObjectId(), MainActivity.class);
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Event");
    }
}
