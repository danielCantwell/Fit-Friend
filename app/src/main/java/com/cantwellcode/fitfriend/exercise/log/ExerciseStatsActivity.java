package com.cantwellcode.fitfriend.exercise.log;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.cantwellcode.fitfriend.R;
import com.cantwellcode.fitfriend.exercise.types.Exercise;

public class ExerciseStatsActivity extends Activity {

    boolean displayingListFragment = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_stats);

        ExerciseListFragment fragment = new ExerciseListFragment();
        fragment.setActivity(this);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }

    public void showStatsFragment(Exercise e) {

        displayingListFragment = false;

        ExerciseStatsFragment fragment = new ExerciseStatsFragment();
        fragment.setExercise(e);
        fragment.setActivity(this);

        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void showListFragment() {

        displayingListFragment = true;

        ExerciseListFragment fragment = new ExerciseListFragment();
        fragment.setActivity(this);

        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_exercise_stats, menu);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            if (displayingListFragment) {
                finish();
                return true;
            } else {
                showListFragment();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (displayingListFragment) {
            finish();
        } else {
            displayingListFragment = true;
            super.onBackPressed();
        }
    }
}
