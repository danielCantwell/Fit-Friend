package com.cantwellcode.athletejournal;

import android.app.ActionBar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.MotionEvent;

public class MainActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    Database db;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        mNavigationDrawerFragment.selectItem(1);

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        switch (position) {
            case 1:
                FragmentManager fm3 = getSupportFragmentManager();
                fm3.beginTransaction()
                        .replace(R.id.container, NutritionLog.newInstance())
                        .commit();
                mTitle = getString(R.string.title_section3);
                break;
            case 5:
                FragmentManager fm2 = getSupportFragmentManager();
                fm2.beginTransaction()
                        .replace(R.id.container, WorkoutLog.newInstance())
                        .commit();
                mTitle = getString(R.string.title_section2);
                break;
            case 12:
                FragmentManager fm4 = getSupportFragmentManager();
                fm4.beginTransaction()
                        .replace(R.id.container, ProfileFragment.newInstance())
                        .commit();
                mTitle = getString(R.string.title_section4);
            break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public void onBackPressed() {super.onBackPressed();}

}
