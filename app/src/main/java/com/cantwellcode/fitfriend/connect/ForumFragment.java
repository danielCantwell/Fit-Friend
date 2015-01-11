package com.cantwellcode.fitfriend.connect;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cantwellcode.fitfriend.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Daniel on 4/28/2014.
 */
public class ForumFragment extends ListFragment {

    private static Fragment instance = null;

    public static Fragment newInstance() {
        if (instance == null)
            instance = new ForumFragment();
        return instance;
    }

    private static final int MAX_POST_SEARCH_RESULTS = 50;

    private ParseQueryAdapter<Post> posts;

    private ParseUser user;

    private List<String> forumCategories;

    private View.OnClickListener mOnClickListener;

    private String currentCategory;

    ParseQueryAdapter.QueryFactory<Post> factory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.list, container, false);

        user = ParseUser.getCurrentUser();

        String[] categories = getResources().getStringArray(R.array.forum_categories);
        forumCategories = Arrays.asList(categories);

        currentCategory = "Friends";

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, forumCategories);

        /* Spinner Listener for which group of posts to display */
        getActivity().getActionBar().setListNavigationCallbacks(adapter, new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                String category = forumCategories.get(itemPosition);
                currentCategory = category;
                if (category.equals("Friends")) {
                    setupFriendsPosts();
                } else {
                    setupCategoryPosts(category);
                }

                return true;
            }
        });

        setupFriendsPosts();

        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        restoreActionBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new:
                Intent intent = new Intent(getActivity(), CreatePostActivity.class);
                intent.putExtra("category", currentCategory);
                startActivityForResult(intent, 1000);
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

                if (e != null) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                } else {
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
                    factory = new ParseQueryAdapter.QueryFactory<Post>() {
                        public ParseQuery<Post> create() {

                        /* Create a query for forum posts */
                            ParseQuery<Post> query = Post.getQuery();
                            // Forum posts must be created by someone in the list of friends
                            query.whereContainedIn("user", friends);
                            query.whereEqualTo("category", "Friends");
                            query.include("user");
                            query.include("comments.user");
                            query.orderByDescending("createdAt");
                            query.setLimit(MAX_POST_SEARCH_RESULTS);

                            return query;
                        }
                    };

                /* Set up list adapter using the factory of friends */
                    posts = new ParseQueryAdapter<Post>(getActivity(), factory) {
                        @Override
                        public View getItemView(final Post post, View view, ViewGroup parent) {

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
                                            Intent intent = new Intent(getActivity(), DiscussionActivity.class);
                                            intent.putExtra("postID", post.getObjectId());
                                            startActivity(intent);
                                    }
                                }
                            };

                            if (view == null) {
                                view = view.inflate(getActivity(), R.layout.forum_item, null);
                            }

                            ImageView picture = (ImageView) view.findViewById(R.id.picture);
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

                            picture = (ImageView) view.findViewById(R.id.picture);

                            ParseFile pic = post.getUser().getParseFile("picture");

                            if (pic != null) {
                                try {
                                    byte[] file = pic.getData();
                                    picture.setImageBitmap(BitmapFactory.decodeByteArray(file, 0, file.length));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                picture.setImageResource(R.drawable.profile);
                            }

                            return view;
                        }
                    };

                    setListAdapter(posts);
                }
            }
        });
    }

    /**
     * Create a factory query on 'ForumPost' table
     * Setup list adapter with this factory
     * Setup forum post views
     * Set list adapter
     */
    private void setupCategoryPosts(final String category) {
        /* Set up factory for forum posts by any user */
        factory = new ParseQueryAdapter.QueryFactory<Post>() {
            public ParseQuery<Post> create() {

                /* Create a query for forum posts */
                ParseQuery<Post> query = Post.getQuery();
                query.include("user");
                query.include("comments.user");
                query.whereEqualTo("category", category);
                query.orderByDescending("createdAt");
                query.setLimit(MAX_POST_SEARCH_RESULTS);

                return query;
            }
        };

                /* Set up list adapter using the factory of friends */
        posts = new ParseQueryAdapter<Post>(getActivity(), factory) {
            @Override
            public View getItemView(final Post post, View view, ViewGroup parent) {

                mOnClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), DiscussionActivity.class);
                        intent.putExtra("postID", post.getObjectId());
                        startActivity(intent);
                    }
                };

                if (view == null) {
                    view = view.inflate(getActivity(), R.layout.forum_item_public, null);
                }

                view.setOnClickListener(mOnClickListener);

                TextView name = (TextView) view.findViewById(R.id.name);
                TextView date = (TextView) view.findViewById(R.id.date);
                TextView content = (TextView) view.findViewById(R.id.content);
                TextView numComments = (TextView) view.findViewById(R.id.numComments);

                name.setText(post.getUser().getString("name"));
                content.setText(post.getContent());
                if (post.has("comments")) {
                    numComments.setText(String.valueOf(post.getComments().size()));
                }

                DateFormat df = new SimpleDateFormat("d MMM yyyy");
                Date dateTime = post.getCreatedAt();
                String dateText = df.format(dateTime);

                date.setText(dateText);

                return view;
            }
        };

        setListAdapter(posts);
    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }

    public static List<Group> getGroups() {
        ParseQuery<Group> query = Group.getQuery();
        query.whereEqualTo("members", ParseUser.getCurrentUser());
        try {
            return query.find();
        } catch (ParseException e) {
            return null;
        }
    }

    private void restoreActionBar() {
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Forum");
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            posts.loadObjects();
        }
    }
}
