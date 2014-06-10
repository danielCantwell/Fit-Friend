package com.cantwellcode.fitfriend.app.startup;

import android.app.Application;

import com.cantwellcode.fitfriend.app.connect.Comment;
import com.cantwellcode.fitfriend.app.connect.Group;
import com.cantwellcode.fitfriend.app.connect.Post;
import com.cantwellcode.fitfriend.app.plan.Event;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

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
        Parse.initialize(this, "6ndNVpRctpv0EB5awdLtiT1nEwg5WidUBSNyKRwo", "QeU6X4k0S1zJDtlMZhiZPoe59DKhhGJONMdhZEBN");
    }
}
