package com.cantwellcode.fitfriend.app.connect;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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
public class ProfileActivity extends FragmentActivity {

    private TextView name;
    private TextView age;
    private TextView mainSport;
    private TextView location;

    private Button friends;
    private Button settings;
    private Button logout;

    private ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_social);

        user = ParseUser.getCurrentUser();

        name = (TextView) findViewById(R.id.name);
        age = (TextView) findViewById(R.id.age);
        mainSport = (TextView) findViewById(R.id.mainSport);
        location = (TextView) findViewById(R.id.location);

        name.setText(user.getString("name"));
        age.setText(user.getInt("age") + "");
        mainSport.setText(user.getString("mainSport"));
        location.setText(user.getString("location"));

        friends = (Button) findViewById(R.id.friends);
        settings = (Button) findViewById(R.id.settings);
        logout = (Button) findViewById(R.id.logout);

        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, FriendsActivity.class);
                startActivityForResult(intent, Statics.INTENT_REQUEST_FRIENDS);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                Intent intent = new Intent(ProfileActivity.this, DispatchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Profile");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
