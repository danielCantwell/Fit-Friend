package com.cantwellcode.athletejournal;

import android.app.ActionBar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;

public class MainActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

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
                FragmentManager fm1 = getSupportFragmentManager();
                fm1.beginTransaction()
                        .replace(R.id.container, NutritionLog.newInstance())
                        .commit();
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                FragmentManager fm2 = getSupportFragmentManager();
                fm2.beginTransaction()
                        .replace(R.id.container, NutritionPlan.newInstance())
                        .commit();
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                FragmentManager fm3 = getSupportFragmentManager();
                fm3.beginTransaction()
                        .replace(R.id.container, NutritionStats.newInstance())
                        .commit();
                mTitle = getString(R.string.title_section3);
                break;
            case 5:
                FragmentManager fm5 = getSupportFragmentManager();
                fm5.beginTransaction()
                        .replace(R.id.container, WorkoutLog.newInstance())
                        .commit();
                mTitle = getString(R.string.title_section5);
                break;
            case 6:
                FragmentManager fm6 = getSupportFragmentManager();
                fm6.beginTransaction()
                        .replace(R.id.container, WorkoutPlan.newInstance())
                        .commit();
                mTitle = getString(R.string.title_section6);
                break;
            case 7:
                FragmentManager fm7 = getSupportFragmentManager();
                fm7.beginTransaction()
                        .replace(R.id.container, WorkoutStats.newInstance())
                        .commit();
                mTitle = getString(R.string.title_section7);
                break;
            case 9:
                FragmentManager fm9 = getSupportFragmentManager();
                fm9.beginTransaction()
                        .replace(R.id.container, ConnectFriends.newInstance())
                        .commit();
                mTitle = getString(R.string.title_section9);
                break;
            case 10:
                FragmentManager fm10 = getSupportFragmentManager();
                fm10.beginTransaction()
                        .replace(R.id.container, ConnectExplore.newInstance())
                        .commit();
                mTitle = getString(R.string.title_section10);
                break;
            case 11:
                FragmentManager fm11 = getSupportFragmentManager();
                fm11.beginTransaction()
                        .replace(R.id.container, ConnectTrainer.newInstance())
                        .commit();
                mTitle = getString(R.string.title_section11);
                break;
            case 13:
                FragmentManager fm13 = getSupportFragmentManager();
                fm13.beginTransaction()
                        .replace(R.id.container, ProfilePersonal.newInstance())
                        .commit();
                mTitle = getString(R.string.title_section13);
                break;
            case 14:
                FragmentManager fm14 = getSupportFragmentManager();
                fm14.beginTransaction()
                        .replace(R.id.container, ProfileSocial.newInstance())
                        .commit();
                mTitle = getString(R.string.title_section14);
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
