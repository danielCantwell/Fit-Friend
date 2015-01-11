package com.cantwellcode.fitfriend.friends;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.cantwellcode.fitfriend.connect.SocialEvent;
import com.cantwellcode.fitfriend.R;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

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

        mList.setAdapter(mAdapter);

        return root;
    }
}
