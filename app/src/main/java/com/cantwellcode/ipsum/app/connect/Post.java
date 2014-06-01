package com.cantwellcode.ipsum.app.connect;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Daniel on 4/27/2014.
 */

/**
 * DATA:
 *
 * user (author), content (text),
 */
@ParseClassName("Post")
public class Post extends ParseObject {

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

    public List<Comment> getComments() { return getList("comments"); }

    public void setComments(List<Comment> comments) { put("comments", comments); }

    public void addComment(Comment comment) {
        add("comments", comment);
    }

    public static ParseQuery<Post> getQuery() {
        return ParseQuery.getQuery(Post.class);
    }
}
