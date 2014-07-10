package com.cantwellcode.fitfriend.app.exercise.types;

import android.widget.RelativeLayout;

import java.util.List;

/**
 * Created by Daniel on 6/23/2014.
 */
public interface Routine {

    public String getName();

    public List<Set> getSets();

    public RelativeLayout getLayout();

    public void addSet(Set set);
}
