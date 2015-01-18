package com.cantwellcode.fitfriend.exercise.log;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cantwellcode.fitfriend.R;
import com.cantwellcode.fitfriend.exercise.types.Exercise;
import com.cantwellcode.fitfriend.exercise.types.ExerciseSet;
import com.cantwellcode.fitfriend.utils.Statics;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Daniel on 1/12/2015.
 */
public class ExerciseStatsFragment extends Fragment {

    private ExerciseStatsActivity mActivity;

    private Exercise mExercise;

    private TextView mName;
    private TextView mNum;
    private TextView mSets;
    private TextView mMaxWeight;
    private TextView mAvgWeight;
    private TextView mReps;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_exercise_stats, container, false);

        mName = (TextView) root.findViewById(R.id.name);
        mNum = (TextView) root.findViewById(R.id.num);
        mSets = (TextView) root.findViewById(R.id.totalSets);
        mMaxWeight = (TextView) root.findViewById(R.id.maxWeight);
        mAvgWeight = (TextView) root.findViewById(R.id.avgWeight);
        mReps = (TextView) root.findViewById(R.id.totalReps);

        mName.setText(mExercise.getName());

        ParseQuery<Exercise> query = mExercise.getDetailedQuery();
        query.fromPin(Statics.PIN_EXERCISES);
        query.include("sets");
        query.findInBackground(new FindCallback<Exercise>() {
            @Override
            public void done(List<Exercise> exercises, ParseException exception) {

                if (exercises.size() == 0) {
                    mNum.setText("0");
                    mSets.setText("---");
                    mMaxWeight.setText("---");
                    mAvgWeight.setText("---");
                    mReps.setText("---");
                } else {

                    mNum.setText("" + exercises.size());
                    int numSets = 0;
                    int maxWeight = 0;
                    int totalWeight = 0;
                    int numReps = 0;

                    for (Exercise e : exercises) {

                        List<ExerciseSet> eSets = e.getSets();

                        for (ExerciseSet s : eSets) {

                            if (mExercise.recordWeight()) {
                                int weight = s.getInt("weight");
                                totalWeight += weight;
                                if (weight > maxWeight) {
                                    maxWeight = weight;
                                }

                            }
                            if (mExercise.recordReps()) {
                                int reps = s.getInt("reps");
                                numReps += reps;
                            }
                        }
                        numSets += eSets.size();
                    }

                    mSets.setText("" + numSets);
                    if (mExercise.recordWeight()) {
                        mMaxWeight.setText("" + maxWeight);
                        int avg = totalWeight / numSets;
                        mAvgWeight.setText("" + avg);
                    }
                    if (mExercise.recordReps()) {
                        mReps.setText("" + numReps);
                    }
                }
            }
        });

        return root;
    }

    public void setExercise(Exercise e) {
        mExercise = e;
    }

    public void setActivity(ExerciseStatsActivity a) {
        mActivity = a;
    }
}
