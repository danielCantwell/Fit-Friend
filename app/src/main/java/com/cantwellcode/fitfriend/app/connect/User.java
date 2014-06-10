package com.cantwellcode.fitfriend.app.connect;

import com.parse.ParseObject;

import java.util.List;

/**
 * Created by Daniel on 4/27/2014.
 */
public class User extends ParseObject {

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    public int getAge() {
        return getInt("age");
    }

    public void setAge(int age) {
        put("age", age);
    }

    public String getLocation() {
        return getString("location");
    }

    public void setLocation(String location) {
        put("location", location);
    }

    public String getMainSport() {
        return getString("mainSport");
    }

    public void setMainSport(String mainSport) {
        put("mainSport", mainSport);
    }

    public boolean isPrivate() {
        return getBoolean("isPrivate");
    }

    public void setPrivate(boolean isPrivate) {
        put("isPrivate", isPrivate);
    }

    public List<User> getFriends() {
        return getList("friends");
    }

    public void setFriends(List<User> friends) {
        put("friends", friends);
    }

    public void addFriend(User friend) {
        List<User> friends = getFriends();
        friends.add(friend);
        setFriends(friends);
    }

    public void removeFriend(User friend) {
        List<User> friends = getFriends();
        friends.remove(friend);
        setFriends(friends);
    }

    public List<Post> getPosts() {
        return getList("posts");
    }

    public void setForumPosts(List<Post> posts) {
        put("posts", posts);
    }

    public void addPost(Post post) {
        List<Post> posts = getPosts();
        posts.add(post);
        setForumPosts(posts);
    }

    public void removePost(Post post) {
        List<Post> posts = getPosts();
        posts.remove(post);
        setForumPosts(posts);
    }
}
