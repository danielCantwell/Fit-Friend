package com.cantwellcode.ipsum.app.exercise.log;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Daniel on 4/21/2014.
 */
public class GymRoutine implements Serializable {

    private String name;
    private List<GymSet> sets;

    public GymRoutine(String name, List<GymSet> sets) {
        this.name = name;
        this.sets = sets;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<GymSet> getSets() {
        return sets;
    }

    public void setSets(List<GymSet> sets) {
        this.sets = sets;
    }

    public void addSet(GymSet set) {
        sets.add(set);
    }

    public void addSet(int reps, int weight) {
        sets.add(new GymSet(reps, weight));
    }
}
