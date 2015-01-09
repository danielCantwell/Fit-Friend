package com.cantwellcode.fitfriend.startup;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import com.cantwellcode.fitfriend.connect.GoalsActivity;
import com.cantwellcode.fitfriend.connect.SettingsActivity;
import com.cantwellcode.fitfriend.nutrition.NutritionFavoritesView;
import com.fitfriend.app.R;
import com.cantwellcode.fitfriend.connect.ForumFragment;
import com.cantwellcode.fitfriend.exercise.log.WorkoutLog;
import com.cantwellcode.fitfriend.nutrition.NutritionLog;
import com.cantwellcode.fitfriend.plan.Plan;
import com.cantwellcode.fitfriend.utils.NavigationDrawerFragment;


public class MainActivity extends FragmentActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        // Set up the action bar.
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

        switch (position) {
            case 0:
                Fragment nutritionFragment = NutritionLog.newInstance();
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().replace(R.id.container, nutritionFragment).commit();
                break;
            case 1:
                Fragment workoutFragment = WorkoutLog.newInstance();
                FragmentManager fm1 = getSupportFragmentManager();
                fm1.beginTransaction().replace(R.id.container, workoutFragment).commit();
                break;
            case 2:
                Fragment planFragment = Plan.newInstance();
                FragmentManager fm2 = getSupportFragmentManager();
                fm2.beginTransaction().replace(R.id.container, planFragment).commit();
                break;
            case 3:
                Fragment forumFragment = ForumFragment.newInstance();
                FragmentManager fm3 = getSupportFragmentManager();
                fm3.beginTransaction().replace(R.id.container, forumFragment).commit();
                break;
            case 4:
                Intent i0 = new Intent(this, SettingsActivity.class);
                startActivity(i0);
                break;
            case 5:
                Intent i1 = new Intent(this, GoalsActivity.class);
                startActivity(i1);
                break;
            case 6:
                Intent i2 = new Intent(this, NutritionFavoritesView.class);
                startActivity(i2);
                break;
        }
    }
}
