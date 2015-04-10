package com.cantwellcode.fitfriend.exercise.log;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.cantwellcode.fitfriend.R;
import com.cantwellcode.fitfriend.exercise.types.Cardio;
import com.cantwellcode.fitfriend.exercise.types.Exercise;
import com.cantwellcode.fitfriend.exercise.types.ExerciseSet;
import com.cantwellcode.fitfriend.exercise.types.Workout;
import com.cantwellcode.fitfriend.utils.Statics;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Daniel on 2/8/14.
 */
public class WorkoutLog extends Fragment implements WorkoutListAdapter.LogItemClickListener{

    private TextView emptyText;

    private List<ParseObject> mDataset;

    private RecyclerView mList;
    private WorkoutListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Button mStats;

    private static Fragment instance = null;

    private ParseQuery workoutQuery;
    private ParseQuery cardioQuery;

    public static Fragment newInstance() {
        if (instance == null)
            instance = new WorkoutLog();
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_workout_log, null);

        mStats = (Button) root.findViewById(R.id.stats);
        mStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ParseUser.getCurrentUser().getBoolean("athlete")) {
                    startActivity(new Intent(getActivity(), ExerciseStatsActivity.class));
                } else {
                    Toast.makeText(getActivity(), "Upgrade to 'Athlete' to view statistics", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mList = (RecyclerView) root.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mList.setLayoutManager(mLayoutManager);

        workoutQuery = Workout.getQuery();
        workoutQuery.fromPin(Statics.PIN_WORKOUT_LOG);
        workoutQuery.include("exercises");
        workoutQuery.orderByDescending("date");

        cardioQuery = Cardio.getQuery();
        cardioQuery.fromPin(Statics.PIN_WORKOUT_LOG);
        cardioQuery.orderByDescending("date");

        emptyText = (TextView) root.findViewById(R.id.empty);

        workoutQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List list, ParseException e) {
                mDataset = list;

                cardioQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List list, ParseException e) {
                        mDataset.addAll(list);

                        // Sort the list by date
                        Collections.sort(mDataset, new Comparator<ParseObject>() {
                            @Override
                            public int compare(ParseObject o1, ParseObject o2) {
                                return o2.getDate("date").compareTo(o1.getDate("date"));
                            }
                        });

                        if (mDataset.isEmpty()) {
                            emptyText.setText("When you add a workout\nyou will see it here");
                        } else {
                            emptyText.setVisibility(View.GONE);
                        }

                        mAdapter = new WorkoutListAdapter(mDataset, WorkoutLog.this);
                        mList.setAdapter(mAdapter);
                    }
                });
            }
        });

        setHasOptionsMenu(true);

        return root;
    }

    private void showPopup(View v, final ParseObject object) {
        PopupMenu popup = new PopupMenu(getActivity(), v);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_delete:
                        if (object instanceof Workout)
                            menuClickDeleteWorkout((Workout) object);
                        else if (object instanceof Cardio)
                            menuClickDeleteCardio((Cardio) object);
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

    private void menuClickDeleteWorkout(Workout workout) {
        try {
            List<ExerciseSet> exerciseSets = new ArrayList<ExerciseSet>();
            List<Exercise> exerciseList = workout.getLocalExerciseList();

            for (Exercise e : exerciseList) {
                if (e.getSets() != null)
                    exerciseSets.addAll(e.getSets());
            }

            // Delete Locally
//            ParseObject.unpinAll(exerciseSets); Not sure where sets are stored, or if they even need to be explicitly deleted
            ParseObject.unpinAll(Statics.PIN_EXERCISES, exerciseList);
            workout.unpin(Statics.PIN_WORKOUT_LOG);

            List<ParseObject> objectsToDelete = new ArrayList<ParseObject>();
            objectsToDelete.add(workout);
            objectsToDelete.addAll(exerciseList);
            objectsToDelete.addAll(exerciseSets);

            // Delete Online
            ParseObject.deleteAllInBackground(objectsToDelete);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        mAdapter.removeItemFromList(workout);
        mAdapter.notifyDataSetChanged();
    }

    private void menuClickDeleteCardio(Cardio cardio) {
        try {
            cardio.unpin(Statics.PIN_WORKOUT_LOG);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cardio.deleteInBackground();
        mAdapter.removeItemFromList(cardio);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        restoreActionBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new:
                showWorkoutSelection();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showWorkoutSelection() {

        WorkoutTypeDialog dialog = new WorkoutTypeDialog();
        dialog.show();
    }

    public void updateWorkouts() {

        workoutQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List list, ParseException e) {
                mDataset = list;

                cardioQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List list, ParseException e) {
                        mDataset.addAll(list);

                        // Sort the list by date
                        Collections.sort(mDataset, new Comparator<ParseObject>() {
                            @Override
                            public int compare(ParseObject o1, ParseObject o2) {
                                return o2.getDate("date").compareTo(o1.getDate("date"));
                            }
                        });

                        if (mDataset.isEmpty()) {
                            emptyText.setText("When you add a workout\nyou will see it here");
                        } else {
                            emptyText.setVisibility(View.GONE);
                        }

                        mAdapter.updateList(mDataset);
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
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
            if (resultCode == getActivity().RESULT_OK) {
                updateWorkouts();
            }
        }
    }

    @Override
    public void onCardioClick(View v) {
        int i = mList.getChildAdapterPosition(v);
        Cardio c = (Cardio) mDataset.get(i);

        if (c != null) {
            c.pinInBackground(Statics.PIN_WORKOUT_DETAILS, new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    startActivity(new Intent(getActivity(), CardioViewActivity.class));
                }
            });
        } else {
            Toast.makeText(getActivity(), "Cardio " + i + " doesn't exist", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWorkoutClick(View v) {
        int i = mList.getChildAdapterPosition(v);
        Workout w = (Workout) mDataset.get(i);

        if (w != null) {
            w.pinInBackground(Statics.PIN_WORKOUT_DETAILS, new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    startActivity(new Intent(getActivity(), WorkoutViewActivity.class));
                }
            });
        } else {
            Toast.makeText(getActivity(), "Workout " + i + " doesn't exist", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCardioLongClick(View v) {
        int i = mList.getChildAdapterPosition(v);
        Cardio c = (Cardio) mDataset.get(i);

        if (c != null) {
            showPopup(v, c);
        }
    }

    @Override
    public void onWorkoutLongClick(View v) {
        int i = mList.getChildAdapterPosition(v);
        Workout w = (Workout) mDataset.get(i);

        if (w != null) {
            showPopup(v, w);
        }
    }

    /**
     * Dialog for choosing a type of workout to begin ( Cardio / Gym )
     */
    public class WorkoutTypeDialog extends AlertDialog {

        public WorkoutTypeDialog() {
            super(getActivity());

            View root = getActivity().getLayoutInflater().inflate(R.layout.dialog_workout_type, null);

            setView(root);

            root.findViewById(R.id.select_cardio).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                    Intent intent = new Intent(getActivity(), CardioActivity.class);
                    startActivityForResult(intent, Statics.INTENT_REQUEST_WORKOUT);
                }
            });

            root.findViewById(R.id.select_gym).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                    Intent intent = new Intent(getActivity(), NewWorkoutActivity.class);
                    startActivityForResult(intent, Statics.INTENT_REQUEST_WORKOUT);
                }
            });
        }

    }
}
