package com.cantwellcode.athletejournal;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Daniel on 2/8/14.
 */
public class ProfileFragment extends Fragment {

    public static final String GOAL_CALORIES = "goalCalories";
    public static final String GOAL_PROTEIN  = "goalProtein";
    public static final String GOAL_CARBS    = "goalCarbs";
    public static final String GOAL_FAT      = "goalFat";

    private TextView goalCalories;
    private TextView goalProtein;
    private TextView goalCarbs;
    private TextView goalFat;

    private Button updateProfile;

    public static Fragment newInstance(Context context) {
        ProfileFragment f = new ProfileFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.profile_fragment, null);

        goalCalories = (TextView) root.findViewById(R.id.profileGoalCalories);
        goalProtein  = (TextView) root.findViewById(R.id.profileGoalProtein);
        goalCarbs    = (TextView) root.findViewById(R.id.profileGoalCarbs);
        goalFat      = (TextView) root.findViewById(R.id.profileGoalFat);

        updateProfile = (Button) root.findViewById(R.id.profileUpdate);

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateGoals();
            }
        });

        return root;
    }

    private void updateGoals() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sp.edit().putString(GOAL_CALORIES, goalCalories.getText().toString()).commit();
        sp.edit().putString(GOAL_PROTEIN, goalProtein.getText().toString()).commit();
        sp.edit().putString(GOAL_CARBS, goalCarbs.getText().toString()).commit();
        sp.edit().putString(GOAL_FAT, goalFat.getText().toString()).commit();
    }
}
