package com.cantwellcode.fitfriend.exercise.types;

import com.cantwellcode.fitfriend.utils.Statics;
import com.parse.ParseClassName;
import com.parse.ParseException;
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

    public Workout(Date date, String notes) {
        put("date", date);
        put("notes", notes);
    }

    public void saveExercisesLocally(List<Exercise> exercises) {
        for (Exercise e : exercises) {
            e.setWorkout(this);
        }
        pinAllInBackground(Statics.PIN_EXERCISES, exercises);
    }

    public List<Exercise> getExerciseList() throws ParseException {
        ParseQuery<Exercise> query = Exercise.getQuery();
        query.whereEqualTo("workout", this);
        return query.find();
    }

    public List<Exercise> getLocalExerciseList() throws ParseException {
        ParseQuery<Exercise> query = Exercise.getQuery();
        query.fromPin(Statics.PIN_EXERCISES);
        query.whereEqualTo("workout", this);
        return query.find();
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

    public static ParseQuery<Workout> getQuery() { return ParseQuery.getQuery("Workout"); }
}
