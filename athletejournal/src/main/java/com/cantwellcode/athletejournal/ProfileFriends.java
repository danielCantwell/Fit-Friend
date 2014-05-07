package com.cantwellcode.athletejournal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 5/6/2014.
 */
public class ProfileFriends extends Fragment {

    private ListView friendsList;
    private Button findFriends;
    private Button friendRequests;
    private Button friends;
    private Button groups;
    private EditText searchIdentifier;
    private Button search;

    private Button friendRequest;

    private ParseUser user;
    private ParseQueryAdapter<ParseObject> friendship;

    private int previous = 0;
    private int current = 2;

    private ViewGroup root;

    public static Fragment newInstance() {
        ProfileFriends f = new ProfileFriends();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.profile_friends, null);

        user = ParseUser.getCurrentUser();

        findFriends = (Button) root.findViewById(R.id.findFriends);
        friendRequests = (Button) root.findViewById(R.id.friendRequests);
        friends = (Button) root.findViewById(R.id.friends);
        groups = (Button) root.findViewById(R.id.groups);
        friendsList = (ListView) root.findViewById(R.id.friendsList);
        searchIdentifier = (EditText) root.findViewById(R.id.searchIdentifier);
        search = (Button) root.findViewById(R.id.search);

        findFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current = 0;
                handleLayout();
            }
        });

        friendRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current = 1;
                handleLayout();
            }
        });

        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current = 2;
                handleLayout();
            }
        });

        groups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current = 3;
                handleLayout();
            }
        });

        handleLayout();

        return root;
    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }

    private void handleLayout() {
        if (current == previous) {
            return;
        }

        /* deal with previous selection */
        switch (previous) {
            case 0:
                findFriends.setBackground(getResources().getDrawable(R.drawable.ic_search_black));
                break;
            case 1:
                friendRequests.setBackground(getResources().getDrawable(R.drawable.ic_add_person_black));
                searchIdentifier.setVisibility(View.VISIBLE);
                search.setVisibility(View.VISIBLE);
                break;
            case 2:
                friends.setBackground(getResources().getDrawable(R.drawable.ic_friends_black));
                break;
            case 3:
                groups.setBackground(getResources().getDrawable(R.drawable.ic_group_black));
                break;
        }

        /* handle current selection */
        switch (current) {
            case 0:
                findFriends.setBackground(getResources().getDrawable(R.drawable.ic_search_red));
                handleSearch();
                previous = 0;
                break;
            case 1:
                friendRequests.setBackground(getResources().getDrawable(R.drawable.ic_add_person_red));
                searchIdentifier.setVisibility(View.GONE);
                search.setVisibility(View.GONE);
                handleFriendRequests();
                previous = 1;
                break;
            case 2:
                friends.setBackground(getResources().getDrawable(R.drawable.ic_friends_red));
                handleFriends();
                previous = 2;
                break;
            case 3:
                groups.setBackground(getResources().getDrawable(R.drawable.ic_group_red));
                handleGroups();
                previous = 3;
                break;
        }
    }

    private void handleSearch() {
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                if (isEmpty(searchIdentifier)) {
                    Toast.makeText(getActivity(), "Search cannot be empty", Toast.LENGTH_LONG).show();
                } else {
                    String identifier = searchIdentifier.getText().toString();
                    String type = "username";
                    if (identifier.contains("@") && identifier.contains(".")) {
                        type = "email";
                    }
                    SocialEvent.requestFriend(getActivity(), "username", identifier);
                }
            }
        });
    }

    private void handleFriendRequests() {
        /* Check for pending friend requests */

        // Create query
        final ParseQuery<ParseObject> query = ParseQuery.getQuery("Friend");
        query.whereEqualTo("to", user);
        query.whereEqualTo("confirmed", false);
        query.include("from");

        // Query for friend table
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject parseObject, ParseException e) {
                if (e == null) {
                    Toast.makeText(getActivity(), "New Friend Request", Toast.LENGTH_SHORT).show();
                    //friendRequests = parseObject;
                    friendRequest = (Button) root.findViewById(R.id.friendRequest);
                    friendRequest.setVisibility(View.VISIBLE);
                    ParseUser friend = parseObject.getParseUser("from");
                    friendRequest.setText(friend.getString("name"));
                    friendRequest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SocialEvent.confirmFriend(parseObject);
                            friendRequest.setVisibility(View.GONE);
                            friendship.loadObjects();
                        }
                    });
                }
            }
        });
    }

    private void handleFriends() {
        /* Query for current friends */

        final ParseQueryAdapter.QueryFactory<ParseObject> factory =
                new ParseQueryAdapter.QueryFactory<ParseObject>() {
                    public ParseQuery<ParseObject> create() {

                        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Friend");
                        query1.whereEqualTo("from", user);
                        query1.whereEqualTo("confirmed", true);
                        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Friend");
                        query2.whereEqualTo("to", user);
                        query2.whereEqualTo("confirmed", true);

                        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
                        queries.add(query1);
                        queries.add(query2);

                        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
                        mainQuery.include("from");
                        mainQuery.include("to");

                        return mainQuery;
                    }
                };

        friendship = new ParseQueryAdapter<ParseObject>(getActivity(), factory) {
            @Override
            public View getItemView(final ParseObject friendshipObject, View view, ViewGroup parent) {
//                mOnClickListener = new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        switch (v.getId()) {
//                            case R.id.name:
//
//                                break;
//                        }
//                    }
//                };
                if (view == null) {
                    view = view.inflate(getActivity(), R.layout.friend_list_item, null);
                }
                TextView name = (TextView) view.findViewById(R.id.name);

                ParseUser from = friendshipObject.getParseUser("from");

                ParseUser friend;
                if (from.hasSameId(user)) {
                    friend = friendshipObject.getParseUser("to");
                } else {
                    friend = from;
                }

                name.setText(friend.getString("name"));

                return view;
            }
        };

        friendsList.setAdapter(friendship);
        friendship.setAutoload(true);
    }

    private void handleGroups() {

    }
}
