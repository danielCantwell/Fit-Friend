package com.cantwellcode.fitfriend.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.fitfriend.app.R;

import java.util.List;

/**
 * Created by Daniel on 8/7/2014.
 */
public class NavigationDrawerAdapter extends ArrayAdapter<DrawerItem> {

    private class ViewHolder {
        TextView text;
        ImageView image;
    }

    private final Context context;

    public int selection = 0;

    public NavigationDrawerAdapter(Context context, int resource, List<DrawerItem> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DrawerItem item = getItem(position);
        ViewHolder holder = new ViewHolder();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (position < 4) {
            convertView = inflater.inflate(R.layout.drawer_item, null);
            holder.text = (TextView) convertView.findViewById(R.id.text);
        } else {
            convertView = inflater.inflate(R.layout.drawer_item_two, null);
            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.image = (ImageView) convertView.findViewById(R.id.icon);
        }

        holder.text.setText(item.text);

        if (position < 4) {
            if (position == selection) {
                holder.text.setTypeface(null, Typeface.BOLD);
                holder.text.setTextColor(context.getResources().getColor(R.color.black));
            } else {
                holder.text.setTypeface(null, Typeface.NORMAL);
                holder.text.setTextColor(context.getResources().getColor(R.color.dark_grey));
            }
        } else {
            holder.image.setBackgroundResource(item.iconRes);
        }


        return convertView;
    }


}