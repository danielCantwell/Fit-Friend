package com.cantwellcode.fitfriend.exercise.log;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cantwellcode.fitfriend.R;
import com.cantwellcode.fitfriend.exercise.types.Exercise;
import com.cantwellcode.fitfriend.exercise.types.ExerciseSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 1/16/2015.
 */
public class AddSetDialog extends AlertDialog.Builder {

    private Exercise mExercise;
    private boolean hasWeight;
    private boolean hasReps;
    private boolean hasTime;

    private NumberPicker mRepsPicker;
    private NumberPicker mWeightPicker;

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

        setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        setPositiveButton("Add Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ExerciseSet eSet = new ExerciseSet();
                if (hasReps) {
                    eSet.setReps(mRepsPicker.getValue());
                }
                if (hasWeight) {
                    eSet.setWeight(mWeightPicker.getValue());
                }
                if (hasTime) {

                }

                mExercise.addSet(eSet);
                mActivity.updateList();
            }
        });
    }

    private void setupDialog() {
        if (hasReps) {

            mRepsPicker = (NumberPicker) root.findViewById(R.id.repsPicker);
            mRepsPicker.setMinValue(1);
            mRepsPicker.setMaxValue(50);
            mRepsPicker.setWrapSelectorWheel(false);
            mRepsPicker.setValue(15);  // TODO - change this to be a 'smart' value

        } else {
            root.findViewById(R.id.includeReps).setVisibility(View.GONE);
        }
        if (hasWeight) {

            mWeightPicker = (NumberPicker) root.findViewById(R.id.weightPicker);
            mWeightPicker.setMinValue(1);
            mWeightPicker.setMaxValue(400);
            mWeightPicker.setWrapSelectorWheel(false);
            mWeightPicker.setValue(50);  // TODO - change this to be a 'smart' value

        } else {
            root.findViewById(R.id.includeWeight).setVisibility(View.GONE);
        }
        if (hasTime) {

        } else {
            root.findViewById(R.id.includeTime).setVisibility(View.GONE);
        }
    }

//    private void setupExercise(Exercise e) {
//
//        TextView name = (TextView) root.findViewById(R.id.name);
//        name.setText(mName);
//
//        mExerciseSets = new ArrayList<ExerciseSet>();
//
//        weightData = e.recordWeight();
//        repsData = e.recordReps();
//        timeData = e.recordTime();
//
//        mScrollLayout = (LinearLayout) root.findViewById(R.id.linearLayout);
//        mAddSet = (Button) root.findViewById(R.id.addSet);
//        mAddSet.setOnClickListener(this);
//
//        mIncludeWeight = root.findViewById(R.id.includeWeight);
//        mIncludeReps = root.findViewById(R.id.includeReps);
//        mIncludeTime = root.findViewById(R.id.includeTime);
//
//        if (weightData) {
//            mSeekWeight = (SeekBar) root.findViewById(R.id.seekWeight);
//            mWeight = (TextView) root.findViewById(R.id.weight);
//            mMinusWeight = (Button) root.findViewById(R.id.minusWeight);
//            mPlusWeight = (Button) root.findViewById(R.id.plusWeight);
//
//            mMinusWeight.setOnClickListener(this);
//            mPlusWeight.setOnClickListener(this);
//
//            mSeekWeight.setOnSeekBarChangeListener(this);
//        } else {
//            mIncludeWeight.setVisibility(View.GONE);
//        }
//        if (repsData) {
//            mSeekReps = (SeekBar) root.findViewById(R.id.seekReps);
//            mReps = (TextView) root.findViewById(R.id.reps);
//            mMinusReps = (Button) root.findViewById(R.id.minusReps);
//            mPlusReps = (Button) root.findViewById(R.id.plusReps);
//
//            mMinusReps.setOnClickListener(this);
//            mPlusReps.setOnClickListener(this);
//
//            mSeekReps.setOnSeekBarChangeListener(this);
//        } else {
//            mIncludeReps.setVisibility(View.GONE);
//        }
//        if (timeData) {
//            mTime = (Chronometer) root.findViewById(R.id.time);
//            mStartTime = (Button) root.findViewById(R.id.start);
//            mStopTime = (Button) root.findViewById(R.id.stop_reset);
//            mStopTime.setVisibility(View.INVISIBLE);
//
//            mStartTime.setOnClickListener(this);
//            mStopTime.setOnClickListener(this);
//        } else {
//            mIncludeTime.setVisibility(View.GONE);
//        }
//    }
}
