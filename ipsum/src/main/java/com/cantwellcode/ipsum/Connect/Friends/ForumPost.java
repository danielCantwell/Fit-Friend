package com.cantwellcode.ipsum.Connect.Friends;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Daniel on 4/27/2014.
 */

/**
 * DATA:
 *
 * user (author), content (text),
 */
@ParseClassName("ForumPost")
public class ForumPost extends ParseObject {

    public ParseUser getUser() {
        return getParseUser("user");
    }

    public void setUser(ParseUser user) {
        put("user", user);
    }

    public String getContent() {
        return getString("content");
    }

    public void setContent(String content) {
        put("content", content);
    }

    public int getHighFives() { return getInt("highFives"); }

    public void setHighFives(int num) { put ("highFives", num); }

    public void addHighFive() {
        increment("highFives");
    }

    public List<String> getComments() { return getList("comments"); }

    public void setComments(List<String> comments) { put("comments", comments); }

    public void addComment(String comment) {
        add("comments", comment);
    }

    public static ParseQuery<ForumPost> getQuery() {
        return ParseQuery.getQuery(ForumPost.class);
    }
}
