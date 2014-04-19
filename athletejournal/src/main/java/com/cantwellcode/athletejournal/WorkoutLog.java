package com.cantwellcode.athletejournal;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
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
    List<Swim> workouts = new ArrayList<Swim>();
    Calendar c;

    LinearLayout workoutView;

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

        db = new DBHelper(getActivity());

        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);

        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        String formattedDate = df.format(c.getTime());

        ViewGroup root;

        //if (workoutCount <= 1) {
            root = (ViewGroup) inflater.inflate(R.layout.workout_log_single, null);
        //} else {
        //    root = (ViewGroup) inflater.inflate(R.layout.workout_log_multiple, null);
        //}

        workoutView = (LinearLayout) root.findViewById(R.id.workoutView);


//        if (workouts.get(0) instanceof Swim) {
//            workoutView.setLayoutResource(R.layout.workout_swim_view);
//        }
//        else if (workouts.get(0) instanceof Bike) {
//            workoutView.setLayoutResource(R.layout.workout_bike_view);
//        }
//        else if (workouts.get(0) instanceof Run) {
//            workoutView.setLayoutResource(R.layout.workout_run_view);
//        }
//        else {

        workouts = db.getSwimList(new Swim(formattedDate));
        List<Nutrition> n = db.getAllMeals();

        Toast.makeText(getActivity(), "meals " + workouts.size(), Toast.LENGTH_LONG).show();

        if (!workouts.isEmpty())
            loadSwimData(inflater, workouts.get(0));

//        }

        setHasOptionsMenu(true);

        previous = (Button) root.findViewById(R.id.w_previous);
        date = (Button) root.findViewById(R.id.w_date);
        next = (Button) root.findViewById(R.id.w_next);

        next.setEnabled(false);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dateFragment = new DatePickerFragment();
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

                //updateWorkouts();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.add(Calendar.DATE, 1);
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH) + 1;
                day = c.get(Calendar.DAY_OF_MONTH);

                //updateWorkouts();
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
//            case R.id.action_selectBike:
//                FragmentManager fm2 = getFragmentManager();
//                fm2.beginTransaction()
//                        .replace(R.id.container, WorkoutAddSwim.newInstance(WorkoutAddSwim.WorkoutType.Bike))
//                        .commit();
//                return true;
//            case R.id.action_selectRun:
//                FragmentManager fm3 = getFragmentManager();
//                fm3.beginTransaction()
//                        .replace(R.id.container, WorkoutAddSwim.newInstance(WorkoutAddSwim.WorkoutType.Run))
//                        .commit();
//                return true;
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

    private void loadSwimData(LayoutInflater inflater, Swim swim) {
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
        distance.setText(swim.getDistance());
        strokeRate.setText(swim.getStrokeRate());
        caloriesBurned.setText(swim.getCalBurned());
        notes.setText(swim.getNotes());

        workoutView.addView(v);
    }

    /* Date Picker Fragment */

    private class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int y = c.get(Calendar.YEAR);
            int m = c.get(Calendar.MONTH);
            int d = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, y, m, d);
        }

        @Override
        public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
            year = i;
            month = i2;
            day = i3;

            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, day);

            //workouts = db.getNutritionList(Database.NutritionListType.Day, month + 1, day, year);
            //updateWorkouts();

            final Calendar cal = Calendar.getInstance();
            int y = cal.get(Calendar.YEAR);
            int m = cal.get(Calendar.MONTH);
            int d = cal.get(Calendar.DAY_OF_MONTH);

            if (y == year && m == month && d == day) {
                date.setText("Today");
            } else {
                SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
                String formattedDate = df.format(c.getTime());
                date.setText(formattedDate);
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
