package com.cantwellcode.fitfriend.app.exercise.log;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


import com.cantwellcode.fitfriend.app.R;
import com.cantwellcode.fitfriend.app.exercise.types.Bike;
import com.cantwellcode.fitfriend.app.exercise.types.Gym;
import com.cantwellcode.fitfriend.app.exercise.types.Run;
import com.cantwellcode.fitfriend.app.exercise.types.Swim;
import com.cantwellcode.fitfriend.app.exercise.types.Workout;
import com.cantwellcode.fitfriend.app.nutrition.Meal;
import com.cantwellcode.fitfriend.app.utils.DBHelper;
import com.cantwellcode.fitfriend.app.utils.DatePickerFragment;
import com.cantwellcode.fitfriend.app.utils.DialogListener;
import com.cantwellcode.fitfriend.app.utils.Statics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Daniel on 2/8/14.
 */
public class WorkoutLog extends Fragment implements TabHost.OnTabChangeListener {

    private static final String SWIM = "Swim";
    private static final String BIKE = "Bike";
    private static final String RUN = "Run";
    private static final String GYM = "Gym";

    private DBHelper mDatabase;
    private Calendar mCalendar;

    private List<Swim> mSwims = new ArrayList<Swim>();
    private List<Bike> mBikes = new ArrayList<Bike>();
    private List<Run> mRuns = new ArrayList<Run>();
    private List<Gym> mGyms = new ArrayList<Gym>();

    private LayoutInflater mInflater;

    private Button mPreviousButton, mDateButton, mNextButton;

    private int mYear, mMonth, mDay;

    private TabHost mTabHost;

    private static Fragment instance = null;

    public static Fragment newInstance() {
        if(instance == null)
            instance = new WorkoutLog();
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mInflater = inflater;
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.workout_log, null);

        mDatabase = new DBHelper(getActivity());

        mCalendar = Calendar.getInstance();
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH) + 1;
        mDay = mCalendar.get(Calendar.DAY_OF_MONTH);

        mPreviousButton = (Button) root.findViewById(R.id.previous);
        mDateButton = (Button) root.findViewById(R.id.date);
        mNextButton = (Button) root.findViewById(R.id.next);

        mNextButton.setEnabled(false);

        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment dateFragment = new DatePickerFragment();
                dateFragment.setDialogListener(new DialogListener() {
                    @Override
                    public void onDialogOK(Bundle bundle) {
                        mYear = bundle.getInt("year");
                        mMonth = bundle.getInt("month");
                        mDay = bundle.getInt("day");

                        mCalendar.set(Calendar.YEAR, mYear);
                        mCalendar.set(Calendar.MONTH, mMonth);
                        mCalendar.set(Calendar.DAY_OF_MONTH, mDay);

                        updateWorkouts();
                    }

                    @Override
                    public void onDialogCancel() {

                    }
                });
                dateFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendar.add(Calendar.DATE, -1);
                mYear = mCalendar.get(Calendar.YEAR);
                mMonth = mCalendar.get(Calendar.MONTH) + 1;
                mDay = mCalendar.get(Calendar.DAY_OF_MONTH);

                updateWorkouts();
            }
        });

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCalendar.add(Calendar.DATE, 1);
                mYear = mCalendar.get(Calendar.YEAR);
                mMonth = mCalendar.get(Calendar.MONTH) + 1;
                mDay = mCalendar.get(Calendar.DAY_OF_MONTH);

                updateWorkouts();
            }
        });

        mTabHost = (TabHost) root.findViewById(R.id.tabHost);
        mTabHost.setup();

        mTabHost.setOnTabChangedListener(this);

        updateWorkouts();

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        restoreActionBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Toast.makeText(getActivity(), "popup should popup first", Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.action_new:
                Toast.makeText(getActivity(), "popup should popup next", Toast.LENGTH_SHORT).show();
                View v = getActivity().findViewById(R.id.action_new);
                showPopup(v);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showPopup(View v) {
        Toast.makeText(getActivity(), "popup should popup", Toast.LENGTH_SHORT).show();

        PopupMenu popup = new PopupMenu(getActivity(), v);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.gym:
                        startAddActivity("Gym", null);
                        return true;
                    case R.id.swim:
                        startAddActivity("Swim", null);
                        return true;
                    case R.id.bike:
                        startAddActivity("Bike", null);
                        return true;
                    case R.id.run:
                        startAddActivity("Run", null);
                        return true;
                    default:
                        return false;
                }
            }
        });

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.new_workout_type, popup.getMenu());
        popup.show();
    }

    private void menuClickEdit(Workout workout) {
        if (workout instanceof Swim) {
            startAddActivity("Swim", workout);
        }
        if (workout instanceof Bike) {
            startAddActivity("Bike", workout);
        }
        if (workout instanceof Run) {
            startAddActivity("Run", workout);
        }
        if (workout instanceof Gym) {
            startAddActivity("Gym", workout);
        }
    }

    private void menuClickDelete(Workout workout) {
        mDatabase.delete(workout);
        updateWorkouts();
    }

    private void loadEmptyLog(LayoutInflater inflater) {
        final View emptyLogView = inflater.inflate(R.layout.workout_log_empty, null);

        ImageButton addSwim = (ImageButton) emptyLogView.findViewById(R.id.addSwim);
        ImageButton addBike = (ImageButton) emptyLogView.findViewById(R.id.addBike);
        ImageButton addRun = (ImageButton) emptyLogView.findViewById(R.id.addRun);
        ImageButton addGym = (ImageButton) emptyLogView.findViewById(R.id.addGym);

        addSwim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddActivity("Swim", null);
            }
        });
        addBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddActivity("Bike", null);
            }
        });
        addRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddActivity("Run", null);
            }
        });
        addGym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddActivity("Gym", null);
            }
        });

        final TabHost.TabSpec tabSpec = mTabHost.newTabSpec("emptyTab");
        tabSpec.setIndicator("Add a New Workout");
        tabSpec.setContent(new TabHost.TabContentFactory() {
            @Override
            public View createTabContent(String tag) {
                return emptyLogView;
            }
        });

        mTabHost.addTab(tabSpec);
    }

    private void loadSwimData(LayoutInflater inflater, List<Swim> swims) {

        int count = 0;
        for (final Swim swim : swims) {
            final View v = inflater.inflate(R.layout.workout_swim_view, null);

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
                    OptionsDialog dialog = new OptionsDialog();
                    dialog.setWorkout(swim);
                    dialog.show(getActivity().getSupportFragmentManager(), "optionsDialog");
                    return true;
                }
            });

            final TabHost.TabSpec tabSpec = mTabHost.newTabSpec(SWIM + count);
            tabSpec.setIndicator("", getResources().getDrawable(R.drawable.swim_selector));
            tabSpec.setContent(new TabHost.TabContentFactory() {
                @Override
                public View createTabContent(String tag) {
                    return v;
                }
            });

            mTabHost.addTab(tabSpec);

            count++;
        }
    }

    private void loadRunData(LayoutInflater inflater, List<Run> runs) {

        int count = 0;
        for (final Run run : runs) {
            final View v = inflater.inflate(R.layout.workout_run_view, null);

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
                    OptionsDialog dialog = new OptionsDialog();
                    dialog.setWorkout(run);
                    dialog.show(getActivity().getSupportFragmentManager(), "optionsDialog");
                    return true;
                }
            });

            final TabHost.TabSpec tabSpec = mTabHost.newTabSpec(RUN + count);
            tabSpec.setIndicator("", getResources().getDrawable(R.drawable.run_selector));
            tabSpec.setContent(new TabHost.TabContentFactory() {
                @Override
                public View createTabContent(String tag) {
                    return v;
                }
            });

            mTabHost.addTab(tabSpec);

            count++;
        }
    }

    private void loadBikeData(LayoutInflater inflater, List<Bike> bikes) {

        int count = 0;
        for (final Bike bike : bikes) {
            final View v = inflater.inflate(R.layout.workout_bike_view, null);

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
            avgSpeed.setText(bike.getAvgSpeed());
            maxSpeed.setText(bike.getMaxSpeed());
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
                    OptionsDialog dialog = new OptionsDialog();
                    dialog.setWorkout(bike);
                    dialog.show(getActivity().getSupportFragmentManager(), "optionsDialog");
                    return true;
                }
            });

            final TabHost.TabSpec tabSpec = mTabHost.newTabSpec(BIKE + count);
            tabSpec.setIndicator("", getResources().getDrawable(R.drawable.bike_selector));
            tabSpec.setContent(new TabHost.TabContentFactory() {
                @Override
                public View createTabContent(String tag) {
                    return v;
                }
            });

            mTabHost.addTab(tabSpec);

            count++;
        }
    }

    private void loadGymData(LayoutInflater inflater, List<Gym> gyms) {

        int count = 0;
        for (final Gym gym : gyms) {
            final View v = inflater.inflate(R.layout.workout_gym_view, null);

            TextView name = (TextView) v.findViewById(R.id.gymview_name);
            TextView date = (TextView) v.findViewById(R.id.gymview_date);
            TextView type = (TextView) v.findViewById(R.id.gymview_type);

            RoutineListAdapter adapter = new RoutineListAdapter(getActivity(), R.id.routineList, gym.getRoutines());
            ListView routineList = (ListView) v.findViewById(R.id.routineList);
            routineList.setAdapter(adapter);

            name.setText(gym.getName());
            date.setText(gym.getDate());
            type.setText(gym.getType());

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    OptionsDialog dialog = new OptionsDialog();
                    dialog.setWorkout(gym);
                    dialog.show(getActivity().getSupportFragmentManager(), "optionsDialog");
                    return true;
                }
            });

            final TabHost.TabSpec tabSpec = mTabHost.newTabSpec(GYM + count);
            tabSpec.setIndicator("", getResources().getDrawable(R.drawable.gym_selector));
            tabSpec.setContent(new TabHost.TabContentFactory() {
                @Override
                public View createTabContent(String tag) {
                    return v;
                }
            });

            mTabHost.addTab(tabSpec);

            count++;
        }
    }

    private void updateWorkouts() {
        mTabHost.clearAllTabs();

        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        String formattedDate = df.format(mCalendar.getTime());

        mSwims = mDatabase.getSwimList(new Swim(formattedDate));
        mBikes = mDatabase.getBikeList(new Bike(formattedDate));
        mRuns = mDatabase.getRunList(new Run(formattedDate));
        mGyms = mDatabase.getGymList(new Gym(formattedDate));

        boolean isEmpty = true;

        if (!mSwims.isEmpty()) {
            loadSwimData(mInflater, mSwims);
            isEmpty = false;
        }
        if (!mBikes.isEmpty()) {
            loadBikeData(mInflater, mBikes);
            isEmpty = false;
        }
        if (!mRuns.isEmpty()) {
            loadRunData(mInflater, mRuns);
            isEmpty = false;
        }
        if (!mGyms.isEmpty()) {
            loadGymData(mInflater, mGyms);
            isEmpty = false;
        }

        if (isEmpty) {
            loadEmptyLog(mInflater);
        }

        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
            mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#ecf0f1"));
        }

        final Calendar cal = Calendar.getInstance();
        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH) + 1;
        int d = cal.get(Calendar.DAY_OF_MONTH);

        if (y == mYear && m == mMonth && d == mDay) {
            mDateButton.setText("Today");
            mNextButton.setEnabled(false);
            mNextButton.setTextColor(Color.GRAY);
        } else {
            mDateButton.setText(formattedDate);
            if (!mNextButton.isEnabled()) {
                mNextButton.setEnabled(true);
                mNextButton.setTextColor(Color.BLACK);
            }
        }
    }

    private void restoreActionBar() {
        ActionBar actionBar = getActivity().getActionBar();
    }

    @Override
    public void onTabChanged(String tabId) {
    }

    private class OptionsDialog extends DialogFragment {

        private Workout workout;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            setCancelable(true);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Workout Options")
                    .setItems(R.array.workout_options, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    menuClickEdit(workout);
                                    break;
                                case 1:
                                    menuClickDelete(workout);
                                    break;
                            }
                        }
                    });
            return builder.create();
        }

        public void setWorkout(Workout workout) {
            this.workout = workout;
        }
    }

    // Starts an activity corresponding to the type of desired workout to add
    private void startAddActivity(String type, Workout workout) {
        if (type.equals("Swim")) {
            if (workout == null) {
                Intent swimIntent = new Intent(getActivity(), AddSwim.class);
                startActivityForResult(swimIntent, Statics.INTENT_REQUEST_WORKOUT);
            } else {
                Intent swimIntent = new Intent(getActivity(), AddSwim.class);
                swimIntent.putExtra("Edit", (Swim) workout);
                startActivityForResult(swimIntent, Statics.INTENT_REQUEST_WORKOUT);
            }
        }
        if (type.equals("Bike")) {
            if (workout == null) {
                Intent bikeIntent = new Intent(getActivity(), AddBike.class);
                startActivityForResult(bikeIntent, Statics.INTENT_REQUEST_WORKOUT);
            } else {
                Intent bikeIntent = new Intent(getActivity(), AddBike.class);
                bikeIntent.putExtra("Edit", (Bike) workout);
                startActivityForResult(bikeIntent, Statics.INTENT_REQUEST_WORKOUT);
            }
        }
        if (type.equals("Run")) {
            if (workout == null) {
                Intent runIntent = new Intent(getActivity(), AddRun.class);
                startActivityForResult(runIntent, Statics.INTENT_REQUEST_WORKOUT);
            } else {
                Intent runIntent = new Intent(getActivity(), AddRun.class);
                runIntent.putExtra("Edit", (Run) workout);
                startActivityForResult(runIntent, Statics.INTENT_REQUEST_WORKOUT);
            }
        }
        if (type.equals("Gym")) {
            if (workout == null) {
                Intent gymIntent = new Intent(getActivity(), AddGym.class);
                startActivityForResult(gymIntent, Statics.INTENT_REQUEST_WORKOUT);
            } else {
                Intent gymIntent = new Intent(getActivity(), AddGym.class);
                gymIntent.putExtra("Edit", (Gym) workout);
                startActivityForResult(gymIntent, Statics.INTENT_REQUEST_WORKOUT);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Statics.INTENT_REQUEST_WORKOUT) {
            updateWorkouts();
        }
    }
}
