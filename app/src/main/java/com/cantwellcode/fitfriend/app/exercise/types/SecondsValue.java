package com.cantwellcode.fitfriend.app.exercise.types;

/**
 * Created by Daniel on 6/27/2014.
 */
public class SecondsValue implements Value {

    private final int data;

    public SecondsValue(int data) {
        this.data = data;
    }

    @Override
    public String getDataDisplayString() {
        return Integer.toString(data);
    }

    @Override
    public String getUnitDisplayString() {
        return "sec";
    }

    @Override
    public String getLabelDisplayString() {
        return null;
    }
}
