package com.cantwellcode.athletejournal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.parse.ParseUser;

/**
 * Created by Daniel on 5/3/2014.
 *
 * Routes the user to either the login screen or the main screen
 */
public class DispatchActivity extends Activity {

    public DispatchActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Check if there is current user info
        if (ParseUser.getCurrentUser() != null) {
            // Start an intent for the logged in activity
            Toast.makeText(this, "logged in as " + ParseUser.getCurrentUser().getUsername(), Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainActivity.class));
        } else {
            // Start and intent for the logged out activity
            startActivity(new Intent(this, LogInActivity.class));
        }
    }
}
