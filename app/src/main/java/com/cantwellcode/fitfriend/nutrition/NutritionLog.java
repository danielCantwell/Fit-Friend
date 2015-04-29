package com.cantwellcode.fitfriend.nutrition;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.cantwellcode.cantwellgraphs.PieChart;
import com.cantwellcode.cantwellgraphs.PieLabelType;
import com.cantwellcode.cantwellgraphs.PieSection;
import com.cantwellcode.fitfriend.R;
import com.cantwellcode.fitfriend.utils.DatePickerFragment;
import com.cantwellcode.fitfriend.utils.DialogListener;
import com.cantwellcode.fitfriend.utils.SmallDecimalTextView;
import com.cantwellcode.fitfriend.utils.Statics;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Daniel on 2/12/14.
 */
public class NutritionLog extends Fragment {

    private static Fragment instance = null;

    public static Fragment newInstance() {
        if (instance == null)
            instance = new NutritionLog();
        return instance;
    }

    private Activity activity;

    private ExpandableListView mList;
    private List<Food> mFoods;
    private NutritionExpandableListAdapter mAdapter;
    private List<Food> mListHeaders;
    private HashMap<Food, List<Food>> mListData;

    private ViewSwitcher viewSwitcher;
    private ImageButton date;
    private ImageButton switchView;
    private Animation slideUp, slideDown;

    private SmallDecimalTextView totalCalories;
    private SmallDecimalTextView totalProtein;
    private SmallDecimalTextView totalCarbs;
    private SmallDecimalTextView totalFat;

    private TextView goalCalories;
    private TextView goalFat;
    private TextView goalCarbs;
    private TextView goalProtein;

    Calendar c;
    int year;
    int month;
    int day;

    private PieChart mCaloriesChart;
    private PieChart mMacrosChart;

    private ProgressBar mProgressCalories;
    private ProgressBar mProgressFat;
    private ProgressBar mProgressCarbs;
    private ProgressBar mProgressProtein;

    private boolean isAthlete;

    private boolean currentViewIsChart;

    private ViewGroup root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.fragment_nutrition_log, null);

        currentViewIsChart = true;

        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        mList = (ExpandableListView) root.findViewById(R.id.list);
        updateList();

        mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                long packedPosition = mList.getExpandableListPosition(position);

                int itemType = ExpandableListView.getPackedPositionType(packedPosition);
                int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
                int childPosition = ExpandableListView.getPackedPositionChild(packedPosition);

                if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    Food food = (Food) mAdapter.getChild(groupPosition, childPosition);
                    showPopup(view, food);

                    return true;
                }
                return false;
            }
        });

        setHasOptionsMenu(true);

        viewSwitcher = (ViewSwitcher) root.findViewById(R.id.viewSwitcher);
        date = (ImageButton) root.findViewById(R.id.date);
        switchView = (ImageButton) root.findViewById(R.id.switchView);

        slideUp = AnimationUtils.loadAnimation(getActivity(), R.anim.abc_slide_in_bottom);
        slideDown = AnimationUtils.loadAnimation(getActivity(), R.anim.abc_slide_out_bottom);

        viewSwitcher.setInAnimation(slideUp);
        viewSwitcher.setOutAnimation(slideDown);

        switchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewSwitcher.showNext();
                if (currentViewIsChart) {
                    currentViewIsChart = false;
                    getActivity().getActionBar().setTitle("Nutrition Log");
                } else {
                    currentViewIsChart = true;
                    getActivity().getActionBar().setTitle("Calorie Breakdown");
                }
            }
        });

        isAthlete = ParseUser.getCurrentUser().getBoolean("athlete");

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAthlete) {
                    DatePickerFragment dateFragment = new DatePickerFragment();
                    dateFragment.setDialogListener(new DialogListener() {
                        @Override
                        public void onDialogOK(Bundle bundle) {
                            year = bundle.getInt("year");
                            month = bundle.getInt("month");
                            day = bundle.getInt("day");

                            updateList();
                        }

                        @Override
                        public void onDialogCancel() {

                        }
                    });
                    dateFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
                } else {
                    Toast.makeText(getActivity(), "Upgrade to 'Athlete' to view other days", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /* Widget Initialization */

        mCaloriesChart = (PieChart) root.findViewById(R.id.chart_calories);
        mMacrosChart = (PieChart) root.findViewById(R.id.chart_macros);

        totalCalories = (SmallDecimalTextView) root.findViewById(R.id.n_view_total_calories);
        totalProtein = (SmallDecimalTextView) root.findViewById(R.id.n_view_total_protein);
        totalCarbs = (SmallDecimalTextView) root.findViewById(R.id.n_view_total_carbs);
        totalFat = (SmallDecimalTextView) root.findViewById(R.id.n_view_total_fat);

        goalCalories = (TextView) root.findViewById(R.id.n_goal_calories);
        goalFat = (TextView) root.findViewById(R.id.n_goal_fat);
        goalCarbs = (TextView) root.findViewById(R.id.n_goal_carbs);
        goalProtein = (TextView) root.findViewById(R.id.n_goal_protein);

        mProgressCalories = (ProgressBar) root.findViewById(R.id.caloriesProgress);
        mProgressFat = (ProgressBar) root.findViewById(R.id.fatProgress);
        mProgressCarbs = (ProgressBar) root.findViewById(R.id.carbsProgress);
        mProgressProtein = (ProgressBar) root.findViewById(R.id.proteinProgress);

        /* Set the macros text values */
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
        goalCalories.setText(sp.getString(Statics.GOAL_CALORIES, "3000"));
        goalProtein.setText(sp.getString(Statics.GOAL_PROTEIN, "150"));
        goalCarbs.setText(sp.getString(Statics.GOAL_CARBS, "300"));
        goalFat.setText(sp.getString(Statics.GOAL_FAT, "70"));

        /* Set the calories progress bar */
        String calories = sp.getString(Statics.GOAL_CALORIES, "3000");
        int calGoal = 3000;
        if (!calories.trim().isEmpty())
            calGoal = Integer.valueOf(calories);
        mProgressCalories.setMax(calGoal);

        /* Set the fat progress bar */
        String fat = sp.getString(Statics.GOAL_FAT, "70");
        int fatGoal = 70;
        if (!fat.trim().isEmpty())
            fatGoal = Integer.valueOf(fat);
        mProgressFat.setMax(fatGoal);

        /* Set the carbs progress bar */
        String carbs = sp.getString(Statics.GOAL_CARBS, "300");
        int carbGoal = 300;
        if (!carbs.trim().isEmpty())
            carbGoal = Integer.valueOf(carbs);
        mProgressCarbs.setMax(carbGoal);

        /* Set the protein progress bar */
        String protein = sp.getString(Statics.GOAL_PROTEIN, "150");
        int protGoal = 150;
        if (!protein.trim().isEmpty())
            protGoal = Integer.valueOf(protein);
        mProgressProtein.setMax(protGoal);

//        updateTotals();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
        goalCalories.setText(sp.getString(Statics.GOAL_CALORIES, "3000"));
        goalFat.setText(sp.getString(Statics.GOAL_FAT, "70"));
        goalCarbs.setText(sp.getString(Statics.GOAL_CARBS, "300"));
        goalProtein.setText(sp.getString(Statics.GOAL_PROTEIN, "150"));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        restoreActionBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new:
                Intent intent = new Intent(getActivity(), NewFoodActivity.class);
                startActivityForResult(intent, Statics.INTENT_REQUEST_MEAL);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void restoreActionBar() {
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Calorie Breakdown");
    }

    private void prepareListData() {
        mListHeaders = new ArrayList<Food>();
        mListData = new HashMap<Food, List<Food>>();

        float breakfastProtein = 0;
        float breakfastCarbs = 0;
        float breakfastFat = 0;

        float lunchProtein = 0;
        float lunchCarbs = 0;
        float lunchFat = 0;

        float dinnerProtein = 0;
        float dinnerCarbs = 0;
        float dinnerFat = 0;

        float snackProtein = 0;
        float snackCarbs = 0;
        float snackFat = 0;

        int breakfastCals = 0;
        int lunchCals = 0;
        int dinnerCals = 0;
        int snackCals = 0;

        for (Food f : mFoods) {
            if (f.getType().equals("Breakfast")) {
                breakfastCals += f.getCalories();
                breakfastFat += f.getFat();
                breakfastCarbs += f.getCarbs();
                breakfastProtein += f.getProtein();
            } else if (f.getType().equals("Lunch")) {
                lunchCals += f.getCalories();
                lunchFat += f.getFat();
                lunchCarbs += f.getCarbs();
                lunchProtein += f.getProtein();
            } else if (f.getType().equals("Dinner")) {
                dinnerCals += f.getCalories();
                dinnerFat += f.getFat();
                dinnerCarbs += f.getCarbs();
                dinnerProtein += f.getProtein();
            } else if (f.getType().equals("Snack")) {
                snackCals += f.getCalories();
                snackFat += f.getFat();
                snackCarbs += f.getCarbs();
                snackProtein += f.getProtein();
            }
        }

        Food breakfast = new Food();
        breakfast.setType("Breakfast");
        breakfast.setCalories(breakfastCals);
        breakfast.setFat(breakfastFat);
        breakfast.setCarbs(breakfastCarbs);
        breakfast.setProtein(breakfastProtein);

        Food lunch = new Food();
        lunch.setType("Lunch");
        lunch.setCalories(lunchCals);
        lunch.setFat(lunchFat);
        lunch.setCarbs(lunchCarbs);
        lunch.setProtein(lunchProtein);

        Food dinner = new Food();
        dinner.setType("Dinner");
        dinner.setCalories(dinnerCals);
        dinner.setFat(dinnerFat);
        dinner.setCarbs(dinnerCarbs);
        dinner.setProtein(dinnerProtein);

        Food snack = new Food();
        snack.setType("Snack");
        snack.setCalories(snackCals);
        snack.setFat(snackFat);
        snack.setCarbs(snackCarbs);
        snack.setProtein(snackProtein);


        // If user selects "meal type" sort type
        mListHeaders.add(breakfast);
        mListHeaders.add(lunch);
        mListHeaders.add(dinner);
        mListHeaders.add(snack);

        for (Food header : mListHeaders) {
            List<Food> foodInType = new ArrayList<Food>();
            for (Food food : mFoods) {
                if (food.getType().equals(header.getType())) {
                    foodInType.add(food);
                }
            }
            mListData.put(header, foodInType);
        }
    }

    private void updateTotals() {
        int calories = 0;
        double protein = 0;
        double carbs = 0;
        double fat = 0;

        int breakfastCals = 0;
        double lunchCals = 0;
        double dinnerCals = 0;
        double snackCals = 0;

        for (Food f : mFoods) {
            calories += f.getCalories();
            fat += f.getFat();
            carbs += f.getCarbs();
            protein += f.getProtein();

            if (f.getType().equals("Breakfast")) {
                breakfastCals += f.getCalories();
            } else if (f.getType().equals("Lunch")) {
                lunchCals += f.getCalories();
            } else if (f.getType().equals("Dinner")) {
                dinnerCals += f.getCalories();
            } else if (f.getType().equals("Snack")) {
                snackCals += f.getCalories();
            }
        }

        totalCalories.setText(String.format("%d", calories));
        totalFat.setText(String.format("%.1f", fat));
        totalCarbs.setText(String.format("%.1f", carbs));
        totalProtein.setText(String.format("%.1f", protein));

        /* Setup the pie charts */

        mCaloriesChart.setEmptyColor(Color.GRAY);
        mMacrosChart.setEmptyColor(Color.GRAY);

        // "breakfast" section of the pie chart
        PieSection pieBreakfast = new PieSection("Breakfast", breakfastCals);
        pieBreakfast.setFillColor(getResources().getColor(R.color.chart_blue));
        pieBreakfast.setLabelSize(35);
        pieBreakfast.setStrokeWidth(1);
        pieBreakfast.setStrokeColor(Color.BLACK);
        pieBreakfast.setLabelType(PieLabelType.PERCENTAGE_then_NAME);

        // "lunch" section of the pie chart
        PieSection pieLunch = new PieSection("Lunch", (float) lunchCals);
        pieLunch.setFillColor(getResources().getColor(R.color.chart_orange));
        pieLunch.setLabelSize(35);
        pieLunch.setStrokeWidth(1);
        pieLunch.setStrokeColor(Color.BLACK);
        pieLunch.setLabelType(PieLabelType.PERCENTAGE_then_NAME);

        // "dinner" section of the pie chart
        PieSection pieDinner = new PieSection("Dinner", (float) dinnerCals);
        pieDinner.setFillColor(getResources().getColor(R.color.chart_green));
        pieDinner.setLabelSize(35);
        pieDinner.setStrokeWidth(1);
        pieDinner.setStrokeColor(Color.BLACK);
        pieDinner.setLabelType(PieLabelType.PERCENTAGE_then_NAME);

        // "snack" section of the pie chart
        PieSection pieSnack = new PieSection("Snack", (float) snackCals);
        pieSnack.setFillColor(getResources().getColor(R.color.chart_pink));
        pieSnack.setLabelSize(35);
        pieSnack.setStrokeWidth(1);
        pieSnack.setStrokeColor(Color.BLACK);
        pieSnack.setLabelType(PieLabelType.PERCENTAGE_then_NAME);

        List<PieSection> pieCalorieSections = new ArrayList<PieSection>();
        if (breakfastCals > 0)
            pieCalorieSections.add(pieBreakfast);
        if (lunchCals > 0)
            pieCalorieSections.add(pieLunch);
        if (dinnerCals > 0)
            pieCalorieSections.add(pieDinner);
        if (snackCals > 0)
            pieCalorieSections.add(pieSnack);

        mCaloriesChart.clearChart();
        mCaloriesChart.setPieItems(pieCalorieSections);
        mCaloriesChart.drawChart();

        // "fat" section of the pie chart
        PieSection pieFat = new PieSection("Fat", (float) fat * 9);
        pieFat.setFillColor(getResources().getColor(R.color.chart_red));
        pieFat.setLabelSize(35);
        pieFat.setStrokeWidth(1);
        pieFat.setStrokeColor(Color.BLACK);
        pieFat.setLabelType(PieLabelType.PERCENTAGE_then_NAME);

        // "carbs" section of the pie chart
        PieSection pieCarbs = new PieSection("Carbs", (float) carbs * 4);
        pieCarbs.setFillColor(getResources().getColor(R.color.chart_yellow));
        pieCarbs.setLabelSize(35);
        pieCarbs.setStrokeWidth(1);
        pieCarbs.setStrokeColor(Color.BLACK);
        pieCarbs.setLabelType(PieLabelType.PERCENTAGE_then_NAME);

        // "protein" section of the pie chart
        PieSection pieProtein = new PieSection("Protein", (float) protein * 4);
        pieProtein.setFillColor(getResources().getColor(R.color.chart_purple));
        pieProtein.setLabelSize(35);
        pieProtein.setStrokeWidth(1);
        pieProtein.setStrokeColor(Color.BLACK);
        pieProtein.setLabelType(PieLabelType.PERCENTAGE_then_NAME);

        List<PieSection> pieMacrosSections = new ArrayList<PieSection>();
        if (fat > 0)
            pieMacrosSections.add(pieFat);
        if (carbs > 0)
            pieMacrosSections.add(pieCarbs);
        if (protein > 0)
            pieMacrosSections.add(pieProtein);

        mMacrosChart.clearChart();
        mMacrosChart.setPieItems(pieMacrosSections);
        mMacrosChart.drawChart();

        mProgressCalories.setProgress(calories);
        mProgressFat.setProgress((int) fat);
        mProgressCarbs.setProgress((int) carbs);
        mProgressProtein.setProgress((int) protein);
    }

    private void showPopup(View v, final Food food) {
        PopupMenu popup = new PopupMenu(activity, v);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.n_action_addToFavorites:
                        menuClickAddToFavorites(food);
                        return true;
                    case R.id.n_action_edit:
                        menuClickEdit(food);
                        return true;
                    case R.id.n_action_delete:
                        menuClickDelete(food);
                        return true;
                    default:
                        return false;
                }
            }
        });

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.n_list_click, popup.getMenu());
        popup.show();
    }

    private void menuClickAddToFavorites(Food food) {
        food.pinInBackground(Statics.PIN_NUTRITION_FAVORITES);
    }

    private void menuClickEdit(Food food) {
//        Intent intent = new Intent(getActivity(), CustomFoodFragment.class);
//        intent.putExtra("Edit", meal);
//        startActivityForResult(intent, Statics.INTENT_REQUEST_MEAL);
        Toast.makeText(getActivity(), "Edit is currently not enabled", Toast.LENGTH_SHORT).show();
    }

    private void menuClickDelete(Food food) {

        mFoods.remove(food);
        food.unpinInBackground(Statics.PIN_NUTRITION_LOG);
        food.deleteInBackground();

        prepareListData();
        mAdapter = new NutritionExpandableListAdapter(activity, mListHeaders, mListData);
        mList.setAdapter(mAdapter);
        updateTotals();

    }

    private void updateList() {
        Calendar midnight = Calendar.getInstance();
        midnight.set(Calendar.YEAR, year);
        midnight.set(Calendar.MONTH, month);
        midnight.set(Calendar.DAY_OF_MONTH, day);
        midnight.set(Calendar.HOUR_OF_DAY, 0);
        midnight.set(Calendar.MINUTE, 0);
        midnight.set(Calendar.SECOND, 0);

        Calendar elevenFiftyNine = Calendar.getInstance();
        elevenFiftyNine.set(Calendar.YEAR, year);
        elevenFiftyNine.set(Calendar.MONTH, month);
        elevenFiftyNine.set(Calendar.DAY_OF_MONTH, day);
        elevenFiftyNine.set(Calendar.HOUR_OF_DAY, 23);
        elevenFiftyNine.set(Calendar.MINUTE, 59);
        elevenFiftyNine.set(Calendar.SECOND, 59);

        Date dayStart = new Date(midnight.getTimeInMillis());
        Date dayEnd = new Date(elevenFiftyNine.getTimeInMillis());

        ParseQuery<Food> foodQuery = Food.getQuery();
        foodQuery.fromPin(Statics.PIN_NUTRITION_LOG);
        foodQuery.whereGreaterThan("date", dayStart);
        foodQuery.whereLessThan("date", dayEnd);
        foodQuery.findInBackground(new FindCallback<Food>() {
            @Override
            public void done(List<Food> foods, ParseException e) {
                mFoods = foods;

                prepareListData();
                mAdapter = new NutritionExpandableListAdapter(activity, mListHeaders, mListData);
                mList.setAdapter(mAdapter);
                updateTotals();
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Statics.INTENT_REQUEST_MEAL) {
            updateList();
        }
    }
}
