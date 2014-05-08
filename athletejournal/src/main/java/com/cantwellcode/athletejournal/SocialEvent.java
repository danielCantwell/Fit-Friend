package com.cantwellcode.athletejournal;

import android.content.Context;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Created by Daniel on 5/5/2014.
 */
public abstract class SocialEvent {

    private static void requestFriend(ParseUser friend) {
        // create an entry in the Friend table
        ParseObject friendRequest = new ParseObject("Friend");
        friendRequest.put("from", ParseUser.getCurrentUser());
        friendRequest.put("to", friend);
        friendRequest.put("confirmed", false);
        friendRequest.saveEventually();
    }

    private static String s;
    public static void requestFriend(final Context context, String type, final String identity) {
        /* Check that username exists */
        final ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo(type, identity);

        query.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(final ParseUser friend, ParseException e) {
                if (e == null) {

                    /* USER EXISTS, continue with request process */

                    /* Check that user is not already a confirmed / requested friend */
                    ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Friend");
                    query1.whereEqualTo("from", ParseUser.getCurrentUser());
                    query1.whereEqualTo("to", friend);

                    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Friend");
                    query2.whereEqualTo("to", ParseUser.getCurrentUser());
                    query2.whereEqualTo("from", friend);

                    List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
                    queries.add(query1);
                    queries.add(query2);

                    ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);

                    mainQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject parseObject, ParseException e) {
                            if (e == null) {
                                // friendship ALREADY EXISTS
                                s = "friendship with " + identity + " already exists";
                            } else {

                                /* this is a new friendship, continue with request */
                                requestFriend(friend);
                                s = "requesting friendship with " + identity;
                            }
                            Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {

                    /* USER does NOT exist */
                    s = "user " + identity + " not found";
                    Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static void confirmFriend(ParseObject object) {
        object.put("confirmed", true);
        object.saveEventually();
    }

    public static void confirmFriend(ParseUser friend) {
        // confirm an entry in the Friend table
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Friend");
        query.whereEqualTo("from", friend);
        query.whereEqualTo("to", ParseUser.getCurrentUser());

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    parseObject.put("confirmed", true);
                    parseObject.saveEventually();
                }
            }
        });
    }

    public static void removeFriend(ParseUser friend) {
        // remove an entry in the Friend table
        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Friend");
        query1.whereEqualTo("from", friend);
        query1.whereEqualTo("to", ParseUser.getCurrentUser());
        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Friend");
        query2.whereEqualTo("from", ParseUser.getCurrentUser());
        query2.whereEqualTo("to", friend);

        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(query1);
        queries.add(query2);

        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);

        mainQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    parseObject.deleteEventually();
                }
            }
        });
    }

    public static void removeFriend(ParseObject friend) {
        // remove an entry in the Friend table
        friend.deleteInBackground();
    }
}
