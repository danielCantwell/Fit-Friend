package com.cantwellcode.athletejournal;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Daniel on 2/8/14.
 */
public class WorkoutLog extends Fragment {

    DBHelper db;
    Calendar c;

    List<Swim> swims = new ArrayList<Swim>();
    List<Bike> bikes = new ArrayList<Bike>();
    List<Run> runs = new ArrayList<Run>();
    List<Gym> gyms = new ArrayList<Gym>();

    LinearLayout workoutView;

    LayoutInflater inflater;

    private Button previous, date, next;

    private int year, month, day;

    public static Fragment newInstance() {
        WorkoutLog f = new WorkoutLog();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;

        db = new DBHelper(getActivity());

        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        String formattedDate = df.format(c.getTime());

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.workout_log, null);

        workoutView = (LinearLayout) root.findViewById(R.id.workoutView);

        swims = db.getSwimList(new Swim(formattedDate));
        bikes = db.getBikeList(new Bike(formattedDate));
        runs = db.getRunList(new Run(formattedDate));

        if (!swims.isEmpty()) {
            loadSwimData(inflater, swims);
        }
        if (!bikes.isEmpty()) {
            loadBikeData(inflater, bikes);
        }
        if (!runs.isEmpty()) {
            loadRunData(inflater, runs);
        }


        setHasOptionsMenu(true);

        previous = (Button) root.findViewById(R.id.w_previous);
        date = (Button) root.findViewById(R.id.w_date);
        next = (Button) root.findViewById(R.id.w_next);

        next.setEnabled(false);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment dateFragment = new DatePickerFragment();
                dateFragment.setDialogListener(new DialogListener() {
                    @Override
                    public void onDialogOK(Bundle bundle) {
                        year = bundle.getInt("year");
                        month = bundle.getInt("month");
                        day = bundle.getInt("day");

                        updateWorkouts();
                    }

                    @Override
                    public void onDialogCancel() {

                    }
                });
                dateFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.add(Calendar.DATE, -1);
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH) + 1;
                day = c.get(Calendar.DAY_OF_MONTH);

                updateWorkouts();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.add(Calendar.DATE, 1);
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH) + 1;
                day = c.get(Calendar.DAY_OF_MONTH);

                updateWorkouts();
            }
        });

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        restoreActionBar();
        inflater.inflate(R.menu.workout_type, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_selectSwim:
                FragmentManager fm1 = getFragmentManager();
                fm1.beginTransaction()
                        .replace(R.id.container, WorkoutAddSwim.newInstance())
                        .commit();
                return true;
            case R.id.action_selectBike:
                FragmentManager fm2 = getFragmentManager();
                fm2.beginTransaction()
                        .replace(R.id.container, WorkoutAddBike.newInstance())
                        .commit();
                return true;
            case R.id.action_selectRun:
                FragmentManager fm3 = getFragmentManager();
                fm3.beginTransaction()
                        .replace(R.id.container, WorkoutAddRun.newInstance())
                        .commit();
                return true;
//            case R.id.action_selectGym:
//                FragmentManager fm4 = getFragmentManager();
//                fm4.beginTransaction()
//                        .replace(R.id.container, WorkoutAddSwim.newInstance(WorkoutAddSwim.WorkoutType.Gym))
//                        .commit();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showPopup(View v, final Workout workout) {
        PopupMenu popup = new PopupMenu(getActivity(), v);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.n_action_addToFavorites:
                        menuClickAddToFavorites(workout);
                        return true;
                    case R.id.n_action_edit:
                        menuClickEdit(workout);
                        return true;
                    case R.id.n_action_delete:
                        menuClickDelete(workout);
                        return true;
                    default:
                        return false;
                }
            }
        });

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.n_list_click, popup.getMenu());
        popup.show();
    }

    private void menuClickAddToFavorites(Workout workout) {
//        DialogFragment categoryDialog = new CategoryDialog(workout, db);
//        categoryDialog.show(getFragmentManager(), "categoryDialog");
    }

    private void menuClickEdit(Workout workout) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        FragmentManager fm = getFragmentManager();

        if (workout instanceof Swim) {
            fm.beginTransaction()
                    .replace(R.id.container, WorkoutAddSwim.newInstance((Swim) workout))
                            .commit();
        }
        if (workout instanceof Bike) {
            fm.beginTransaction()
                    .replace(R.id.container, WorkoutAddBike.newInstance((Bike) workout))
                    .commit();
        }
        if (workout instanceof Run) {
            fm.beginTransaction()
                    .replace(R.id.container, WorkoutAddRun.newInstance((Run) workout))
                    .commit();
        }
    }

    private void menuClickDelete(Workout workout) {
        db.delete(workout);

        updateWorkouts();
    }

    private void loadSwimData(LayoutInflater inflater, List<Swim> swims) {
        for (final Swim swim : swims) {
            View v = inflater.inflate(R.layout.workout_swim_view, null);

            TextView name = (TextView) v.findViewById(R.id.swimview_name);
            TextView type = (TextView) v.findViewById(R.id.swimview_type);
            TextView date = (TextView) v.findViewById(R.id.swimview_date);
            TextView distance = (TextView) v.findViewById(R.id.swimview_distance);
            TextView time = (TextView) v.findViewById(R.id.swimview_time);
            TextView avgPace = (TextView) v.findViewById(R.id.swimview_avg_pace);
            TextView maxPace = (TextView) v.findViewById(R.id.swimview_max_pace);
            TextView strokeRate = (TextView) v.findViewById(R.id.swimview_strokeRate);
            TextView caloriesBurned = (TextView) v.findViewById(R.id.swimview_cal);
            TextView notes = (TextView) v.findViewById(R.id.swimview_notes);

            name.setText(swim.getName());
            type.setText(swim.getType());
            date.setText(swim.getDate());
            time.setText(swim.getTime());
            avgPace.setText(swim.getAvgPace());
            maxPace.setText(swim.getMaxPace());
            distance.setText(swim.getDistance() + "m");
            strokeRate.setText(swim.getStrokeRate());
            caloriesBurned.setText(swim.getCalBurned());
            notes.setText(swim.getNotes());

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showPopup(v, swim);
                    return true;
                }
            });

            workoutView.addView(v);
        }
    }

    private void loadRunData(LayoutInflater inflater, List<Run> runs) {
        for (final Run run : runs) {
            View v = inflater.inflate(R.layout.workout_run_view, null);

            TextView name = (TextView) v.findViewById(R.id.runview_name);
            TextView type = (TextView) v.findViewById(R.id.runview_type);
            TextView date = (TextView) v.findViewById(R.id.runview_date);
            TextView distance = (TextView) v.findViewById(R.id.runview_distance);
            TextView time = (TextView) v.findViewById(R.id.runview_time);
            TextView avgPace = (TextView) v.findViewById(R.id.runview_avg_pace);
            TextView maxPace = (TextView) v.findViewById(R.id.runview_max_pace);
            TextView avgHR = (TextView) v.findViewById(R.id.runview_avg_hr);
            TextView maxHR = (TextView) v.findViewById(R.id.runview_max_hr);
            TextView elevation = (TextView) v.findViewById(R.id.runview_climb);
            TextView caloriesBurned = (TextView) v.findViewById(R.id.runview_cal);
            TextView notes = (TextView) v.findViewById(R.id.runview_notes);

            name.setText(run.getName());
            type.setText(run.getType());
            date.setText(run.getDate());
            time.setText(run.getTime());
            avgPace.setText(run.getAvgPace());
            maxPace.setText(run.getMaxPace());
            avgHR.setText(run.getAvgHR());
            maxHR.setText(run.getMaxHR());
            distance.setText(run.getDistance() + " miles");
            elevation.setText(run.getElevation());
            caloriesBurned.setText(run.getCalBurned());
            notes.setText(run.getNotes());

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showPopup(v, run);
                    return true;
                }
            });

            workoutView.addView(v);
        }
    }

    private void loadBikeData(LayoutInflater inflater, List<Bike> bikes) {
        for (final Bike bike : bikes) {
            View v = inflater.inflate(R.layout.workout_bike_view, null);

            TextView name = (TextView) v.findViewById(R.id.bikeview_name);
            TextView type = (TextView) v.findViewById(R.id.bikeview_type);
            TextView date = (TextView) v.findViewById(R.id.bikeview_date);
            TextView distance = (TextView) v.findViewById(R.id.bikeview_distance);
            TextView time = (TextView) v.findViewById(R.id.bikeview_time);
            TextView avgSpeed = (TextView) v.findViewById(R.id.bikeview_avg_speed);
            TextView maxSpeed = (TextView) v.findViewById(R.id.bikeview_max_speed);
            TextView avgCadence = (TextView) v.findViewById(R.id.bikeview_avg_cadence);
            TextView maxCadence = (TextView) v.findViewById(R.id.bikeview_max_cadence);
            TextView avgHR = (TextView) v.findViewById(R.id.bikeview_avg_hr);
            TextView maxHR = (TextView) v.findViewById(R.id.bikeview_max_hr);
            TextView elevation = (TextView) v.findViewById(R.id.bikeview_climb);
            TextView caloriesBurned = (TextView) v.findViewById(R.id.bikeview_cal);
            TextView notes = (TextView) v.findViewById(R.id.bikeview_notes);

            name.setText(bike.getName());
            type.setText(bike.getType());
            date.setText(bike.getDate());
            time.setText(bike.getTime());
            avgSpeed.setText(bike.getAvgSpeed() + " mph");
            maxSpeed.setText(bike.getMaxSpeed() + " mph");
            avgCadence.setText(bike.getAvgCadence());
            maxCadence.setText(bike.getMaxCadence());
            avgHR.setText(bike.getAvgHR());
            maxHR.setText(bike.getMaxHR());
            distance.setText(bike.getDistance() + " miles");
            elevation.setText(bike.getElevation());
            caloriesBurned.setText(bike.getCalBurned());
            notes.setText(bike.getNotes());

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showPopup(v, bike);
                    return true;
                }
            });

            workoutView.addView(v);
        }
    }


    private void updateWorkouts() {
        workoutView.removeAllViews();

        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        String formattedDate = df.format(c.getTime());

        swims = db.getSwimList(new Swim(formattedDate));
        bikes = db.getBikeList(new Bike(formattedDate));
        runs = db.getRunList(new Run(formattedDate));

        if (!swims.isEmpty()) {
            loadSwimData(inflater, swims);
        }
        if (!bikes.isEmpty()) {
            loadBikeData(inflater, bikes);
        }
        if (!runs.isEmpty()) {
            loadRunData(inflater, runs);
        }

        final Calendar cal = Calendar.getInstance();
        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH) + 1;
        int d = cal.get(Calendar.DAY_OF_MONTH);

        if (y == year && m == month && d == day) {
            date.setText("Today");
            next.setEnabled(false);
            next.setTextColor(Color.GRAY);
        } else {
            date.setText(formattedDate);
            if (!next.isEnabled()) {
                next.setEnabled(true);
                next.setTextColor(Color.BLACK);
            }
        }
    }


    private void restoreActionBar() {
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Workout Log");
    }
}
