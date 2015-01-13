package com.cantwellcode.fitfriend.exercise.types;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by Daniel on 10/27/2014.
 */
@ParseClassName("Exercise")
public class Exercise extends ParseObject {

    public void setWorkout(Workout workout) {
        put("workout", workout);
    }

    public Workout getWorkout() {
        return (Workout) get("workout");
    }

    public void setName(String name) {
        put("name", name);
    }

    public String getName() {
        return getString("name");
    }

    public void setSets(List<ExerciseSet> exerciseSets) {
        put("sets", exerciseSets);
    }

    public void addSet(ExerciseSet exerciseSet) {
        add("sets", exerciseSet);
    }

    public List<ExerciseSet> getSets() {
        return getList("sets");
    }

    public void weight(boolean recordWeight) {
        put("recordWeight", recordWeight);
    }

    public boolean recordWeight() {
        return getBoolean("recordWeight");
    }

    public void reps(boolean recordReps) {
        put("recordReps", recordReps);
    }

    public boolean recordReps() {
        return getBoolean("recordReps");
    }

    public void time(boolean recordTime) {
        put("recordTime", recordTime);
    }

    public boolean recordTime() {
        return getBoolean("recordTime");
    }

    public void setArms(boolean isArms) {
        put("arms", isArms);
    }

    public boolean usesArms() {
        return getBoolean("arms");
    }

    public void setChest(boolean isChest) {
        put("chest", isChest);
    }

    public boolean usesChest() {
        return getBoolean("chest");
    }

    public void setShoulders(boolean isShoulders) {
        put("shoulders", isShoulders);
    }

    public boolean usesShoulders() {
        return getBoolean("shoulders");
    }

    public void setBack(boolean isBack) {
        put("back", isBack);
    }

    public boolean usesBack() {
        return getBoolean("back");
    }

    public void setAbs(boolean isAbs) {
        put("abs", isAbs);
    }

    public boolean usesAbs() {
        return getBoolean("abs");
    }

    public void setLegs(boolean isLegs) {
        put("legs", isLegs);
    }

    public boolean usesLegs() {
        return getBoolean("legs");
    }

    public void setGlutes(boolean isGlutes) {
        put("glutes", isGlutes);
    }

    public boolean usesGlutes() {
        return getBoolean("glutes");
    }

    public Exercise createNew() {
        Exercise e = new Exercise();

        e.weight(recordWeight());
        e.reps(recordReps());
        e.time(recordTime());

        e.setArms(usesArms());
        e.setShoulders(usesShoulders());
        e.setChest(usesChest());
        e.setBack(usesBack());
        e.setAbs(usesAbs());
        e.setLegs(usesLegs());
        e.setGlutes(usesGlutes());

        return e;
    }

    public ParseQuery<Exercise> getDetailedQuery() {
        ParseQuery<Exercise> query = Exercise.getQuery();
        query.whereEqualTo("name", getName());
        query.whereEqualTo("recordWeight", recordWeight());
        query.whereEqualTo("recordReps", recordReps());
        query.whereEqualTo("recordTime", recordTime());
        query.whereEqualTo("arms", usesArms());
        query.whereEqualTo("chest", usesChest());
        query.whereEqualTo("shoulders", usesShoulders());
        query.whereEqualTo("back", usesBack());
        query.whereEqualTo("abs", usesAbs());
        query.whereEqualTo("legs", usesLegs());
        query.whereEqualTo("glutes", usesGlutes());
        return query;
    }

    public boolean sameExercise(Exercise e) {
        return (getName().equals(e.getName())
                && (recordWeight() == e.recordWeight())
                && (recordReps() == e.recordReps())
                && (recordTime() == e.recordTime())
                && (usesArms() == e.usesArms())
                && (usesShoulders() == e.usesShoulders())
                && (usesChest() == e.usesChest())
                && (usesBack() == e.usesBack())
                && (usesAbs() == e.usesAbs())
                && (usesLegs() == e.usesLegs())
                && (usesGlutes() == e.usesGlutes()));
    }

    public static ParseQuery<Exercise> getQuery() {
        return ParseQuery.getQuery("Exercise");
    }
}
