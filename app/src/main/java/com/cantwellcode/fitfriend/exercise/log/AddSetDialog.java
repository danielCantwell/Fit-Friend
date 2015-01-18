package com.cantwellcode.fitfriend.exercise.log;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cantwellcode.fitfriend.R;
import com.cantwellcode.fitfriend.exercise.types.Exercise;
import com.cantwellcode.fitfriend.exercise.types.ExerciseSet;

import java.util.ArrayList;
import java.util.List;

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

    private Button mNegative;
    private Button mPositive;

    private View root;
    private NewWorkoutActivity mActivity;

    protected AddSetDialog(NewWorkoutActivity activity, Exercise e) {
        super(activity);

        mExercise = e;
        mActivity = activity;

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
                dismiss();
            }
        });

        mPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            final EditText time = (EditText) root.findViewById(R.id.time);
            ExerciseSet lastSet = mExercise.getLastSet();
            if (lastSet != null) {
                time.setText(String.valueOf(lastSet.getTime()));
            } else {
                time.setText("30");
            }
            time.setSelectAllOnFocus(true);
            Button start_stop = (Button) root.findViewById(R.id.start_stop);
            Button reset = (Button) root.findViewById(R.id.reset);
            final ProgressBar timerProgress = (ProgressBar) root.findViewById(R.id.progressBar);

            start_stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!timerActive) {
                        String countdownTime = time.getText().toString().trim();
                        if (countdownTime.length() == 0) {
                            Toast.makeText(mActivity, "Please set a time for the timer", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            timer = Long.valueOf(countdownTime) * 1000;
                            timerProgress.setMax((int) timer);
                        }

                        CountDownTimer countDownTimer = new CountDownTimer(timer, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                millisLeft = millisUntilFinished;
                                // fill the progress bar as time goes on
                                time.setText("" + (int) (millisUntilFinished / 1000));
                                timerProgress.setProgress((int) (timer - millisUntilFinished));
                            }

                            @Override
                            public void onFinish() {
                                // flash when time is up
                                root.setBackgroundColor(mActivity.getResources().getColor(R.color.pomegranate));
                                Vibrator v = (Vibrator) mActivity.getSystemService(Context.VIBRATOR_SERVICE);
                                v.vibrate(300);
                                // auto-add set when time is up
                                addSet();
                                dismiss();
                            }
                        };
                        countDownTimer.start();
                    }
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
        mActivity.updateList();
    }
}
