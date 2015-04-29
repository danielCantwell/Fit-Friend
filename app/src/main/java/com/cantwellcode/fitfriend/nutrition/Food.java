package com.cantwellcode.fitfriend.nutrition;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;

/**
 * Created by danielCantwell on 4/28/15.
 * Copyright (c) Cantwell Code 2015. All Rights Reserved
 */
@ParseClassName("Food")
public class Food extends ParseObject {

    /* ********************* *
     *        Setters        *
     * ********************* */

    public void setUserToCurrent() {
        put("user", ParseUser.getCurrentUser());
    }

    public void setName(String name) {
        put("name", name);
    }

    public void setDate(Date date) {
        put("date", date);
    }

    public void setType(String type) {
        put("type", type);
    }

    public void setSize(String size) {
        put("size", size);
    }

    public void setBrand(String brand) {
        put("brand", brand);
    }

    public void setCalories(int calories) {
        put("calories", calories);
    }

    public void setFat(double fat) {
        put("fat", fat);
    }

    public void setCarbs(double carbs) {
        put("carbs", carbs);
    }

    public void setProtein(double protein) {
        put("protein", protein);
    }

    public void setSaturatedFat(double saturatedFat) {
        put("saturatedFat", saturatedFat);
    }

    public void setTransFat(double transFat) {
        put("transFat", transFat);
    }

    public void setFiber(double fiber) {
        put("fiber", fiber);
    }

    public void setSugar(double sugar) {
        put("sugar", sugar);
    }

    /* ********************* *
     *        Getters        *
     * ********************* */

    public ParseUser getUser() {
        return getParseUser("user");
    }

    public String getName() {
        return getString("name");
    }

    public Date getDate() {
        return getDate("date");
    }

    public String getType() {
        return getString("type");
    }

    public String getSize() {
        return getString("size");
    }

    public String getBrand() {
        return getString("brand");
    }

    public int getCalories() {
        return getInt("calories");
    }

    public double getFat() {
        return getDouble("fat");
    }

    public double getCarbs() {
        return getDouble("carbs");
    }

    public double getProtein() {
        return getDouble("protein");
    }

    public double getSaturatedFat() {
        return getDouble("saturatedFat");
    }

    public double getTransFat() {
        return getDouble("transFat");
    }

    public double getFiber() {
        return getDouble("fiber");
    }

    public double getSugar() {
        return getDouble("sugar");
    }

    /* ******************* *
     *        Query        *
     * ******************* */

    public static ParseQuery<Food> getQuery() {
        return ParseQuery.getQuery("Food");
    }
}
