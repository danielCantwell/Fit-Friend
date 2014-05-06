package com.cantwellcode.athletejournal;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
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
 * Created by Daniel on 4/15/2014.
 */
public class ProfileSocial extends Fragment {

    private TextView name;
    private TextView age;
    private TextView mainSport;
    private TextView location;
    private ListView friendsList;
    private Button findFriends;

    private FragmentTabHost tabHost;

    private Button friendRequest;

    private ParseUser user;
    private ParseQueryAdapter<ParseObject> friendship;

    private ParseObject friendRequests;

    public static Fragment newInstance() {
        ProfileSocial f = new ProfileSocial();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.profile_social, null);

        user = ParseUser.getCurrentUser();

        name = (TextView) root.findViewById(R.id.name);
        age = (TextView) root.findViewById(R.id.age);
        mainSport = (TextView) root.findViewById(R.id.mainSport);
        location = (TextView) root.findViewById(R.id.location);

        name.setText(user.getString("name"));
        age.setText(user.getInt("age") + "");
        mainSport.setText(user.getString("mainSport"));
        location.setText(user.getString("location"));

        findFriends = (Button) root.findViewById(R.id.findFriend);
        friendsList = (ListView) root.findViewById(R.id.friendsList);

        findFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FindFriendDialog dialog = new FindFriendDialog();
                dialog.show(fm, "FindFriendDialog");
            }
        });


        ParseQuery<ParseObject> query = ParseQuery.getQuery("Friend");
        query.whereContainedIn("users", Arrays.asList(user));
        query.whereEqualTo("confirmed", false);
        query.whereNotEqualTo("createdBy", user);

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    Toast.makeText(getActivity(), "New Friend Request", Toast.LENGTH_SHORT).show();
                    friendRequests = parseObject;
                    friendRequest = (Button) root.findViewById(R.id.friendRequest);
                    friendRequest.setVisibility(View.VISIBLE);
                    friendRequest.setText(friendRequests.getString("createdBy"));
                    friendRequest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            friendRequests.put("confirmed", true);
                            friendRequests.saveEventually();
                            friendRequest.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });

        // Set up a customized query
        final ParseQueryAdapter.QueryFactory<ParseObject> factory =
                new ParseQueryAdapter.QueryFactory<ParseObject>() {
                    public ParseQuery<ParseObject> create() {
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Friend");
                        query.whereContainedIn("users", Arrays.asList(user));
                        query.whereEqualTo("confirmed", true);
                        query.include("users");
                        return query;
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

                List<ParseUser> users = friendshipObject.getList("users");
                ParseUser friend;
                if (users.get(0).hasSameId(user)) {
                    friend = users.get(1);
                } else {
                    friend = users.get(0);
                }

                name.setText(friend.getString("name"));

                return view;
            }
        };

        friendsList.setAdapter(friendship);
        friendship.setAutoload(true);

        return root;
    }

    private class FindFriendDialog extends DialogFragment {

        private EditText friendUsername;
        private View view;

        private AlertDialog.Builder builder;

        private Context context;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();

            this.context = getActivity();

            view = inflater.inflate(R.layout.find_friend_dialog, null);

            friendUsername = (EditText) view.findViewById(R.id.friendUsername);

            builder.setView(view);
            builder.setTitle("Find Friend");

            builder.setPositiveButton("Send Request", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (isEmpty(friendUsername)) {
                        Toast.makeText(context, "Request not sent.\nUsername required.", Toast.LENGTH_LONG).show();
                    } else {
                        /* Check that username exists */
                        ParseQuery<ParseUser> query = ParseUser.getQuery();
                        query.whereEqualTo("username", friendUsername.getText().toString());
                        /* Check that user is not already a confirmed / requested friend */
                        final ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Friend");
                        query1.whereNotContainedIn("users", Arrays.asList(user));

                        query.getFirstInBackground(new GetCallback<ParseUser>() {
                            @Override
                            public void done(final ParseUser parseUser, ParseException e) {
                                /* Send friend request */
                                if (e == null) {
                                    query1.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(List<ParseObject> parseObjects, ParseException e) {
                                            if (parseObjects == null) {
                                                SocialEvent.requestFriend(parseUser);
                                                Toast.makeText(context, "Request sent to\n" + friendUsername.getText().toString(), Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(context, "cannot send duplicate request", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    /* No user found with that username */
                                    Toast.makeText(context, "request not sent\n" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
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
