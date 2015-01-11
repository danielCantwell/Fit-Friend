package com.cantwellcode.fitfriend.connect;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.cantwellcode.fitfriend.R;
import com.cantwellcode.fitfriend.utils.Statics;

/**
 * Created by Daniel on 8/28/2014.
 */
public class GoalsActivity extends FragmentActivity {

    private EditText goalCalories;
    private EditText goalProtein;
    private EditText goalCarbs;
    private EditText goalFat;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        goalCalories = (EditText) findViewById(R.id.profileGoalCalories);
        goalProtein = (EditText) findViewById(R.id.profileGoalProtein);
        goalCarbs = (EditText) findViewById(R.id.profileGoalCarbs);
        goalFat = (EditText) findViewById(R.id.profileGoalFat);

        sp = PreferenceManager.getDefaultSharedPreferences(this);

        goalCalories.setText(sp.getString(Statics.GOAL_CALORIES, "3000"));
        goalProtein.setText(sp.getString(Statics.GOAL_PROTEIN, "150"));
        goalCarbs.setText(sp.getString(Statics.GOAL_CARBS, "300"));
        goalFat.setText(sp.getString(Statics.GOAL_FAT, "70"));
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
    }

    private void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Goals");
    }
}
