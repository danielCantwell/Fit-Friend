package com.cantwellcode.athletejournal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.content.AsyncTaskLoader;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Daniel on 4/28/2014.
 */
public class ConnectForum extends ListFragment {

    private static final int MAX_POST_SEARCH_RESULTS = 20;

    private ParseQueryAdapter<ForumPost> posts;

    private ParseUser user;

    private View.OnClickListener mOnClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.list, container, false);

        user = ParseUser.getCurrentUser();


//        ForumArrayAdapter adapter = new ForumArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, posts);
//        setListAdapter(adapter);

        // Set up a customized query
        final ParseQueryAdapter.QueryFactory<ForumPost> factory =
                new ParseQueryAdapter.QueryFactory<ForumPost>() {
                    public ParseQuery<ForumPost> create() {
                        // Query for friends the current user is following
                        // Query for forum posts authored by the current user's friends
                        // Query for forum posts authored by the current user
                        // Final query for combined forum posts
                        ParseQuery<ForumPost> query = ForumPost.getQuery();
                        query.include("user");
                        query.orderByDescending("createdAt");
                        query.setLimit(MAX_POST_SEARCH_RESULTS);
                        return query;
                    }
                };

        posts = new ParseQueryAdapter<ForumPost>(getActivity(), factory) {
            @Override
            public View getItemView(final ForumPost post, View view, ViewGroup parent) {

                mOnClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.highFive:
                                post.addHighFive();
                                post.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        loadObjects();
                                    }
                                });
                                break;
                        }
                    }
                };

                if (view == null) {
                    view = view.inflate(getActivity(), R.layout.forum_item, null);
                }
                TextView name = (TextView) view.findViewById(R.id.name);
                TextView date = (TextView) view.findViewById(R.id.date);
                TextView content = (TextView) view.findViewById(R.id.content);
                TextView numHighFives = (TextView) view.findViewById(R.id.numHighFives);
                TextView numComments = (TextView) view.findViewById(R.id.numComments);
                Button highFive = (Button) view.findViewById(R.id.highFive);
                Button comment = (Button) view.findViewById(R.id.discusson);

                name.setText(post.getUser().getString("name"));
                content.setText(post.getContent());
                numHighFives.setText(String.valueOf(post.getHighFives()));
                if (post.has("comments")) {
                    numComments.setText(String.valueOf(post.getComments().size()));
                }

                highFive.setOnClickListener(mOnClickListener);

                DateFormat df = new SimpleDateFormat("d MMM yyyy");
                Date dateTime = post.getCreatedAt();
                String dateText = df.format(dateTime);

                date.setText(dateText);

                return view;
            }
        };

        setListAdapter(posts);
        posts.setAutoload(true);

        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_createPost:
                FragmentManager fm1 = getFragmentManager();
                ForumPostDialog dialog = new ForumPostDialog();
                dialog.show(fm1, "ForumPostDialog");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class ForumPostDialog extends DialogFragment {

        private Spinner group;
        private CheckBox publiclyAvailable;
        private Button options;
        private EditText content;
        private EditText comment;
        private View view;

        AlertDialog.Builder builder;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();

            view = inflater.inflate(R.layout.forum_post_dialog, null);

            group = (Spinner) view.findViewById(R.id.group);
            publiclyAvailable = (CheckBox) view.findViewById(R.id.publiclyAvailable);
            options = (Button) view.findViewById(R.id.options);
            content = (EditText) view.findViewById(R.id.content);
            comment = (EditText) view.findViewById(R.id.comment);

            builder.setView(view);
            builder.setTitle("Post in Forum");

            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (isEmpty(content)) {
                        Toast.makeText(getActivity(), "Post not created.\nTitle required.", Toast.LENGTH_LONG).show();
                    } else {
                        /* Create new post */
                        ForumPost post = new ForumPost();
                        post.setUser(ParseUser.getCurrentUser());
                        post.setContent(content.getText().toString());
                        /* Add comment if exists */
                        if (!isEmpty(comment)) {
                            post.addComment(comment.getText().toString());
                        }
                        post.setHighFives(0);
                        /* Save post */
                        post.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                posts.loadObjects();
                            }
                        });
                    }
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            return builder.create();
        }
    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }
}
