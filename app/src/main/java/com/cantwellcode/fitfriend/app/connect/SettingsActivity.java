package com.cantwellcode.fitfriend.app.connect;

import android.support.v4.app.FragmentActivity;
import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.cantwellcode.fitfriend.app.R;
import com.cantwellcode.fitfriend.app.nutrition.NutritionFavoritesView;
import com.cantwellcode.fitfriend.app.startup.DispatchActivity;
import com.cantwellcode.fitfriend.app.utils.Statics;
import com.parse.ParseUser;

/**
 * Created by Daniel on 6/8/2014.
 */
public class SettingsActivity extends FragmentActivity {

    private EditText goalCalories;
    private EditText goalProtein;
    private EditText goalCarbs;
    private EditText goalFat;

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
        setContentView(R.layout.profile_personal);

        user = ParseUser.getCurrentUser();

        goalCalories = (EditText) findViewById(R.id.profileGoalCalories);
        goalProtein = (EditText) findViewById(R.id.profileGoalProtein);
        goalCarbs = (EditText) findViewById(R.id.profileGoalCarbs);
        goalFat = (EditText) findViewById(R.id.profileGoalFat);

        headline = (EditText) findViewById(R.id.headline);
        name = (EditText) findViewById(R.id.name);
        age = (EditText) findViewById(R.id.age);
        location = (EditText) findViewById(R.id.location);
        mainSport = (EditText) findViewById(R.id.mainSport);
        email = (EditText) findViewById(R.id.email);

        sp = PreferenceManager.getDefaultSharedPreferences(this);

        goalCalories.setText(sp.getString(Statics.GOAL_CALORIES, ""));
        goalProtein.setText(sp.getString(Statics.GOAL_PROTEIN, ""));
        goalCarbs.setText(sp.getString(Statics.GOAL_CARBS, ""));
        goalFat.setText(sp.getString(Statics.GOAL_FAT, ""));

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
        sp.edit().putString(Statics.GOAL_CALORIES, goalCalories.getText().toString()).commit();
        sp.edit().putString(Statics.GOAL_FAT, goalFat.getText().toString()).commit();
        sp.edit().putString(Statics.GOAL_CARBS, goalCarbs.getText().toString()).commit();
        sp.edit().putString(Statics.GOAL_PROTEIN, goalProtein.getText().toString()).commit();

        if (age.length() > 0) {
            user.put("age", Integer.parseInt(age.getText().toString()));
        }
        user.put("headline", headline.getText().toString());
        user.put("name", name.getText().toString());
        user.put("location", location.getText().toString());
        user.put("mainSport", mainSport.getText().toString());
        user.setEmail(email.getText().toString());

        user.saveEventually();
    }

    private void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Settings");
    }
}
