package com.cantwellcode.athletejournal;

/**
 * Created by Daniel on 2/7/14.
 */
public class Favorite {

    int _id;
    String _name;
    String _category;
    String _type;
    String _calories;
    String _protein;
    String _carbs;
    String _fat;

    public Favorite() {
    }

    public Favorite(int id, String name, String category, String type, String calories, String protein, String carbs, String fat) {
        this._id = id;
        this._name = name;
        this._category = category;
        this._type = type;
        this._calories = calories;
        this._protein = protein;
        this._carbs = carbs;
        this._fat = fat;
    }

    public Favorite(String name, String category, String type, String calories, String protein, String carbs, String fat) {
        this._name = name;
        this._category = category;
        this._type = type;
        this._calories = calories;
        this._protein = protein;
        this._carbs = carbs;
        this._fat = fat;
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

    public String get_category() {
        return _category;
    }

    public void set_date(String category) {
        this._category = category;
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
