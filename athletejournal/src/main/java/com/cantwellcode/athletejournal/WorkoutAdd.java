package com.cantwellcode.athletejournal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

/**
 * Created by Daniel on 4/15/2014.
 */
public class WorkoutAdd extends Fragment {

    public static enum WorkoutType {
        Swim, Bike, Run, Gym
    };

    private static WorkoutType type = WorkoutType.Run;

    public static Fragment newInstance(WorkoutType t) {
        WorkoutAdd f = new WorkoutAdd();
        type = t;
        return f;
    }

    private DBHelper db;
    private Calendar c;
    private int year, month, day;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        db = new DBHelper(getActivity());

        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);

        ViewGroup root;

        switch(type) {
            case Swim:
                root = (ViewGroup) inflater.inflate(R.layout.workout_new_swim, null);
                break;
            case Bike:
                root = (ViewGroup) inflater.inflate(R.layout.workout_new_bike, null);
                break;
            case Run:
                root = (ViewGroup) inflater.inflate(R.layout.workout_new_run, null);
                break;
            case Gym:
                // TODO fix this later
                type = WorkoutType.Run;
                root = (ViewGroup) inflater.inflate(R.layout.workout_new_run, null);
                break;
            default:
                root = (ViewGroup) inflater.inflate(R.layout.workout_new_run, null);
                break;
        }

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
//        restoreActionBar();
        inflater.inflate(R.menu.workout, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_saveWorkout:
//                InstanceType instanceType = (InstanceType) getArguments().getSerializable("InstanceType");
//                if (instanceType == InstanceType.NewWorkout) {
//                    saveWorkout();
//                }
//                else if (instanceType == InstanceType.EditWorkout) {
//                    editWorkout();
//                }
//                FragmentManager fm1 = getFragmentManager();
//                fm1.beginTransaction()
//                        .replace(R.id.container, NutritionLog.newInstance())
//                        .commit();
//                break;
//            case R.id.action_addLap:
//                FragmentManager fm2 = getFragmentManager();
//                fm2.beginTransaction()
//                        .replace(R.id.container, NutritionLog.newInstance())
//                        .commit();
//                break;
//        }
        return super.onOptionsItemSelected(item);
    }
}
