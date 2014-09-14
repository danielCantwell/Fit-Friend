package com.fitfriend.app.connect;

import android.support.v4.app.FragmentActivity;
import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.fitfriend.app.R;
import com.parse.ParseUser;

/**
 * Created by Daniel on 6/8/2014.
 */
public class SettingsActivity extends FragmentActivity {

    private EditText headline;
    private EditText name;
    private EditText age;
    private EditText location;
    private EditText mainSport;
    private EditText email;

    SharedPreferences sp;

    ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        user = ParseUser.getCurrentUser();

        headline = (EditText) findViewById(R.id.settings_headline);
        name = (EditText) findViewById(R.id.settings_name);
        age = (EditText) findViewById(R.id.settings_age);
        location = (EditText) findViewById(R.id.settings_location);
        mainSport = (EditText) findViewById(R.id.settings_mainSport);
        email = (EditText) findViewById(R.id.settings_email);

        if (user.containsKey("headline")) {
            headline.setText(user.getString("headline"));
        }
        if (user.containsKey("name")) {
            name.setText(user.getString("name"));
        }
        if (user.containsKey("age")) {
            age.setText(Integer.toString(user.getInt("age")));
        }
        if (user.containsKey("location")) {
            location.setText(user.getString("location"));
        }
        if (user.containsKey("mainSport")) {
            mainSport.setText(user.getString("mainSport"));
        }
        email.setText(user.getEmail());
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
                save();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        save();
        finish();
    }

    private void save() {
        if (age.length() > 0) {
            user.put("age", Integer.parseInt(age.getText().toString()));
        }
        user.put("headline", headline.getText().toString());
        user.put("name", name.getText().toString());
        user.put("location", location.getText().toString());
        user.put("mainSport", mainSport.getText().toString());
        user.setEmail(email.getText().toString());

        user.saveInBackground();
    }

    private void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Settings");
    }
}
