package com.cantwellcode.fitfriend.exercise.log;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.cantwellcode.fitfriend.exercise.types.Exercise;
import com.cantwellcode.fitfriend.utils.Statics;
import com.fitfriend.app.R;
import com.parse.ParseException;
import com.parse.SaveCallback;

public class CreateExerciseActivity extends Activity {

    private EditText mName;
    private CheckBox mWeight;
    private CheckBox mReps;
    private CheckBox mTime;
    private CheckBox mArms;
    private CheckBox mShoulders;
    private CheckBox mChest;
    private CheckBox mBack;
    private CheckBox mAbs;
    private CheckBox mLegs;
    private CheckBox mGlutes;
    private Button mSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_exercise);

        mName = (EditText) findViewById(R.id.name);
        mWeight = (CheckBox) findViewById(R.id.checkWeight);
        mReps = (CheckBox) findViewById(R.id.checkReps);
        mTime = (CheckBox) findViewById(R.id.checkTime);
        mArms = (CheckBox) findViewById(R.id.checkArms);
        mShoulders = (CheckBox) findViewById(R.id.checkShoulders);
        mChest = (CheckBox) findViewById(R.id.checkChest);
        mBack = (CheckBox) findViewById(R.id.checkBack);
        mAbs = (CheckBox) findViewById(R.id.checkAbs);
        mLegs = (CheckBox) findViewById(R.id.checkLegs);
        mGlutes = (CheckBox) findViewById(R.id.checkGlutes);
        mSave = (Button) findViewById(R.id.save);

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mName.getText().toString().trim().isEmpty()) {
                    Toast.makeText(CreateExerciseActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                } else {
                    Exercise e = new Exercise();
                    e.setName(mName.getText().toString().trim());

                    e.weight(mWeight.isChecked() ? true : false);
                    e.reps(mReps.isChecked() ? true : false);
                    e.time(mTime.isChecked() ? true : false);

                    e.setArms(mArms.isChecked() ? true : false);
                    e.setShoulders(mShoulders.isChecked() ? true : false);
                    e.setChest(mChest.isChecked() ? true : false);
                    e.setBack(mBack.isChecked() ? true : false);
                    e.setAbs(mAbs.isChecked() ? true : false);
                    e.setLegs(mLegs.isChecked() ? true : false);
                    e.setGlutes(mGlutes.isChecked() ? true : false);

                    e.pinInBackground(Statics.SAVED_EXERCISES, new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Intent intent = new Intent(CreateExerciseActivity.this, NewExerciseActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Create New Exercise");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
