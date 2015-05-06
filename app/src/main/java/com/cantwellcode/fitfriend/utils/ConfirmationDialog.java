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

    private String msg;

    private ConfirmationListener mListener;

    public static ConfirmationDialog newInstance(String msg) {
        ConfirmationDialog d = new ConfirmationDialog();

        Bundle args = new Bundle();
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

        msg = getArguments().getString("message");

        message.setText(msg);

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                mListener.onNo(msg);
            }
        });

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                mListener.onYes(msg);
            }
        });

        builder.setView(root);
        return builder.create();
    }
}
