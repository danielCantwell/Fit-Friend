package com.cantwellcode.fitfriend.connect;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fitfriend.app.R;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 5/6/2014.
 */
public class FriendsActivity extends FragmentActivity {

    private ListView friendsList;
    private Button findFriends;
    private Button friendRequests;
    private Button friends;
    private Button groups;
    private EditText searchIdentifier;
    private Button search;

    private ParseUser user;

    private ParseQueryAdapter<ParseObject> currentFriendsAdapter;
    private ParseQueryAdapter<ParseObject> friendRequestsAdapter;
    private ParseQueryAdapter<Group> groupsAdapter;

    private int previous = 0;
    private int current = 2;
    private String searchType;
    private String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_friends);

        user = ParseUser.getCurrentUser();

        findFriends = (Button) findViewById(R.id.findFriends);
        friendRequests = (Button) findViewById(R.id.friendRequests);
        friends = (Button) findViewById(R.id.friends);
        groups = (Button) findViewById(R.id.groups);
        friendsList = (ListView) findViewById(R.id.friendsList);
        searchIdentifier = (EditText) findViewById(R.id.searchIdentifier);
        search = (Button) findViewById(R.id.search);

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

        /*
         Query Factory for finding current friends
         */
        ParseQueryAdapter.QueryFactory<ParseObject> factoryCurrentFriends = new ParseQueryAdapter.QueryFactory<ParseObject>() {
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
        /*
        Adapter for listing current friends
         */
        currentFriendsAdapter = new ParseQueryAdapter<ParseObject>(this, factoryCurrentFriends) {
            @Override
            public View getItemView(final ParseObject object, View view, ViewGroup parent) {
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
                    view = view.inflate(FriendsActivity.this, R.layout.friend_list_item, null);
                }
                TextView name = (TextView) view.findViewById(R.id.name);
                TextView mainSport = (TextView) view.findViewById(R.id.mainSport);

                ParseUser from = object.getParseUser("from");

                ParseUser friend;
                if (from.hasSameId(user)) {
                    friend = object.getParseUser("to");
                } else {
                    friend = from;
                }

                name.setText(friend.getString("name"));
                mainSport.setText(friend.getString("mainSport"));

                return view;
            }
        };

        /*
        Query Factory for finding friend requests
         */
        ParseQueryAdapter.QueryFactory<ParseObject> factoryFriendRequests = new ParseQueryAdapter.QueryFactory<ParseObject>() {
            @Override
            public ParseQuery<ParseObject> create() {
                // Friend Requests Query
                ParseQuery<ParseObject> queryFriendRequests = ParseQuery.getQuery("Friend");
                queryFriendRequests.whereEqualTo("to", user);
                queryFriendRequests.whereEqualTo("confirmed", false);
                queryFriendRequests.include("from");

                return queryFriendRequests;
            }
        };
        /*
        Adapter for listing friend requests
         */
        friendRequestsAdapter = new ParseQueryAdapter<ParseObject>(this, factoryFriendRequests) {
            @Override
            public View getItemView(final ParseObject object, View view, final ViewGroup parent) {
                if (view == null) {
                    view = view.inflate(FriendsActivity.this, R.layout.friend_request_item, null);
                }
                TextView name = (TextView) view.findViewById(R.id.name);
                final Button confirm = (Button) view.findViewById(R.id.confirm);
                final Button deny = (Button) view.findViewById(R.id.deny);

                ParseUser from = object.getParseUser("from");

                final ParseUser friend;
                if (from.hasSameId(user)) {
                    friend = object.getParseUser("to");
                } else {
                    friend = from;
                }

                name.setText(friend.getString("name"));

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SocialEvent.confirmFriend(friend);
                        confirm.setText("Confirmed");
                        deny.setVisibility(View.GONE);
                        confirm.setClickable(false);
                    }
                });

                deny.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SocialEvent.removeFriend(object);
                        deny.setText("Denied");
                        confirm.setVisibility(View.INVISIBLE);
                        deny.setClickable(false);
                    }
                });

                return view;
            }
        };

        /*
        Query Factory for finding groups
         */
        ParseQueryAdapter.QueryFactory<Group> factoryGroups = new ParseQueryAdapter.QueryFactory<Group>() {
            @Override
            public ParseQuery<Group> create() {
                ParseQuery<Group> groupQuery = Group.getQuery();
                groupQuery.whereEqualTo("members", user);
                return groupQuery;
            }
        };
        /*
        Adapter for listing groups
         */
        groupsAdapter = new ParseQueryAdapter<Group>(this, factoryGroups) {
            @Override
            public View getItemView(Group group, View view, ViewGroup parent) {
                if (view == null) {
                    view = view.inflate(FriendsActivity.this, R.layout.group_item, null);
                }

                return view;
            }
        };

        handleLayout();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Friends");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_OK);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
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
                findFriends.setBackground(getResources().getDrawable(R.drawable.ic_search_red));
                searchIdentifier.setHint("find friends");
                break;
            case 1:
                friendRequests.setBackground(getResources().getDrawable(R.drawable.ic_add_person_red));
                searchIdentifier.setVisibility(View.VISIBLE);
                search.setVisibility(View.VISIBLE);
                break;
            case 2:
                friends.setBackground(getResources().getDrawable(R.drawable.ic_friends_red));
                friendsList.setAdapter(null);
                break;
            case 3:
                groups.setBackground(getResources().getDrawable(R.drawable.ic_group_red));
                searchIdentifier.setVisibility(View.VISIBLE);
                search.setVisibility(View.VISIBLE);
                break;
        }

        /* handle current selection */
        switch (current) {
            case 0:
                findFriends.setBackground(getResources().getDrawable(R.drawable.ic_search_black));
                searchIdentifier.setHint("username, email or  phone");
                handleSearch();
                previous = 0;
                break;
            case 1:
                friendRequests.setBackground(getResources().getDrawable(R.drawable.ic_add_person_black));
                searchIdentifier.setVisibility(View.GONE);
                search.setVisibility(View.GONE);
                handleFriendRequests();
                previous = 1;
                break;
            case 2:
                friends.setBackground(getResources().getDrawable(R.drawable.ic_friends_black));
                handleFriends();
                previous = 2;
                break;
            case 3:
                groups.setBackground(getResources().getDrawable(R.drawable.ic_group_black));
                searchIdentifier.setVisibility(View.GONE);
                search.setVisibility(View.GONE);
                handleGroups();
                previous = 3;
                break;
        }
    }

    private void handleSearch() {
        /* on click listener for the search button */
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* close soft keyboard */
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                /* make sure identifier is not empty */
                if (isEmpty(searchIdentifier)) {
                    Toast.makeText(FriendsActivity.this, "Search cannot be empty", Toast.LENGTH_LONG).show();
                } else {
                    /* if identifier is not empty, determine type */
                    final String identifier = searchIdentifier.getText().toString();
                    final String regex = "\\d+";
                    if (identifier.contains("@") && identifier.contains(".")) {
                        searchType = "email"; // if it contains @ and . you can safely say it is an email
                    } else if (identifier.matches(regex)) {
                        searchType = "phone";
                    } else {
                        searchType = "username"; // default type is username
                    }

                    /* Query Factory for finding new friends */
                    ParseQueryAdapter.QueryFactory<ParseUser> factoryFindFriends = new ParseQueryAdapter.QueryFactory<ParseUser>() {
                        @Override
                        public ParseQuery<ParseUser> create() {
                            /* find users with this info */
                            final ParseQuery<ParseUser> query = ParseUser.getQuery();
                            query.whereEqualTo(searchType, identifier);
                            query.whereNotEqualTo("objectId", user.getObjectId());
                            query.orderByAscending("name");
                            return query;
                        }
                    };

                    ParseQueryAdapter<ParseUser> searchFriendsAdapter = new ParseQueryAdapter<ParseUser>(FriendsActivity.this, factoryFindFriends) {
                        @Override
                        public View getItemView(final ParseUser friend, View view, ViewGroup parent) {
                            if (view == null) {
                                view = view.inflate(FriendsActivity.this, R.layout.friend_search_item, null);
                            }
                            TextView name = (TextView) view.findViewById(R.id.name);
                            TextView mainSport = (TextView) view.findViewById(R.id.mainSport);
                            TextView age = (TextView) view.findViewById(R.id.age);
                            TextView location = (TextView) view.findViewById(R.id.location);

                            name.setText(friend.getString("name"));
                            mainSport.setText(friend.getString("mainSport"));
                            age.setText(friend.getInt("age") + "");
                            location.setText(friend.getString("location"));

                            view.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    SocialEvent.requestFriend(FriendsActivity.this, friend);
                                }
                            });

                            return view;
                        }
                    };

                    friendsList.setAdapter(searchFriendsAdapter);
                }
            }
        });
    }

    private void handleFriendRequests() {
        /* Check for pending friend requests */
        friendsList.setAdapter(friendRequestsAdapter);
    }

    private void handleFriends() {
        /* Query for current friends */
        friendsList.setAdapter(currentFriendsAdapter);
    }

    private void handleGroups() {
        friendsList.setAdapter(groupsAdapter);
    }
}
