package com.cantwellcode.fitfriend.connect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cantwellcode.fitfriend.R;

import java.util.List;

/**
 * Created by Daniel on 6/1/2014.
 */
public class CommentsAdapter extends ArrayAdapter<Comment> {

    private class ViewHolder {
        TextView comment;
        TextView name;
    }

    private final Context context;

    public CommentsAdapter(Context context, int resource, List<Comment> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Comment comment = getItem(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.forum_comment, null);
            holder = new ViewHolder();
            holder.comment = (TextView) convertView.findViewById(R.id.comment);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.comment.setText(comment.getContent());
        holder.name.setText(comment.getUser().getString("name"));

        return convertView;
    }
}
