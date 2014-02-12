package com.cantwellcode.athletejournal;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.ViewFlipper;

/**
 * Created by Daniel on 2/8/14.
 */
public class WorkoutFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    Spinner type;
    ViewFlipper viewFlipper;

    public static Fragment newInstance(Context context) {
        WorkoutFragment f = new WorkoutFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.workout_fragment, null);

        viewFlipper = (ViewFlipper) root.findViewById(R.id.viewFlipper);
        type = (Spinner)  root.findViewById(R.id.w_type);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.workout_types, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        type.setAdapter(adapter);
        type.setOnItemSelectedListener(this);
        type.setSelection(2);

        return root;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        viewFlipper.setDisplayedChild(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
