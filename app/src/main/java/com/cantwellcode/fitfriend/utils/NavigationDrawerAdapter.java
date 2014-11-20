package com.cantwellcode.fitfriend.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.fitfriend.app.R;

import java.util.List;

/**
 * Created by Daniel on 8/7/2014.
 */
public class NavigationDrawerAdapter extends ArrayAdapter<String> {

    private class ViewHolder {
        TextView textHolder;
    }

    private final Context context;
    private final int res;

    public int selection = 0;

    public NavigationDrawerAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.res = resource;
    }

    public void addItem(String item) {
        add(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String item = getItem(position);
        ViewHolder holder = null;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.drawer_item, null);
            holder = new ViewHolder();
            holder.textHolder = (TextView) convertView.findViewById(R.id.drawer_item);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == selection) {
            holder.textHolder.setTypeface(null, Typeface.BOLD);
            holder.textHolder.setTextColor(context.getResources().getColor(R.color.black));
        } else {
            holder.textHolder.setTypeface(null, Typeface.NORMAL);
            holder.textHolder.setTextColor(context.getResources().getColor(R.color.dark_grey));
        }

        holder.textHolder.setText(item);

        return convertView;
    }


}