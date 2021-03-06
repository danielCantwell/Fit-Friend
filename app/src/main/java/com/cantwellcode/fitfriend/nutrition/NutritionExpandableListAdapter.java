package com.cantwellcode.fitfriend.nutrition;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.cantwellcode.fitfriend.R;
import com.cantwellcode.fitfriend.utils.SmallDecimalTextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by danielCantwell on 4/27/15.
 * Copyright (c) Cantwell Code 2015. All Rights Reserved
 */
public class NutritionExpandableListAdapter extends BaseExpandableListAdapter {

    private class ViewHolder {
        TextView name;
        SmallDecimalTextView calories;
        SmallDecimalTextView protein;
        SmallDecimalTextView carbs;
        SmallDecimalTextView fat;
    }

    private final Context context;

    private List<Food> headerData;
    private HashMap<Food, List<Food>> childData;

    public NutritionExpandableListAdapter(Context context, List<Food> headerData,
                                          HashMap<Food, List<Food>> childData) {
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

        Food header = (Food) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.nutrition_log_header, null);
        }

        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView calories = (TextView) convertView.findViewById(R.id.calories);
        TextView fat = (TextView) convertView.findViewById(R.id.fat);
        TextView carbs = (TextView) convertView.findViewById(R.id.carbs);
        TextView protein = (TextView) convertView.findViewById(R.id.protein);

        title.setText(header.getType());
        calories.setText(String.format("%d", header.getCalories()));
        fat.setText(String.format("%.1f", header.getFat()));
        carbs.setText(String.format("%.1f", header.getCarbs()));
        protein.setText(String.format("%.1f", header.getProtein()));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Food meal = (Food) getChild(groupPosition, childPosition);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.nutrition_data, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.n_data_name);
            holder.calories = (SmallDecimalTextView) convertView.findViewById(R.id.n_data_calories);
            holder.protein = (SmallDecimalTextView) convertView.findViewById(R.id.n_data_protein);
            holder.carbs = (SmallDecimalTextView) convertView.findViewById(R.id.n_data_carbs);
            holder.fat = (SmallDecimalTextView) convertView.findViewById(R.id.n_data_fat);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(meal.getName());
        holder.calories.setText(String.format("%d", meal.getCalories()));
        holder.protein.setText(String.format("%.1f", meal.getProtein()));
        holder.carbs.setText(String.format("%.1f", meal.getCarbs()));
        holder.fat.setText(String.format("%.1f", meal.getFat()));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}