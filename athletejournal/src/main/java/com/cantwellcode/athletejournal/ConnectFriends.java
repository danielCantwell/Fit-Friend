package com.cantwellcode.athletejournal;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Daniel on 4/15/2014.
 */
public class ConnectFriends extends Fragment {

    public static Fragment newInstance() {
        ConnectFriends f = new ConnectFriends();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.connect_friends, null);

        return root;
    }
}
