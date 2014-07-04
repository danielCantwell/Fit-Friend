package com.cantwellcode.fitfriend.app.exercise.types;

import java.util.List;

/**
 * Created by Daniel on 5/31/2014.
 */
public class Workout {

    private String name;
    private String type;
    private String intensity;
    private String date;
    private String notes;

    private List<Routine> routines;

    public Workout() {};

    public Workout(String date) {
        this.date = date;
    }

    public Workout(String name, String type, String intensity, String date, String notes, List<Routine> routines) {
        this.name = name;
        this.type = type;
        this.intensity = intensity;
        this.date = date;
        this.notes = notes;
        this.routines = routines;
    }
}
