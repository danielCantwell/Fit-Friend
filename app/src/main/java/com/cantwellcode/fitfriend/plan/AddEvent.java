package com.cantwellcode.fitfriend.plan;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cantwellcode.fitfriend.R;
import com.cantwellcode.fitfriend.utils.Statics;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Daniel on 6/2/2014.
 */
public class AddEvent extends FragmentActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener,
        OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener, LocationListener {

    private MapFragment mMap;

    private EditText mTitleText;
    private Button mDateButton;
    private Button mTimeButton;
    private EditText mDescriptionText;

    private int mYear;
    private int mMonth;
    private int mDay;

    private int mHour;
    private int mMinute;

    private ParseGeoPoint mLocation;
    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        mTitleText = (EditText) findViewById(R.id.title);
        mDateButton = (Button) findViewById(R.id.date);
        mTimeButton = (Button) findViewById(R.id.time);
        mDescriptionText = (EditText) findViewById(R.id.description);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 400, 1000, this);

        mMap = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mMap.getMapAsync(this);

        mMap.getMap().setOnMapClickListener(this);
        mMap.getMap().setOnMarkerClickListener(this);

        mLocation = null;

        Bundle args = getIntent().getBundleExtra("args");

        Calendar c = Calendar.getInstance();
        final int year = args.getInt("year");
        final int month = args.getInt("month");
        final int day = args.getInt("day");
        final int hour = c.get(Calendar.HOUR_OF_DAY);
        final int minute = c.get(Calendar.MINUTE);

        c.set(year, month, day);

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
                break;
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

        if (mTitleText.getText().toString().trim().isEmpty() || mDescriptionText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please make sure you have a title and description", Toast.LENGTH_LONG).show();
            return;
        }

        String title = mTitleText.getText().toString();
        String description = mDescriptionText.getText().toString().trim();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, mYear);
        calendar.set(Calendar.MONTH, mMonth);
        calendar.set(Calendar.DAY_OF_MONTH, mDay);
        calendar.set(Calendar.HOUR_OF_DAY, mHour);
        calendar.set(Calendar.MINUTE, mMinute);

        Date dateTime = calendar.getTime();

        final Event event = new Event();
        event.setUser(ParseUser.getCurrentUser());
        event.setTitle(title);
        event.setDateTime(dateTime);
        event.setDescription(description);
        event.addAttendee(ParseUser.getCurrentUser());

        if (mLocation != null) event.setLocation(mLocation);

        event.pinInBackground(Statics.PIN_PLAN);

        event.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    String channel = Statics.PUSH_CHANNEL_ID + event.getObjectId();
                    Log.d("Event Subscription", "Subscribing to channel: " + channel);
                                       /* Subscribe to push notifications */
                    ParsePush.subscribeInBackground(channel, new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.d("Event Subscription", e.getMessage());
                            }
                        }
                    });
                    Log.d("AddEvent", "Event Added");
                } else {
                    Log.d("AddEvent", e.getMessage());
                }
            }
        });

        setResult(RESULT_OK);
        finish();
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

    @Override
    public void onMapClick(LatLng latLng) {
        // Update Location
        mLocation = new ParseGeoPoint(latLng.latitude, latLng.longitude);

        // Add Marker to Map
        mMap.getMap().clear();
        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(latLng.latitude, latLng.longitude))
                .title(mTitleText.getText().toString().trim().isEmpty() ? "Event Location" : mTitleText.getText().toString().trim());
        Marker marker = mMap.getMap().addMarker(markerOptions);
        marker.showInfoWindow();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if (mMap != null && mMap.getMap() != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 11);
            mMap.getMap().animateCamera(cameraUpdate);
            mLocationManager.removeUpdates(this);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mMap.getMap().clear();
        return true;
    }
}
