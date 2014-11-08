package com.fitfriend.app.exercise.log;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.fitfriend.app.exercise.types.Exercise;

import java.util.List;

/**
 * Created by Daniel on 10/28/2014.
 */
public class ExerciseSetsAdapter extends ArrayAdapter<Exercise> {

    private Context context;

    public ExerciseSetsAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
