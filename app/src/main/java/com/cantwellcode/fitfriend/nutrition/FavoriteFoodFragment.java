package com.cantwellcode.fitfriend.nutrition;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
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
 * Created by Daniel on 11/8/2014.
 */
public class FavoriteFoodFragment extends Fragment {

    private ExpandableListView mListView;
    private List<Food> mFoods;
    private FavoritesExpandableListAdapter mAdapter;
    private List<String> mListHeaders;
    private HashMap<String, List<Food>> mListData;
    private NewFoodActivity mActivity;

    public static FavoriteFoodFragment newInstance() {
        return new FavoriteFoodFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.nutrition_favorites_view, null);

        mListView = (ExpandableListView) root.findViewById(android.R.id.list);

        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Food food = (Food) mAdapter.getChild(groupPosition, childPosition);
                mActivity.setDetails(food);
                Toast.makeText(mActivity, food.getName(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        ParseQuery<Food> favoritesQuery = Food.getQuery();
        favoritesQuery.fromPin(Statics.PIN_NUTRITION_FAVORITES);

        favoritesQuery.findInBackground(new FindCallback<Food>() {
            @Override
            public void done(List<Food> foods, ParseException e) {
                mFoods = foods;
                prepareListData();
                mAdapter = new FavoritesExpandableListAdapter(getActivity(), mListHeaders, mListData);
                mListView.setAdapter(mAdapter);
            }
        });

        return root;
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

    public void setActivity(NewFoodActivity activity) {
        this.mActivity = activity;
    }
}
