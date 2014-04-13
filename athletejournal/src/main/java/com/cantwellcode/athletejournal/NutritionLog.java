package com.cantwellcode.athletejournal;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Daniel on 2/12/14.
 */
public class NutritionLog extends ListFragment {

    Menu menu;
    MenuInflater menuInflater;

    public static Fragment newInstance(Context context) {
        NutritionLog f = new NutritionLog();
        return f;
    }

    // SQLite Database
    private Database db;

    private ListView listView;
    private List<Nutrition> meals;
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

    public GestureDetectorCompat mDetector;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.nutrition_list_view, null);

        mDetector = new GestureDetectorCompat(getActivity(), new SwipeListener());

        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDetector.onTouchEvent(event);
                return true;
            }
        });

        c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);

        db = new Database(getActivity(), Database.DATABASE_NAME, null, Database.DATABASE_VERSION);
        meals = db.getNutritionList(Database.NutritionListType.Day, month, day, year);

        if (meals.isEmpty()) {
            Toast.makeText(getActivity(), "No Meals Have Been Added Today", Toast.LENGTH_LONG).show();
        }

        mAdapter = new NutritionArrayAdapter(getActivity(), android.R.id.list, meals);

        listView = (ListView) root.findViewById(android.R.id.list);
        listView.setAdapter(mAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                Nutrition meal = mAdapter.getItem(position);
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
                DialogFragment dateFragment = new DatePickerFragment();
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

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        goalCalories.setText(sp.getString(ProfileFragment.GOAL_CALORIES, "defaultCal"));
        goalProtein.setText(sp.getString(ProfileFragment.GOAL_PROTEIN, "defaultPro"));
        goalCarbs.setText(sp.getString(ProfileFragment.GOAL_CARBS, "defaultCarb"));
        goalFat.setText(sp.getString(ProfileFragment.GOAL_FAT, "defaultFat"));

        updateTotals();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        goalCalories.setText(sp.getString(ProfileFragment.GOAL_CALORIES, "defaultCal") + " cal");
        goalFat.setText(sp.getString(ProfileFragment.GOAL_FAT, "defaultFat") + " fat");
        goalCarbs.setText(sp.getString(ProfileFragment.GOAL_CARBS, "defaultCarb") + " carbs");
        goalProtein.setText(sp.getString(ProfileFragment.GOAL_PROTEIN, "defaultPro") + " prot");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        restoreActionBar();
        inflater.inflate(R.menu.add_new, menu);
        this.menu = menu;
        this.menuInflater = inflater;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_addNew:
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.container, AddNutritionFragment.newInstance(getActivity(), AddNutritionFragment.InstanceType.NewMeal))
                        .commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void restoreActionBar() {
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Nutrition Log");

    }

    private void updateTotals() {
        BigDecimal calories = new BigDecimal(0);
        BigDecimal protein = new BigDecimal(0);
        BigDecimal carbs = new BigDecimal(0);
        BigDecimal fat = new BigDecimal(0);

        for (Nutrition n : meals) {
            calories = calories.add(BigDecimal.valueOf(Double.parseDouble(n.get_calories())));
            protein = protein.add(BigDecimal.valueOf(Double.parseDouble(n.get_protein())));
            carbs = carbs.add(BigDecimal.valueOf(Double.parseDouble(n.get_carbs())));
            fat = fat.add(BigDecimal.valueOf(Double.parseDouble(n.get_fat())));
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

        updateWidget();
    }

    private void showPopup(View v, final Nutrition meal) {
        PopupMenu popup = new PopupMenu(getActivity(), v);

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

    private void menuClickAddToFavorites(Nutrition meal) {
        DialogFragment categoryDialog = new CategoryDialog(meal, db);
        categoryDialog.show(getFragmentManager(), "categoryDialog");
    }

    private void menuClickEdit(Nutrition meal) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sp.edit().putInt("MealToEdit_ID", meal.get_id()).commit();
        sp.edit().putString("MealToEdit_Name", meal.get_name()).commit();
        sp.edit().putString("MealToEdit_Type", meal.get_type()).commit();
        sp.edit().putString("MealToEdit_Calories", meal.get_calories()).commit();
        sp.edit().putString("MealToEdit_Protein", meal.get_protein()).commit();
        sp.edit().putString("MealToEdit_Carbs", meal.get_carbs()).commit();
        sp.edit().putString("MealToEdit_Fat", meal.get_fat()).commit();

        Log.d("PutEditName", sp.getString("MealToEdit_Name", "default"));

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .replace(R.id.container, AddNutritionFragment.newInstance(getActivity(), AddNutritionFragment.InstanceType.EditMeal))
                .commit();
    }

    private void menuClickDelete(Nutrition meal) {
        db.deleteNutrition(meal);

        meals = db.getNutritionList(Database.NutritionListType.Day, month, day, year);

        if (meals.isEmpty()) {
            Toast.makeText(getActivity(), "No Meals Have Been Added", Toast.LENGTH_LONG).show();
        }

        mAdapter.clear();
        mAdapter.addAll(meals);
        updateTotals();

    }

    /* Date Picker Fragment */

    private class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int y = c.get(Calendar.YEAR);
            int m = c.get(Calendar.MONTH);
            int d = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, y, m, d);
        }

        @Override
        public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
            year = i;
            month = i2;
            day = i3;

            c.set(Calendar.YEAR, year);
            c.set(Calendar.MONTH, month);
            c.set(Calendar.DAY_OF_MONTH, day);

            meals = db.getNutritionList(Database.NutritionListType.Day, month + 1, day, year);
            mAdapter.clear();
            mAdapter.addAll(meals);
            updateTotals();

            final Calendar cal = Calendar.getInstance();
            int y = cal.get(Calendar.YEAR);
            int m = cal.get(Calendar.MONTH);
            int d = cal.get(Calendar.DAY_OF_MONTH);

            if (y == year && m == month && d == day) {
                date.setText("Today");
            } else {
                SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
                String formattedDate = df.format(c.getTime());
                date.setText(formattedDate);
            }
        }
    }

    private void updateList() {
        meals = db.getNutritionList(Database.NutritionListType.Day, month, day, year);
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
            SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy");
            String formattedDate = df.format(c.getTime());
            date.setText(formattedDate);
            if (!next.isEnabled()) {
                next.setEnabled(true);
                next.setTextColor(Color.BLACK);
            }
        }
    }

    private void updateWidget() {
        Context context = getActivity();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context, WidgetProvider.class);

        WidgetProvider w = new WidgetProvider();
        w.onUpdate(context, appWidgetManager, appWidgetManager.getAppWidgetIds(thisWidget));
    }

    private class SwipeListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            if (velocityX * velocityX <= velocityY * velocityY) return false;

            if (e2.getX() > e1.getX()) {
                previous.callOnClick();
            }
            else if (e2.getX() < e1.getX()) {
                if (next.isEnabled())
                    next.callOnClick();
            }

            return true;
        }
    }
}