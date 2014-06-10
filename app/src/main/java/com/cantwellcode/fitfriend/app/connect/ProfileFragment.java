package com.cantwellcode.fitfriend.app.connect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cantwellcode.fitfriend.app.R;
import com.cantwellcode.fitfriend.app.startup.DispatchActivity;
import com.cantwellcode.fitfriend.app.utils.Statics;
import com.parse.ParseUser;

/**
 * Created by Daniel on 4/15/2014.
 */
public class ProfileFragment extends Fragment {

    private TextView name;
    private TextView age;
    private TextView mainSport;
    private TextView location;

    private Button friends;
    private Button settings;
    private Button logout;

    private ParseUser user;

    public static Fragment newInstance() {
        ProfileFragment f = new ProfileFragment();
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

        friends = (Button) root.findViewById(R.id.friends);
        settings = (Button) root.findViewById(R.id.settings);
        logout = (Button) root.findViewById(R.id.logout);

        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FriendsActivity.class);
                startActivityForResult(intent, Statics.INTENT_REQUEST_FRIENDS);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
