package com.cantwellcode.fitfriend.app.exercise.types;

/**
 * Created by Daniel on 8/28/2014.
 */
public class SimpleFloatValue implements Value {

    private float data;
    private String unit;
    private String label;

    public SimpleFloatValue() {
    }

    public SimpleFloatValue(float data, String unit, String label) {
        this.data = data;
        this.unit = unit;
        this.label = label;
    }

    @Override
    public void setData(String data) {
        this.data = Float.valueOf(data);
    }

    @Override
    public String getDataDisplayString() {
        return Float.toString(data);
    }

    @Override
    public String getUnitDisplayString() {
        return unit;
    }

    @Override
    public String getLabelDisplayString() {
        return label;
    }
}
