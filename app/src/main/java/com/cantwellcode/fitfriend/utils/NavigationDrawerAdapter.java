package com.cantwellcode.fitfriend.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cantwellcode.fitfriend.R;

import java.util.List;

/**
 * Created by Daniel on 8/7/2014.
 */
public class NavigationDrawerAdapter extends ArrayAdapter<DrawerItem> {

    /*
    In this case, viewholder is not used in the normal way
    Because a different view is loaded depending on the position
     */
    private class ViewHolder {
        TextView text;
        ImageView image;
    }

    private final Context context;

    // Used for checking if the view being loaded is the currently selected view
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

        // Items 0, 1, 2, 3 are main fragment items, and use the "drawer_item" layout
        if (position < 4) {
            convertView = inflater.inflate(R.layout.drawer_item, null);
            holder.text = (TextView) convertView.findViewById(R.id.text);

            // if the item is currently selected, bold and darken the text
            if (position == selection) {
                holder.text.setTypeface(null, Typeface.BOLD);
                holder.text.setTextColor(context.getResources().getColor(R.color.black));
            } else {
                holder.text.setTypeface(null, Typeface.NORMAL);
                holder.text.setTextColor(context.getResources().getColor(R.color.dark_grey));
            }
            // Items 4+ are secondary items that are used less often and open a new activity
        } else {
            convertView = inflater.inflate(R.layout.drawer_item_two, null);
            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.image = (ImageView) convertView.findViewById(R.id.icon);
            // only these items have icons
            holder.image.setBackgroundResource(item.iconRes);
        }

        // all items have text
        holder.text.setText(item.text);

        return convertView;
    }


}