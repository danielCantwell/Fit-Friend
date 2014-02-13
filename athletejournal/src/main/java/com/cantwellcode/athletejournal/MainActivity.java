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

    Menu actionMenu;

    Database db;

    JournalFragment journalFragment = (JournalFragment) JournalFragment.newInstance(this);
    WorkoutFragment workoutFragment = (WorkoutFragment) WorkoutFragment.newInstance(this);
    NutritionFragment nutritionFragment = (NutritionFragment) NutritionFragment.newInstance(this);
    NutritionViewFragment nutritionViewFragment = (NutritionViewFragment) NutritionViewFragment.newInstance(this);
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

        db = new Database(this, "journalData", null, 1);
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
                        .replace(R.id.container, nutritionViewFragment)
                        .commit();
                mTitle = getString(R.string.title_section3);
                nutritionViewFragment.setDB(db);
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

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
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
    public boolean onCreateOptionsMenu(Menu menu) {
        actionMenu = menu;
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            switch (mNavigationDrawerFragment.getItemSelected()) {
                case 1:
                    getMenuInflater().inflate(R.menu.workout, actionMenu);
                    break;
                case 2:
                    getMenuInflater().inflate(R.menu.add_new, actionMenu);
                    break;
            }
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(actionMenu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_saveNutrition) {
            saveNutrition();
            return true;
        }
        else if (id == R.id.action_addNew) {
            switch (mNavigationDrawerFragment.getItemSelected()) {
                case 1:
                    break;
                case 2:
                    FragmentManager fm = getSupportFragmentManager();
                    fm.beginTransaction()
                            .replace(R.id.container, nutritionFragment)
                            .commit();
                    actionMenu.clear();
                    getMenuInflater().inflate(R.menu.nutrition, actionMenu);
                    break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void saveNutrition() {
        nutritionFragment.saveNutrition(db, nutritionViewFragment, actionMenu);
    }

}
