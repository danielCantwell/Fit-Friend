package com.cantwellcode.ipsum.Exercise.Analyze;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cantwellcode.ipsum.R;

/**
 * Created by Daniel on 4/15/2014.
 */
public class WorkoutStats extends Fragment {

    public static Fragment newInstance() {
        WorkoutStats f = new WorkoutStats();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.workout_stats, null);

        return root;
    }
}
