package com.cantwellcode.athletejournal;

import android.app.Application;
import android.content.SharedPreferences;

import com.parse.Parse;

/**
 * Created by Daniel on 5/3/2014.
 */
public class App extends Application {

    private static SharedPreferences preferences;

    public App() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(this, "6ndNVpRctpv0EB5awdLtiT1nEwg5WidUBSNyKRwo", "QeU6X4k0S1zJDtlMZhiZPoe59DKhhGJONMdhZEBN");
    }
}
