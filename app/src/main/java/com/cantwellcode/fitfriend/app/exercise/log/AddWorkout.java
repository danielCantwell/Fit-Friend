package com.cantwellcode.fitfriend.app.exercise.log;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.cantwellcode.fitfriend.app.R;
import com.cantwellcode.fitfriend.app.utils.DBHelper;

/**
 * Created by Daniel on 6/27/2014.
 */
public class AddWorkout extends FragmentActivity implements View.OnClickListener{

    private AutoCompleteTextView mNameText;
    private Spinner mTypeSpinner;
    private Button mDateButton;
    private Spinner mIntensitySpinner;
    private Button mNewRoutineButton;

    private LinearLayout mLayout;

    private DBHelper mDatabase;

    private DialogFragment mDateDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_new);
        mLayout = (LinearLayout) findViewById(R.id.linearLayout);

        mNewRoutineButton = (Button) findViewById(R.id.newRoutine);

        mNewRoutineButton.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save, menu);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("New Workout");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.newRoutine:
                RelativeLayout relativeLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.add_routine, null);

                Button copySet = (Button) relativeLayout.findViewById(R.id.copySet);
                Button newSet = (Button) relativeLayout.findViewById(R.id.newSet);

                copySet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(AddWorkout.this, "Copy", Toast.LENGTH_SHORT).show();
                    }
                });

                newSet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new SetDialog().show(getSupportFragmentManager(), null);
                    }
                });

                mLayout.addView(relativeLayout);
                break;
        }
    }

    private class SetDialog extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.set_dialog, null))
                    // Add action buttons
                    .setPositiveButton(R.string.action_save, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // sign in the user ...
                        }
                    })
                    .setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SetDialog.this.getDialog().cancel();
                        }
                    });
            return builder.create();
        }
    }
}
