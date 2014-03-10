package com.cantwellcode.athletejournal;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
    private Button favorites;

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

        updateProfile   = (Button) root.findViewById(R.id.profileUpdate);
        favorites       = (Button) root.findViewById(R.id.profileFavorites);

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateGoals();
            }
        });

        favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.container, FavoritesViewFragment.newInstance(getActivity()))
                        .commit();
            }
        });

        restoreActionBar();

        return root;
    }

    private void updateGoals() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sp.edit().putString(GOAL_CALORIES, goalCalories.getText().toString()).commit();
        sp.edit().putString(GOAL_PROTEIN, goalProtein.getText().toString()).commit();
        sp.edit().putString(GOAL_CARBS, goalCarbs.getText().toString()).commit();
        sp.edit().putString(GOAL_FAT, goalFat.getText().toString()).commit();
        Toast.makeText(getActivity(), "Nutrition Goals Updated", Toast.LENGTH_SHORT).show();
    }

    private void restoreActionBar() {
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Profile");
    }
}
