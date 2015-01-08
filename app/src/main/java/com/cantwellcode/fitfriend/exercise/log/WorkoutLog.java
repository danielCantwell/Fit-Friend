package com.cantwellcode.fitfriend.exercise.log;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;


import com.cantwellcode.fitfriend.exercise.types.Exercise;
import com.fitfriend.app.R;
import com.cantwellcode.fitfriend.exercise.types.Workout;
import com.cantwellcode.fitfriend.utils.Statics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

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
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_workout_log, null);

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
                query.orderByDescending("date");

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

                if (workout.getNotes().trim().isEmpty()) {
                    notes.setVisibility(View.GONE);
                } else {
                    notes.setVisibility(View.VISIBLE);
                    notes.setText(workout.getNotes());
                }

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

                    detailsText += e.getName() + ",  ";
                }
                // this next line removes the extra 'plus' at the end from the for loop
                if (!detailsText.trim().isEmpty()) {
                    detailsText = detailsText.substring(0, detailsText.length() - 3);
                }
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

        mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Workout workout = mAdapter.getItem(position);
                showPopup(view, workout);
                return true;
            }
        });

        setHasOptionsMenu(true);

        return root;
    }

    private void showPopup(View v, final Workout workout) {
        PopupMenu popup = new PopupMenu(getActivity(), v);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_delete:
                        menuClickDelete(workout);
                        return true;
                    default:
                        return false;
                }
            }
        });

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.workout_list_click, popup.getMenu());
        popup.show();
    }

    private void menuClickDelete(Workout workout) {
        try {
            List<Workout> workouts = factory.create().find();
            workouts.remove(workout);
            ParseObject.unpinAll("Workout Log");
            ParseObject.pinAll("Workout Log", workouts);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        updateWorkouts();
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
                startActivityForResult(intent, Statics.INTENT_REQUEST_WORKOUT);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateWorkouts() {
//        mAdapter.clear();
        mAdapter.loadObjects();
    }

    private void restoreActionBar() {
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Workout Log");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Statics.INTENT_REQUEST_WORKOUT) {
            updateWorkouts();
        }
    }
}
