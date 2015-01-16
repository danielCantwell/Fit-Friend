package com.cantwellcode.fitfriend.friends;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.cantwellcode.fitfriend.R;
import com.cantwellcode.fitfriend.utils.Statics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ProfileActivity extends Activity {

    TextView mName;
    TextView mSport;
    TextView mAge;
    TextView mLocation;
    TextView mHeadline;

    ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_temp);

        // Find user for the requested profile
        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.fromPin(Statics.PIN_FRIEND_PROFILE);
        try {
            user = query.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ParseObject.unpinAllInBackground(Statics.PIN_FRIEND_PROFILE);

        // Initialize widgets
        mName = (TextView) findViewById(R.id.name);
        mSport = (TextView) findViewById(R.id.sport);
        mAge = (TextView) findViewById(R.id.age);
        mLocation = (TextView) findViewById(R.id.location);
//        mHeadline = (TextView) findViewById(R.id.headline);

        // Fill in widgets
        if (user != null) {
            mName.setText(user.getString("name"));
            mSport.setText(user.getString("mainSport"));
            mAge.setText("" + user.get("age"));
            mLocation.setText(user.getString("location"));
//            mHeadline.setText(user.getString("headline"));
        } else {
            Toast.makeText(this, "User could not be found", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_profile, menu);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
