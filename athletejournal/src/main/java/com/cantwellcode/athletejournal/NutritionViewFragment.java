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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import java.util.Calendar;
import java.util.List;

/**
 * Created by Daniel on 2/12/14.
 */
public class NutritionViewFragment extends ListFragment {

    private enum CurrentView {Day, Week, Month, Total}

    ;
    private CurrentView currentView = CurrentView.Day;

    int year;
    int month;
    int day;

    Menu menu;
    MenuInflater menuInflater;

    public static Fragment newInstance(Context context) {
        NutritionViewFragment f = new NutritionViewFragment();
        return f;
    }

    // SQLite Database
    private Database db;

    private ListView listView;
    private List<Nutrition> meals;
    private NutritionArrayAdapter mAdapter;

    private Button dayView;
    private Button weekView;
    private Button monthView;
    private Button totalView;

    private SmallDecimalTextView totalCalories;
    private SmallDecimalTextView totalProtein;
    private SmallDecimalTextView totalCarbs;
    private SmallDecimalTextView totalFat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.nutrition_list_view, null);

        final Calendar c = Calendar.getInstance();
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

        dayView = (Button) root.findViewById(R.id.n_day_button);
        weekView = (Button) root.findViewById(R.id.n_week_button);
        monthView = (Button) root.findViewById(R.id.n_month_button);
        totalView = (Button) root.findViewById(R.id.n_total_button);

        dayView.setTextColor(Color.BLUE);

        dayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                meals = db.getNutritionList(Database.NutritionListType.Day, month, day, year);
                mAdapter.clear();
                mAdapter.addAll(meals);
                updateTotals();

                dayView.setTextColor(Color.BLUE);
                weekView.setTextColor(Color.BLACK);
                monthView.setTextColor(Color.BLACK);
                totalView.setTextColor(Color.BLACK);

                currentView = CurrentView.Day;

                onPrepareOptionsMenu(menu);
            }
        });

        monthView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meals = db.getNutritionList(Database.NutritionListType.Month, month, day, year);
                mAdapter.clear();
                mAdapter.addAll(meals);
                updateTotals();

                dayView.setTextColor(Color.BLACK);
                weekView.setTextColor(Color.BLACK);
                monthView.setTextColor(Color.BLUE);
                totalView.setTextColor(Color.BLACK);

                currentView = CurrentView.Month;

                onPrepareOptionsMenu(menu);
            }
        });

        totalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                meals = db.getNutritionList(Database.NutritionListType.Total, month, day, year);
                mAdapter.clear();
                mAdapter.addAll(meals);
                updateTotals();

                dayView.setTextColor(Color.BLACK);
                weekView.setTextColor(Color.BLACK);
                monthView.setTextColor(Color.BLACK);
                totalView.setTextColor(Color.BLUE);

                currentView = CurrentView.Total;

                onPrepareOptionsMenu(menu);
            }
        });

        totalCalories = (SmallDecimalTextView) root.findViewById(R.id.n_view_total_calories);
        totalProtein = (SmallDecimalTextView) root.findViewById(R.id.n_view_total_protein);
        totalCarbs = (SmallDecimalTextView) root.findViewById(R.id.n_view_total_carbs);
        totalFat = (SmallDecimalTextView) root.findViewById(R.id.n_view_total_fat);

        updateTotals();

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        restoreActionBar();
        inflater.inflate(R.menu.n_view_day, menu);
        this.menu = menu;
        this.menuInflater = inflater;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);

        menu.clear();
        restoreActionBar();

        switch (currentView) {
            case Day:
                menuInflater.inflate(R.menu.n_view_day, menu);
                menu.getItem(0).setTitle((month) + " / " + day + " / " + year);
                break;
            case Month:
                menuInflater.inflate(R.menu.n_view_month, menu);
                switch (month) {
                    case 1:
                        menu.getItem(0).setTitle("January");
                        break;
                    case 2:
                        menu.getItem(0).setTitle("February");
                        break;
                    case 3:
                        menu.getItem(0).setTitle("March");
                        break;
                    case 4:
                        menu.getItem(0).setTitle("April");
                        break;
                    case 5:
                        menu.getItem(0).setTitle("May");
                        break;
                    case 6:
                        menu.getItem(0).setTitle("June");
                        break;
                    case 7:
                        menu.getItem(0).setTitle("July");
                        break;
                    case 8:
                        menu.getItem(0).setTitle("August");
                        break;
                    case 9:
                        menu.getItem(0).setTitle("September");
                        break;
                    case 10:
                        menu.getItem(0).setTitle("October");
                        break;
                    case 11:
                        menu.getItem(0).setTitle("November");
                        break;
                    case 12:
                        menu.getItem(0).setTitle("December");
                        break;
                }
                break;
            case Total:
                menuInflater.inflate(R.menu.n_view_total, menu);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int monthSelect = 0;
        switch (item.getItemId()) {
            case R.id.action_addNew:
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.container, AddNutritionFragment.newInstance(getActivity(), AddNutritionFragment.InstanceType.NewMeal))
                        .commit();
                break;
            case R.id.action_changeDate:
                DialogFragment dateFragment = new DatePickerFragment();
                dateFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
                break;
            case R.id.action_selectJanuary:
                monthSelect = 1;
                menu.getItem(0).setTitle("January");
                break;
            case R.id.action_selectFebruary:
                monthSelect = 2;
                menu.getItem(0).setTitle("February");
                break;
            case R.id.action_selectMarch:
                monthSelect = 3;
                menu.getItem(0).setTitle("March");
                break;
            case R.id.action_selectApril:
                monthSelect = 4;
                menu.getItem(0).setTitle("April");
                break;
            case R.id.action_selectMay:
                monthSelect = 5;
                menu.getItem(0).setTitle("May");
                break;
            case R.id.action_selectJune:
                monthSelect = 6;
                menu.getItem(0).setTitle("June");
                break;
            case R.id.action_selectJuly:
                monthSelect = 7;
                menu.getItem(0).setTitle("July");
                break;
            case R.id.action_selectAugust:
                monthSelect = 8;
                menu.getItem(0).setTitle("August");
                break;
            case R.id.action_selectSeptember:
                monthSelect = 9;
                menu.getItem(0).setTitle("September");
                break;
            case R.id.action_selectOctober:
                monthSelect = 10;
                menu.getItem(0).setTitle("October");
                break;
            case R.id.action_selectNovember:
                monthSelect = 11;
                menu.getItem(0).setTitle("November");
                break;
            case R.id.action_selectDecember:
                monthSelect = 12;
                menu.getItem(0).setTitle("December");
                break;
        }
        if (monthSelect != 0) {
            meals = db.getNutritionList(Database.NutritionListType.Month, monthSelect, day, year);
            mAdapter.clear();
            mAdapter.addAll(meals);
            updateTotals();
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

        switch (currentView) {
            case Day:
                meals = db.getNutritionList(Database.NutritionListType.Day, month, day, year);
                break;
            case Month:
                meals = db.getNutritionList(Database.NutritionListType.Month, month, day, year);
                break;
            case Total:
                meals = db.getNutritionList(Database.NutritionListType.Total, month, day, year);
                break;
        }

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
            month = i2 + 1;
            day = i3;
            menu.getItem(0).setTitle(" " + (month) + " / " + day + " / " + year + " ");

            meals = db.getNutritionList(Database.NutritionListType.Day, month, day, year);
            mAdapter.clear();
            mAdapter.addAll(meals);
            updateTotals();
        }
    }

    private void updateWidget() {
        Context context = getActivity();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisWidget = new ComponentName(context, WidgetProvider.class);

        WidgetProvider w = new WidgetProvider();
        w.onUpdate(context, appWidgetManager, appWidgetManager.getAppWidgetIds(thisWidget));
    }
}
