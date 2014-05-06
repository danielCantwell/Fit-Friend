package com.cantwellcode.athletejournal;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 5/5/2014.
 */
public abstract class SocialEvent {

    public static void requestFriend(ParseUser friend) {
        // create an entry in the Friend table
        ParseObject friendRequest = new ParseObject("Friend");
        friendRequest.addUnique("users", ParseUser.getCurrentUser());
        friendRequest.addUnique("users", friend);
        friendRequest.put("confirmed", false);
        friendRequest.put("createdBy", ParseUser.getCurrentUser());
        friendRequest.saveEventually();
    }

    public static void confirmFriend(ParseUser friend) {
        // confirm an entry in the Friend table
        List<ParseUser> users = new ArrayList<ParseUser>();
        users.add(ParseUser.getCurrentUser());
        users.add(friend);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Friend");
        query.whereContainsAll("users", users);

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                parseObject.put("confirmed", true);
                parseObject.saveEventually();
            }
        });
    }

    public static void removeFriend(ParseUser friend) {
        // confirm an entry in the Friend table
        List<ParseUser> users = new ArrayList<ParseUser>();
        users.add(ParseUser.getCurrentUser());
        users.add(friend);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Friend");
        query.whereContainsAll("users", users);

        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                parseObject.deleteEventually();
            }
        });
    }
}
