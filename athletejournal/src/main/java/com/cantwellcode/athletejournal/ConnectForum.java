package com.cantwellcode.athletejournal;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.content.AsyncTaskLoader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Daniel on 4/28/2014.
 */
public class ConnectForum extends ListFragment {

    private List<ForumPost> posts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //ForumArrayAdapter adapter = new ForumArrayAdapter(getActivity(), android.R.id.list, posts);
        //setListAdapter(adapter);


        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public static class ForumLoader extends AsyncTaskLoader<List<ForumPost>> {

        public ForumLoader(Context context) {
            // Connect to Parse.com database
            super(context);
        }

        @Override
        public List<ForumPost> loadInBackground() {
            // Load Forum Posts
            // return Forum Posts
            return null;
        }


    }
}
