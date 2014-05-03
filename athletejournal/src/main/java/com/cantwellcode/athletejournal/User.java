package com.cantwellcode.athletejournal;

import android.accounts.Account;

import java.util.List;
import java.util.Map;

/**
 * Created by Daniel on 4/27/2014.
 */
public class User {

    private String name;
    private String birthday;
    private String age;
    private String location;
    private String mainSport;
    private boolean isPrivate;

    private List<Swim> swims;
    private List<Bike> bikes;
    private List<Run> runs;
    private List<Gym> gyms;

    private int numForumPosts;
    private int numExercisePosts;
    private int numNutritionPosts;

    private List<User> friends;
    private Map<User, List<String>> messages;
    private Map<String, List<User>> groups;
    private List<Post> posts;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMainSport() {
        return mainSport;
    }

    public void setMainSport(String mainSport) {
        this.mainSport = mainSport;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public List<Swim> getSwims() {
        return swims;
    }

    public void setSwims(List<Swim> swims) {
        this.swims = swims;
    }

    public List<Bike> getBikes() {
        return bikes;
    }

    public void setBikes(List<Bike> bikes) {
        this.bikes = bikes;
    }

    public List<Run> getRuns() {
        return runs;
    }

    public void setRuns(List<Run> runs) {
        this.runs = runs;
    }

    public List<Gym> getGyms() {
        return gyms;
    }

    public void setGyms(List<Gym> gyms) {
        this.gyms = gyms;
    }

    public int getNumForumPosts() {
        return numForumPosts;
    }

    public void setNumForumPosts(int numForumPosts) {
        this.numForumPosts = numForumPosts;
    }

    public int getNumExercisePosts() {
        return numExercisePosts;
    }

    public void setNumExercisePosts(int numExercisePosts) {
        this.numExercisePosts = numExercisePosts;
    }

    public int getNumNutritionPosts() {
        return numNutritionPosts;
    }

    public void setNumNutritionPosts(int numNutritionPosts) {
        this.numNutritionPosts = numNutritionPosts;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

    public Map<User, List<String>> getMessages() {
        return messages;
    }

    public void setMessages(Map<User, List<String>> messages) {
        this.messages = messages;
    }

    public Map<String, List<User>> getGroups() {
        return groups;
    }

    public void setGroups(Map<String, List<User>> groups) {
        this.groups = groups;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
