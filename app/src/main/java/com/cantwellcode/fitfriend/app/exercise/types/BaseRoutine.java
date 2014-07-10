package com.cantwellcode.fitfriend.app.exercise.types;

import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 6/27/2014.
 */
public class BaseRoutine implements Routine {

    private String name;
    private List<Set> sets;

    public BaseRoutine() {
        name = "Routine Name";
        sets = new ArrayList<Set>();
    }

    public BaseRoutine(String name, List<Set> sets) {
        this.name = name;
        this.sets = sets;
    }

    @Override
    public String getName() {
        return name;
    }


    @Override
    public List<Set> getSets() {
        return sets;
    }

    @Override
    public RelativeLayout getLayout() {
        return null;
    }

    @Override
    public void addSet(Set set) {
        sets.add(set);
    }
}
