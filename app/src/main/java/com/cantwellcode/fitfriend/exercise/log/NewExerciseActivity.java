package com.cantwellcode.fitfriend.exercise.log;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.cantwellcode.fitfriend.exercise.types.Exercise;
import com.fitfriend.app.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewExerciseActivity extends Activity {

    private ListView mList;
    private Button mCreateExercise;

    private List<Exercise> mExercises;
    private SavedExercisesAdapter mAdapter;
    private ParseQuery<Exercise> mExerciseQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_exercise);

        mCreateExercise = (Button) findViewById(R.id.create);

        mList = (ListView) findViewById(R.id.listView);

        mExerciseQuery = Exercise.getQuery();
        mExerciseQuery.orderByAscending("name");
        mExerciseQuery.fromPin(getResources().getString(R.string.saved_exercises));
        mExerciseQuery.findInBackground(new FindCallback<Exercise>() {
            @Override
            public void done(List<Exercise> exercises, ParseException e) {
                mAdapter = new SavedExercisesAdapter(NewExerciseActivity.this, R.layout.exercise_list_item, exercises);
                mList.setAdapter(mAdapter);
                mExercises = exercises;
            }
        });

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Exercise e = mExercises.get(position);
                Intent intent = new Intent(NewExerciseActivity.this, ExerciseSetsActivity.class);
                intent.putExtra("name", e.getName());
                startActivity(intent);
            }
        });

        mCreateExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewExerciseActivity.this, CreateExerciseActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_exercise, menu);
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

            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
