package com.cantwellcode.fitfriend.exercise.log;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cantwellcode.fitfriend.exercise.types.Exercise;
import com.fitfriend.app.R;

import java.util.List;

/**
 * Created by Daniel on 1/5/2015.
 */
public class SavedExercisesAdapter extends ArrayAdapter<Exercise> {

    private class ViewHolder {
        TextView name;
        TextView details;
        TextView arms;
        TextView shoulders;
        TextView chest;
        TextView back;
        TextView abs;
        TextView legs;
        TextView glutes;
    }

    private Context context;

    public SavedExercisesAdapter(Context context, int resource, List<Exercise> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Exercise exercise = getItem(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.exercise_list_item, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.details = (TextView) convertView.findViewById(R.id.sets);
            holder.arms = (TextView) convertView.findViewById(R.id.arms);
            holder.shoulders = (TextView) convertView.findViewById(R.id.shoulders);
            holder.chest = (TextView) convertView.findViewById(R.id.chest);
            holder.back = (TextView) convertView.findViewById(R.id.back);
            holder.abs = (TextView) convertView.findViewById(R.id.abs);
            holder.legs = (TextView) convertView.findViewById(R.id.legs);
            holder.glutes = (TextView) convertView.findViewById(R.id.glutes);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(exercise.getName());

        String details = "";
        if (exercise.recordWeight()) { details = "Weight  "; }
        if (exercise.recordReps()) { details += "Reps  "; }
        if (exercise.recordTime()) { details += "Time"; }
        holder.details.setText(details);

        holder.arms.setVisibility(exercise.usesArms() ? View.VISIBLE : View.GONE);
        holder.shoulders.setVisibility(exercise.usesShoulders() ? View.VISIBLE : View.GONE);
        holder.chest.setVisibility(exercise.usesChest() ? View.VISIBLE : View.GONE);
        holder.back.setVisibility(exercise.usesBack() ? View.VISIBLE : View.GONE);
        holder.abs.setVisibility(exercise.usesAbs() ? View.VISIBLE : View.GONE);
        holder.legs.setVisibility(exercise.usesLegs() ? View.VISIBLE : View.GONE);
        holder.glutes.setVisibility(exercise.usesGlutes() ? View.VISIBLE : View.GONE);

        return convertView;
    }
}
