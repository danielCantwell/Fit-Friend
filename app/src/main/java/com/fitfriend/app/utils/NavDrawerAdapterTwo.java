package com.fitfriend.app.utils;

import android.content.Context;
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
public class NavDrawerAdapterTwo extends ArrayAdapter<DrawerItemTwo> {

    private class ViewHolder {
        ImageView imageHolder;
        TextView textHolder;
    }

    private final Context context;
    private final int res;

    public NavDrawerAdapterTwo(Context context, int resource, List<DrawerItemTwo> objects) {
        super(context, resource, objects);
        this.context = context;
        this.res = resource;
    }

    public void addItem(String text, int iconRes) {
        add(new DrawerItemTwo(text, iconRes));
    }

    public void addItem(DrawerItemTwo item) {
        add(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DrawerItemTwo item = getItem(position);
        ViewHolder holder = null;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.drawer_item_two, null);
            holder = new ViewHolder();
            holder.textHolder = (TextView) convertView.findViewById(R.id.text);
            holder.imageHolder = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textHolder.setText(item.text);
        holder.imageHolder.setBackgroundResource(item.iconRes);

        return convertView;
    }


}