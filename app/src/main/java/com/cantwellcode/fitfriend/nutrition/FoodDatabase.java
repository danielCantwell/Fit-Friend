package com.cantwellcode.fitfriend.nutrition;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by Daniel on 11/8/2014.
 */
@ParseClassName("FoodDatabase")
public class FoodDatabase extends ParseObject {

    public String getName() {
        return getString("name");
    }

    public String getBrand() {
        return getString("brand");
    }

    public String getSize() {
        return getString("size");
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

    public static ParseQuery<FoodDatabase> getQuery() {
        return ParseQuery.getQuery("FoodDatabase");
    }
}
