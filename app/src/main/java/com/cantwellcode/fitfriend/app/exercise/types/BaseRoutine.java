package com.cantwellcode.fitfriend.app.exercise.types;

import android.widget.RelativeLayout;

import java.util.List;

/**
 * Created by Daniel on 6/27/2014.
 */
public class BaseRoutine implements Routine {

    private final String name;
    private final List<Set> sets;

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
}
