package com.fitfriend.app.exercise.types;

/**
 * Created by Daniel on 6/27/2014.
 */
public class DistanceValue implements Value {

    private final float data;
    private final String units;

    public DistanceValue(int data, String units) {
        this.data = data;
        this.units = units;
    }

    @Override
    public void setData(String data) {

    }

    @Override
    public String getDataDisplayString() {
        return Float.toString(data);
    }

    @Override
    public String getUnitDisplayString() {
        return units;
    }

    @Override
    public String getLabelDisplayString() {
        return null;
    }
}
