package com.cantwellcode.fitfriend.app.exercise.types;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cantwellcode.fitfriend.app.R;

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
    public View getLayout(LayoutInflater inflater) {

        View view = inflater.inflate(R.layout.exercise_set, null);

        TextView pLabel = (TextView) view.findViewById(R.id.primaryLabel);
        TextView pData = (TextView) view.findViewById(R.id.primaryData);
        TextView pUnits = (TextView) view.findViewById(R.id.primaryUnits);
        TextView sLabel = (TextView) view.findViewById(R.id.secondaryLabel);
        TextView sData = (TextView) view.findViewById(R.id.secondaryData);
        TextView sUnits = (TextView) view.findViewById(R.id.secondaryUnits);

        pLabel.setText(values.get(0).getLabelDisplayString());
        pData.setText(values.get(0).getDataDisplayString());
        pUnits.setText(values.get(0).getUnitDisplayString());
        sLabel.setText(values.get(1).getLabelDisplayString());
        sData.setText(values.get(1).getDataDisplayString());
        sUnits.setText(values.get(1).getUnitDisplayString());

        return view;
    }

}
