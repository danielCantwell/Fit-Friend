package com.cantwellcode.fitfriend.app.exercise.log;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.cantwellcode.fitfriend.app.R;

import java.util.List;

/**
 * Created by Daniel on 4/22/2014.
 */
public class NewRoutineListAdapter extends ArrayAdapter<GymRoutine> {

    private final Context context;

    private class ViewHolder {
        TextView name;
        Button setCount;
    }

    public NewRoutineListAdapter(Context context, int resource, List<GymRoutine> objects) {
        super(context, resource, objects);

        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        GymRoutine routine = getItem(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.gym_routine_list_item, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.routineName);
            holder.setCount = (Button) convertView.findViewById(R.id.routineSetCount);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(routine.getName());
        holder.setCount.setText(routine.getSets().size() + "");

        return convertView;
    }
}
