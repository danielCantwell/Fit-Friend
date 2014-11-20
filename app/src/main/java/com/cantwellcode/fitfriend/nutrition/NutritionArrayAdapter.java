package com.cantwellcode.fitfriend.nutrition;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fitfriend.app.R;
import com.cantwellcode.fitfriend.utils.SmallDecimalTextView;

import java.util.List;

/**
 * Created by Daniel on 2/12/14.
 */
public class NutritionArrayAdapter extends ArrayAdapter<Meal> {

    private class ViewHolder {
        TextView name;
        TextView date;
        TextView type;
        SmallDecimalTextView calories;
        SmallDecimalTextView protein;
        SmallDecimalTextView carbs;
        SmallDecimalTextView fat;
    }

    private final Context context;

    public NutritionArrayAdapter(Context context, int resource, List<Meal> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Meal meal = getItem(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.nutrition_data, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.n_data_name);
            holder.date = (TextView) convertView.findViewById(R.id.n_data_date);
            holder.type = (TextView) convertView.findViewById(R.id.n_data_type);
            holder.calories = (SmallDecimalTextView) convertView.findViewById(R.id.n_data_calories);
            holder.protein = (SmallDecimalTextView) convertView.findViewById(R.id.n_data_protein);
            holder.carbs = (SmallDecimalTextView) convertView.findViewById(R.id.n_data_carbs);
            holder.fat = (SmallDecimalTextView) convertView.findViewById(R.id.n_data_fat);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(meal.getName());
        holder.date.setText(meal.getDate());
        holder.type.setText(meal.getType());
        holder.calories.setText(meal.getCalories());
        holder.protein.setText(meal.getProtein());
        holder.carbs.setText(meal.getCarbs());
        holder.fat.setText(meal.getFat());

        return convertView;
    }
}
