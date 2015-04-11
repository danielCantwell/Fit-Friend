package com.cantwellcode.fitfriend.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cantwellcode.fitfriend.R;
import com.cantwellcode.fitfriend.exercise.log.CardioActivity;

import java.io.Serializable;

/**
 * Created by danielCantwell on 4/10/15.
 */
public class ConfirmationDialog extends DialogFragment {

    private TextView message;
    private Button noButton;
    private Button yesButton;

    public static final String TYPE_SAVE = "Save";
    public static final String TYPE_CANCEL = "Cancel";

    private String type;
    private String msg;

    private ConfirmationListener mListener;

    public static ConfirmationDialog newInstance(String t, String msg) {
        ConfirmationDialog d = new ConfirmationDialog();

        Bundle args = new Bundle();
        args.putString("type", t);;
        args.putString("message", msg);
        d.setArguments(args);

        return d;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        mListener = (ConfirmationListener) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View root = inflater.inflate(R.layout.dialog_confirmation, null);

        message = (TextView) root.findViewById(R.id.message);
        noButton = (Button) root.findViewById(R.id.no);
        yesButton = (Button) root.findViewById(R.id.yes);

        type = getArguments().getString("type");
        msg = getArguments().getString("message");

        message.setText(msg);

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equals(TYPE_SAVE)) {
                    save();
                } else if (type.equals(TYPE_CANCEL)) {
                    cancel();
                }
            }
        });

        builder.setView(root);
        return builder.create();
    }

    private void cancel() {
        dismiss();
        mListener.onCancel();
    }

    private void save() {
        dismiss();
        mListener.onSave();
    }
}
