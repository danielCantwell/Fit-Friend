package com.cantwellcode.fitfriend.app.startup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.ui.ParseLoginDispatchActivity;

/**
 * Created by Daniel on 5/3/2014.
 *
 * Routes the user to either the login screen or the main screen
 */
public class DispatchActivity extends ParseLoginDispatchActivity {


    @Override
    protected Class<?> getTargetClass() {
        return MainActivity.class;
    }
}
