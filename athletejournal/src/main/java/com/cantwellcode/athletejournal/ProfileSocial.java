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
                        }
                    });
                }
            }
        });

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
                        Toast.makeText(context, "Please enter a name", Toast.LENGTH_LONG).show();
                    } else {
                        SocialEvent.requestFriend(getActivity(), "username", friendUsername.getText().toString());
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
