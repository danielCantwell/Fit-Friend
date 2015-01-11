package com.cantwellcode.fitfriend.friends;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cantwellcode.fitfriend.connect.SocialEvent;
import com.cantwellcode.fitfriend.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 1/9/2015.
 */
public class FriendsFragment extends Fragment {

    private ParseUser user;
    private ParseQueryAdapter<ParseObject> mAdapter;
    private ParseQueryAdapter.QueryFactory<ParseObject> factory;

    private ListView mList;

    public static FriendsFragment newInstance() {
        return new FriendsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_friends, null);

        user = ParseUser.getCurrentUser();

        mList = (ListView) root.findViewById(R.id.listView);

        TextView empty = (TextView) root.findViewById(android.R.id.empty);
        mList.setEmptyView(empty);

        factory = new ParseQueryAdapter.QueryFactory<ParseObject>() {
            @Override
            public ParseQuery<ParseObject> create() {
//                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Friend");
//                query1.whereEqualTo("from", user);
//                query1.whereEqualTo("confirmed", true);
//                ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Friend");
//                query2.whereEqualTo("to", user);
//                query2.whereEqualTo("confirmed", true);
//
//                List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
//                queries.add(query1);
//                queries.add(query2);
//
//                ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
//                mainQuery.include("from");
//                mainQuery.include("to");

                return SocialEvent.getCurrentFriendshipsQuery();
            }
        };

        mAdapter = new ParseQueryAdapter<ParseObject>(getActivity(), factory) {
            @Override
            public View getItemView(ParseObject object, View view, ViewGroup parent) {
                if (view == null) {
                    view = view.inflate(getActivity(), R.layout.friend_list_item, null);
                }
                TextView name = (TextView) view.findViewById(R.id.name);
                TextView mainSport = (TextView) view.findViewById(R.id.mainSport);


                ParseUser friend = getFriendFromFriendship(object);
                friend.pinInBackground("Friends");

                name.setText(friend.getString("name"));
                mainSport.setText(friend.getString("mainSport"));

                return view;
            }
        };

        mList.setAdapter(mAdapter);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ParseObject friendship = mAdapter.getItem(position);
                final ParseUser friend = getFriendFromFriendship(friendship);

                friend.pinInBackground("Friend_Profile", new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Intent intent = new Intent(getActivity(), ProfileActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        return root;
    }

    /*
    Friend queries will be for a friendship object, where both you and the friend are in it
    This method extracts your friend from the friendship object
     */
    private ParseUser getFriendFromFriendship(ParseObject friendship) {
        ParseUser from = friendship.getParseUser("from");

        ParseUser friend;
        if (from.hasSameId(user)) {
            friend = friendship.getParseUser("to");
        } else {
            friend = from;
        }

        return friend;
    }
}
