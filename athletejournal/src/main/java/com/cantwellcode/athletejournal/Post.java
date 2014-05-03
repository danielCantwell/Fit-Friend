package com.cantwellcode.athletejournal;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 4/27/2014.
 */
public class Post {

    private User author;
    private String group;
    private String dateTime;
    private String content;

    private List<User> highFives;
    private List<Comment> discussion;

    private Bundle options;

    public Post(User author, String group, String dateTime, String content, Bundle options) {
        this.author = author;
        this.group = group;
        this.dateTime = dateTime;
        this.content = content;
        this.options = options;

        highFives = new ArrayList<User>();
        discussion = new ArrayList<Comment>();
    }

    public Post(User author, String group, String dateTime, String content, List<User> highFives, List<Comment> discussion, Bundle options) {
        this.author = author;
        this.group = group;
        this.dateTime = dateTime;
        this.content = content;
        this.highFives = highFives;
        this.discussion = discussion;
        this.options = options;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<User> getHighFives() {
        return highFives;
    }

    public void setHighFives(List<User> highFives) {
        this.highFives = highFives;
    }

    public void addHighFive(User user) {
        this.highFives.add(user);
    }

    public int getHighFiveCount() {
        return highFives.size();
    }

    public List<Comment> getDiscussion() {
        return discussion;
    }

    public void setDiscussion(List<Comment> discussion) {
        this.discussion = discussion;
    }

    public int getDiscussionCount() {
        return discussion.size();
    }

    public void addComment(Comment comment) {
        this.discussion.add(comment);
    }

    public Bundle getOptions() {
        return options;
    }

    public void setOptions(Bundle options) {
        this.options = options;
    }
}
