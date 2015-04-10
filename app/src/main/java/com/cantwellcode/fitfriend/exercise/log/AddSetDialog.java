package com.cantwellcode.fitfriend.exercise.log;

import android.app.AlertDialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cantwellcode.fitfriend.R;
import com.cantwellcode.fitfriend.exercise.types.Exercise;
import com.cantwellcode.fitfriend.exercise.types.ExerciseSet;
import com.cantwellcode.fitfriend.utils.Statics;

/**
 * Created by Daniel on 1/16/2015.
 */
public class AddSetDialog extends AlertDialog {

    private Exercise mExercise;
    private boolean hasWeight;
    private boolean hasReps;
    private boolean hasTime;

    private boolean timerActive = false;
    private boolean timerPaused = false;
    private long millisLeft = 0;
    long timer;

    private NumberPicker mRepsPicker;
    private NumberPicker mWeightPicker;

    private EditText time;
    ProgressBar timerProgress;

    private Button start_stop;
    private Button reset;

    private Button mNegative;
    private Button mPositive;

    private CountDownTimer mCountDownTimer;

    private View root;
    private NewWorkoutActivity mActivity;

    Vibrator mVibrator;

    protected AddSetDialog(NewWorkoutActivity activity, Exercise e) {
        super(activity);

        mExercise = e;
        mActivity = activity;

        mVibrator = (Vibrator) mActivity.getSystemService(Context.VIBRATOR_SERVICE);

        hasReps = mExercise.recordReps();
        hasWeight = mExercise.recordWeight();
        hasTime = mExercise.recordTime();

        root = mActivity.getLayoutInflater().inflate(R.layout.dialog_exercise_sets, null);

        TextView name = (TextView) root.findViewById(R.id.name);
        name.setText(mExercise.getName());

        setView(root);

        setupDialog();

        mNegative = (Button) root.findViewById(R.id.negativeButton);
        mPositive = (Button) root.findViewById(R.id.positiveButton);

        mNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerActive) {
                    mCountDownTimer.cancel();
                }
                dismiss();
            }
        });

        mPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerActive) {
                    mCountDownTimer.cancel();
                }
                addSet();
                dismiss();
            }
        });
    }

    private void setupDialog() {
        /* If the exercise records reps, setup reps */
        if (hasReps) {

            mRepsPicker = (NumberPicker) root.findViewById(R.id.repsPicker);
            mRepsPicker.setMinValue(1);
            mRepsPicker.setMaxValue(50);
            mRepsPicker.setWrapSelectorWheel(false);
            ExerciseSet lastSet = mExercise.getLastSet();
            if (lastSet != null) {
                mRepsPicker.setValue(lastSet.getReps());
            } else {
                mRepsPicker.setValue(15);
            }

        } else {
            root.findViewById(R.id.includeReps).setVisibility(View.GONE);
        }
        /* If the exercise records weight, setup weight */
        if (hasWeight) {

            mWeightPicker = (NumberPicker) root.findViewById(R.id.weightPicker);
            mWeightPicker.setMinValue(1);
            mWeightPicker.setMaxValue(400);
            mWeightPicker.setWrapSelectorWheel(false);
            ExerciseSet lastSet = mExercise.getLastSet();
            if (lastSet != null) {
                mWeightPicker.setValue(lastSet.getWeight());
            } else {
                mWeightPicker.setValue(50);
            }

        } else {
            root.findViewById(R.id.includeWeight).setVisibility(View.GONE);
        }
        /* If the exercise records time, setup time */
        if (hasTime) {
            time = (EditText) root.findViewById(R.id.time);
            ExerciseSet lastSet = mExercise.getLastSet();
            if (lastSet != null) {
                time.setText(String.valueOf(lastSet.getTime()));
            } else {
                time.setText("30");
            }
            time.setSelectAllOnFocus(true);
            start_stop = (Button) root.findViewById(R.id.start_stop);
            reset = (Button) root.findViewById(R.id.reset);
            timerProgress = (ProgressBar) root.findViewById(R.id.progressBar);
            timer = Long.valueOf(time.getText().toString().trim()) * 1000;

            start_stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!timerActive) { // timer is not active, so the user wants to start the timer

                        if (PreferenceManager.getDefaultSharedPreferences(mActivity).getBoolean(Statics.SETTINGS_DELAYED_TIMER, false)) {
                            CountDownTimer tempTimer = new CountDownTimer(5000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    start_stop.setText("Timer starting in ... " + String.valueOf((int) millisUntilFinished / 1000));
                                }

                                @Override
                                public void onFinish() {
                                    startTimer();
                                }
                            }.start();
                        } else {
                            startTimer();
                        }

                    } else if (timerActive && !timerPaused) { // timer is active, so the user clicked on pause

                        timerActive = true;
                        timerPaused = true;
                        start_stop.setText("Restart");
                        mCountDownTimer.cancel();
                    } else { // timer is active, but paused, so the user clicked on restart

                        timerActive = true;
                        timerPaused = false;
                        start_stop.setText("Pause");
                        startCountdownTimer(millisLeft);
                    }
                }
            });

            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mCountDownTimer.cancel();

                    timerActive = false;
                    timerPaused = false;
                    time.setText(String.valueOf(timer / 1000));
                    reset.setVisibility(View.GONE);
                    start_stop.setText("Start");
                    timerProgress.setProgress(0);
                }
            });

        } else {
            root.findViewById(R.id.includeTime).setVisibility(View.GONE);
        }
    }

    private void addSet() {
        ExerciseSet eSet = new ExerciseSet();
        if (hasReps) {
            eSet.setReps(mRepsPicker.getValue());
        }
        if (hasWeight) {
            eSet.setWeight(mWeightPicker.getValue());
        }
        if (hasTime) {
            eSet.setTime((int) timer / 1000);
        }

        mExercise.addSet(eSet);

        if (PreferenceManager.getDefaultSharedPreferences(mActivity).getBoolean(Statics.SETTINGS_REST_TIMER, false)) {
            mActivity.restAndUpdate();
        } else {
            mActivity.updateList();
        }
    }

    private void startTimer() {
        String countdownTime = time.getText().toString().trim();
        if (countdownTime.length() == 0) {
            Toast.makeText(mActivity, "Please set a time for the timer", Toast.LENGTH_SHORT).show();
            time.requestFocus();
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(time, InputMethodManager.SHOW_IMPLICIT);
            return;
        } else {
            timerActive = true;
            timerPaused = false;
            reset.setVisibility(View.VISIBLE);
            start_stop.setText("Pause");

            timer = Long.valueOf(countdownTime) * 1000;
            timerProgress.setMax((int) timer - 1000);
            startCountdownTimer(timer);
        }
    }

    private void startCountdownTimer(long millis) {
        mCountDownTimer = new CountDownTimer(millis, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                AddSetDialog.this.onTick(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                AddSetDialog.this.onFinish();
            }
        };
        mCountDownTimer.start();
    }

    public void onTick(long millisUntilFinished) {
        millisLeft = millisUntilFinished;
        // fill the progress bar as time goes on
        time.setText("" + (int) (millisUntilFinished / 1000));
        timerProgress.setProgress((int) (timer - millisUntilFinished));

        // TODO - find a good way for the user to notice when the timer is finished
//        if (millisUntilFinished < 2500) {
//            root.setBackgroundColor(mActivity.getResources().getColor(R.color.pomegranate));
//        }
    }

    public void onFinish() {
        // flash when time is up
        root.setBackgroundColor(mActivity.getResources().getColor(R.color.pomegranate));

        mVibrator.vibrate(1000);
        // auto-add set when time is up
        addSet();
        dismiss();
    }

    @Override
    public void onBackPressed() {
        if (timerActive) {
            mCountDownTimer.cancel();
        }
        super.onBackPressed();
    }
}
