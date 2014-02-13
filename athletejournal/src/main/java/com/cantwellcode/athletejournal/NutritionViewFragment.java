package com.cantwellcode.athletejournal;

import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Daniel on 2/12/14.
 */
public class NutritionViewFragment extends ListFragment {

    int year;
    int month;
    int day;

    Menu menu;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.nutrition_list_view, null);

        db = new Database(getActivity(), Database.DATABASE_NAME, null, Database.DATABASE_VERSION);
        meals = db.getNutritionList(Database.NutritionListType.Day);

        if (meals.isEmpty()) {
            Toast.makeText(getActivity(), "No Meals Have Been Added Today", Toast.LENGTH_LONG).show();
        }

        mAdapter = new NutritionArrayAdapter(getActivity(), android.R.id.list, meals);

        listView = (ListView) root.findViewById(android.R.id.list);
        listView.setAdapter(mAdapter);

        setHasOptionsMenu(true);

        dayView = (Button) root.findViewById(R.id.n_day_button);
        weekView = (Button) root.findViewById(R.id.n_week_button);
        monthView = (Button) root.findViewById(R.id.n_month_button);
        totalView = (Button) root.findViewById(R.id.n_total_button);

        dayView.setTextColor(Color.BLUE);

        dayView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                meals = db.getNutritionList(Database.NutritionListType.Day);
                mAdapter.clear();
                mAdapter.addAll(meals);

                dayView.setTextColor(Color.BLUE);
                weekView.setTextColor(Color.BLACK);
                monthView.setTextColor(Color.BLACK);
                totalView.setTextColor(Color.BLACK);

                menu.getItem(0).setVisible(true);
            }
        });

        totalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                meals = db.getNutritionList(Database.NutritionListType.Total);
                mAdapter.clear();
                mAdapter.addAll(meals);

                dayView.setTextColor(Color.BLACK);
                weekView.setTextColor(Color.BLACK);
                monthView.setTextColor(Color.BLACK);
                totalView.setTextColor(Color.BLUE);

                menu.getItem(0).setVisible(false);
            }
        });

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        restoreActionBar();
        inflater.inflate(R.menu.add_new, menu);
        this.menu = menu;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        menu.getItem(0).setTitle((month + 1) + " / " + day + " / " + year);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_addNew:
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.container, NutritionFragment.newInstance(getActivity()))
                        .commit();
                break;
            case R.id.action_changeDate:
                DialogFragment dateFragment = new DatePickerFragment();
                dateFragment.show(getActivity().getSupportFragmentManager(), "datPicker");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Nutrition Log");
    }

    /* Date Picker Fragment */

    private class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

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
            menu.getItem(0).setTitle(" " + (month + 1) + " / " + day + " / " + year + " ");
        }
    }
}
