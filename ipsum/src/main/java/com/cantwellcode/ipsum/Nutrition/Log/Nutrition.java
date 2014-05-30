package com.cantwellcode.ipsum.Nutrition.Log;

import java.io.Serializable;

/**
 * Created by Daniel on 2/7/14.
 */
public class Nutrition implements Serializable{

    String _name;
    String _date;
    String _type;
    String _calories;
    String _protein;
    String _carbs;
    String _fat;

    public Nutrition() {
    }

    /**
     *
     * @param name - name of the meal
     * @param date - in simple date format DD MMMM YYYY
     * @param type - type of meal
     * @param calories - how many calories in the meal
     * @param protein - how much protein in the meal
     * @param carbs - how many carbs in the meal
     * @param fat - how much fat in the meal
     */
    public Nutrition(String name, String date, String type, String calories, String protein, String carbs, String fat) {
        this._name = name;
        this._date = date;
        this._type = type;
        this._calories = calories;
        this._protein = protein;
        this._carbs = carbs;
        this._fat = fat;
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

    public String get_calories() {
        return _calories;
    }

    public void set_calories(String calories) {
        this._calories = calories;
    }

    public String get_protein() {
        return _protein;
    }

    public void set_protein(String protein) {
        this._protein = protein;
    }

    public String get_carbs() {
        return _carbs;
    }

    public void set_carbs(String carbs) {
        this._carbs = carbs;
    }

    public String get_fat() {
        return _fat;
    }

    public void set_fat(String fat) {
        this._fat = fat;
    }
}
