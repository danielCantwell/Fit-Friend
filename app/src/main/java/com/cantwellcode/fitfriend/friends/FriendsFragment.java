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

import com.cantwellcode.fitfriend.R;
import com.cantwellcode.fitfriend.utils.SocialEvent;
import com.cantwellcode.fitfriend.utils.Statics;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by Daniel on 1/9/2015.
 */
public class FriendsFragment extends Fragment {

    private ParseQueryAdapter<ParseUser> mAdapter;
    private ParseQueryAdapter.QueryFactory<ParseUser> factory;

    private ListView mList;

    public static FriendsFragment newInstance() {
        return new FriendsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_friends, null);

        mList = (ListView) root.findViewById(R.id.listView);

        TextView empty = (TextView) root.findViewById(android.R.id.empty);
        mList.setEmptyView(empty);

        factory = new ParseQueryAdapter.QueryFactory<ParseUser>() {
            @Override
            public ParseQuery<ParseUser> create() {

                return SocialEvent.getCurrentFriendsLocalQuery();
            }
        };

        mAdapter = new ParseQueryAdapter<ParseUser>(getActivity(), factory) {
            @Override
            public View getItemView(ParseUser friend, View view, ViewGroup parent) {
                if (view == null) {
                    view = view.inflate(getActivity(), R.layout.friend_list_item, null);
                }
                TextView name = (TextView) view.findViewById(R.id.name);
                TextView mainSport = (TextView) view.findViewById(R.id.mainSport);

                name.setText(friend.getString("name"));
                mainSport.setText(friend.getString("mainSport"));

                return view;
            }
        };

        mList.setAdapter(mAdapter);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ParseUser friend = mAdapter.getItem(position);

                friend.pinInBackground(Statics.PIN_FRIEND_PROFILE, new SaveCallback() {
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
//    private ParseUser getFriendFromFriendship(ParseObject friendship) {
//        ParseUser from = friendship.getParseUser("from");
//
//        ParseUser friend;
//        if (from.hasSameId(user)) {
//            friend = friendship.getParseUser("to");
//        } else {
//            friend = from;
//        }
//
//        return friend;
//    }
}
