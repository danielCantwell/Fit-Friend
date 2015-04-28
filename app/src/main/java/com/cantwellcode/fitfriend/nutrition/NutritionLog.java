package com.cantwellcode.fitfriend.nutrition;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.cantwellcode.cantwellgraphs.PieChart;
import com.cantwellcode.cantwellgraphs.PieLabelType;
import com.cantwellcode.cantwellgraphs.PieSection;
import com.cantwellcode.fitfriend.R;
import com.cantwellcode.fitfriend.utils.DBHelper;
import com.cantwellcode.fitfriend.utils.DatePickerFragment;
import com.cantwellcode.fitfriend.utils.DialogListener;
import com.cantwellcode.fitfriend.utils.SmallDecimalTextView;
import com.cantwellcode.fitfriend.utils.Statics;
import com.parse.ParseUser;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    // SQLite Database
    private DBHelper db;

    private ExpandableListView list;
    private List<Meal> meals;
    private NutritionExpandableListAdapter mAdapter;
    private List<Meal> listHeaders;
    private HashMap<Meal, List<Meal>> listData;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_nutrition_log, null);

        currentViewIsChart = true;

        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);

        db = new DBHelper(getActivity());

        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        String formattedDate = df.format(c.getTime());
        meals = db.getMealList(new Meal(formattedDate));

        prepareListData();
        list = (ExpandableListView) root.findViewById(R.id.list);
        mAdapter = new NutritionExpandableListAdapter(activity, listHeaders, listData);
        list.setAdapter(mAdapter);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                long packedPosition = list.getExpandableListPosition(position);

                int itemType = ExpandableListView.getPackedPositionType(packedPosition);
                int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
                int childPosition = ExpandableListView.getPackedPositionChild(packedPosition);

                if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    Meal meal = (Meal) mAdapter.getChild(groupPosition, childPosition);
                    showPopup(view, meal);

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

                            c.set(Calendar.YEAR, year);
                            c.set(Calendar.MONTH, month);
                            c.set(Calendar.DAY_OF_MONTH, day);

                            SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
                            String formattedDate = df.format(c.getTime());
                            meals = db.getMealList(new Meal(formattedDate));

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

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
        goalCalories.setText(sp.getString(Statics.GOAL_CALORIES, "3000 cal"));
        goalProtein.setText(sp.getString(Statics.GOAL_PROTEIN, "150 prot"));
        goalCarbs.setText(sp.getString(Statics.GOAL_CARBS, "300 carbs"));
        goalFat.setText(sp.getString(Statics.GOAL_FAT, "70 fat"));

        String calories = sp.getString(Statics.GOAL_CALORIES, "3000");
        int calGoal = 3000;
        if (!calories.trim().isEmpty()) {
            calGoal = Integer.valueOf(calGoal);
        }
        mProgressCalories = (ProgressBar) root.findViewById(R.id.caloriesProgress);
        mProgressCalories.setMax(calGoal);

        String fat = sp.getString(Statics.GOAL_FAT, "70");
        int fatGoal = 70;
        if (!fat.trim().isEmpty()) {
            fatGoal = Integer.valueOf(fat);
        }
        mProgressFat = (ProgressBar) root.findViewById(R.id.fatProgress);
        mProgressFat.setMax(fatGoal);

        String carbs = sp.getString(Statics.GOAL_CARBS, "300");
        int carbGoal = 300;
        if (!carbs.trim().isEmpty()) {
            carbGoal = Integer.valueOf(carbs);
        }
        mProgressCarbs = (ProgressBar) root.findViewById(R.id.carbsProgress);
        mProgressCarbs.setMax(carbGoal);

        String protein = sp.getString(Statics.GOAL_PROTEIN, "150");
        int protGoal = 150;
        if (!protein.trim().isEmpty()) {
            protGoal = Integer.valueOf(protein);
        }
        mProgressProtein = (ProgressBar) root.findViewById(R.id.proteinProgress);
        mProgressProtein.setMax(protGoal);

        updateTotals();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
        goalCalories.setText(sp.getString(Statics.GOAL_CALORIES, "goal"));
        goalFat.setText(sp.getString(Statics.GOAL_FAT, "goal"));
        goalCarbs.setText(sp.getString(Statics.GOAL_CARBS, "goal"));
        goalProtein.setText(sp.getString(Statics.GOAL_PROTEIN, "goal"));
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
        listHeaders = new ArrayList<Meal>();
        listData = new HashMap<Meal, List<Meal>>();

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

        float breakfastCals = 0;
        float lunchCals = 0;
        float dinnerCals = 0;
        float snackCals = 0;

        for (Meal m : meals) {
            if (m.getType().equals("Breakfast")) {
                breakfastCals += Float.valueOf(m.getCalories());
                breakfastFat += Float.valueOf(m.getFat());
                breakfastCarbs += Float.valueOf(m.getCarbs());
                breakfastProtein += Float.valueOf(m.getProtein());
            } else if (m.getType().equals("Lunch")) {
                lunchCals += Float.valueOf(m.getCalories());
                lunchFat += Float.valueOf(m.getFat());
                lunchCarbs += Float.valueOf(m.getCarbs());
                lunchProtein += Float.valueOf(m.getProtein());
            } else if (m.getType().equals("Dinner")) {
                dinnerCals += Float.valueOf(m.getCalories());
                dinnerFat += Float.valueOf(m.getFat());
                dinnerCarbs += Float.valueOf(m.getCarbs());
                dinnerProtein += Float.valueOf(m.getProtein());
            } else if (m.getType().equals("Snack")) {
                snackCals += Float.valueOf(m.getCalories());
                snackFat += Float.valueOf(m.getFat());
                snackCarbs += Float.valueOf(m.getCarbs());
                snackProtein += Float.valueOf(m.getProtein());
            }
        }

        Meal breakfast = new Meal("", "", "Breakfast",
                String.valueOf(breakfastCals),
                String.valueOf(breakfastProtein),
                String.valueOf(breakfastCarbs),
                String.valueOf(breakfastFat));

        Meal lunch = new Meal("", "", "Lunch",
                String.valueOf(lunchCals),
                String.valueOf(lunchProtein),
                String.valueOf(lunchCarbs),
                String.valueOf(lunchFat));

        Meal dinner = new Meal("", "", "Dinner",
                String.valueOf(dinnerCals),
                String.valueOf(dinnerProtein),
                String.valueOf(dinnerCarbs),
                String.valueOf(dinnerFat));

        Meal snack = new Meal("", "", "Snack",
                String.valueOf(snackCals),
                String.valueOf(snackProtein),
                String.valueOf(snackCarbs),
                String.valueOf(snackFat));


        // If user selects "meal type" sort type
        listHeaders.add(breakfast);
        listHeaders.add(lunch);
        listHeaders.add(dinner);
        listHeaders.add(snack);

        for (Meal header : listHeaders) {
            List<Meal> mealsInType = new ArrayList<Meal>();
            for (Meal meal : meals) {
                if (meal.getType().equals(header.getType())) {
                    mealsInType.add(meal);
                }
            }
            listData.put(header, mealsInType);
        }
    }

    private void updateTotals() {
        BigDecimal calories = new BigDecimal(0);
        BigDecimal protein = new BigDecimal(0);
        BigDecimal carbs = new BigDecimal(0);
        BigDecimal fat = new BigDecimal(0);

        float breakfastCals = 0;
        float lunchCals = 0;
        float dinnerCals = 0;
        float snackCals = 0;

        for (Meal n : meals) {
            calories = calories.add(BigDecimal.valueOf(Double.parseDouble(n.getCalories())));
            protein = protein.add(BigDecimal.valueOf(Double.parseDouble(n.getProtein())));
            carbs = carbs.add(BigDecimal.valueOf(Double.parseDouble(n.getCarbs())));
            fat = fat.add(BigDecimal.valueOf(Double.parseDouble(n.getFat())));

            if (n.getType().equals("Breakfast")) {
                breakfastCals += Float.valueOf(n.getCalories());
            } else if (n.getType().equals("Lunch")) {
                lunchCals += Float.valueOf(n.getCalories());
            } else if (n.getType().equals("Dinner")) {
                dinnerCals += Float.valueOf(n.getCalories());
            } else if (n.getType().equals("Snack")) {
                snackCals += Float.valueOf(n.getCalories());
            }
        }

        if (calories.toString().endsWith(".0")) {
            totalCalories.setText(calories.toString().substring(0, calories.toString().indexOf(".")));
        } else {
            totalCalories.setText(calories.toString());
        }

        if (protein.toString().endsWith(".0")) {
            totalProtein.setText(protein.toString().substring(0, protein.toString().indexOf(".")));
        } else {
            totalProtein.setText(protein.toString());
        }

        if (carbs.toString().endsWith(".0")) {
            totalCarbs.setText(carbs.toString().substring(0, carbs.toString().indexOf(".")));
        } else {
            totalCarbs.setText(carbs.toString());
        }

        if (fat.toString().endsWith(".0")) {
            totalFat.setText(fat.toString().substring(0, fat.toString().indexOf(".")));
        } else {
            totalFat.setText(fat.toString());
        }

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
        PieSection pieLunch = new PieSection("Lunch", lunchCals);
        pieLunch.setFillColor(getResources().getColor(R.color.chart_orange));
        pieLunch.setLabelSize(35);
        pieLunch.setStrokeWidth(1);
        pieLunch.setStrokeColor(Color.BLACK);
        pieLunch.setLabelType(PieLabelType.PERCENTAGE_then_NAME);

        // "dinner" section of the pie chart
        PieSection pieDinner = new PieSection("Dinner", dinnerCals);
        pieDinner.setFillColor(getResources().getColor(R.color.chart_green));
        pieDinner.setLabelSize(35);
        pieDinner.setStrokeWidth(1);
        pieDinner.setStrokeColor(Color.BLACK);
        pieDinner.setLabelType(PieLabelType.PERCENTAGE_then_NAME);

        // "snack" section of the pie chart
        PieSection pieSnack = new PieSection("Snack", snackCals);
        pieSnack.setFillColor(getResources().getColor(R.color.chart_pink));
        pieSnack.setLabelSize(35);
        pieSnack.setStrokeWidth(1);
        pieSnack.setStrokeColor(Color.BLACK);
        pieSnack.setLabelType(PieLabelType.PERCENTAGE_then_NAME);

        List<PieSection> pieCalorieSections = new ArrayList<PieSection>();
        pieCalorieSections.add(pieBreakfast);
        pieCalorieSections.add(pieLunch);
        pieCalorieSections.add(pieDinner);
        pieCalorieSections.add(pieSnack);

        mCaloriesChart.clearChart();
        mCaloriesChart.setPieItems(pieCalorieSections);
        mCaloriesChart.drawChart();

        // "fat" section of the pie chart
        PieSection pieFat = new PieSection("Fat", fat.floatValue() * 9);
        pieFat.setFillColor(getResources().getColor(R.color.chart_red));
        pieFat.setLabelSize(35);
        pieFat.setStrokeWidth(1);
        pieFat.setStrokeColor(Color.BLACK);
        pieFat.setLabelType(PieLabelType.PERCENTAGE_then_NAME);

        // "carbs" section of the pie chart
        PieSection pieCarbs = new PieSection("Carbs", carbs.floatValue() * 4);
        pieCarbs.setFillColor(getResources().getColor(R.color.chart_yellow));
        pieCarbs.setLabelSize(35);
        pieCarbs.setStrokeWidth(1);
        pieCarbs.setStrokeColor(Color.BLACK);
        pieCarbs.setLabelType(PieLabelType.PERCENTAGE_then_NAME);

        // "protein" section of the pie chart
        PieSection pieProtein = new PieSection("Protein", protein.floatValue() * 4);
        pieProtein.setFillColor(getResources().getColor(R.color.chart_purple));
        pieProtein.setLabelSize(35);
        pieProtein.setStrokeWidth(1);
        pieProtein.setStrokeColor(Color.BLACK);
        pieProtein.setLabelType(PieLabelType.PERCENTAGE_then_NAME);

        List<PieSection> pieMacrosSections = new ArrayList<PieSection>();
        pieMacrosSections.add(pieFat);
        pieMacrosSections.add(pieCarbs);
        pieMacrosSections.add(pieProtein);

        mMacrosChart.clearChart();
        mMacrosChart.setPieItems(pieMacrosSections);
        mMacrosChart.drawChart();

        mProgressCalories.setProgress(calories.intValue());
        mProgressFat.setProgress(fat.intValue());
        mProgressCarbs.setProgress(carbs.intValue());
        mProgressProtein.setProgress(protein.intValue());
    }

    private void showPopup(View v, final Meal meal) {
        PopupMenu popup = new PopupMenu(activity, v);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.n_action_addToFavorites:
                        menuClickAddToFavorites(meal);
                        return true;
                    case R.id.n_action_edit:
                        menuClickEdit(meal);
                        return true;
                    case R.id.n_action_delete:
                        menuClickDelete(meal);
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

    private void menuClickAddToFavorites(Meal meal) {
        db.store(new FavoriteMeal(
                meal.getName(), meal.getType(), meal.getCalories(), meal.getProtein(), meal.getCarbs(), meal.getFat()
        ));
    }

    private void menuClickEdit(Meal meal) {
//        Intent intent = new Intent(getActivity(), CustomFoodFragment.class);
//        intent.putExtra("Edit", meal);
//        startActivityForResult(intent, Statics.INTENT_REQUEST_MEAL);
        Toast.makeText(getActivity(), "Edit is currently not enabled", Toast.LENGTH_SHORT).show();
    }

    private void menuClickDelete(Meal meal) {
        db.delete(meal);

        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        String formattedDate = df.format(c.getTime());
        meals = db.getMealList(new Meal(formattedDate));

        prepareListData();
        mAdapter = new NutritionExpandableListAdapter(activity, listHeaders, listData);
        list.setAdapter(mAdapter);
        updateTotals();

    }

    private void updateList() {
        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        String formattedDate = df.format(c.getTime());

        meals = db.getMealList(new Meal(formattedDate));
        prepareListData();
        mAdapter = new NutritionExpandableListAdapter(activity, listHeaders, listData);
        list.setAdapter(mAdapter);
        updateTotals();
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
