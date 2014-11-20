package com.cantwellcode.fitfriend.nutrition;

import java.io.Serializable;

/**
 * Created by Daniel on 2/7/14.
 */
public class Meal implements Serializable{

    private String name;
    private String date;
    private String type;
    private String calories;
    private String protein;
    private String carbs;
    private String fat;

    public Meal() {};

    /**
     * Constructor used for creating a meal
     * @param name - name of the meal
     * @param date - in simple date format DD MMMM YYYY
     * @param type - type of meal
     * @param calories - how many calories in the meal
     * @param protein - how much protein in the meal
     * @param carbs - how many carbs in the meal
     * @param fat - how much fat in the meal
     */
    public Meal(String name, String date, String type, String calories, String protein, String carbs, String fat) {
        this.name = name;
        this.date = date;
        this.type = type;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fat = fat;
    }

    /**
     * Constructor used for retrieving meals with a certain date
     * @param date
     */
    public Meal(String date) {
        this.date = date;
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

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getCarbs() {
        return carbs;
    }

    public void setCarbs(String carbs) {
        this.carbs = carbs;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }
}
