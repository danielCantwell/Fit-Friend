package com.cantwellcode.ipsum.Connect.Friends;

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
import android.widget.TabHost;

import com.cantwellcode.ipsum.R;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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

        mTabHost.addTab(mTabHost.newTabSpec("Forum").setIndicator("Forum"), ForumFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Exercise").setIndicator("Exercise"), ExerciseFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Nutrition").setIndicator("Food"), NutritionFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Profile")
                .setIndicator("", getResources().getDrawable(R.drawable.ic_person_black)), SocialProfile.class, null);

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
