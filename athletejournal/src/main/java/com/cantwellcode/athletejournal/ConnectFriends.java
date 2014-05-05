package com.cantwellcode.athletejournal;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

import com.parse.ParseUser;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mTabHost = new FragmentTabHost(getActivity());
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.tabHost);

        mTabHost.addTab(mTabHost.newTabSpec("Forum").setIndicator("Forum"), ConnectForum.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Exercise").setIndicator("Exercise"), ConnectForum.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Nutrition").setIndicator("Nutrition"), ConnectForum.class, null);

        mTabHost.setOnTabChangedListener(this);

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
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Connect");
    }

    @Override
    public void onTabChanged(String tabId) {

    }


}
