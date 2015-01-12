package com.cantwellcode.fitfriend.exercise.log;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cantwellcode.fitfriend.R;
import com.cantwellcode.fitfriend.exercise.types.Exercise;
import com.cantwellcode.fitfriend.exercise.types.Set;
import com.cantwellcode.fitfriend.utils.Statics;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by Daniel on 1/12/2015.
 */
public class ExerciseStatsFragment extends Fragment {

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
        mReps = (TextView) root.findViewById(R.id.reps);

        mName.setText(mExercise.getName());

        ParseQuery<Exercise> query = mExercise.getDetailedQuery();
        query.fromPin(Statics.EXERCISES);
        query.findInBackground(new FindCallback<Exercise>() {
            @Override
            public void done(List<Exercise> exercises, ParseException exception) {
                mNum.setText("" + exercises.size());
                int numSets = 0;
//                int maxWeight = 0;
//                int avgWeight = 0;
//                int totalWeight = 0;
//                int numReps = 0;
                for (Exercise e : exercises) {
                    List<Set> sets = e.getSets();
                    numSets += sets.size();
                }
                mSets.setText("" + numSets);
            }
        });

        return root;
    }

    public void setExercise(Exercise e) {
        mExercise = e;
    }
}
