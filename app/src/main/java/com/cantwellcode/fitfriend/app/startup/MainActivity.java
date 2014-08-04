package com.cantwellcode.fitfriend.app.startup;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import com.cantwellcode.fitfriend.app.R;
import com.cantwellcode.fitfriend.app.connect.ForumFragment;
import com.cantwellcode.fitfriend.app.connect.ProfileActivity;
import com.cantwellcode.fitfriend.app.exercise.log.WorkoutLog;
import com.cantwellcode.fitfriend.app.nutrition.NutritionLog;
import com.cantwellcode.fitfriend.app.plan.Plan;
import com.cantwellcode.fitfriend.app.utils.NavigationDrawerFragment;


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
        int id = item.getItemId();
        if (id == R.id.action_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Fragment fragment = new Fragment();
        switch (position) {
            case 0:
                fragment = NutritionLog.newInstance();
                break;
            case 1:
                fragment = WorkoutLog.newInstance();
                break;
            case 2:
                fragment = Plan.newInstance();
                break;
            case 3:
                fragment = ForumFragment.newInstance();
                break;
        }
        // update the main content by replacing fragments
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.container, fragment).commit();
    }
}
