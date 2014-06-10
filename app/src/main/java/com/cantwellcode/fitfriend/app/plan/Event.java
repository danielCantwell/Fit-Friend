package com.cantwellcode.fitfriend.app.plan;

import com.cantwellcode.fitfriend.app.connect.Comment;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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
    public String getDate() {
        return getString("date");
    }

    public void setDate(String date) {
        put("date", date);
    }

    // Time of the Event
    public String getTime() {
        return getString("time");
    }

    public void setTime(String time) {
        put("time", time);
    }

    // Location of the Event
    public String getLocation() {
        return getString("location");
    }

    public void setLocation(String location) {
        put("location", location);
    }

    // Description of the Event
    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        put("description", description);
    }

    // Type of Event
    public String getType() {
        return getString("type");
    }

    public void setType(String type) {
        put("type", type);
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

    // Query
    public static ParseQuery<Event> getQuery() {
        return ParseQuery.getQuery(Event.class);
    }
}
