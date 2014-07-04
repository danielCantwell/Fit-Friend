package com.cantwellcode.fitfriend.app.exercise.types;

/**
 * Created by Daniel on 6/27/2014.
 */
public class MinutesSecondsValue implements Value {

    private final int data;

    public MinutesSecondsValue(int data) {
        this.data = data;
    }

    @Override
    public String getDataDisplayString() {
        int minutes = data / 60;
        int seconds = data % 60;
        return Integer.toString(minutes) + ":" + Integer.toString(seconds);
    }

    @Override
    public String getUnitDisplayString() {
        return "m:s";
    }

    @Override
    public String getLabelDisplayString() {
        return null;
    }
}
