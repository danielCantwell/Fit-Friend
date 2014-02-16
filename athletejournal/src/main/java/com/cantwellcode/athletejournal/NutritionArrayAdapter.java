package com.cantwellcode.athletejournal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Daniel on 2/12/14.
 */
public class NutritionArrayAdapter extends ArrayAdapter<Nutrition> {

    private class ViewHolder {
        TextView name;
        TextView date;
        TextView type;
        TextView calories;
        TextView protein;
        TextView carbs;
        TextView fat;
    }

    private final Context context;
    private final int res;

    public NutritionArrayAdapter(Context context, int resource, List<Nutrition> objects) {
        super(context, resource, objects);
        this.context = context;
        this.res = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Nutrition meal = getItem(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.n_data, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.n_data_name);
            holder.date = (TextView) convertView.findViewById(R.id.n_data_date);
            holder.type = (TextView) convertView.findViewById(R.id.n_data_type);
            holder.calories = (TextView) convertView.findViewById(R.id.n_data_calories);
            holder.protein = (TextView) convertView.findViewById(R.id.n_data_protein);
            holder.carbs = (TextView) convertView.findViewById(R.id.n_data_carbs);
            holder.fat = (TextView) convertView.findViewById(R.id.n_data_fat);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(meal.get_name());
        holder.date.setText(meal.get_date());
        holder.type.setText(meal.get_type());
        holder.calories.setText(meal.get_calories());
        holder.protein.setText(meal.get_protein());
        holder.carbs.setText(meal.get_carbs());
        holder.fat.setText(meal.get_fat());

        return convertView;
    }
}
