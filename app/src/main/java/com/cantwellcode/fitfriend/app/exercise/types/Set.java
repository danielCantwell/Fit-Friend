package com.cantwellcode.fitfriend.app.exercise.types;

import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by Daniel on 6/23/2014.
 */
public interface Set {

    public String getLabel();

    public List<Value> getValues();

    public LinearLayout getLayout();
}
