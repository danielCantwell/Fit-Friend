package com.cantwellcode.athletejournal;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Daniel on 4/11/2014.
 */
public class NavigationDrawerAdapter extends ArrayAdapter<DrawerItem> {

    private class ViewHolder {
        TextView textHolder;
        ImageView imageHolder;
    }

    private final Context context;
    private final int res;

    public int selection = 9;

    public NavigationDrawerAdapter(Context context, int resource, List<DrawerItem> objects) {
        super(context, resource, objects);
        this.context = context;
        this.res = resource;
    }

    public void addHeader(String title) {
        add(new DrawerItem(title, true));
    }

    public void addItem(String title, int icon) {
        add(new DrawerItem(title, icon));
    }

    public void addItem(DrawerItem item) {
        add(item);
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).isHeader ? 0 : 1;
    }

    @Override
    public boolean isEnabled(int position) {
        return !getItem(position).isHeader;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        DrawerItem item = getItem(position);
        ViewHolder holder = null;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            if (item.isHeader) {
                convertView = inflater.inflate(R.layout.drawer_header, null);
                holder = new ViewHolder();
                holder.textHolder = (TextView) convertView.findViewById(R.id.drawer_header_text);
                holder.textHolder.setTextColor(Color.rgb(192, 57, 43));
                convertView.setTag(holder);
            }
            else {
                convertView = inflater.inflate(R.layout.drawer_item, null);
                holder = new ViewHolder();
                holder.textHolder = (TextView) convertView.findViewById(R.id.drawer_item_text);
                //holder.imageHolder = (ImageView) convertView.findViewById(R.id.drawer_item_image);
                holder.textHolder.setTextColor(Color.rgb(236, 240, 241));
                convertView.setTag(holder);
            }
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == selection) {
            convertView.setBackgroundColor(Color.rgb(192, 57, 43));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#222222"));
        }

        holder.textHolder.setText(item.title);

        //if (!item.isHeader)
        //    holder.imageHolder.setBackgroundResource(item.iconRes);

        return convertView;
    }


}
