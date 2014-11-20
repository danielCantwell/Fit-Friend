package com.cantwellcode.fitfriend.exercise.log;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.fitfriend.app.R;

import java.util.ArrayList;
import java.util.Arrays;

public class NewExerciseActivity extends Activity {

    private EditText mName;
    private CheckBox mWeight;
    private CheckBox mReps;
    private CheckBox mTime;
    private GridView mGrid;
    private Button mStartExercise;

    private ArrayList<String> mExercises;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_exercise);

        mExercises = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.routines)));
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mExercises);

        mName = (EditText) findViewById(R.id.name);
        mWeight = (CheckBox) findViewById(R.id.checkWeight);
        mReps = (CheckBox) findViewById(R.id.checkReps);
        mTime = (CheckBox) findViewById(R.id.checkTime);
        mGrid = (GridView) findViewById(R.id.gridView);
        mStartExercise = (Button) findViewById(R.id.startExercise);

        mGrid.setAdapter(mAdapter);

        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = mExercises.get(position);
                Intent intent = new Intent(NewExerciseActivity.this, ExerciseSetsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", name);
                bundle.putBoolean("weight", mWeight.isChecked());
                bundle.putBoolean("reps", mReps.isChecked());
                bundle.putBoolean("time", mTime.isChecked());
                intent.putExtra("args", bundle);
                startActivity(intent);
            }
        });

        mStartExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mName.getText().toString().trim().length() == 0) {
                    Toast.makeText(NewExerciseActivity.this, "Please enter a name for the routine", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(NewExerciseActivity.this, ExerciseSetsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("name", mName.getText().toString());
                    bundle.putBoolean("weight", mWeight.isChecked());
                    bundle.putBoolean("reps", mReps.isChecked());
                    bundle.putBoolean("time", mTime.isChecked());
                    intent.putExtra("args", bundle);
                    startActivity(intent);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_exercise, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
}
