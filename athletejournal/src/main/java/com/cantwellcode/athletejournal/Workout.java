package com.cantwellcode.athletejournal;

import java.io.Serializable;

/**
 * Created by Daniel on 2/7/14.
 */
public class Workout implements Serializable {

    protected String name;
    protected String date;
    protected String type;
    protected String notes;

    public Workout() {
        name = "";
        date = "";
        type = "";
        notes = "";
    }

    public Workout(String name, String date, String type, String notes) {
        this.name = name;
        this.date = date;
        this.type = type;
        this.notes = notes;
    }

    public String get_name() {
        return name;
    }

    public void set_name(String name) {
        this.name = name;
    }

    public String get_date() {
        return date;
    }

    public void set_date(String date) {
        this.date = date;
    }

    public String get_type() {
        return type;
    }

    public void set_type(String type) {
        this.type = type;
    }

    public String get_notes() {
        return notes;
    }

    public void set_notes(String notes) {
        this.notes = notes;
    }
}
