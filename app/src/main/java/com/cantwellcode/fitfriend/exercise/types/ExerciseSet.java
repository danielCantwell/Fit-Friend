package com.cantwellcode.fitfriend.exercise.types;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Daniel on 10/26/2014.
 */
@ParseClassName("ExerciseSet")
public class ExerciseSet extends ParseObject {

    public int getReps() {
        return getInt("reps");
    }

    public void setReps(int reps) {
        put("reps", reps);
    }

    public int getWeight() {
        return getInt("weight");
    }

    public void setWeight(int weight) {
        put("weight", weight);
    }

    public int getTime() {
        return getInt("time");
    }

    public void setTime(int time) {
        put("time", time);
    }
}
