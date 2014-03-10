package com.cantwellcode.athletejournal;

import android.app.ActionBar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

public class MainActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    Database db;

    JournalFragment journalFragment = (JournalFragment) JournalFragment.newInstance(this);
    WorkoutFragment workoutFragment = (WorkoutFragment) WorkoutFragment.newInstance(this);
    ProfileFragment profileFragment = (ProfileFragment) ProfileFragment.newInstance(this);

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

        db = new Database(this, Database.DATABASE_NAME, null, Database.DATABASE_VERSION);
        journalFragment.setDb(db);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        switch (position) {
            case 0:
                FragmentManager fm1 = getSupportFragmentManager();
                fm1.beginTransaction()
                        .replace(R.id.container, journalFragment)
                        .commit();
                mTitle = getString(R.string.title_section1);
                break;
            case 1:
                FragmentManager fm2 = getSupportFragmentManager();
                fm2.beginTransaction()
                        .replace(R.id.container, workoutFragment)
                        .commit();
                mTitle = getString(R.string.title_section2);
                break;
            case 2:
                FragmentManager fm3 = getSupportFragmentManager();
                fm3.beginTransaction()
                        .replace(R.id.container, NutritionViewFragment.newInstance(this))
                        .commit();
                mTitle = getString(R.string.title_section3);
                break;
            case 3:
                FragmentManager fm4 = getSupportFragmentManager();
                fm4.beginTransaction()
                        .replace(R.id.container, profileFragment)
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
