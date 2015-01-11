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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cantwellcode.fitfriend.R;
import com.cantwellcode.fitfriend.utils.DBHelper;
import com.cantwellcode.fitfriend.utils.DatePickerFragment;
import com.cantwellcode.fitfriend.utils.DialogListener;
import com.cantwellcode.fitfriend.utils.PieChart;
import com.cantwellcode.fitfriend.utils.SmallDecimalTextView;
import com.cantwellcode.fitfriend.utils.Statics;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Daniel on 2/12/14.
 */
public class NutritionLog extends ListFragment {

    private static Fragment instance = null;

    public static Fragment newInstance() {
        if (instance == null)
            instance = new NutritionLog();
        return instance;
    }

    private Activity activity;

    // SQLite Database
    private DBHelper db;

    private ListView listView;
    private List<Meal> meals;
    private NutritionArrayAdapter mAdapter;

    private Button previous;
    private Button date;
    private Button next;

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

    private PieChart mSurfaceCalories;
    private PieChart mSurfaceMacros;

    private ProgressBar mProgressCalories;
    private ProgressBar mProgressFat;
    private ProgressBar mProgressCarbs;
    private ProgressBar mProgressProtein;

    private ImageView mEmptyListImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.nutrition_log, null);

        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);

        db = new DBHelper(getActivity());

        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        String formattedDate = df.format(c.getTime());
        meals = db.getMealList(new Meal(formattedDate));

        mAdapter = new NutritionArrayAdapter(activity, android.R.id.list, meals);

        listView = (ListView) root.findViewById(android.R.id.list);
        listView.setAdapter(mAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                Meal meal = mAdapter.getItem(position);
                showPopup(view, meal);
                return true;
            }
        });

        setHasOptionsMenu(true);

        previous = (Button) root.findViewById(R.id.n_previous);
        date = (Button) root.findViewById(R.id.n_date);
        next = (Button) root.findViewById(R.id.n_next);

        next.setEnabled(false);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

                        mAdapter.clear();
                        mAdapter.addAll(meals);
                        updateList();

                        final Calendar cal = Calendar.getInstance();
                        int y = cal.get(Calendar.YEAR);
                        int m = cal.get(Calendar.MONTH);
                        int d = cal.get(Calendar.DAY_OF_MONTH);

                        if (y == year && m == month && d == day) {
                            date.setText("Today");
                            next.setEnabled(false);
                            next.setTextColor(Color.GRAY);
                        } else {
                            date.setText(formattedDate);
                        }
                    }

                    @Override
                    public void onDialogCancel() {

                    }
                });
                dateFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.add(Calendar.DATE, -1);
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH) + 1;
                day = c.get(Calendar.DAY_OF_MONTH);

                updateList();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c.add(Calendar.DATE, 1);
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH) + 1;
                day = c.get(Calendar.DAY_OF_MONTH);

                updateList();
            }
        });

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

        mSurfaceCalories = (PieChart) root.findViewById(R.id.surfaceCalories);
        mSurfaceMacros = (PieChart) root.findViewById(R.id.surfaceMacros);

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

        mEmptyListImage = (ImageView) root.findViewById(android.R.id.empty);
        listView.setEmptyView(mEmptyListImage);

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
        actionBar.setTitle("Nutrition Log");
    }

    private void updateTotals() {
        BigDecimal calories = new BigDecimal(0);
        BigDecimal protein = new BigDecimal(0);
        BigDecimal carbs = new BigDecimal(0);
        BigDecimal fat = new BigDecimal(0);

        int breakfastCals = 0;
        int lunchCals = 0;
        int dinnerCals = 0;
        int snackCals = 0;

        for (Meal n : meals) {
            calories = calories.add(BigDecimal.valueOf(Double.parseDouble(n.getCalories())));
            protein = protein.add(BigDecimal.valueOf(Double.parseDouble(n.getProtein())));
            carbs = carbs.add(BigDecimal.valueOf(Double.parseDouble(n.getCarbs())));
            fat = fat.add(BigDecimal.valueOf(Double.parseDouble(n.getFat())));

            if (n.getType().equals("Breakfast")) {
                breakfastCals += Integer.valueOf(n.getCalories());
            } else if (n.getType().equals("Lunch")) {
                lunchCals += Integer.valueOf(n.getCalories());
            } else if (n.getType().equals("Dinner")) {
                dinnerCals += Integer.valueOf(n.getCalories());
            } else if (n.getType().equals("Snack")) {
                snackCals += Integer.valueOf(n.getCalories());
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

        mSurfaceCalories.setValues(breakfastCals, lunchCals, dinnerCals, snackCals);
        // Fat: 9 calories per gram    Carbs: 4 calories per gram    Protein: 4 calories per gram
        mSurfaceMacros.setValues(fat.intValue() * 9, carbs.intValue() * 4, protein.intValue() * 4, 0);

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

        if (meals.isEmpty()) {
            Toast.makeText(activity, "No Meals Have Been Added", Toast.LENGTH_LONG).show();
        }

        mAdapter.clear();
        mAdapter.addAll(meals);
        updateTotals();

    }

    private void updateList() {
        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
        String formattedDate = df.format(c.getTime());

        meals = db.getMealList(new Meal(formattedDate));
        mAdapter.clear();
        mAdapter.addAll(meals);
        updateTotals();

        final Calendar cal = Calendar.getInstance();
        int y = cal.get(Calendar.YEAR);
        int m = cal.get(Calendar.MONTH) + 1;
        int d = cal.get(Calendar.DAY_OF_MONTH);

        if (y == year && m == month && d == day) {
            date.setText("Today");
            next.setEnabled(false);
            next.setTextColor(Color.GRAY);
        } else {
            date.setText(formattedDate);
            if (!next.isEnabled()) {
                next.setEnabled(true);
                next.setTextColor(Color.BLACK);
            }
        }
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
