package com.cantwellcode.fitfriend.utils;

/**
 * Created by Daniel on 5/31/2014.
 */
public abstract class Statics {

    public static final String SAVED_EXERCISES = "Saved Exercises";

    public static final String GOAL_CALORIES = "GoalCalories";
    public static final String GOAL_FAT = "GoalFat";
    public static final String GOAL_CARBS = "GoalCarbs";
    public static final String GOAL_PROTEIN = "GoalProtein";

    public static final String FATSECRET_URL = "http://platform.fatsecret.com/rest/server.api";
    public static final String API_CONSUMER_KEY = "ffb559ce0118461abac9a23d5de05128";
    public static final String API_SHARED_SECRET = "148ce93f2f3d4346b3952864b3a99528";

    public static final int INTENT_REQUEST_MEAL = 1001;
    public static final int INTENT_REQUEST_WORKOUT = 1002;
    public static final int INTENT_REQUEST_EVENT = 1003;
    public static final int INTENT_REQUEST_EVENT_DETAILS = 1004;
    public static final int INTENT_REQUEST_FRIENDS = 1005;
    public static final int INTENT_REQUEST_SELECT_PICTURE = 1006;

    public static enum RoutineType {
        Lifting, Speed, HeartRate, Cadence
    }

    // Category for friends posts
    public static String FORUM_CATEGORY_FRIENDS = "Friends";
    // Category for general public posts
    public static String FORUM_CATEGORY_GENERAL = "General";
    // Categories for exercise
    public static String FORUM_CATEGORY_EXERCISE = "Exercise";
    public static String FORUM_CATEGORY_SWIMMING = "Swimming";
    public static String FORUM_CATEGORY_RUNNING = "Running";
    public static String FORUM_CATEGORY_CYCLING = "Cycling";
    public static String FORUM_CATEGORY_TRIATHLON = "Triathlon";
    public static String FORUM_CATEGORY_LIFTING = "Lifting";
    public static String FORUM_CATEGORY_CROSSFIT = "Crossfit";
    public static String FORUM_CATEGORY_STRETCHING = "Stretching";
    public static String FORUM_CATEGORY_RECOVERY = "Recovery";
    // Categories for nutrition
    public static String FORUM_CATEGORY_NUTRITION = "Nutrition";
    public static String FORUM_CATEGORY_BULKING = "Bulking";
    public static String FORUM_CATEGORY_WEIGHTLOSS = "WeightLoss";
    public static String FORUM_CATEGORY_TONING = "Toning";

    // Push Channels
    public static String EVENT_CHANNEL_ = "Event-";
}
