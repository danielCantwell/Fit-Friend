package com.cantwellcode.athletejournal;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TabHost;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 4/27/2014.
 */
public class ConnectFriends extends Fragment implements TabHost.OnTabChangeListener {

    private MenuInflater inflater;
    private FragmentTabHost mTabHost;

    public static Fragment newInstance() {
        ConnectFriends f = new ConnectFriends();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {

        mTabHost = new FragmentTabHost(getActivity());
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.tabHost);

        mTabHost.addTab(mTabHost.newTabSpec("Forum").setIndicator("Forum"), ConnectForum.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Exercise").setIndicator("Exercise"), ConnectExercise.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Nutrition").setIndicator("Food"), ConnectNutrition.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Profile")
                .setIndicator("", getResources().getDrawable(R.drawable.ic_person_black)), ProfileSocial.class, null);

        mTabHost.setBackgroundColor(Color.parseColor("#95a5a6"));
        mTabHost.getTabWidget().setBackgroundColor(Color.parseColor("#ecf0f1"));

        setHasOptionsMenu(true);

        return mTabHost;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTabHost = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.inflater = inflater;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        restoreActionBar();
        inflater.inflate(R.menu.connect_friends, menu);
    }

    private void restoreActionBar() {
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        actionBar.setDisplayShowTitleEnabled(false);
    }

    @Override
    public void onTabChanged(String tabId) {

    }

    public static List<ParseUser> getCurrentFriends() {

        final List<ParseUser> friends = new ArrayList<ParseUser>();

        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Friend");
        query1.whereEqualTo("from", ParseUser.getCurrentUser());
        query1.whereEqualTo("confirmed", true);
        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Friend");
        query2.whereEqualTo("to", ParseUser.getCurrentUser());
        query2.whereEqualTo("confirmed", true);

        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(query1);
        queries.add(query2);

        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
        mainQuery.include("from");
        mainQuery.include("to");

        mainQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {

                if (e == null) {
                    for (ParseObject object : parseObjects) {
                        ParseUser from = object.getParseUser("from");

                        ParseUser friend;
                        if (from.hasSameId(ParseUser.getCurrentUser())) {
                            friend = object.getParseUser("to");
                        } else {
                            friend = from;
                        }

                        friends.add(friend);
                    }
                }
            }
        });

        return friends;
    }

    public static List<ParseUser> getCurrentFriendsImmediately() {

        final List<ParseUser> friends = new ArrayList<ParseUser>();

        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Friend");
        query1.whereEqualTo("from", ParseUser.getCurrentUser());
        query1.whereEqualTo("confirmed", true);
        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Friend");
        query2.whereEqualTo("to", ParseUser.getCurrentUser());
        query2.whereEqualTo("confirmed", true);

        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(query1);
        queries.add(query2);

        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
        mainQuery.include("from");
        mainQuery.include("to");

        List<ParseObject> friendships = new ArrayList<ParseObject>();
        try {
            friendships = mainQuery.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (ParseObject object : friendships) {
            ParseUser from = object.getParseUser("from");

            ParseUser friend;
            if (from.hasSameId(ParseUser.getCurrentUser())) {
                friend = object.getParseUser("to");
            } else {
                friend = from;
            }

            friends.add(friend);
        }

        return friends;
    }

    public static ParseQuery<ParseObject> getCurrentFriendsQuery() {

        final List<ParseUser> friends = new ArrayList<ParseUser>();

        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Friend");
        query1.whereEqualTo("from", ParseUser.getCurrentUser());
        query1.whereEqualTo("confirmed", true);
        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Friend");
        query2.whereEqualTo("to", ParseUser.getCurrentUser());
        query2.whereEqualTo("confirmed", true);

        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(query1);
        queries.add(query2);

        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
        mainQuery.include("from");
        mainQuery.include("to");

        return mainQuery;
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
}
