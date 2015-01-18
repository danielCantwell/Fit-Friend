package com.cantwellcode.fitfriend.startup;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.cantwellcode.fitfriend.exercise.types.ExerciseSet;
import com.cantwellcode.fitfriend.exercise.types.Workout;
import com.cantwellcode.fitfriend.connect.Comment;
import com.cantwellcode.fitfriend.connect.Group;
import com.cantwellcode.fitfriend.connect.Post;
import com.cantwellcode.fitfriend.exercise.types.Exercise;
import com.cantwellcode.fitfriend.nutrition.Food;
import com.cantwellcode.fitfriend.plan.Event;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseRole;
import com.parse.SaveCallback;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Daniel on 5/3/2014.
 */
public class App extends Application {

    public App() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Group.class);
        ParseObject.registerSubclass(Comment.class);
        ParseObject.registerSubclass(Event.class);
        ParseObject.registerSubclass(Exercise.class);
        ParseObject.registerSubclass(ExerciseSet.class);
        ParseObject.registerSubclass(Workout.class);
        ParseObject.registerSubclass(Food.class);

        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "6ndNVpRctpv0EB5awdLtiT1nEwg5WidUBSNyKRwo", "QeU6X4k0S1zJDtlMZhiZPoe59DKhhGJONMdhZEBN");
//        ParseFacebookUtils.initialize(getString(R.string.facebook_app_id));

        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });


//        try{
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    "com.cantwellcode.fitfriend.app", PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }
    }
}
