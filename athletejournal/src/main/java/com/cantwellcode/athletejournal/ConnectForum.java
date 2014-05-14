package com.cantwellcode.athletejournal;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Daniel on 4/28/2014.
 */
public class ConnectForum extends ListFragment {

    private static final int MAX_POST_SEARCH_RESULTS = 50;

    private ParseQueryAdapter<ForumPost> posts;

    private ParseUser user;

    private List<String> friendGroups;

    private View.OnClickListener mOnClickListener;

    private String currentGroup;

    ParseQueryAdapter.QueryFactory<ForumPost> factory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.list, container, false);

        user = ParseUser.getCurrentUser();

        friendGroups = new ArrayList<String>();
        friendGroups.add("Friends");
        friendGroups.add("Explore");
        for (Group group : ConnectFriends.getGroups()) {
            friendGroups.add(group.getName());
        }

        currentGroup = "Friends";

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, friendGroups);

        /* Spinner Listener for which group of posts to display */
        getActivity().getActionBar().setListNavigationCallbacks(adapter, new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                String groupName = friendGroups.get(itemPosition);
                if (groupName.equals("Friends")) {
                    setupFriendsPosts();
                } else if (groupName.equals("Explore")) {
                    setupExplorePosts();
                } else {
                    setupGroupPosts(groupName);
                }

                return true;
            }
        });

        setupFriendsPosts();

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

    /**
     * Query 'Friend' table for friendships with current user
     * Determine which user in the friendship object is the friend
     * Create a factory query on 'ForumPost' table
     * Add constraint where the forum post's 'user' must be in the list of friends
     * Setup list adapter with this factory
     * Setup forum post views
     * Set list adapter
     */
    private void setupFriendsPosts() {

        ParseQuery<ParseObject> friendships = SocialEvent.getCurrentFriendshipsQuery();
        final List<ParseUser> friends = new ArrayList<ParseUser>();
        friends.add(user);

        /* Query for current friendships */
        friendships.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {

                /* Add all friends to the list */
                for (ParseObject object : parseObjects) {
                    ParseUser from = object.getParseUser("from");

                    ParseUser friend;
                    /* Check if friend is the 'from' or 'to' user in the friendship */
                    if (from.hasSameId(ParseUser.getCurrentUser())) {
                        friend = object.getParseUser("to");
                    } else {
                        friend = from;
                    }

                    friends.add(friend);
                }

                /* Set up factory for forum posts by friends */
                factory = new ParseQueryAdapter.QueryFactory<ForumPost>() {
                    public ParseQuery<ForumPost> create() {

                        /* Create a query for forum posts */
                        ParseQuery<ForumPost> query = ForumPost.getQuery();
                        // Forum posts must be created by someone in the list of friends
                        query.whereContainedIn("user", friends);
                        query.include("user");
                        query.orderByDescending("createdAt");
                        query.setLimit(MAX_POST_SEARCH_RESULTS);

                        return query;
                    }
                };

                /* Set up list adapter using the factory of friends */
                posts = new ParseQueryAdapter<ForumPost>(getActivity(), factory) {
                    @Override
                    public View getItemView(final ForumPost post, View view, ViewGroup parent) {

                        mOnClickListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                switch (v.getId()) {
                                    /* Click listener for high five button */
                                    case R.id.highFive:
                                        post.addHighFive();
                                        post.saveInBackground(new SaveCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                posts.notifyDataSetChanged();
                                            }
                                        });
                                        break;
                                    /* Click listener for discussion button */
                                    case R.id.discusson:
                                        FragmentManager fm = getFragmentManager();
                                        DiscussionDialog discussionDialog = new DiscussionDialog(post);
                                        discussionDialog.show(fm, "DiscussionDialog");
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
                        comment.setOnClickListener(mOnClickListener);

                        DateFormat df = new SimpleDateFormat("d MMM yyyy");
                        Date dateTime = post.getCreatedAt();
                        String dateText = df.format(dateTime);

                        date.setText(dateText);

                        return view;
                    }
                };

                setListAdapter(posts);
            }
        });
    }

    /**
     * Create a factory query on 'ForumPost' table
     * Setup list adapter with this factory
     * Setup forum post views
     * Set list adapter
     */
    private void setupExplorePosts() {
        /* Set up factory for forum posts by any user */
        factory = new ParseQueryAdapter.QueryFactory<ForumPost>() {
            public ParseQuery<ForumPost> create() {

                /* Create a query for forum posts */
                ParseQuery<ForumPost> query = ForumPost.getQuery();
                query.include("user");
                query.orderByDescending("createdAt");
                query.setLimit(MAX_POST_SEARCH_RESULTS);

                return query;
            }
        };

                /* Set up list adapter using the factory of friends */
        posts = new ParseQueryAdapter<ForumPost>(getActivity(), factory) {
            @Override
            public View getItemView(final ForumPost post, View view, ViewGroup parent) {

                mOnClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                                    /* Click listener for high five button */
                            case R.id.highFive:
                                post.addHighFive();
                                post.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        posts.notifyDataSetChanged();
                                    }
                                });
                                break;
                                    /* Click listener for discussion button */
                            case R.id.discusson:
                                FragmentManager fm = getFragmentManager();
                                DiscussionDialog discussionDialog = new DiscussionDialog(post);
                                discussionDialog.show(fm, "DiscussionDialog");
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
                comment.setOnClickListener(mOnClickListener);

                DateFormat df = new SimpleDateFormat("d MMM yyyy");
                Date dateTime = post.getCreatedAt();
                String dateText = df.format(dateTime);

                date.setText(dateText);

                return view;
            }
        };

        setListAdapter(posts);
    }

    private void setupGroupPosts(String groupName) {

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

    private class DiscussionDialog extends DialogFragment {

        private ForumPost post;

        public DiscussionDialog(ForumPost post) {
            this.post = post;
        }

        private TextView content;
        private TextView name;
        private ListView listView;
        private Button postComment;
        private EditText createComment;
        private View view;

        AlertDialog.Builder builder;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();

            view = inflater.inflate(R.layout.forum_discussion_dialog, null);

            content = (TextView) view.findViewById(R.id.content);
            name = (TextView) view.findViewById(R.id.name);
            listView = (ListView) view.findViewById(R.id.comments);
            postComment = (Button) view.findViewById(R.id.postComment);
            createComment = (EditText) view.findViewById(R.id.createComment);

            content.setText(post.getContent());
            name.setText(post.getUser().getString("name"));

            ArrayAdapter adapter;
            if (post.has("comments")) {
                adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, post.getComments());
            } else {
                List<String> comments = new ArrayList<String>();
                comments.add("NO COMMENTS HAVE BEEN ADDED");
                adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, comments);
            }
            listView.setAdapter(adapter);

            postComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    post.addComment(createComment.getText().toString());
                    posts.notifyDataSetChanged();
                    post.saveInBackground();
                    DiscussionDialog.this.dismiss();
                }
            });

            builder.setView(view);

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
