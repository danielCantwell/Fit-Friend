package com.cantwellcode.fitfriend.startup;

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
