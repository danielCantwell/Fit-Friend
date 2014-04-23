package com.cantwellcode.athletejournal;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Daniel on 4/17/2014.
 */
public class Gym extends Workout implements Serializable {

    private String name;
    private String date;
    private String type;

    private List<GymRoutine> routines;

    public Gym() {

    }

    public Gym(String date) {
        this.date = date;
    }

    public Gym(String name, String date, String type, List<GymRoutine> routines) {
        this.name = name;
        this.date = date;
        this.type = type;
        this.routines = routines;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<GymRoutine> getRoutines() {
        return routines;
    }

    public void setRoutines(List<GymRoutine> routines) {
        this.routines = routines;
    }

    public void addRoutine(GymRoutine routine) {
        routines.add(routine);
    }

    public void addRoutine(String name, List<GymSet> sets) {
        routines.add(new GymRoutine(name, sets));
    }
}
