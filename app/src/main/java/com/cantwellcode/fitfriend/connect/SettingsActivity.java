package com.cantwellcode.fitfriend.connect;

import android.app.ProgressDialog;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.cantwellcode.fitfriend.R;
import com.cantwellcode.fitfriend.exercise.types.Cardio;
import com.cantwellcode.fitfriend.exercise.types.Exercise;
import com.cantwellcode.fitfriend.exercise.types.Workout;
import com.cantwellcode.fitfriend.utils.Statics;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 6/8/2014.
 */
public class SettingsActivity extends FragmentActivity {

    //    private EditText headline;
    private EditText name;
    private EditText age;
    private EditText location;
    private EditText mainSport;
    private EditText email;

    private Button mLoadOnlineWorkouts;
    private CheckBox mDelayedTimer;
    private CheckBox mRestTimer;

    SharedPreferences sp;

    ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sp = PreferenceManager.getDefaultSharedPreferences(this);

        user = ParseUser.getCurrentUser();

//        headline = (EditText) findViewById(R.id.settings_headline);
        name = (EditText) findViewById(R.id.settings_name);
        age = (EditText) findViewById(R.id.settings_age);
        location = (EditText) findViewById(R.id.settings_location);
        mainSport = (EditText) findViewById(R.id.settings_mainSport);
        email = (EditText) findViewById(R.id.settings_email);

        mLoadOnlineWorkouts = (Button) findViewById(R.id.load_online_workouts);
        mDelayedTimer = (CheckBox) findViewById(R.id.check_delayed_timer);
        mRestTimer = (CheckBox) findViewById(R.id.check_rest_timer);

        mLoadOnlineWorkouts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(SettingsActivity.this);
                progressDialog.setMessage("Loading workouts, please wait...");
                progressDialog.show();

                ParseQuery workoutQuery = Workout.getQuery();
                workoutQuery.whereEqualTo("user", user);
                workoutQuery.include("exercises");
                workoutQuery.findInBackground(new FindCallback<Workout>() {
                    @Override
                    public void done(final List<Workout> workouts, ParseException e) {

                        if (e == null) {
                            ParseObject.unpinAllInBackground(Statics.PIN_WORKOUT_LOG, new DeleteCallback() {
                                @Override
                                public void done(ParseException e) {
                                    ParseObject.pinAllInBackground(Statics.PIN_WORKOUT_LOG, workouts);
                                }
                            });

                            Log.d("Load Workouts", workouts.size() + " gym workouts loaded");

                            ParseQuery<Exercise> exerciseQuery = Exercise.getQuery();
                            exerciseQuery.whereContainedIn("workout", workouts);
                            exerciseQuery.include("sets");

                            exerciseQuery.findInBackground(new FindCallback<Exercise>() {
                                @Override
                                public void done(final List<Exercise> exercises, ParseException e) {
                                    ParseObject.unpinAllInBackground(Statics.PIN_EXERCISES, new DeleteCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            ParseObject.pinAllInBackground(Statics.PIN_EXERCISES, exercises, new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {

                                                    // Now populate the "saved exercises" as well
                                                    final List<Exercise> savedExercises = new ArrayList<Exercise>();

                                                    // Loop through each exercise pulled from online
                                                    for (Exercise exercise : exercises) {

                                                        // Create a template of that exercise
                                                        Exercise ex = exercise.createNew();
                                                        boolean alreadyExists = false;

                                                        // Loop through the exercises already in the list of saved exercises
                                                        for (Exercise savedExercise : savedExercises) {

                                                            // If this exercise already exists, we don't want to add another
                                                            if (savedExercise.sameExercise(ex)) {
                                                                alreadyExists = true;
                                                                break;
                                                            }
                                                        }

                                                        // If this is a new exercise template, add it to the list
                                                        if (!alreadyExists) {
                                                            savedExercises.add(ex);
                                                        }
                                                    }

                                                    // Replace the "saved exercises"
                                                    ParseObject.unpinAllInBackground(Statics.PIN_SAVED_EXERCISES, new DeleteCallback() {
                                                        @Override
                                                        public void done(ParseException e) {
                                                            ParseObject.pinAllInBackground(Statics.PIN_SAVED_EXERCISES, savedExercises);
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        } else {
                            mLoadOnlineWorkouts.setText(e.getMessage());
                        }
                    }
                });

                ParseQuery cardioQuery = Cardio.getQuery();
                cardioQuery.whereEqualTo("user", user);
                cardioQuery.findInBackground(new FindCallback<Cardio>() {
                    @Override
                    public void done(List<Cardio> list, ParseException e) {
                        if (e == null) {
                            ParseObject.pinAllInBackground(Statics.PIN_WORKOUT_LOG, list);
                            Log.d("Load Workouts", list.size() + " cardio workouts loaded");
                        } else {
                            Log.e("Load Workouts", e.getMessage());
                        }
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            mLoadOnlineWorkouts.setText("Workouts Loaded");
                        }
                    }
                });
            }
        });

        mDelayedTimer.setChecked(sp.getBoolean(Statics.SETTINGS_DELAYED_TIMER, false));
        mRestTimer.setChecked(sp.getBoolean(Statics.SETTINGS_REST_TIMER, false));

//        if (user.containsKey("headline")) {
//            headline.setText(user.getString("headline"));
//        }
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
        getMenuInflater().inflate(R.menu.save, menu);
        restoreActionBar();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_save:
                save();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void save() {
        if (age.length() > 0) {
            user.put("age", Integer.parseInt(age.getText().toString()));
        }
//        user.put("headline", headline.getText().toString());
        user.put("name", name.getText().toString());
        user.put("location", location.getText().toString());
        user.put("mainSport", mainSport.getText().toString());
        user.setEmail(email.getText().toString());

        user.saveInBackground();

        sp.edit().putBoolean(Statics.SETTINGS_DELAYED_TIMER, mDelayedTimer.isChecked()).commit();
        sp.edit().putBoolean(Statics.SETTINGS_REST_TIMER, mRestTimer.isChecked()).commit();
    }

    private void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Settings");
    }
}
