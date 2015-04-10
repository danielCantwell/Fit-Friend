package com.cantwellcode.fitfriend.exercise.log;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cantwellcode.fitfriend.R;
import com.cantwellcode.fitfriend.exercise.types.Exercise;
import com.cantwellcode.fitfriend.exercise.types.ExerciseSet;
import com.cantwellcode.fitfriend.utils.Statics;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.text.NumberFormat;
import java.util.ArrayList;
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
    private TextView mTotalWeight;

    private TextView mMaxReps;
    private TextView mAvgReps;
    private TextView mTotalReps;

    private TextView mMaxTime;
    private TextView mAvgTime;
    private TextView mTotalTime;

    private GraphView mGraph;
    private List<DataPoint> mWeights;
    private List<DataPoint> mReps;
    private List<DataPoint> mTimes;

    private int minGraphWeight;

    private int numSets = 0;

    private int maxWeight = 0, totalWeight = 0, weightVolume = 0;
    private int maxReps = 0, totalReps = 0;
    private int maxTime = 0, totalTime = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_exercise_stats, container, false);

        mName = (TextView) root.findViewById(R.id.name);
        mNum = (TextView) root.findViewById(R.id.num);
        mSets = (TextView) root.findViewById(R.id.totalSets);

        mMaxWeight = (TextView) root.findViewById(R.id.maxWeight);
        mAvgWeight = (TextView) root.findViewById(R.id.avgWeight);
        mTotalWeight = (TextView) root.findViewById(R.id.totalWeight);

        mMaxReps = (TextView) root.findViewById(R.id.maxReps);
        mAvgReps = (TextView) root.findViewById(R.id.avgReps);
        mTotalReps = (TextView) root.findViewById(R.id.totalReps);

        mMaxTime = (TextView) root.findViewById(R.id.maxTime);
        mAvgTime = (TextView) root.findViewById(R.id.avgTime);
        mTotalTime = (TextView) root.findViewById(R.id.totalTime);


        mGraph = (GraphView) root.findViewById(R.id.graph);

        mName.setText(mExercise.getName());

        mWeights = new ArrayList<DataPoint>();
        mReps = new ArrayList<DataPoint>();
        mTimes = new ArrayList<DataPoint>();

        minGraphWeight = 0;

        /*
            Query for this exercises to get the details
         */
        ParseQuery<Exercise> query = mExercise.getDetailedQuery();
        query.fromPin(Statics.PIN_EXERCISES);
        query.orderByAscending("createdAt");
        query.include("sets");
        query.findInBackground(new FindCallback<Exercise>() {
            @Override
            public void done(List<Exercise> exercises, ParseException exception) {

                // Only analyze if the exercise has been used at least once
                if (exercises.size() != 0) {

                    analyzeExercises(exercises);

                    updateUI(root);
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

    private void analyzeExercises(List<Exercise> exercises) {
        mNum.setText("" + exercises.size());

        // For each exercise recorded
        for (Exercise e : exercises) {

            List<ExerciseSet> eSets = e.getSets();

            // Go through each set in the exercise
            for (ExerciseSet s : eSets) {
                analyzeWeight(s);
                analyzeReps(s);
                analyzeTime(s);
            }
            numSets += eSets.size();
        }
    }

    private void analyzeWeight(ExerciseSet s) {
        // If the exercises has weight data
        if (mExercise.recordWeight()) {
            int weight = s.getWeight();
            totalWeight += weight;

            // Add this weight to list for the graph series
            mWeights.add(new DataPoint(mWeights.size() + 1, weight));

            // Check for max weight
            if (weight > maxWeight) {
                maxWeight = weight;
            }
            // Check for min weight [this is used only for the graph height]
            if (weight < minGraphWeight || minGraphWeight == 0) {
                minGraphWeight = weight;
            }

            // If recording reps, add to weight volume
            if (mExercise.recordReps()) {
                weightVolume += weight * s.getReps();
            }

        }
    }

    private void analyzeReps(ExerciseSet s) {
        // If the exercise has reps data
        if (mExercise.recordReps()) {
            int reps = s.getReps();
            totalReps += reps;

            // Add this reps to list for the graph series
            mReps.add(new DataPoint(mReps.size() + 1, reps));

            // Check for max reps
            if (reps > maxReps) {
                maxReps = reps;
            }

            // Check for min weight [this is used only for the graph height]
            if (reps < minGraphWeight || minGraphWeight == 0) {
                minGraphWeight = reps;
            }
        }
    }

    private void analyzeTime(ExerciseSet s) {
        // If the exercise has time data
        if (mExercise.recordTime()) {
            int time = s.getTime();
            totalTime += time;

            // Add this time to list for the graph series
            mTimes.add(new DataPoint(mTimes.size() + 1, time));

            // Check for max time
            if (time > maxTime) {
                maxTime = time;
            }

            // Check for min weight [this is used only for the graph height]
            if (time < minGraphWeight || minGraphWeight == 0) {
                minGraphWeight = time;
            }
        }
    }

    private void updateUI(View root) {
        // After every set has been analyzed, update the UI elements

        mSets.setText(String.valueOf(numSets));

        // Only update weight components if there is weight data
        if (mExercise.recordWeight()) {
            int avg = totalWeight / numSets;
            mMaxWeight.setText(String.valueOf(maxWeight));
            mAvgWeight.setText(String.valueOf(avg));
            // this is only used if both weight and reps are recorded
            if (mExercise.recordReps()) {
                mTotalWeight.setText(String.valueOf(weightVolume));
            }
        } else {
            root.findViewById(R.id.rowWeightText).setVisibility(View.GONE);
            root.findViewById(R.id.rowWeightData).setVisibility(View.GONE);
        }

        // Only update the reps components if there is reps data
        if (mExercise.recordReps()) {
            int avg = totalReps / numSets;
            mMaxReps.setText(String.valueOf(maxReps));
            mAvgReps.setText(String.valueOf(avg));
            mTotalReps.setText(String.valueOf(totalReps));
        } else {
            root.findViewById(R.id.rowRepsText).setVisibility(View.GONE);
            root.findViewById(R.id.rowRepsData).setVisibility(View.GONE);
        }

        // Only update the time components if there is time data
        if (mExercise.recordTime()) {
            int avg = totalTime / numSets;
            mMaxTime.setText(String.valueOf(maxTime));
            mAvgTime.setText(String.valueOf(avg));
            mTotalTime.setText(String.valueOf(totalTime));
        } else {
            root.findViewById(R.id.rowTimeText).setVisibility(View.GONE);
            root.findViewById(R.id.rowTimeData).setVisibility(View.GONE);
        }

        /* Data points used for the graph series */
        mGraph.getLegendRenderer().setVisible(true);
        mGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.MIDDLE);

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(0);
        mGraph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(nf, nf));

        // Weight Data
        if (mExercise.recordWeight()) {
            DataPoint[] weightData = new DataPoint[mWeights.size()];
            weightData = mWeights.toArray(weightData);

            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(weightData);
            series.setColor(getResources().getColor(R.color.chart_red));
            series.setThickness(5);
            series.setTitle("Weight");
            series.setDrawDataPoints(true);
            series.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    Toast.makeText(getActivity(),
                            "Set #" + dataPoint.getX() + "  Weight: " + dataPoint.getY(), Toast.LENGTH_SHORT).show();
                }
            });

            // Add the series to the graph
            mGraph.addSeries(series);
        }

        // Reps Data
        if (mExercise.recordReps()) {
            DataPoint[] repsData = new DataPoint[mReps.size()];
            repsData = mReps.toArray(repsData);

            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(repsData);
            series.setColor(getResources().getColor(R.color.chart_blue));
            series.setThickness(5);
            series.setTitle("Reps");
            series.setDrawDataPoints(true);
            series.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    Toast.makeText(getActivity(),
                            "Set #" + dataPoint.getX() + "  Reps: " + dataPoint.getY(), Toast.LENGTH_SHORT).show();
                }
            });

            // Add the series to the graph
            mGraph.addSeries(series);
        }

        // Time Data
        if (mExercise.recordTime()) {
            DataPoint[] timeData = new DataPoint[mTimes.size()];
            timeData = mTimes.toArray(timeData);

            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(timeData);
            series.setColor(getResources().getColor(R.color.chart_green));
            series.setThickness(5);
            series.setTitle("Time");
            series.setDrawDataPoints(true);
            series.setOnDataPointTapListener(new OnDataPointTapListener() {
                @Override
                public void onTap(Series series, DataPointInterface dataPoint) {
                    Toast.makeText(getActivity(),
                            "Set #" + dataPoint.getX() + "  Time: " + dataPoint.getY() + "s", Toast.LENGTH_SHORT).show();
                }
            });

            // Add the series to the graph
            mGraph.addSeries(series);
        }

        // Set graph titles
        GridLabelRenderer gridLabelRenderer = mGraph.getGridLabelRenderer();
        gridLabelRenderer.setHighlightZeroLines(false);
        gridLabelRenderer.setHorizontalLabelsVisible(false);

        int maxY = 0;
        if (maxWeight > maxY) {
            maxY = maxWeight;
        }
        if (maxReps > maxY) {
            maxY = maxReps;
        }
        if (maxTime > maxY) {
            maxY = maxTime;
        }
        maxY += 5;

        // Set vertical bounds for the graph
        Viewport viewport = mGraph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMaxY(maxY);
        viewport.setMinY(minGraphWeight - 5);
        // Set horizontal bounds for the graph
        viewport.setXAxisBoundsManual(true);
        viewport.setMaxX(numSets + 1);
        viewport.setMinX(0);

        viewport.setScalable(true);
    }
}
