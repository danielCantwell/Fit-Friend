package com.cantwellcode.athletejournal;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Daniel on 2/12/14.
 */
public class FavoritesExpandableListAdapter extends BaseExpandableListAdapter {

    private class ViewHolder {
        TextView name;
        TextView category;
        TextView type;
        TextView calories;
        TextView protein;
        TextView carbs;
        TextView fat;
    }

    private final Context context;

    private List<String> headerData;
    private HashMap<String, List<Favorite>> childData;

    FavoritesViewFragment.SortFavoritesBy sortType;

    public FavoritesExpandableListAdapter(Context context, List<String> headerData,
                          HashMap<String, List<Favorite>> childData, FavoritesViewFragment.SortFavoritesBy sortType) {
        this.context = context;
        this.headerData = headerData;
        this.childData = childData;
        this.sortType = sortType;
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
            convertView = inflater.inflate(R.layout.favorites_list_group, null);
        }

        TextView header = (TextView) convertView.findViewById(R.id.favorites_group_header);
        header.setTypeface(null, Typeface.BOLD);
        header.setText(headerTitle);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Favorite childFavorite = (Favorite) getChild(groupPosition, childPosition);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.f_data, null);
            holder = new ViewHolder();
            holder.name     = (TextView) convertView.findViewById(R.id.f_data_name);
            holder.category = (TextView) convertView.findViewById(R.id.f_data_category);
            holder.type     = (TextView) convertView.findViewById(R.id.f_data_type);
            holder.calories = (TextView) convertView.findViewById(R.id.f_data_calories);
            holder.protein  = (TextView) convertView.findViewById(R.id.f_data_protein);
            holder.carbs    = (TextView) convertView.findViewById(R.id.f_data_carbs);
            holder.fat      = (TextView) convertView.findViewById(R.id.f_data_fat);
            convertView.setTag(holder);
            Log.d("Holder", "holder is created");
        }
        else {
            holder = (ViewHolder) convertView.getTag();
            Log.d("Holder", "holder is set");
        }

        Log.d("Child Name", childFavorite.get_name());
        Log.d("Holder Name", holder.name.getText().toString());

        holder.name.setText(childFavorite.get_name());
        if (sortType.equals(FavoritesViewFragment.SortFavoritesBy.Type)) {
            holder.category.setText(childFavorite.get_category());
            holder.type.setText("");
        } else if (sortType.equals(FavoritesViewFragment.SortFavoritesBy.Category)) {
            holder.category.setText("");
            holder.type.setText(childFavorite.get_type());
        }
        holder.calories.setText(childFavorite.get_calories());
        holder.protein.setText(childFavorite.get_protein());
        holder.carbs.setText(childFavorite.get_carbs());
        holder.fat.setText(childFavorite.get_fat());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
