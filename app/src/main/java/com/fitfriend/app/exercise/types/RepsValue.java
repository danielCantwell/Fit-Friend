package com.fitfriend.app.exercise.types;

/**
 * Created by Daniel on 6/27/2014.
 */
public class RepsValue implements Value {

    private int data;

    public RepsValue() {}

    public RepsValue(int data) {
        this.data = data;
    }

    @Override
    public void setData(String data) {
        this.data = Integer.valueOf(data);
    }

    @Override
    public String getDataDisplayString() {
        return Integer.toString(data);
    }

    @Override
    public String getUnitDisplayString() {
        return "reps";
    }

    @Override
    public String getLabelDisplayString() {
        return null;
    }
}
