package com.fitfriend.app.nutrition;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by Daniel on 11/8/2014.
 */
public class FavoriteFoodFragment extends Fragment {

    private DBHelper db;

    private ExpandableListView listView;
    private List<FavoriteMeal> meals;
    private FavoritesExpandableListAdapter mAdapter;
    private List<String> listHeaders;
    private HashMap<String, List<FavoriteMeal>> listData;
    private NewFoodActivity mActivity;

    public static FavoriteFoodFragment newInstance() {
        return new FavoriteFoodFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.nutrition_favorites_view, null);

        db = new DBHelper(getActivity());
        meals = db.getAllFavorites();

        if (meals.isEmpty()) {
            Toast.makeText(getActivity(), "No Favorites Have Been Added", Toast.LENGTH_LONG).show();
        }

        prepareListData(db);
        mAdapter = new FavoritesExpandableListAdapter(getActivity(), listHeaders, listData);

        listView = (ExpandableListView) root.findViewById(android.R.id.list);
        listView.setAdapter(mAdapter);

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                FavoriteMeal food = (FavoriteMeal) mAdapter.getChild(groupPosition, childPosition);
                mActivity.setDetails(food);
                Toast.makeText(mActivity, food.getName(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        return root;
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

    public void setActivity(NewFoodActivity activity) {
        this.mActivity = activity;
    }
}
