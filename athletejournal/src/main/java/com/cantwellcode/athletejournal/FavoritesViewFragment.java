package com.cantwellcode.athletejournal;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Daniel on 3/6/14.
 */
public class FavoritesViewFragment extends Fragment {

    public static enum SortFavoritesBy { Type, Category };
    private SortFavoritesBy sortType = SortFavoritesBy.Category;

    private Context context;

    private Button typeSort;
    private Button categorySort;

    // SQLite Database
    private Database db;

    private ExpandableListView listView;
    private List<Favorite> meals;
    private FavoritesExpandableListAdapter mAdapter;
    private List<String> listHeaders;
    private HashMap<String, List<Favorite>> listData;

    public static Fragment newInstance(Context context) {
        FavoritesViewFragment f = new FavoritesViewFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.favorites_list_view, null);

        context = getActivity();

        db = new Database(getActivity(), Database.DATABASE_NAME, null, Database.DATABASE_VERSION);
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
                    Log.d("Favorite LongClick", "Group " + groupPosition + " Item " + childPosition);
                    Favorite meal = (Favorite) mAdapter.getChild(groupPosition, childPosition);
                    showPopup(view, meal);

                    return true;
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
            }
        });

        setHasOptionsMenu(true);

        return root;
    }

    private void prepareListData(Database db) {
        listHeaders = new ArrayList<String>();
        listData = new HashMap<String, List<Favorite>>();

        List<Favorite> favorites = db.getAllFavorites();

        if (sortType == SortFavoritesBy.Category) {
            // If user selects "category" sort type
            listHeaders = db.getFavoriteCategories();
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
                listData.put(header, favoritesInType);
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
                        .replace(R.id.container, AddFavoriteFragment.newInstance(getActivity(), AddFavoriteFragment.InstanceType.NewFavorite))
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

    private void menuClickEdit(Favorite meal) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putInt("FavoriteToEdit_ID",           meal.get_id()).commit();
        sp.edit().putString("FavoriteToEdit_Name",      meal.get_name()).commit();
        sp.edit().putString("FavoriteToEdit_Category",  meal.get_category()).commit();
        sp.edit().putString("FavoriteToEdit_Type",      meal.get_type()).commit();
        sp.edit().putString("FavoriteToEdit_Calories",  meal.get_calories()).commit();
        sp.edit().putString("FavoriteToEdit_Protein",   meal.get_protein()).commit();
        sp.edit().putString("FavoriteToEdit_Carbs",     meal.get_carbs()).commit();
        sp.edit().putString("FavoriteToEdit_Fat",       meal.get_fat()).commit();

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .replace(R.id.container, AddFavoriteFragment.newInstance(getActivity(), AddFavoriteFragment.InstanceType.EditFavorite))
                .commit();
    }

    private void menuClickDelete(Favorite meal) {
        db.deleteFavorite(meal);
        meals = db.getAllFavorites();
        if (meals.isEmpty()) {
            Toast.makeText(getActivity(), "No Favorites Have Been Added", Toast.LENGTH_LONG).show();
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
}
