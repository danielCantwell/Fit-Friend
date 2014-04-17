package com.cantwellcode.athletejournal;

import java.io.Serializable;

/**
 * Created by Daniel on 2/7/14.
 */
public class Workout implements Serializable {

    String _name        = "";
    String _date        = "";
    String _type        = "";
    String _notes       = "";

    public Workout() {
    }

    public Workout(String name, String date, String type, String notes) {
        this._name = name;
        this._date = date;
        this._type = type;
        this._notes = notes;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String name) {
        this._name = name;
    }

    public String get_date() {
        return _date;
    }

    public void set_date(String date) {
        this._date = date;
    }

    public String get_type() {
        return _type;
    }

    public void set_type(String type) {
        this._type = type;
    }

    public String get_notes() {
        return _notes;
    }

    public void set_notes(String notes) {
        this._notes = notes;
    }
}
