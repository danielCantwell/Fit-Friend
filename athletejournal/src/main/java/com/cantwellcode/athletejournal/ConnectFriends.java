package com.cantwellcode.athletejournal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by Daniel on 4/27/2014.
 */
public class ConnectFriends extends Fragment {

    public static Fragment newInstance() {
        ConnectFriends f = new ConnectFriends();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Parse.initialize(getActivity(), "6ndNVpRctpv0EB5awdLtiT1nEwg5WidUBSNyKRwo", "QeU6X4k0S1zJDtlMZhiZPoe59DKhhGJONMdhZEBN");
//
//        ParseUser user = new ParseUser();
//        user.setUsername("test");
//        user.setPassword("passTest");
//        user.setEmail("test@test.com");
//
//        // other fields can be set just like with ParseObject
//        user.put("phone", "650-555-0000");
//
//        user.signUpInBackground(new SignUpCallback() {
//            public void done(ParseException e) {
//                if (e == null) {
//                    // Hooray! Let them use the app now.
//                } else {
//                    // Sign up didn't succeed. Look at the ParseException
//                    // to figure out what went wrong
//                }
//            }
//        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.connect_friends, null);

        return root;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }
}
