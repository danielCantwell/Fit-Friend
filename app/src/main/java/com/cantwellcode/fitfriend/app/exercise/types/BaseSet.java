package com.cantwellcode.fitfriend.app.exercise.types;

import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by Daniel on 6/23/2014.
 */
public class BaseSet implements Set {

    private final List<Value> values;

    public BaseSet(List<Value> values) {
        this.values = values;
    }

    @Override
    public String getLabel() {
        return null;
    }

    @Override
    public List<Value> getValues() {
        return values;
    }

    @Override
    public LinearLayout getLayout() {
        return null;
    }

}
