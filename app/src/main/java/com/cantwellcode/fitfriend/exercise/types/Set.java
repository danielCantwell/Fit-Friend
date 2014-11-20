package com.cantwellcode.fitfriend.exercise.types;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Daniel on 10/26/2014.
 */
public class Set extends JSONObject {
    public Set() {
    }

    public Set(int weight, int reps, int time) {
        try {
            put("weight", weight);
            put("reps", reps);
            put("time", time);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getWeight() throws JSONException {
        return getInt("weight");
    }

    public void setWeight(int weight) {
        try {
            put("weight", weight);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getReps() throws JSONException {
        return getInt("reps");
    }

    public void setReps(int reps) {
        try {
            put("reps", reps);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getTime() throws JSONException {
        return getInt("time");
    }

    public void setTime(int time) {
        try {
            put("time", time);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
