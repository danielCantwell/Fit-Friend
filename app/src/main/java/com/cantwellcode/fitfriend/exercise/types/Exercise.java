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

    public void setName(String name) {
        put("name", name);
    }

    public String getName() {
        return getString("name");
    }

    public void setSets(List<Set> sets) {
        put("sets", sets);
    }

    public void addSet(Set set) {
        add("sets", set);
    }

    public List<Set> getSets() {
        return getList("sets");
    }

    public void weight(boolean recordWeight) { put("recordWeight", recordWeight); }

    public boolean recordWeight() { return getBoolean("recordWeight"); }

    public void reps(boolean recordReps) { put("recordReps", recordReps); }

    public boolean recordReps() { return getBoolean("recordReps"); }

    public void time(boolean recordTime) { put("recordTime", recordTime); }

    public boolean recordTime() { return getBoolean("recordTime"); }

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

    public static ParseQuery<Exercise> getQuery() {
        return ParseQuery.getQuery("Exercise");
    }
}
