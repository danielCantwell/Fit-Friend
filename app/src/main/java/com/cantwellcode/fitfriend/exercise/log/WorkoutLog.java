package com.cantwellcode.fitfriend.exercise.log;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;


import com.cantwellcode.fitfriend.exercise.types.Exercise;
import com.cantwellcode.fitfriend.exercise.types.Set;
import com.fitfriend.app.R;
import com.cantwellcode.fitfriend.exercise.types.Workout;
import com.cantwellcode.fitfriend.utils.DBHelper;
import com.cantwellcode.fitfriend.utils.DatePickerFragment;
import com.cantwellcode.fitfriend.utils.DialogListener;
import com.cantwellcode.fitfriend.utils.Statics;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Daniel on 2/8/14.
 */
public class WorkoutLog extends Fragment {

    private Calendar mCalendar;

    private ListView mList;
    private ParseQueryAdapter<Workout> mAdapter;
    private ParseQueryAdapter.QueryFactory<Workout> factory;

    private static Fragment instance = null;

    public static Fragment newInstance() {
        if (instance == null)
            instance = new WorkoutLog();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_workout_log, null);

        mCalendar = Calendar.getInstance();

        mList = (ListView) root.findViewById(R.id.listView);

        TextView emptyView = (TextView) root.findViewById(android.R.id.empty);
        mList.setEmptyView(emptyView);

        /* Set up factory for current exercises */
        factory = new ParseQueryAdapter.QueryFactory<Workout>() {
            public ParseQuery<Workout> create() {

                /* Create a query for forum posts */
                ParseQuery<Workout> query = Workout.getQuery();
                query.fromPin("Workout Log");
                query.include("exercises");
                query.orderByAscending("date");

                return query;
            }
        };

        /* Set up list adapter using the factory of exercises */
        mAdapter = new ParseQueryAdapter<Workout>(getActivity(), factory) {
            @Override
            public View getItemView(final Workout workout, View view, ViewGroup parent) {

                if (view == null) {
                    view = view.inflate(getActivity(), R.layout.workout_log_item, null);
                }

                TextView date = (TextView) view.findViewById(R.id.date);
                TextView details = (TextView) view.findViewById(R.id.details);
                TextView notes = (TextView) view.findViewById(R.id.notes);
                TextView arms = (TextView) view.findViewById(R.id.arms);
                TextView shoulders = (TextView) view.findViewById(R.id.shoulders);
                TextView chest = (TextView) view.findViewById(R.id.chest);
                TextView back = (TextView) view.findViewById(R.id.back);
                TextView abs = (TextView) view.findViewById(R.id.abs);
                TextView legs = (TextView) view.findViewById(R.id.legs);
                TextView glutes = (TextView) view.findViewById(R.id.glutes);

                SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
                String dateFormat = formatter.format(workout.getDate());
                date.setText(dateFormat);
                notes.setText(workout.getNotes());

                boolean usesArms = false;
                boolean usesShoulders = false;
                boolean usesChest = false;
                boolean usesBack = false;
                boolean usesAbs = false;
                boolean usesLegs = false;
                boolean usesGlutes = false;

                String detailsText = "";

                List<Exercise> exerciseList = workout.getExerciseList();
                for (Exercise e : exerciseList) {
                    if (e.usesArms()) usesArms = true;
                    if (e.usesShoulders()) usesShoulders = true;
                    if (e.usesChest()) usesChest = true;
                    if (e.usesBack()) usesBack = true;
                    if (e.usesAbs()) usesAbs = true;
                    if (e.usesLegs()) usesLegs = true;
                    if (e.usesGlutes()) usesGlutes = true;

                    detailsText += e.getName() + "  +  ";
                }
                // this next line removes the extra 'plus' at the end from the for loop
                detailsText = detailsText.substring(0, detailsText.length() - 5);
                details.setText(detailsText);

                arms.setVisibility(usesArms ? View.VISIBLE : View.GONE);
                shoulders.setVisibility(usesShoulders ? View.VISIBLE : View.GONE);
                chest.setVisibility(usesChest ? View.VISIBLE : View.GONE);
                back.setVisibility(usesBack ? View.VISIBLE : View.GONE);
                abs.setVisibility(usesAbs ? View.VISIBLE : View.GONE);
                legs.setVisibility(usesLegs ? View.VISIBLE : View.GONE);
                glutes.setVisibility(usesGlutes ? View.VISIBLE : View.GONE);

                return view;
            }
        };

        mList.setAdapter(mAdapter);

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
                Intent intent = new Intent(getActivity(), NewWorkoutActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void menuClickEdit(Workout workout) {
//        if (workout instanceof Cardio) {
//            startAddActivity("Cardio", workout);
//        }
    }

    private void menuClickDelete(Workout workout) {
        updateWorkouts();
    }

    private void updateWorkouts() {

        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        String formattedDate = df.format(mCalendar.getTime());

        final Calendar cal = Calendar.getInstance();
        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH) + 1;
        int d = cal.get(Calendar.DAY_OF_MONTH);
    }

    private void restoreActionBar() {
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Exercise Log");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Statics.INTENT_REQUEST_WORKOUT) {
            updateWorkouts();
        }
    }
}
