package com.cantwellcode.ipsum.Connect;

import com.cantwellcode.ipsum.Connect.Friends.ExercisePost;
import com.cantwellcode.ipsum.Connect.Friends.ForumPost;
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

    public List<ForumPost> getForumPosts() {
        return getList("forumPosts");
    }

    public void setForumPosts(List<ForumPost> forumPosts) {
        put("forumPosts", forumPosts);
    }

    public void addForumPost(ForumPost forumPost) {
        List<ForumPost> forumPosts = getForumPosts();
        forumPosts.add(forumPost);
        setForumPosts(forumPosts);
    }

    public void removeForumPost(ForumPost forumPost) {
        List<ForumPost> forumPosts = getForumPosts();
        forumPosts.remove(forumPost);
        setForumPosts(forumPosts);
    }

    public List<ExercisePost> getExercisePosts() {
        return getList("exercisePosts");
    }

    public void setExercisePosts(List<ExercisePost> exercisePost) {
        put("exercisePosts", exercisePost);
    }

    public void addExercisePost(ExercisePost exercisePost) {
        List<ExercisePost> exercisePosts = getExercisePosts();
        exercisePosts.add(exercisePost);
        setExercisePosts(exercisePosts);
    }

    public void removeExercisePost(ExercisePost exercisePost) {
        List<ExercisePost> exercisePosts = getExercisePosts();
        exercisePosts.add(exercisePost);
        setExercisePosts(exercisePosts);
    }

//    public List<NutritionPost> getNutritionPosts() {
//        return getList("nutritionPosts");
//    }
//
//    public void setNutritionPosts(List<NutritionPost> nutritionPosts) {
//        put("nutritionPosts", nutritionPosts);
//    }
//
//    public void addNutritionPost(NutritionPost nutritionPost) {
//        List<NutritionPost> nutritionPosts = getNutritionPosts();
//        nutritionPosts.add(nutritionPost);
//        setNutritionPosts(nutritionPosts);
//    }
//
//    public void removeNutritionPost(NutritionPost nutritionPost) {
//        List<NutritionPost> nutritionPosts = getNutritionPosts();
//        nutritionPosts.remove(nutritionPost);
//        setNutritionPosts(nutritionPosts);
//    }
}
