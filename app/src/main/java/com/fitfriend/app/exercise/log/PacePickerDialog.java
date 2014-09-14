package com.fitfriend.app.exercise.log;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.fitfriend.app.R;
import com.fitfriend.app.utils.DialogListener;

/**
* Created by Daniel on 4/19/2014.
*/
public class PacePickerDialog extends DialogFragment {


    DialogListener listener;

    public PacePickerDialog() {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.time_picker_ms, null);

        final NumberPicker minutePicker = (NumberPicker) view.findViewById(R.id.time_minute);
        final NumberPicker secondPicker = (NumberPicker) view.findViewById(R.id.time_second);

        minutePicker.setMaxValue(59);
        secondPicker.setMaxValue(59);

        minutePicker.setMinValue(0);
        secondPicker.setMinValue(0);

        builder.setView(view);
        builder.setTitle("Set Pace   (M:S)");

        builder.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bundle bundle = new Bundle();
                bundle.putInt("minute", minutePicker.getValue());
                bundle.putInt("second", secondPicker.getValue());
                listener.onDialogOK(bundle);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onDialogCancel();
            }
        });

        return builder.create();
    }

    public void setDialogListener(DialogListener listener) {
        this.listener = listener;
    }
}
