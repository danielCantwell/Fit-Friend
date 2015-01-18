package com.cantwellcode.fitfriend.exercise.log;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.cantwellcode.fitfriend.R;
import com.cantwellcode.fitfriend.exercise.types.Exercise;
import com.cantwellcode.fitfriend.exercise.types.ExerciseSet;
import com.cantwellcode.fitfriend.exercise.types.Workout;
import com.cantwellcode.fitfriend.utils.Statics;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;

public class WorkoutViewActivity extends Activity {

    private TextView mNumExercies;
    private ListView mList;
    private TextView mNotes;

    private ParseQueryAdapter<Exercise> mAdapter;
    private ParseQueryAdapter.QueryFactory<Exercise> factory;

    private int mExerciseCount = 0;

    private Workout mWorkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_view);

        mNumExercies = (TextView) findViewById(R.id.numExercises);
        mNotes = (TextView) findViewById(R.id.notes);

        ParseQuery<Workout> workoutParseQuery = Workout.getQuery();
        workoutParseQuery.fromPin("Workout to View");
        try {
            mWorkout = workoutParseQuery.getFirst();
            mWorkout.unpin("Workout to View");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
        String dateFormat = formatter.format(mWorkout.getDate());

        getActionBar().setTitle(dateFormat);

        mNotes.setText(mWorkout.getNotes());

        TextView emptyView = (TextView) findViewById(android.R.id.empty);

        mList = (ListView) findViewById(R.id.exerciseList);
        mList.setEmptyView(emptyView);

        /* Set up factory for current exercises */
        factory = new ParseQueryAdapter.QueryFactory<Exercise>() {
            public ParseQuery<Exercise> create() {

                /* Create a query for forum posts */
                ParseQuery<Exercise> query = Exercise.getQuery();
                query.fromPin(Statics.PIN_EXERCISES);
                query.whereEqualTo("workout", mWorkout);
                query.orderByAscending("num");
                query.include("sets");

                return query;
            }
        };

        /* Set up list adapter using the factory of exercises */
        mAdapter = new ParseQueryAdapter<Exercise>(this, factory) {
            @Override
            public View getItemView(final Exercise exercise, View view, ViewGroup parent) {

                if (view == null) {
                    view = view.inflate(WorkoutViewActivity.this, R.layout.exercise_list_item, null);
                }

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

                String setsText = "";

                boolean reps = exercise.recordReps();
                boolean weight = exercise.recordWeight();
                boolean time = exercise.recordTime();

                List<ExerciseSet> eSets = exercise.getSets();

                if (eSets == null) {
                    setsText = "No Sets";
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
        try {
            // TODO - there could be a better way to do this
            mExerciseCount = factory.create().count();
            mNumExercies.setText("" + mExerciseCount);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_workout_view, menu);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
