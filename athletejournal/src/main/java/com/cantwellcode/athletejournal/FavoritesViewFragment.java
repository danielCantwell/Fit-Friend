package com.cantwellcode.athletejournal;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Daniel on 3/6/14.
 */
public class FavoritesViewFragment extends Fragment {

    // SQLite Database
    private Database db;

    private ListView listView;
    private List<Nutrition> meals;
    private NutritionArrayAdapter mAdapter;

    public static Fragment newInstance(Context context) {
        FavoritesViewFragment f = new FavoritesViewFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.favorites_list_view, null);

        db = new Database(getActivity(), Database.DATABASE_NAME, null, Database.DATABASE_VERSION);
        meals = db.getAllFavorites();

        if (meals.isEmpty()) {
            Toast.makeText(getActivity(), "No Favorites Have Been Added", Toast.LENGTH_LONG).show();
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

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        restoreActionBar();
        inflater.inflate(R.menu.add_favorite, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_addNew:
                FragmentManager fm = getFragmentManager();
                fm.beginTransaction()
                        .replace(R.id.container, AddFavoriteFragment.newInstance(getActivity()))
                        .commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void restoreActionBar() {
        ActionBar actionBar = getActivity().getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Favorite Meals");
    }

    private void showPopup(View v, final Nutrition meal) {
        PopupMenu popup = new PopupMenu(getActivity(), v);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_edit:
                        menuClickEdit(meal);
                        return true;
                    case R.id.action_delete:
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

    private void menuClickEdit(Nutrition meal) {

    }

    private void menuClickDelete(Nutrition meal) {
        db.deleteFavorite(meal);
        meals = db.getAllFavorites();
        if (meals.isEmpty()) {
            Toast.makeText(getActivity(), "No Favorites Have Been Added", Toast.LENGTH_LONG).show();
        }
        mAdapter = new NutritionArrayAdapter(getActivity(), android.R.id.list, meals);
        listView.setAdapter(mAdapter);
    }
}
