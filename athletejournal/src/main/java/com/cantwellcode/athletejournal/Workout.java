package com.cantwellcode.athletejournal;

/**
 * Created by Daniel on 2/7/14.
 */
public class Workout {

    int _id;
    String _name;
    String _date;
    String _type;
    String _time;
    String _distance;
    String _speed;
    String _calories;
    String _heartRate;
    String _notes;

    public Workout() {
    }

    public Workout(int id, String name, String date, String type, String time,
                   String distance, String speed, String calories, String heartRate, String notes) {
        this._id = id;
        this._name = name;
        this._date = date;
        this._type = type;
        this._time = time;
        this._distance = distance;
        this._speed = speed;
        this._calories = calories;
        this._heartRate = heartRate;
        this._notes = notes;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int id) {
        this._id = id;
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

    public String get_time() {
        return _time;
    }

    public void set_time(String time) {
        this._time = time;
    }

    public String get_distance() {
        return _distance;
    }

    public void set_distance(String distance) {
        this._distance = distance;
    }

    public String get_speed() {
        return _speed;
    }

    public void set_speed(String speed) {
        this._speed = speed;
    }

    public String get_calories() {
        return _calories;
    }

    public void set_calories(String calories) {
        this._calories = calories;
    }

    public String get_heartRate() {
        return _heartRate;
    }

    public void set_heartRate(String heartRate) {
        this._heartRate = heartRate;
    }

    public String get_notes() {
        return _notes;
    }

    public void set_notes(String notes) {
        this._notes = notes;
    }
}
