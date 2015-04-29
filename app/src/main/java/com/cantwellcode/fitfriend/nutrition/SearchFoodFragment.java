package com.cantwellcode.fitfriend.nutrition;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.cantwellcode.fitfriend.R;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daniel on 11/8/2014.
 */
public class SearchFoodFragment extends Fragment {

    private NewFoodActivity mActivity;

    private SearchView mSearch;
    private ListView mList;
    private TextView mEmpty;

    private ParseQueryAdapter.QueryFactory<FoodDatabase> mFactory;
    private ParseQueryAdapter<FoodDatabase> mAdapter;

    public static SearchFoodFragment newInstance() {
        return new SearchFoodFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_food_search, null);

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());

        mSearch = (SearchView) root.findViewById(R.id.search);
        mList = (ListView) root.findViewById(R.id.list);
        mEmpty = (TextView) root.findViewById(android.R.id.empty);

        mList.setEmptyView(mEmpty);

        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {

                if (query.trim().length() < 3) {
                    Toast.makeText(getActivity(), "Please enter at leat 3 letters", Toast.LENGTH_SHORT).show();
                    return false;
                }

                progressDialog.setMessage("Searching for " + query);
                progressDialog.show();

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                // Parse Query Factory + Adapter to fill list
                mFactory = new ParseQueryAdapter.QueryFactory<FoodDatabase>() {
                    @Override
                    public ParseQuery<FoodDatabase> create() {
                        /* Query for food where the input matches the name */
                        ParseQuery<FoodDatabase> nameQuery = FoodDatabase.getQuery();
                        nameQuery.whereContains("name", query);

                        /* Query for food where the input matches the brand */
                        ParseQuery<FoodDatabase> brandQuery = FoodDatabase.getQuery();
                        brandQuery.whereContains("brand", query);

                        /* Put both queries in a list */
                        List<ParseQuery<FoodDatabase>> queries = new ArrayList<ParseQuery<FoodDatabase>>();
                        queries.add(nameQuery);
                        queries.add(brandQuery);

                        /* Create the "OR" of the two queries */
                        ParseQuery<FoodDatabase> searchQuery = ParseQuery.or(queries);
                        return searchQuery;
                    }
                };

                mAdapter = new ParseQueryAdapter<FoodDatabase>(getActivity(), mFactory) {
                    @Override
                    public View getItemView(final FoodDatabase foodDatabase, View view, ViewGroup parent) {

                        ViewHolder holder = null;

                        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                        if (view == null) {
                            view = inflater.inflate(R.layout.nutrition_search_data, null);
                            holder = new ViewHolder();
                            holder.name = (TextView) view.findViewById(R.id.n_data_name);
                            holder.brand = (TextView) view.findViewById(R.id.n_data_brand);
                            holder.size = (TextView) view.findViewById(R.id.n_data_size);
                            holder.calories = (TextView) view.findViewById(R.id.n_data_calories);
                            holder.protein = (TextView) view.findViewById(R.id.n_data_protein);
                            holder.carbs = (TextView) view.findViewById(R.id.n_data_carbs);
                            holder.fat = (TextView) view.findViewById(R.id.n_data_fat);
                            view.setTag(holder);
                        } else {
                            holder = (ViewHolder) view.getTag();
                        }

                        holder.name.setText(foodDatabase.getName());
                        holder.calories.setText("" + foodDatabase.getCalories());
                        holder.protein.setText("" + foodDatabase.getProtein());
                        holder.carbs.setText("" + foodDatabase.getCarbs());
                        holder.fat.setText("" + foodDatabase.getFat());

                        holder.brand.setText(foodDatabase.getBrand());
                        holder.size.setText(foodDatabase.getSize());

                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mActivity.setDetails(foodDatabase.getName(),
                                        foodDatabase.getCalories(), foodDatabase.getFat(), foodDatabase.getCarbs(), foodDatabase.getProtein());
                                Toast.makeText(mActivity, "(" + foodDatabase.getBrand() + ") " + foodDatabase.getName(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        return view;
                    }
                };

                mList.setAdapter(mAdapter);

                mAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<FoodDatabase>() {
                    @Override
                    public void onLoading() {

                    }

                    @Override
                    public void onLoaded(List<FoodDatabase> foodDatabases, Exception e) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        if (e != null) {
                            mEmpty.setText(e.getMessage());
                        } else if (foodDatabases == null || foodDatabases.isEmpty()) {
                            mEmpty.setText("No food found");
                        }
                    }
                });

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return root;
    }

    public void setActivity(NewFoodActivity activity) {
        this.mActivity = activity;
    }

    /*
    Used for ParseQueryAdapter
     */
    private class ViewHolder {
        TextView name;
        TextView size;
        TextView brand;
        TextView calories;
        TextView protein;
        TextView carbs;
        TextView fat;
    }
}
