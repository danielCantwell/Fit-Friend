package com.cantwellcode.athletejournal;

import android.app.ActionBar;
import android.content.Intent;
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
import android.widget.Toast;

import com.parse.ParseUser;

/**
 * Created by Daniel on 2/8/14.
 */
public class ProfilePersonal extends Fragment {

    public static final String GOAL_CALORIES = "goalCalories";
    public static final String GOAL_PROTEIN  = "goalProtein";
    public static final String GOAL_CARBS    = "goalCarbs";
    public static final String GOAL_FAT      = "goalFat";

    private EditText goalCalories;
    private EditText goalProtein;
    private EditText goalCarbs;
    private EditText goalFat;

    private EditText name;
    private EditText age;
    private EditText location;
    private EditText mainSport;
    private EditText email;

    private Button favorites;
    private Button logOut;

    SharedPreferences sp;

    ParseUser user;

    public static Fragment newInstance() {
        ProfilePersonal f = new ProfilePersonal();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.profile_personal, null);

        user = ParseUser.getCurrentUser();

        goalCalories = (EditText) root.findViewById(R.id.profileGoalCalories);
        goalProtein  = (EditText) root.findViewById(R.id.profileGoalProtein);
        goalCarbs    = (EditText) root.findViewById(R.id.profileGoalCarbs);
        goalFat      = (EditText) root.findViewById(R.id.profileGoalFat);

        name        = (EditText) root.findViewById(R.id.name);
        age         = (EditText) root.findViewById(R.id.age);
        location    = (EditText) root.findViewById(R.id.location);
        mainSport   = (EditText) root.findViewById(R.id.mainSport);
        email       = (EditText) root.findViewById(R.id.email);

        sp = PreferenceManager.getDefaultSharedPreferences(getActivity());

        goalCalories.setText(sp.getString(GOAL_CALORIES, ""));
        goalProtein.setText(sp.getString(GOAL_PROTEIN, ""));
        goalCarbs.setText(sp.getString(GOAL_CARBS, ""));
        goalFat.setText(sp.getString(GOAL_FAT, ""));

        favorites = (Button) root.findViewById(R.id.profileFavorites);
        logOut = (Button) root.findViewById(R.id.logOut);

        favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.container, NutritionFavoritesView.newInstance(getActivity()))
                        .commit();
            }
        });

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                Intent intent = new Intent(getActivity(), DispatchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);
            }
        });

        goalCalories.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sp.edit().putString(GOAL_CALORIES, goalCalories.getText().toString()).commit();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        goalFat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sp.edit().putString(GOAL_FAT, goalFat.getText().toString()).commit();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        goalCarbs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sp.edit().putString(GOAL_CARBS, goalCarbs.getText().toString()).commit();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        goalProtein.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sp.edit().putString(GOAL_PROTEIN, goalProtein.getText().toString()).commit();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if (user.containsKey("name")) {
            name.setText(user.getString("name"));
        }
        if (user.containsKey("age")) {
            age.setText(Integer.toString(user.getInt("age")));
        }
        if (user.containsKey("location")) {
            location.setText(user.getString("location"));
        }
        if (user.containsKey("mainSport")) {
            mainSport.setText(user.getString("mainSport"));
        }
        email.setText(user.getEmail());

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                user.put("name", s.toString());
                user.saveInBackground();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        age.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                user.put("age", Integer.parseInt(s.toString()));
                user.saveInBackground();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                user.put("location", s.toString());
                user.saveInBackground();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mainSport.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                user.put("mainSport", s.toString());
                user.saveInBackground();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                user.setEmail(s.toString());
                user.saveInBackground();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        restoreActionBar();

        return root;
    }

    private void restoreActionBar() {
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Profile");
    }
}
