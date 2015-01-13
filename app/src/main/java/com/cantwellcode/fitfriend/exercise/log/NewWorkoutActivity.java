package com.cantwellcode.fitfriend.exercise.log;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
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
                query.fromPin("CurrentExercises");
                query.orderByAscending("name");

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

                JSONArray setArray = null;
                try {
                    setArray = new JSONArray(exercise.getSets().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < setArray.length(); i++) {

                    try {

                        JSONObject s = setArray.getJSONObject(i);

                        if (reps) {
                            setsText += s.get("reps") + "";
                            if (weight) {
                                setsText += "x" + s.get("weight") + "lbs";
                            }
                            if (time) {
                                setsText += "x" + s.get("time") + "s";
                            }
                        } else if (weight) {
                            setsText += s.get("weight") + "lbs";
                            if (time) {
                                setsText += "x" + s.get("time") + "s";
                            }
                        } else if (time) {
                            setsText += s.get("time") + "s";
                        }
                        setsText += "  +  ";
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // this next line removes the extra 'plus' at the end from the for loop
                setsText = setsText.substring(0, setsText.length() - 5);
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

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
                startActivity(intent);
                return true;
            case android.R.id.home:
                ParseObject.unpinAllInBackground("CurrentExercises");
                setResult(RESULT_CANCELED);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveWorkout() {
        if (mExerciseCount == 0) {
            Toast.makeText(this, "Please add at least 1 exercise", Toast.LENGTH_SHORT).show();
            return;
        }

        Workout workout = new Workout(mDate, mNotes.getText().toString().trim());
        try {
            workout.saveExercisesLocally(factory.create().find());
            workout.pinInBackground("Workout Log", new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(NewWorkoutActivity.this);
                    sp.edit().remove("Workout Notes").commit();
                    ParseObject.unpinAllInBackground("CurrentExercises");
                    setResult(Statics.INTENT_REQUEST_WORKOUT);
                    finish();
                }
            });
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
