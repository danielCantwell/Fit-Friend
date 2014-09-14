package com.fitfriend.app.plan;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.fitfriend.app.R;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Created by Daniel on 6/4/2014.
 */
public class EventDetails extends FragmentActivity {

    private TextView mTitle;
    private TextView mTime;
    private TextView mCreator;
    private TextView mDate;
    private ImageView mType;
    private TextView mLocation;
    private TextView mDescription;

    private Button mAttendeesButton;
    private Button mJoin;

    private Event mEvent;

    private boolean going = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_event_details);

        /*
          Initialize widgets
         */
        mTitle = (TextView) findViewById(R.id.title);
        mTime = (TextView) findViewById(R.id.time);
        mCreator = (TextView) findViewById(R.id.creator);
        mDate = (TextView) findViewById(R.id.date);
        mType = (ImageView) findViewById(R.id.icon);
        mLocation = (TextView) findViewById(R.id.location);
        mDescription = (TextView) findViewById(R.id.description);
        mAttendeesButton = (Button) findViewById(R.id.attendees);
        mJoin = (Button) findViewById(R.id.join);

        /*
          Query for event
         */
        Bundle bundle = getIntent().getExtras();
        String eventID = bundle.getString("id");

        ParseQuery<Event> eventQuery = Event.getQuery();
        eventQuery.whereEqualTo("objectId", eventID);
        eventQuery.include("user");
        eventQuery.include("attendees");
        try {
            mEvent = eventQuery.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        /*
          Set event details
         */
        mTitle.setText(mEvent.getTitle());
        mTime.setText(mEvent.getTime());
        mCreator.setText(mEvent.getUser().get("name").toString());
        mDate.setText(mEvent.getDate());
        mLocation.setText(mEvent.getLocation());
        mDescription.setText(mEvent.getDescription());

        String type = mEvent.getType();
        if (type.equals("Swim")) {
            mType.setImageResource(R.drawable.swim_icon);
        } else if (type.equals("Bike")) {
            mType.setImageResource(R.drawable.bike_icon);
        } else if (type.equals("Run")) {
            mType.setImageResource(R.drawable.run_icon);
        } else if (type.equals("Gym")) {
            mType.setImageResource(R.drawable.gym_icon);
        } // else icon is food icon

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
                } else {
                    mEvent.addAttendee(ParseUser.getCurrentUser());
                    mEvent.saveInBackground();
                    mJoin.setText("Going");
                    going = true;
                }
                mAttendeesButton.setText(mEvent.getAttendees().size() + " Attending");
            }
        });
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
        actionBar.setTitle("Event Details");
    }
}
