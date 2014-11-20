package com.cantwellcode.fitfriend.nutrition;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.fitfriend.app.R;
import com.cantwellcode.fitfriend.utils.SmallDecimalTextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Daniel on 2/12/14.
 */
public class FavoritesExpandableListAdapter extends BaseExpandableListAdapter {

    private class ViewHolder {
        TextView name;
        TextView type;
        SmallDecimalTextView calories;
        SmallDecimalTextView protein;
        SmallDecimalTextView carbs;
        SmallDecimalTextView fat;
    }

    private final Context context;

    private List<String> headerData;
    private HashMap<String, List<FavoriteMeal>> childData;

    public FavoritesExpandableListAdapter(Context context, List<String> headerData,
                                          HashMap<String, List<FavoriteMeal>> childData) {
        this.context = context;
        this.headerData = headerData;
        this.childData = childData;
    }

    @Override
    public int getGroupCount() {
        return this.headerData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.childData.get(this.headerData.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.headerData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.childData.get(this.headerData.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.nutrition_favorites_header, null);
        }

        TextView header = (TextView) convertView.findViewById(R.id.favorites_group_header);
        header.setTypeface(null, Typeface.BOLD);
        header.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        FavoriteMeal meal = (FavoriteMeal) getChild(groupPosition, childPosition);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.nutrition_data, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.n_data_name);
            holder.type = (TextView) convertView.findViewById(R.id.n_data_type);
            holder.calories = (SmallDecimalTextView) convertView.findViewById(R.id.n_data_calories);
            holder.protein = (SmallDecimalTextView) convertView.findViewById(R.id.n_data_protein);
            holder.carbs = (SmallDecimalTextView) convertView.findViewById(R.id.n_data_carbs);
            holder.fat = (SmallDecimalTextView) convertView.findViewById(R.id.n_data_fat);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(meal.getName());
        holder.type.setText(meal.getType());
        holder.calories.setText(meal.getCalories());
        holder.protein.setText(meal.getProtein());
        holder.carbs.setText(meal.getCarbs());
        holder.fat.setText(meal.getFat());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
