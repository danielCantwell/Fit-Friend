package com.cantwellcode.ipsum.Connect.Friends;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cantwellcode.ipsum.R;
import com.cantwellcode.ipsum.Startup.DispatchActivity;
import com.parse.ParseUser;

/**
 * Created by Daniel on 4/15/2014.
 */
public class SocialProfile extends Fragment {

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
        SocialProfile f = new SocialProfile();
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
