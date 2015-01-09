package com.cantwellcode.fitfriend.exercise.log;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.cantwellcode.fitfriend.exercise.types.Exercise;
import com.cantwellcode.fitfriend.utils.Statics;
import com.fitfriend.app.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.List;

public class NewExerciseActivity extends Activity {

    private ListView mList;
    private Button mCreateExercise;

    private ParseQueryAdapter<Exercise> mAdapter;
    private ParseQueryAdapter.QueryFactory<Exercise> factory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_exercise);

        mCreateExercise = (Button) findViewById(R.id.create);

        mList = (ListView) findViewById(R.id.listView);

        factory = new ParseQueryAdapter.QueryFactory<Exercise>() {
            @Override
            public ParseQuery<Exercise> create() {
                ParseQuery<Exercise> query = Exercise.getQuery();
                query.orderByAscending("name");
                query.fromPin(Statics.SAVED_EXERCISES);
                return query;
            }
        };

        mAdapter = new ParseQueryAdapter<Exercise>(this, factory) {
            @Override
            public View getItemView(Exercise exercise, View v, ViewGroup parent) {

                if (v == null) {
                    v = v.inflate(NewExerciseActivity.this, R.layout.exercise_list_item, null);
                }

                TextView name = (TextView) v.findViewById(R.id.name);
                TextView details = (TextView) v.findViewById(R.id.sets);
                TextView arms = (TextView) v.findViewById(R.id.arms);
                TextView shoulders = (TextView) v.findViewById(R.id.shoulders);
                TextView chest = (TextView) v.findViewById(R.id.chest);
                TextView back = (TextView) v.findViewById(R.id.back);
                TextView abs = (TextView) v.findViewById(R.id.abs);
                TextView legs = (TextView) v.findViewById(R.id.legs);
                TextView glutes = (TextView) v.findViewById(R.id.glutes);


                name.setText(exercise.getName());

                String detailsText = "";
                if (exercise.recordWeight()) {
                    detailsText = "Weight  ";
                }

                if (exercise.recordReps()) {
                    detailsText += "Reps  ";
                }

                if (exercise.recordTime()) {
                    detailsText += "Time";
                }

                details.setText(detailsText);

                arms.setVisibility(exercise.usesArms() ? View.VISIBLE : View.GONE);
                shoulders.setVisibility(exercise.usesShoulders() ? View.VISIBLE : View.GONE);
                chest.setVisibility(exercise.usesChest() ? View.VISIBLE : View.GONE);
                back.setVisibility(exercise.usesBack() ? View.VISIBLE : View.GONE);
                abs.setVisibility(exercise.usesAbs() ? View.VISIBLE : View.GONE);
                legs.setVisibility(exercise.usesLegs() ? View.VISIBLE : View.GONE);
                glutes.setVisibility(exercise.usesGlutes() ? View.VISIBLE : View.GONE);

                return v;
            }
        };

        mList.setAdapter(mAdapter);

        mCreateExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewExerciseActivity.this, CreateExerciseActivity.class);
                startActivity(intent);
            }
        });

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Exercise e = mAdapter.getItem(position);
                Intent intent = new Intent(NewExerciseActivity.this, ExerciseSetsActivity.class);
                intent.putExtra("name", e.getName());
                startActivity(intent);
            }
        });

        mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Exercise exercise = mAdapter.getItem(position);
                showPopup(view, exercise);
                return true;
            }
        });
    }

    private void showPopup(View v, final Exercise exercise) {
        PopupMenu popup = new PopupMenu(this, v);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_delete:
                        menuClickDelete(exercise);
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

    private void menuClickDelete(Exercise exercise) {
        try {
            List<Exercise> exercises = factory.create().find();
            exercises.remove(exercise);
            ParseObject.unpinAll(Statics.SAVED_EXERCISES);
            ParseObject.pinAll(Statics.SAVED_EXERCISES, exercises);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mAdapter.loadObjects();
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

        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
