package com.cantwellcode.fitfriend.nutrition;

import android.app.ActionBar;
import android.app.ProgressDialog;
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

import com.cantwellcode.fitfriend.R;
import com.cantwellcode.fitfriend.utils.Statics;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Daniel on 3/6/14.
 */
public class NutritionFavoritesView extends FragmentActivity {

    private ExpandableListView mListView;
    private List<Food> mFoods;
    private FavoritesExpandableListAdapter mAdapter;
    private List<String> mListHeaders;
    private HashMap<String, List<Food>> mListData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nutrition_favorites_view);

        mListView = (ExpandableListView) findViewById(android.R.id.list);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                long packedPosition = mListView.getExpandableListPosition(position);

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

        ParseQuery<Food> favoritesQuery = Food.getQuery();
        favoritesQuery.fromPin(Statics.PIN_NUTRITION_FAVORITES);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Searching for Favorite Foods");

        favoritesQuery.findInBackground(new FindCallback<Food>() {
            @Override
            public void done(List<Food> foods, ParseException e) {
                mFoods = foods;
                prepareListData();
                mAdapter = new FavoritesExpandableListAdapter(NutritionFavoritesView.this, mListHeaders, mListData);
                mListView.setAdapter(mAdapter);

                if (foods.isEmpty()) {
                    Toast.makeText(NutritionFavoritesView.this, "No Favorites Have Been Added", Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
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

    private void prepareListData() {
        mListHeaders = new ArrayList<String>();
        mListData = new HashMap<String, List<Food>>();

        // If user selects "meal type" sort type
        mListHeaders.add("Breakfast");
        mListHeaders.add("Lunch");
        mListHeaders.add("Dinner");
        mListHeaders.add("Snack");

        for (String header : mListHeaders) {
            List<Food> favoritesInType = new ArrayList<Food>();
            for (Food favorite : mFoods) {
                if (favorite.getType().equals(header)) {
                    favoritesInType.add(favorite);
                }
            }
            mListData.put(header, sortFavoritesByName(favoritesInType));
        }

    }

    private void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle("Favorite Meals");
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void showPopup(View v, final Food food) {
        PopupMenu popup = new PopupMenu(this, v);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.f_action_delete:
                        menuClickDelete(food);
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

    private void menuClickDelete(Food food) {
        mFoods.remove(food);
        food.unpinInBackground(Statics.PIN_NUTRITION_FAVORITES);
        reloadList();
    }

    private List<Food> sortFavoritesByName(List<Food> favoritesUnsorted) {
        List<Food> favoritesSorted = new ArrayList<Food>();
        List<String> favoritesNames = new ArrayList<String>();

        for (Food f1 : favoritesUnsorted) {
            favoritesNames.add(f1.getName());
        }
        Collections.sort(favoritesNames);

        for (String name : favoritesNames) {
            for (Food f2 : favoritesUnsorted) {
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
        prepareListData();
        mAdapter = new FavoritesExpandableListAdapter(this, mListHeaders, mListData);
        mListView.setAdapter(mAdapter);
    }
}
