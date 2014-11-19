package com.fitfriend.app.exercise.log;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.fitfriend.app.R;
import com.fitfriend.app.connect.Post;
import com.fitfriend.app.exercise.types.Exercise;
import com.fitfriend.app.exercise.types.Workout;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewWorkoutActivity extends Activity {

    private EditText mName;
    private TextView mNumExercies;
    private ListView mList;
    private ParseQueryAdapter<Exercise> mAdapter;
    private ParseQueryAdapter.QueryFactory<Exercise> factory;

    private int mExerciseCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_workout);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        String formattedDate = df.format(c.getTime());

        mName = (EditText) findViewById(R.id.name);
        mNumExercies = (TextView) findViewById(R.id.numExercises);

        mName.setText(formattedDate);

        mList = (ListView) findViewById(R.id.exerciseList);

        /* Set up factory for forum posts by any user */
        factory = new ParseQueryAdapter.QueryFactory<Exercise>() {
            public ParseQuery<Exercise> create() {

                /* Create a query for forum posts */
                ParseQuery<Exercise> query = Exercise.getQuery();
                query.fromPin("CurrentExercises");
                query.orderByAscending("createdAt");

                return query;
            }
        };

                /* Set up list adapter using the factory of friends */
        mAdapter = new ParseQueryAdapter<Exercise>(this, factory) {
            @Override
            public View getItemView(final Exercise exercise, View view, ViewGroup parent) {

                if (view == null) {
                    view = view.inflate(NewWorkoutActivity.this, R.layout.exercise_list_item, null);
                }

                TextView name = (TextView) view.findViewById(R.id.name);

                name.setText(exercise.getName());

                return view;
            }
        };

        mList.setAdapter(mAdapter);
        try {
            // TODO - this is very inefficient (creating the factory again) - find another way
            mExerciseCount = factory.create().count();
            mNumExercies.setText("" + mExerciseCount);
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.action_new_routine:
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

}
