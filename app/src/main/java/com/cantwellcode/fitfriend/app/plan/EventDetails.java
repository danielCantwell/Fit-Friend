package com.cantwellcode.fitfriend.app.plan;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cantwellcode.fitfriend.app.R;

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

    private Button mAttendees;
    private Button mJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_event_details);

        mTitle = (TextView) findViewById(R.id.title);
        mTime = (TextView) findViewById(R.id.time);
        mCreator = (TextView) findViewById(R.id.creator);
        mDate = (TextView) findViewById(R.id.date);
        mType = (ImageView) findViewById(R.id.icon);
        mLocation = (TextView) findViewById(R.id.location);
        mDescription = (TextView) findViewById(R.id.description);
        mAttendees = (Button) findViewById(R.id.attendees);
        mJoin = (Button) findViewById(R.id.join);

        Bundle bundle = getIntent().getExtras();

        mTitle.setText(bundle.getString("title"));
        mTime.setText(bundle.getString("time"));
        mCreator.setText(bundle.getString("creator"));
        mDate.setText(bundle.getString("date"));
        mLocation.setText(bundle.getString("location"));
        mDescription.setText(bundle.getString("description"));

        String type = bundle.getString("type");
        if (type.equals("Swim")) {
            mType.setImageResource(R.drawable.swim_icon);
        } else if (type.equals("Bike")) {
            mType.setImageResource(R.drawable.bike_icon);
        } else if (type.equals("Run")) {
            mType.setImageResource(R.drawable.run_icon);
        } else if (type.equals("Gym")) {
            mType.setImageResource(R.drawable.gym_icon);
        } // else icon is food icon

//        ParseQuery<Event> query = Event.getQuery();
//        query.whereEqualTo("objectId", getIntent().getStringExtra("id"));
//        query.getFirstInBackground(new GetCallback<Event>() {
//            @Override
//            public void done(Event event, ParseException e) {
//
//            }
//        });
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
