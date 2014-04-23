package com.cantwellcode.athletejournal;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Daniel on 4/23/2014.
 */
public class RoutineListAdapter extends ArrayAdapter<GymRoutine> {
    private final Context context;

    private class ViewHolder {
        TextView name;
        TableRow rowSets;
        TableRow rowReps;
        TableRow rowWeight;
    }

    public RoutineListAdapter(Context context, int resource, List<GymRoutine> objects) {
        super(context, resource, objects);

        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        GymRoutine routine = getItem(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.workout_gym_set_view, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.gym_set_name);
            holder.rowSets = (TableRow) convertView.findViewById(R.id.row_sets);
            holder.rowReps = (TableRow) convertView.findViewById(R.id.row_reps);
            holder.rowWeight = (TableRow) convertView.findViewById(R.id.row_weight);
            convertView.setTag(holder);

            holder.name.setText(routine.getName());

            int count = 0;
            for (GymSet set : routine.getSets()) {
                count++;

                TextView setNum = new TextView(context);
                TextView reps = new TextView(context);
                TextView weight = new TextView(context);

                setNum.setTextSize(12);
                setNum.setTextColor(Color.LTGRAY);
                setNum.setText(" " + count + "  ");
                reps.setText(" " + set.reps + "  ");
                weight.setText(" " + set.weight + "  ");

                holder.rowSets.addView(setNum);
                holder.rowReps.addView(reps);
                holder.rowWeight.addView(weight);
            }
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }
}
