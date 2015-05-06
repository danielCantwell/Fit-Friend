package com.cantwellcode.fitfriend.plan;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cantwellcode.fitfriend.R;
import com.cantwellcode.fitfriend.utils.Statics;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Daniel on 6/4/2014.
 */
public class EventDetails extends FragmentActivity implements OnMapReadyCallback {

    private TextView mTime;
    private TextView mCreator;
    private TextView mDate;
    private TextView mDescription;

    private Button mAttendeesButton;
    private Button mJoin;

    private Event mEvent = null;

    private MapFragment mMap;

    private boolean going = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        /*
          Initialize widgets
         */
        mTime = (TextView) findViewById(R.id.time);
        mCreator = (TextView) findViewById(R.id.creator);
        mDate = (TextView) findViewById(R.id.date);
        mDescription = (TextView) findViewById(R.id.description);
        mAttendeesButton = (Button) findViewById(R.id.attendees);
        mJoin = (Button) findViewById(R.id.join);

        mMap = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mMap.getMapAsync(this);

        /*
          Query for event
         */

        ParseQuery<Event> eventQuery = Event.getQuery();
        eventQuery.fromPin(Statics.PIN_EVENT_DETAILS);
        eventQuery.include("user");
        eventQuery.include("attendees");
        try {
            mEvent = eventQuery.getFirst();
            mEvent.unpinInBackground(Statics.PIN_EVENT_DETAILS);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        /*
          Set event details
         */
        if (mEvent != null) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("K:mm aa", Locale.US);
            SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy", Locale.US);

            getActionBar().setTitle(mEvent.getTitle());
            mTime.setText(timeFormat.format(mEvent.getDateTime()));
            mCreator.setText(mEvent.getUser().getString("name"));
            mDate.setText(dateFormat.format(mEvent.getDateTime()));
            mDescription.setText(mEvent.getDescription());

            if (mEvent.getLocation() != null) {
                if (mMap != null && mMap.getMap() != null) {
                    mMap.getMap().clear();
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(new LatLng(mEvent.getLocation().getLatitude(), mEvent.getLocation().getLongitude()))
                            .title(mEvent.getTitle());
                    Marker marker = mMap.getMap().addMarker(markerOptions);
//                    marker.showInfoWindow();

                    LatLng latLng = new LatLng(mEvent.getLocation().getLatitude(), mEvent.getLocation().getLongitude());
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 11);
                    mMap.getMap().animateCamera(cameraUpdate);
                }
            } else {
                mMap.getView().setVisibility(View.GONE);
            }

            /*
            Create the list of attendees
            */
            mAttendeesButton.setText(mEvent.getAttendees().size() + " Attending");

            /*
            Check if user is in the list of attendees
            */
            if (mEvent.checkIfAttending(ParseUser.getCurrentUser())) {
                mJoin.setText("Going");
                going = true;
            }

            mJoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (going) {
                        mEvent.removeAttendee(ParseUser.getCurrentUser());
                        mEvent.saveInBackground();
                        mJoin.setText("Join");
                        going = false;
                    /* Unsubscribe to push notifications */
                        ParsePush.unsubscribeInBackground(Statics.PUSH_CHANNEL_ID + mEvent.getObjectId());
                    } else {
                        mEvent.addAttendee(ParseUser.getCurrentUser());
                        mEvent.saveInBackground();
                        mJoin.setText("Going");
                        going = true;
                    /* Subscribe to push notifications */
                        ParsePush.subscribeInBackground(Statics.PUSH_CHANNEL_ID + mEvent.getObjectId());
                    /* Notify subscribers */
                        ParsePush push = new ParsePush();
                        push.setChannel(Statics.PUSH_CHANNEL_ID + mEvent.getObjectId());
                        push.setMessage(ParseUser.getCurrentUser().getString("name") + " has joined the event " + mEvent.getTitle());
                        push.sendInBackground();
                    }
                    mAttendeesButton.setText(mEvent.getAttendees().size() + " Attending");
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        restoreActionBar();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_OK);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (mEvent != null && mEvent.getLocation() != null) {
            mMap.getMap().clear();
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(mEvent.getLocation().getLatitude(), mEvent.getLocation().getLongitude()))
                    .title(mEvent.getTitle());
            Marker marker = mMap.getMap().addMarker(markerOptions);
//            marker.showInfoWindow();

            LatLng latLng = new LatLng(mEvent.getLocation().getLatitude(), mEvent.getLocation().getLongitude());
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 11);
            mMap.getMap().animateCamera(cameraUpdate);
        } else {
            mMap.getView().setVisibility(View.GONE);
        }
    }
}
