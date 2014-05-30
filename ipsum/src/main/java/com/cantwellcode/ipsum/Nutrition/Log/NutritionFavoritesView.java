package com.cantwellcode.ipsum.Nutrition.Log;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.cantwellcode.ipsum.Utils.DBHelper;
import com.cantwellcode.ipsum.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Daniel on 3/6/14.
 */
public class NutritionFavoritesView extends Fragment {

    public static enum SortFavoritesBy { Type, Category };
    private SortFavoritesBy sortType = SortFavoritesBy.Category;

    private Button typeSort;
    private Button categorySort;

    // SQLite Database
    private DBHelper db;

    private ExpandableListView listView;
    private List<Favorite> meals;
    private FavoritesExpandableListAdapter mAdapter;
    private List<String> listHeaders;
    private HashMap<String, List<Favorite>> listData;

    public static Fragment newInstance(Context context) {
        NutritionFavoritesView f = new NutritionFavoritesView();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.nutrition_favorites_view, null);

        db = new DBHelper(getActivity());
        meals = db.getAllFavorites();

        if (meals.isEmpty()) {
            Toast.makeText(getActivity(), "No Favorites Have Been Added", Toast.LENGTH_LONG).show();
        }

        prepareListData(db);
        mAdapter = new FavoritesExpandableListAdapter(getActivity(), listHeaders, listData, sortType);

        listView = (ExpandableListView) root.findViewById(android.R.id.list);
        listView.setAdapter(mAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                long packedPosition = listView.getExpandableListPosition(position);

                int itemType = ExpandableListView.getPackedPositionType(packedPosition);
                int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
                int childPosition = ExpandableListView.getPackedPositionChild(packedPosition);

                if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    Favorite meal = (Favorite) mAdapter.getChild(groupPosition, childPosition);
                    showPopup(view, meal);

                    return true;
                }

                if (itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    String category = mAdapter.getGroup(groupPosition).toString();
                    showPopup(category);
                }
                return false;
            }
        });

        typeSort = (Button) root.findViewById(R.id.f_type_button);
        categorySort = (Button) root.findViewById(R.id.f_category_button);

        categorySort.setTextColor(Color.BLUE);

        typeSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeSort.setTextColor(Color.BLUE);
                categorySort.setTextColor(Color.BLACK);

                sortType = SortFavoritesBy.Type;

                prepareListData(db);
                mAdapter = new FavoritesExpandableListAdapter(getActivity(), listHeaders, listData, sortType);
                listView.setAdapter(mAdapter);

                if (listData.isEmpty()) {
                    Toast.makeText(getActivity(), "No Favorites Added", Toast.LENGTH_SHORT).show();
                }
            }
        });

        categorySort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categorySort.setTextColor(Color.BLUE);
                typeSort.setTextColor(Color.BLACK);

                sortType = SortFavoritesBy.Category;

                prepareListData(db);
                mAdapter = new FavoritesExpandableListAdapter(getActivity(), listHeaders, listData, sortType);
                listView.setAdapter(mAdapter);

                if (listData.isEmpty()) {
                    Toast.makeText(getActivity(), "No Favorites Added", Toast.LENGTH_SHORT).show();
                }
            }
        });

        setHasOptionsMenu(true);

        return root;
    }

    private void prepareListData(DBHelper db) {
        listHeaders = new ArrayList<String>();
        listData = new HashMap<String, List<Favorite>>();

        List<Favorite> favorites = db.getAllFavorites();

        if (sortType == SortFavoritesBy.Category) {
            // If user selects "category" sort type
            listHeaders = db.getFavoritesCategories();
            Collections.sort(listHeaders);
            for (String header : listHeaders) {
                List<Favorite> favoritesInCategory = new ArrayList<Favorite>();
                List<String> favoritesNames = new ArrayList<String>();
                for (Favorite favorite : favorites) {
                    if (favorite.get_category().equals(header)) {
                        favoritesInCategory.add(favorite);
                    }
                }

                listData.put(header, sortFavoritesByName(favoritesInCategory));
            }
        }
        else if (sortType == SortFavoritesBy.Type) {
            // If user selects "meal type" sort type
            listHeaders.add("Breakfast");
            listHeaders.add("Lunch");
            listHeaders.add("Dinner");
            listHeaders.add("Snack");
            listHeaders.add("Pre-Workout");
            listHeaders.add("Post-Workout");

            for (String header : listHeaders) {
                List<Favorite> favoritesInType = new ArrayList<Favorite>();
                for (Favorite favorite : favorites) {
                    if (favorite.get_type().equals(header)) {
                        favoritesInType.add(favorite);
                    }
                }
                listData.put(header, sortFavoritesByName(favoritesInType));
            }
        }
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
                        .replace(R.id.container, NutritionAddFavorite.newInstance(NutritionAddFavorite.InstanceType.NewFavorite))
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

    private void showPopup(View v, final Favorite meal) {
        PopupMenu popup = new PopupMenu(getActivity(), v);

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.f_action_edit:
                        menuClickEdit(meal);
                        return true;
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

    private void showPopup(String category) {
        DialogFragment categoryDialog = new CategoryDialog(this, category, db);
        categoryDialog.show(getFragmentManager(), "categoryDialog");
    }

    private void menuClickEdit(Favorite meal) {

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .replace(R.id.container, NutritionAddFavorite.newInstance(NutritionAddFavorite.InstanceType.EditFavorite, meal))
                .commit();
    }

    private void menuClickDelete(Favorite meal) {
        db.delete(meal);
        meals = db.getAllFavorites();
        if (meals.isEmpty()) {
            Toast.makeText(getActivity(), "No Favorites Added", Toast.LENGTH_LONG).show();
        }
        prepareListData(db);
        mAdapter = new FavoritesExpandableListAdapter(getActivity(), listHeaders, listData, sortType);
        listView.setAdapter(mAdapter);
    }

    private List<Favorite> sortFavoritesByName(List<Favorite> favoritesUnsorted) {
        List<Favorite> favoritesSorted = new ArrayList<Favorite>();
        List<String> favoritesNames = new ArrayList<String>();

        for (Favorite f1 : favoritesUnsorted) {
            favoritesNames.add(f1.get_name());
        }
        Collections.sort(favoritesNames);

        for (String name : favoritesNames) {
            for (Favorite f2 : favoritesUnsorted) {
                if (name.equals(f2.get_name())) {
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
        mAdapter = new FavoritesExpandableListAdapter(getActivity(), listHeaders, listData, sortType);

        listView.setAdapter(mAdapter);
    }
}
