package com.cantwellcode.fitfriend.plan;

import com.cantwellcode.fitfriend.connect.Comment;
import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

/**
 * Created by Daniel on 6/1/2014.
 */
@ParseClassName("Event")
public class Event extends ParseObject {

    // User who created the Event
    public ParseUser getUser() {
        return getParseUser("user");
    }

    public void setUser(ParseUser user) {
        put("user", user);
    }

    // Title of the Event
    public String getTitle() {
        return getString("title");
    }

    public void setTitle(String title) {
        put("title", title);
    }

    // Date of the Event
    public Date getDateTime() {
        return getDate("dateTime");
    }

    public void setDateTime(Date dateTime) {
        put("dateTime", dateTime);
    }

    // Location of the Event
    public ParseGeoPoint getLocation() {
        return getParseGeoPoint("location");
    }

    public void setLocation(ParseGeoPoint location) {
        put("location", location);
    }

    // Description of the Event
    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        put("description", description);
    }

    // Comments on the Event
    public List<Comment> getComments() {
        return getList("comments");
    }

    public void addComment(Comment comment) {
        add("comments", comment);
    }

    // Attendees for the Event
    public List<ParseUser> getAttendees() {
        return getList("attendees");
    }

    public void addAttendee(ParseUser user) {
        add("attendees", user);
    }

    public void removeAttendee(ParseUser user) {
        List<ParseUser> list = getAttendees();
        for (ParseUser listUser : list) {
            if (listUser.getObjectId().equals(user.getObjectId())) {
                list.remove(listUser);
                break;
            }
        }
        put("attendees", list);
    }

    public boolean checkIfAttending(ParseUser user) {

        // return true if user is attending
        List<ParseUser> list = getAttendees();
        for (ParseUser listUser : list) {
            if (listUser.getObjectId().equals(user.getObjectId())) {
                return true;
            }
        }

        // if user is not attending, return false
        return false;
    }

    // Query
    public static ParseQuery<Event> getQuery() {
        return ParseQuery.getQuery(Event.class);
    }
}
