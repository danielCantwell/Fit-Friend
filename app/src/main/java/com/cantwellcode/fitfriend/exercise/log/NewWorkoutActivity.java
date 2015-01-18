package com.cantwellcode.fitfriend.exercise.log;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.cantwellcode.fitfriend.exercise.types.ExerciseSet;
import com.cantwellcode.fitfriend.exercise.types.Workout;
import com.cantwellcode.fitfriend.utils.Statics;
import com.cantwellcode.fitfriend.R;
import com.cantwellcode.fitfriend.exercise.types.Exercise;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewWorkoutActivity extends Activity {

    private TextView mName;
    private TextView mNumExercies;
    private ListView mList;
    private EditText mNotes;

    private ParseQueryAdapter<Exercise> mAdapter;
    private ParseQueryAdapter.QueryFactory<Exercise> factory;

    private int mExerciseCount = 0;

    private Date mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_workout);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        String formattedDate = df.format(c.getTime());
        mDate = c.getTime();

        mName = (TextView) findViewById(R.id.name);
        mNumExercies = (TextView) findViewById(R.id.numExercises);
        mNotes = (EditText) findViewById(R.id.notes);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        mNotes.setText(sp.getString("Workout Notes", ""));

        mName.setText(formattedDate);

        TextView emptyView = (TextView) findViewById(android.R.id.empty);

        mList = (ListView) findViewById(R.id.exerciseList);
        mList.setEmptyView(emptyView);

        /* Set up factory for current exercises */
        factory = new ParseQueryAdapter.QueryFactory<Exercise>() {
            public ParseQuery<Exercise> create() {

                /* Create a query for forum posts */
                ParseQuery<Exercise> query = Exercise.getQuery();
                query.fromPin(Statics.PIN_CURRENT_EXERCISES);
                query.orderByAscending("num");

                return query;
            }
        };

        /* Set up list adapter using the factory of exercises */
        mAdapter = new ParseQueryAdapter<Exercise>(this, factory) {
            @Override
            public View getItemView(final Exercise exercise, View view, ViewGroup parent) {

                if (view == null) {
                    view = view.inflate(NewWorkoutActivity.this, R.layout.exercise_list_item, null);
                }

                /* Initialize all of the widgets in the exercise item view */
                TextView name = (TextView) view.findViewById(R.id.name);
                TextView sets = (TextView) view.findViewById(R.id.sets);
                TextView arms = (TextView) view.findViewById(R.id.arms);
                TextView shoulders = (TextView) view.findViewById(R.id.shoulders);
                TextView chest = (TextView) view.findViewById(R.id.chest);
                TextView back = (TextView) view.findViewById(R.id.back);
                TextView abs = (TextView) view.findViewById(R.id.abs);
                TextView legs = (TextView) view.findViewById(R.id.legs);
                TextView glutes = (TextView) view.findViewById(R.id.glutes);

                name.setText(exercise.getName());

                // empty string - will be adjusting it
                String setsText = "";

                boolean reps = exercise.recordReps();
                boolean weight = exercise.recordWeight();
                boolean time = exercise.recordTime();

                List<ExerciseSet> eSets = exercise.getSets();

                if (eSets == null) {
                    setsText = "Click to Add Sets";
                } else {

                    int count = 0;
                    for (ExerciseSet eSet : eSets) {
                        count++;
                        if (reps) {
                            setsText += String.valueOf(eSet.getReps());
                            if (weight) {
                                setsText += "x" + String.valueOf(eSet.getWeight()) + "lbs";
                            }
                            if (time) {
                                setsText += "x" + String.valueOf(eSet.getTime()) + "s";
                            }
                        } else if (weight) {
                            setsText += String.valueOf(eSet.getWeight()) + "lbs";
                            if (time) {
                                setsText += "x" + String.valueOf(eSet.getTime()) + "s";
                            }
                        } else if (time) {
                            setsText += String.valueOf(eSet.getTime()) + "s";
                        }

                        if (count != eSets.size()) {
                            setsText += "  +  ";
                        }
                    }
                }

                sets.setText(setsText);

                arms.setVisibility(exercise.usesArms() ? View.VISIBLE : View.GONE);
                shoulders.setVisibility(exercise.usesShoulders() ? View.VISIBLE : View.GONE);
                chest.setVisibility(exercise.usesChest() ? View.VISIBLE : View.GONE);
                back.setVisibility(exercise.usesBack() ? View.VISIBLE : View.GONE);
                abs.setVisibility(exercise.usesAbs() ? View.VISIBLE : View.GONE);
                legs.setVisibility(exercise.usesLegs() ? View.VISIBLE : View.GONE);
                glutes.setVisibility(exercise.usesGlutes() ? View.VISIBLE : View.GONE);

                return view;
            }
        };

        mList.setAdapter(mAdapter);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AddSetDialog dialog = new AddSetDialog(NewWorkoutActivity.this, mAdapter.getItem(position));
                dialog.show();
            }
        });

        mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showPopup(view, mAdapter.getItem(position));
                return true;
            }
        });

        mAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<Exercise>() {
            @Override
            public void onLoading() {

            }

            @Override
            public void onLoaded(List<Exercise> exercises, Exception e) {
                if (e == null) {
                    mExerciseCount = exercises.size();
                } else {
                    mExerciseCount = 0;
                }
                mNumExercies.setText(String.valueOf(mExerciseCount));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_workout, menu);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_save:
                saveWorkout();
                break;
            case R.id.action_new_routine:
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                sp.edit().putString("Workout Notes", mNotes.getText().toString().trim()).commit();
                Intent intent = new Intent(this, NewExerciseActivity.class);
                intent.putExtra("num", mExerciseCount + 1);
                startActivityForResult(intent, Statics.INTENT_REQUEST_ADD_EXERCISE);
                return true;
            case android.R.id.home:
                ParseObject.unpinAllInBackground(Statics.PIN_CURRENT_EXERCISES);
                setResult(RESULT_CANCELED);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /* Save the workout and go back to the log */
    private void saveWorkout() {
        /* Make sure the user is not trying to save an empty workout */
        if (mExerciseCount == 0) {
            Toast.makeText(this, "Please add at least 1 exercise", Toast.LENGTH_SHORT).show();
            return;
        }

        /* Create the workout object to save, and set the current user to the workout */
        final Workout workout = new Workout(mDate, mNotes.getText().toString().trim());
        workout.setUserAsCurrent();

        try {
            final List<Exercise> exerciseList = factory.create().find();
            /* Save exercises and workout locally */
            workout.pinInBackground("Workout Log", new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(NewWorkoutActivity.this);
                    sp.edit().remove("Workout Notes").commit();
                    ParseObject.unpinAllInBackground(Statics.PIN_CURRENT_EXERCISES);
                    setResult(Statics.INTENT_REQUEST_WORKOUT);
                    finish();
                }
            });
            workout.saveExercisesLocally(exerciseList);
            /* Save exercises and workout online */
            workout.saveEventually(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    workout.saveExercises(exerciseList);
                }
            });

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void updateList() {
        // Update the exercise since it has a new set
        mAdapter.loadObjects();
    }

    private void showPopup(View v, final Exercise exercise) {
        PopupMenu popup = new PopupMenu(this, v);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_delete_last_set:
                        deleteLastSet(exercise);
                        return true;
                    case R.id.action_delete_all_sets:
                        deleteAllSets(exercise);
                        return true;
                    case R.id.action_delete_exercise:
                        deleteExercise(exercise);
                        return true;
                    default:
                        return false;
                }
            }
        });

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.exercise_sets, popup.getMenu());
        popup.show();
    }

    private void deleteLastSet(Exercise e) {
//        e.removeLastSet(); TODO
        updateList();
    }

    private void deleteAllSets(Exercise e) {
//        e.removeAllSets(); TODO
        updateList();
    }

    private void deleteExercise(Exercise e) {
        e.unpinInBackground(Statics.PIN_CURRENT_EXERCISES);
        updateList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Statics.INTENT_REQUEST_ADD_EXERCISE && resultCode == RESULT_OK) {
            updateList();
        }
    }
}
