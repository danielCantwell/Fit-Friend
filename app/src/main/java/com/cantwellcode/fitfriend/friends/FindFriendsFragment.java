package com.cantwellcode.fitfriend.friends;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cantwellcode.fitfriend.utils.SocialEvent;
import com.cantwellcode.fitfriend.R;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

/**
 * Created by Daniel on 1/9/2015.
 */
public class FindFriendsFragment extends Fragment {

    private EditText mSearchText;
    private Button mSearch;
    private ListView mList;

    ParseQueryAdapter<ParseUser> mAdapter;

    public static FindFriendsFragment newInstance() {
        return new FindFriendsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_find_friends, null);

        final ParseUser user = ParseUser.getCurrentUser();

        mSearchText = (EditText) root.findViewById(R.id.searchName);
        mSearch = (Button) root.findViewById(R.id.search);
        mList = (ListView) root.findViewById(R.id.listView);

        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mSearchText.getText().toString().trim();
                if (email.isEmpty()) {
                    Toast.makeText(getActivity(), "Please enter an email", Toast.LENGTH_SHORT).show();
                } else {

                    /* Query Factory for finding new friends */
                    ParseQueryAdapter.QueryFactory<ParseUser> factoryFindFriends = new ParseQueryAdapter.QueryFactory<ParseUser>() {
                        @Override
                        public ParseQuery<ParseUser> create() {
                            /* find users with this info */
                            final ParseQuery<ParseUser> query = ParseUser.getQuery();
                            query.whereEqualTo("email", email);
                            query.whereNotEqualTo("objectId", user.getObjectId());
                            return query;
                        }
                    };

                    mAdapter = new ParseQueryAdapter<ParseUser>(getActivity(), factoryFindFriends) {
                        @Override
                        public View getItemView(final ParseUser friend, View view, ViewGroup parent) {
                            if (view == null) {
                                view = view.inflate(getActivity(), R.layout.friend_search_item, null);
                            }
                            TextView name = (TextView) view.findViewById(R.id.name);
                            TextView mainSport = (TextView) view.findViewById(R.id.mainSport);
                            TextView age = (TextView) view.findViewById(R.id.age);
                            TextView location = (TextView) view.findViewById(R.id.location);

                            name.setText(friend.getString("name"));
                            mainSport.setText(friend.getString("mainSport"));
                            age.setText(friend.getInt("age") + "");
                            location.setText(friend.getString("location"));

                            return view;
                        }
                    };

                    mList.setAdapter(mAdapter);

                }
            }
        });

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SocialEvent.requestFriend(getActivity(), mAdapter.getItem(position));
            }
        });

        return root;
    }
}
