package com.cantwellcode.ipsum.app.startup;

import android.app.Application;

import com.cantwellcode.ipsum.app.connect.Group;
import com.cantwellcode.ipsum.app.connect.Post;
import com.parse.Parse;
import com.parse.ParseObject;

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
        Parse.initialize(this, "6ndNVpRctpv0EB5awdLtiT1nEwg5WidUBSNyKRwo", "QeU6X4k0S1zJDtlMZhiZPoe59DKhhGJONMdhZEBN");
    }
}
