package com.cantwellcode.fitfriend.utils;

import android.content.Context;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 5/5/2014.
 */
public abstract class SocialEvent {

    private static String s;
    public static void requestFriend(final Context context, final ParseUser friend) {

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
                    s = "friendship with " + friend.getUsername() + " already exists";
                } else {

                    /* this is a new friendship, continue with request */
                    s = "requesting friendship with " + friend.getUsername();

                    /* create an entry in the Friend table */
                    ParseObject friendRequest = new ParseObject("Friend");
                    friendRequest.put("from", ParseUser.getCurrentUser());
                    friendRequest.put("to", friend);
                    friendRequest.put("confirmed", false);
                    friendRequest.saveEventually();
                }
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void confirmFriend(ParseUser friend) {

//        friend.pinInBackground("Friends");
        // confirm an entry in the Friend table
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Friend");
        query.whereEqualTo("from", friend);
        query.whereEqualTo("to", ParseUser.getCurrentUser());

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    parseObject.put("confirmed", true);
                    parseObject.saveInBackground();
                }
            }
        });
    }

    public static void removeFriend(ParseUser friend) {
        ParsePush.unsubscribeInBackground(friend.getObjectId());

        JSONObject j = new JSONObject();
        try {
            j.put(Statics.PUSH_ACTION_UNSUBSCRIBE, ParseUser.getCurrentUser().getObjectId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ParseQuery query = ParseInstallation.getQuery();
        query.whereEqualTo("channels", ParseUser.getCurrentUser().getObjectId());

        ParsePush push = new ParsePush();
        push.setData(j);
        push.setQuery(query);
        push.sendInBackground();

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
                    parseObject.deleteInBackground();
                }
            }
        });
    }

    public static void removeFriend(ParseObject friend) {
        // remove an entry in the Friend table
        friend.deleteInBackground();
    }

    public static ParseQuery<ParseObject> getCurrentFriendshipsQuery() {

        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Friend");
        query1.whereEqualTo("from", ParseUser.getCurrentUser());
        query1.whereEqualTo("confirmed", true);
        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Friend");
        query2.whereEqualTo("to", ParseUser.getCurrentUser());
        query2.whereEqualTo("confirmed", true);

        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(query1);
        queries.add(query2);

        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
        mainQuery.addAscendingOrder("name");
        mainQuery.include("from");
        mainQuery.include("to");

        return mainQuery;
    }

    public static ParseQuery<ParseUser> getCurrentFriendsLocalQuery() {

        ParseQuery<ParseUser> query = ParseQuery.getQuery(ParseUser.class);
        query.fromPin(Statics.PIN_FRIENDS);

        return query;
    }

    /*
    Friend queries will be for a friendship object, where both you and the friend are in it
    This method extracts your friend from the friendship object
    */
    public static ParseUser getFriendFromFriendship(ParseObject friendship) {
        ParseUser from = friendship.getParseUser("from");

        ParseUser friend;
        if (from.hasSameId(ParseUser.getCurrentUser())) {
            friend = friendship.getParseUser("to");
        } else {
            friend = from;
        }

        return friend;
    }
}
