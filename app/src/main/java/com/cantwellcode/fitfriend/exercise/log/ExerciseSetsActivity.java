package com.cantwellcode.fitfriend.exercise.log;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fitfriend.app.R;
import com.cantwellcode.fitfriend.exercise.types.Exercise;
import com.cantwellcode.fitfriend.exercise.types.Set;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class ExerciseSetsActivity extends Activity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private enum TimerState {
        ready, started, stopped
    }

    private TimerState timerState = TimerState.ready;

    private View mIncludeWeight;
    private View mIncludeReps;
    private View mIncludeTime;

    private SeekBar mSeekWeight;
    private SeekBar mSeekReps;

    private Button mMinusWeight;
    private Button mMinusReps;
    private Button mPlusWeight;
    private Button mPlusReps;
    private Button mStartTime;
    private Button mStopTime;

    private Chronometer mTime;
    private long mStoppedTime;

    private TextView mWeight;
    private TextView mReps;

    private LinearLayout mScrollLayout;

    private Button mAddSet;

    private List<Set> mSets;
    private String mName;
    private boolean weightData;
    private boolean repsData;
    private boolean timeData;

    private Exercise mExercise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_sets);


        mName = getIntent().getStringExtra("name");
        ParseQuery<Exercise> mExerciseQuery = Exercise.getQuery();
        mExerciseQuery.fromPin(getResources().getString(R.string.saved_exercises));
        mExerciseQuery.whereEqualTo("name", mName);
        try {
            mExercise = mExerciseQuery.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        mExerciseQuery.getFirstInBackground(new GetCallback<Exercise>() {
//            @Override
//            public void done(Exercise exercise, ParseException e) {
//                mExercise = exercise;
//                setupExercise(mExercise);
//            }
//        });
        setupExercise(mExercise);
    }

    private void setupExercise(Exercise e) {

        TextView name = (TextView) findViewById(R.id.name);
        name.setText(mName);

        mSets = new ArrayList<Set>();

        weightData = e.recordWeight();
        repsData = e.recordReps();
        timeData = e.recordTime();

        mScrollLayout = (LinearLayout) findViewById(R.id.linearLayout);
        mAddSet = (Button) findViewById(R.id.addSet);
        mAddSet.setOnClickListener(this);

        mIncludeWeight = findViewById(R.id.includeWeight);
        mIncludeReps = findViewById(R.id.includeReps);
        mIncludeTime = findViewById(R.id.includeTime);

        if (weightData) {
            mSeekWeight = (SeekBar) findViewById(R.id.seekWeight);
            mWeight = (TextView) findViewById(R.id.weight);
            mMinusWeight = (Button) findViewById(R.id.minusWeight);
            mPlusWeight = (Button) findViewById(R.id.plusWeight);

            mMinusWeight.setOnClickListener(this);
            mPlusWeight.setOnClickListener(this);

            mSeekWeight.setOnSeekBarChangeListener(this);
        } else {
            mIncludeWeight.setVisibility(View.GONE);
        }
        if (repsData) {
            mSeekReps = (SeekBar) findViewById(R.id.seekReps);
            mReps = (TextView) findViewById(R.id.reps);
            mMinusReps = (Button) findViewById(R.id.minusReps);
            mPlusReps = (Button) findViewById(R.id.plusReps);

            mMinusReps.setOnClickListener(this);
            mPlusReps.setOnClickListener(this);

            mSeekReps.setOnSeekBarChangeListener(this);
        } else {
            mIncludeReps.setVisibility(View.GONE);
        }
        if (timeData) {
            mTime = (Chronometer) findViewById(R.id.time);
            mStartTime = (Button) findViewById(R.id.start);
            mStopTime = (Button) findViewById(R.id.stop_reset);
            mStopTime.setVisibility(View.INVISIBLE);

            mStartTime.setOnClickListener(this);
            mStopTime.setOnClickListener(this);
        } else {
            mIncludeTime.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.exercise_sets, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.action_save:
                if (mSets.isEmpty()) {
                    Toast.makeText(this, "Add at least 1 set", Toast.LENGTH_SHORT).show();
                } else {
                    Exercise exercise = mExercise.createNew();
                    exercise.setName(mName);
                    exercise.setSets(mSets);
                    exercise.pinInBackground("CurrentExercises", new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Toast.makeText(ExerciseSetsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            Intent intent = new Intent(ExerciseSetsActivity.this, NewWorkoutActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                }

                return true;
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.seekWeight:
                mWeight.setText("" + progress);
                break;
            case R.id.seekReps:
                mReps.setText("" + progress);
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addSet:
                addSet();
                break;
            case R.id.minusWeight:
                mSeekWeight.setProgress(mSeekWeight.getProgress() - 1);
                break;
            case R.id.plusWeight:
                mSeekWeight.setProgress(mSeekWeight.getProgress() + 1);
                break;
            case R.id.minusReps:
                mSeekReps.setProgress(mSeekReps.getProgress() - 1);
                break;
            case R.id.plusReps:
                mSeekReps.setProgress(mSeekReps.getProgress() + 1);
                break;
            case R.id.start:
                startTimer();
                break;
            case R.id.stop_reset:
                stopResetTimer();
                break;
        }
    }

    /*
    Adds the set to the layout, and adds it to the list of sets
     */
    public void addSet() {
        View view = getLayoutInflater().inflate(R.layout.exercise_set, null);
        TextView weightText = (TextView) view.findViewById(R.id.weight);
        TextView repsText = (TextView) view.findViewById(R.id.reps);
        TextView timeText = (TextView) view.findViewById(R.id.time_text);
        Set set = new Set();

        if (weightData) {
            set.setWeight(mSeekWeight.getProgress());
            weightText.setText("" + mSeekWeight.getProgress());
        } else {
            view.findViewById(R.id.row_weight).setVisibility(View.GONE);
        }
        if (repsData) {
            set.setReps(mSeekReps.getProgress());
            repsText.setText("" + mSeekReps.getProgress());
        } else {
            view.findViewById(R.id.row_reps).setVisibility(View.GONE);
        }
        if (timeData) {
            int time;
            if (timerState == TimerState.stopped) {
                time = (int) mStoppedTime / -1000;
            } else if (timerState == TimerState.started) {
                time = (int) (SystemClock.elapsedRealtime() - mTime.getBase()) / 1000;
            } else {
                time = 0;
            }
            set.setTime(time);
            timeText.setText("" + time + "s");
        } else {
            view.findViewById(R.id.row_time).setVisibility(View.GONE);
        }

        mScrollLayout.addView(view);
        mSets.add(set);
    }

    public void startTimer() {
        if (timerState == TimerState.stopped) {
            mTime.setBase(SystemClock.elapsedRealtime() + mStoppedTime);
            mStopTime.setText("Stop");
        } else if (timerState == TimerState.ready) {
            mTime.setBase(SystemClock.elapsedRealtime());
        }
        mTime.start();
        mStartTime.setVisibility(View.INVISIBLE);
        mStopTime.setVisibility(View.VISIBLE);
        timerState = TimerState.started;
    }

    public void stopResetTimer() {
        if (timerState == TimerState.started) {
            mTime.stop();
            mStoppedTime = mTime.getBase() - SystemClock.elapsedRealtime();
            mStopTime.setText("Reset");
            mStartTime.setVisibility(View.VISIBLE);
            timerState = TimerState.stopped;
        } else if (timerState == TimerState.stopped) {
            mTime.setBase(SystemClock.elapsedRealtime());
            mStopTime.setText("Stop");
            mStartTime.setVisibility(View.VISIBLE);
            mStopTime.setVisibility(View.INVISIBLE);
            timerState = TimerState.ready;
        }
    }
}
