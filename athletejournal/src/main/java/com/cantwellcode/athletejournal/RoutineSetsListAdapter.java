package com.cantwellcode.athletejournal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Daniel on 4/22/2014.
 */
public class RoutineSetsListAdapter extends ArrayAdapter<GymSet> {

    private final Context context;

    public RoutineSetsListAdapter(Context context, int resource, List<GymSet> objects) {
        super(context, resource, objects);

        this.context = context;
    }

    private class ViewHolder {
        TextView setNumber;
        TextView reps;
        TextView weight;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        GymSet set = getItem(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.routine_sets_list_item, null);
            holder = new ViewHolder();
            holder.setNumber = (TextView) convertView.findViewById(R.id.setNumber);
            holder.reps = (TextView) convertView.findViewById(R.id.setReps);
            holder.weight = (TextView) convertView.findViewById(R.id.setWeight);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.setNumber.setText(position + 1 + "");
        holder.reps.setText(set.reps + "");
        holder.weight.setText(set.weight + "");

        return convertView;
    }
}
