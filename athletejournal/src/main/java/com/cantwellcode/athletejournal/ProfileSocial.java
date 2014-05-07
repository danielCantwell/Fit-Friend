package com.cantwellcode.athletejournal;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Daniel on 4/15/2014.
 */
public class ProfileSocial extends Fragment {

    private TextView name;
    private TextView age;
    private TextView mainSport;
    private TextView location;

    private Button chat;
    private Button events;
    private Button posts;
    private Button friends;

    private Button logout;

    private ParseUser user;

    public static Fragment newInstance() {
        ProfileSocial f = new ProfileSocial();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.profile_social, null);

        user = ParseUser.getCurrentUser();

        name = (TextView) root.findViewById(R.id.name);
        age = (TextView) root.findViewById(R.id.age);
        mainSport = (TextView) root.findViewById(R.id.mainSport);
        location = (TextView) root.findViewById(R.id.location);

        name.setText(user.getString("name"));
        age.setText(user.getInt("age") + "");
        mainSport.setText(user.getString("mainSport"));
        location.setText(user.getString("location"));

        chat = (Button) root.findViewById(R.id.chat);
        events = (Button) root.findViewById(R.id.events);
        posts = (Button) root.findViewById(R.id.posts);
        friends = (Button) root.findViewById(R.id.friends);
        logout = (Button) root.findViewById(R.id.logout);

        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RelativeLayout) root.findViewById(R.id.profileSocial)).removeAllViews();
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.profileSocial, ProfileFriends.newInstance())
                        .addToBackStack(null).commit();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                Intent intent = new Intent(getActivity(), DispatchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);
            }
        });

        return root;
    }
}
