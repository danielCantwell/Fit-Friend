package com.fitfriend.app.nutrition;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.fitfriend.app.R;
import com.fitfriend.app.utils.DBHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Daniel on 3/6/14.
 */
public class NutritionFavoritesView extends FragmentActivity {

    private DBHelper db;

    private ExpandableListView listView;
    private List<FavoriteMeal> meals;
    private FavoritesExpandableListAdapter mAdapter;
    private List<String> listHeaders;
    private HashMap<String, List<FavoriteMeal>> listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nutrition_favorites_view);

        db = new DBHelper(this);
        meals = db.getAllFavorites();

        if (meals.isEmpty()) {
            Toast.makeText(this, "No Favorites Have Been Added", Toast.LENGTH_LONG).show();
        }

        prepareListData(db);
        mAdapter = new FavoritesExpandableListAdapter(this, listHeaders, listData);

        listView = (ExpandableListView) findViewById(android.R.id.list);
        listView.setAdapter(mAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                long packedPosition = listView.getExpandableListPosition(position);

                int itemType = ExpandableListView.getPackedPositionType(packedPosition);
                int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
                int childPosition = ExpandableListView.getPackedPositionChild(packedPosition);

                if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    FavoriteMeal meal = (FavoriteMeal) mAdapter.getChild(groupPosition, childPosition);
                    showPopup(view, meal);

                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        restoreActionBar();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void prepareListData(DBHelper db) {
        listHeaders = new ArrayList<String>();
        listData = new HashMap<String, List<FavoriteMeal>>();

        List<FavoriteMeal> favorites = db.getAllFavorites();
        // If user selects "meal type" sort type
        listHeaders.add("Breakfast");
        listHeaders.add("Lunch");
        listHeaders.add("Dinner");
        listHeaders.add("Snack");
        listHeaders.add("Pre-Workout");
        listHeaders.add("Post-Workout");

        for (String header : listHeaders) {
            List<FavoriteMeal> favoritesInType = new ArrayList<FavoriteMeal>();
            for (FavoriteMeal favorite : favorites) {
                if (favorite.getType().equals(header)) {
                    favoritesInType.add(favorite);
                }
            }
            listData.put(header, sortFavoritesByName(favoritesInType));
        }

    }

    private void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Favorite Meals");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void showPopup(View v, final FavoriteMeal meal) {
        PopupMenu popup = new PopupMenu(this, v);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.f_action_delete:
                        menuClickDelete(meal);
                        return true;
                    default:
                        return false;
                }
            }
        });

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.f_list_child_click, popup.getMenu());
        popup.show();
    }

    private void menuClickDelete(FavoriteMeal meal) {
        db.delete(meal);
        meals = db.getAllFavorites();
        if (meals.isEmpty()) {
            Toast.makeText(this, "No Favorites Added", Toast.LENGTH_LONG).show();
        }
        reloadList();
    }

    private List<FavoriteMeal> sortFavoritesByName(List<FavoriteMeal> favoritesUnsorted) {
        List<FavoriteMeal> favoritesSorted = new ArrayList<FavoriteMeal>();
        List<String> favoritesNames = new ArrayList<String>();

        for (FavoriteMeal f1 : favoritesUnsorted) {
            favoritesNames.add(f1.getName());
        }
        Collections.sort(favoritesNames);

        for (String name : favoritesNames) {
            for (FavoriteMeal f2 : favoritesUnsorted) {
                if (name.equals(f2.getName())) {
                    favoritesSorted.add(f2);
                    favoritesUnsorted.remove(f2);
                    break;
                }
            }
        }

        return favoritesSorted;
    }

    public void reloadList() {
        prepareListData(db);
        mAdapter = new FavoritesExpandableListAdapter(this, listHeaders, listData);

        listView.setAdapter(mAdapter);
    }
}
