package com.cantwellcode.fitfriend.friends;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cantwellcode.fitfriend.connect.SocialEvent;
import com.fitfriend.app.R;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

/**
 * Created by Daniel on 1/9/2015.
 */
public class FriendRequestsFragment extends Fragment {

    private ParseUser user;
    private ParseQueryAdapter<ParseObject> mAdapter;

    private ListView mList;

    public static FriendRequestsFragment newInstance() {
        return new FriendRequestsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_friends, null);

        mList = (ListView) root.findViewById(R.id.listView);

        user = ParseUser.getCurrentUser();

        /*
        Query Factory for finding friend requests
         */
        ParseQueryAdapter.QueryFactory<ParseObject> factory = new ParseQueryAdapter.QueryFactory<ParseObject>() {
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
        mAdapter = new ParseQueryAdapter<ParseObject>(getActivity(), factory) {
            @Override
            public View getItemView(final ParseObject object, View view, final ViewGroup parent) {
                if (view == null) {
                    view = view.inflate(getActivity(), R.layout.friend_request_item, null);
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

        mList.setAdapter(mAdapter);

        return root;
    }
}
