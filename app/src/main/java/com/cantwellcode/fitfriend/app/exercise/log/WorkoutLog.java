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
import android.widget.ExpandableListView;
import android.widget.PopupMenu;
import android.widget.Toast;


import com.cantwellcode.fitfriend.app.R;
import com.cantwellcode.fitfriend.app.exercise.types.Workout;
import com.cantwellcode.fitfriend.app.utils.DBHelper;
import com.cantwellcode.fitfriend.app.utils.DatePickerFragment;
import com.cantwellcode.fitfriend.app.utils.DialogListener;
import com.cantwellcode.fitfriend.app.utils.Statics;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Daniel on 2/8/14.
 */
public class WorkoutLog extends Fragment {

    private static final String CARDIO = "Cardio";
    private static final String GYM = "Gym";

    private DBHelper mDatabase;
    private Calendar mCalendar;

    private LayoutInflater mInflater;

    private ExpandableListView listView;
    private Button mPreviousButton, mDateButton, mNextButton;

    private int mYear, mMonth, mDay;

    private WorkoutsExpandableListAdapter mAdapter;

    private static Fragment instance = null;

    public static Fragment newInstance() {
        if(instance == null)
            instance = new WorkoutLog();
        return instance;
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

        listView = (ExpandableListView) root.findViewById(android.R.id.list);

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
        switch (item.getItemId()) {
            case R.id.action_new:
                Intent intent = new Intent(getActivity(), AddWorkout.class);
                startActivity(intent);
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
                    case R.id.cardio:
                        startAddActivity("Cardio", null);
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
//        if (workout instanceof Cardio) {
//            startAddActivity("Cardio", workout);
//        }
    }

    private void menuClickDelete(Workout workout) {
        mDatabase.delete(workout);
        updateWorkouts();
    }

    private void updateWorkouts() {

        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        String formattedDate = df.format(mCalendar.getTime());

//        mDatabase.openDb(DBHelper.DB_NAME_WORKOUTS);
//        mCardios = mDatabase.getCardioListNOC(new Cardio(formattedDate));
//        mGyms = mDatabase.getGymListNOC(new Gym(formattedDate));
//        mDatabase.closeDb();

//        mAdapter = new WorkoutsExpandableListAdapter(getActivity(), mCardios, mGyms);
//        listView.setAdapter(mAdapter);

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
        if (type.equals("Cardio")) {
//            if (workout == null) {
//                Intent cardioIntent = new Intent(getActivity(), AddCardio.class);
//                startActivityForResult(cardioIntent, Statics.INTENT_REQUEST_WORKOUT);
//            } else {
//                Intent cardioIntent = new Intent(getActivity(), AddSwim.class);
//                cardioIntent.putExtra("Edit", (Cardio) workout);
//                startActivityForResult(cardioIntent, Statics.INTENT_REQUEST_WORKOUT);
//            }
        }
        if (type.equals("Gym")) {
//            if (workout == null) {
//                Intent gymIntent = new Intent(getActivity(), AddGym.class);
//                startActivityForResult(gymIntent, Statics.INTENT_REQUEST_WORKOUT);
//            } else {
//                Intent gymIntent = new Intent(getActivity(), AddGym.class);
//                gymIntent.putExtra("Edit", (Gym) workout);
//                startActivityForResult(gymIntent, Statics.INTENT_REQUEST_WORKOUT);
//            }
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
