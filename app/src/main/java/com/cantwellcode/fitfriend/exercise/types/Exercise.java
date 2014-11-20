package com.cantwellcode.fitfriend.exercise.types;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by Daniel on 10/27/2014.
 */
@ParseClassName("Exercise")
public class Exercise extends ParseObject {

    public void setName(String name) {
        put("name", name);
    }

    public String getName() {
        return getString("name");
    }

    public void setSets(List<Set> sets) {
        put("sets", sets);
    }

    public void addSet(Set set) {
        add("sets", set);
    }

    public List<Set> getSets() {
        return getList("sets");
    }

    public static ParseQuery<Exercise> getQuery() {
        return ParseQuery.getQuery("Exercise");
    }
}
