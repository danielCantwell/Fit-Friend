package com.cantwellcode.fitfriend.exercise.log;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cantwellcode.fitfriend.R;

/**
 * Created by danielCantwell on 4/10/15.
 */
public class CardioConfirmationDialog extends DialogFragment {

    private TextView message;
    private Button noButton;
    private Button yesButton;

    public static final String TYPE_SAVE = "Save Cardio";
    public static final String TYPE_CANCEL = "Cancel Cardio";

    private String type;

    static CardioConfirmationDialog newInstance(String t) {
        CardioConfirmationDialog d = new CardioConfirmationDialog();

        Bundle args = new Bundle();
        args.putString("type", t);
        d.setArguments(args);

        return d;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View root = inflater.inflate(R.layout.dialog_cardio_confirmation, null);

        message = (TextView) root.findViewById(R.id.message);
        noButton = (Button) root.findViewById(R.id.no);
        yesButton = (Button) root.findViewById(R.id.yes);

        type = getArguments().getString("type");

        if (type == TYPE_SAVE) {
            message.setText("Save Run?");
        } else if (type == TYPE_CANCEL) {
            message.setText("Cancel Run?");
        }

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == TYPE_SAVE) {
                    saveCardio();
                } else if (type == TYPE_CANCEL) {
                   cancelCardio();
                }
            }
        });

        builder.setView(root);
        return builder.create();
    }

    private void cancelCardio() {
        dismiss();
        ((CardioActivity)getActivity()).cancelCardio();
    }

    private void saveCardio() {
        dismiss();
        ((CardioActivity)getActivity()).saveCardio();
    }
}
