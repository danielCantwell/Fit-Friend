package com.cantwellcode.athletejournal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Daniel on 4/28/2014.
 */
public class ForumArrayAdapter extends ArrayAdapter<ForumPost> {

    private class ViewHolder {
        TextView name;
        TextView date;
        TextView content;
        Button options;
        Button numComments;
        Button discussion;
        Button numHighFives;
        Button highFive;
    }

    private final Context context;

    public ForumArrayAdapter(Context context, int resource, List<ForumPost> objects) {
        super(context, resource, objects);

        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        ForumPost post = getItem(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.nutrition_data, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            //holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            //holder.options = (Button) convertView.findViewById(R.id.options);
            //holder.numComments = (Button) convertView.findViewById(R.id.numComments);
            //holder.discussion = (Button) convertView.findViewById(R.id.discusson);
            //holder.numHighFives = (Button) convertView.findViewById(R.id.numHighFives);
            //holder.highFive = (Button) convertView.findViewById(R.id.highFive);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

//        holder.name.setText(post.getAuthor().getName());
//        holder.date.setText(post.getDateTime());
//        holder.content.setText(post.getContent());
//        holder.numComments.setText(post.getDiscussionCount());
//        holder.numHighFives.setText(post.getHighFiveCount());

        holder.name.setText(post.getUser().getString("name"));
        holder.content.setText(post.getContent());

        return convertView;
    }
}
