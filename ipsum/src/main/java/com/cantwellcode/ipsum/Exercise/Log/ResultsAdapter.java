package com.cantwellcode.ipsum.Exercise.Log;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.widget.DataBufferAdapter;

/**
 * Created by Daniel on 5/30/2014.
 */
public class ResultsAdapter extends DataBufferAdapter<Metadata> {

    private Context mContext;

    public ResultsAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(getContext(), android.R.layout.simple_list_item_1, null);
        }

        Metadata metadata = getItem(position);
        TextView titleTextView = (TextView) convertView.findViewById(android.R.id.text1);
        titleTextView.setText(metadata.getTitle());

        Log.d("ResultsAdapter", "File retrieved: " + metadata.getTitle());

        return convertView;
    }
}
