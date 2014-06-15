package com.cantwellcode.fitfriend.app.connect;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cantwellcode.fitfriend.app.R;
import com.cantwellcode.fitfriend.app.nutrition.NutritionFavoritesView;
import com.cantwellcode.fitfriend.app.startup.DispatchActivity;
import com.cantwellcode.fitfriend.app.utils.Statics;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by Daniel on 4/15/2014.
 */
public class ProfileActivity extends FragmentActivity {

    private ImageButton picture;

    private TextView name;
    private TextView age;
    private TextView mainSport;
    private TextView location;
    private TextView headline;

    private Button friends;
    private Button settings;
    private Button logout;

    private Button favorites;

    private ParseUser user;

    private Bitmap userPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_social);

        user = ParseUser.getCurrentUser();

        picture = (ImageButton) findViewById(R.id.picture);

        ParseFile pic = user.getParseFile("picture");

        if (pic != null) {
            try {
                byte[] file = pic.getData();
                picture.setImageBitmap(BitmapFactory.decodeByteArray(file, 0, file.length));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            picture.setImageResource(R.drawable.profile);
        }

        name = (TextView) findViewById(R.id.name);
        age = (TextView) findViewById(R.id.age);
        mainSport = (TextView) findViewById(R.id.mainSport);
        location = (TextView) findViewById(R.id.location);
        headline = (TextView) findViewById(R.id.headline);

        name.setText(user.getString("name"));
        age.setText(user.getInt("age") + "");
        mainSport.setText(user.getString("mainSport"));
        location.setText(user.getString("location"));
        headline.setText(user.getString("headline"));

        friends = (Button) findViewById(R.id.friends);
        settings = (Button) findViewById(R.id.settings);
        favorites = (Button) findViewById(R.id.profileFavorites);
        logout = (Button) findViewById(R.id.logout);

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, Statics.INTENT_REQUEST_SELECT_PICTURE);
            }
        });

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

        favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, NutritionFavoritesView.class);
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

        switch(requestCode) {
            case Statics.INTENT_REQUEST_SELECT_PICTURE:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);

                        final Bitmap b = BitmapFactory.decodeStream(imageStream);

                        userPicture = Bitmap.createScaledBitmap(b, 140, 140, true);

                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        userPicture.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        // get byte array here
                        byte[] imageData = stream.toByteArray();

                        final ParseFile imgFile = new ParseFile("picture.png", imageData);
                        user.put("picture", imgFile);
                        user.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                picture.setImageBitmap(userPicture);
                            }
                        });

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }
}
