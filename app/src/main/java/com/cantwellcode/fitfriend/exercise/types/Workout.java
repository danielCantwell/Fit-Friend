package com.cantwellcode.fitfriend.exercise.types;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;
import java.util.List;

/**
 * Created by Daniel on 5/31/2014.
 */
@ParseClassName("Workout")
public class Workout extends ParseObject {

    public Workout() {};

    public Workout(Date date, String notes, List<Exercise> exerciseList) {
        put("date", date);
        put("notes", notes);
        put("exercises", exerciseList);
    }

    public Date getDate() {
        return getDate("date");
    }

    public void setDate(Date date) {
        put("date", date);
    }

    public String getNotes() {
        return getString("notes");
    }

    public void setNotes(String notes) {
        put("notes", notes);
    }

    public List<Exercise> getExerciseList() {
        return getList("exercises");
    }

    public void setExerciseList(List<Exercise> exerciseList) {
        put("exercises", exerciseList);
    }

    public static ParseQuery<Workout> getQuery() { return ParseQuery.getQuery("Workout"); }
}
