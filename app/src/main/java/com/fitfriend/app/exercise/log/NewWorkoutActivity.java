package com.fitfriend.app.exercise.log;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.fitfriend.app.R;
import com.fitfriend.app.exercise.types.Workout;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewWorkoutActivity extends Activity {

    private EditText mName;
    private TextView mNumExercies;
    private ListView mList;
    private ArrayAdapter<String> mAdapter;

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

        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mList = (ListView) findViewById(R.id.exerciseList);

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
                setResult(RESULT_CANCELED);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
