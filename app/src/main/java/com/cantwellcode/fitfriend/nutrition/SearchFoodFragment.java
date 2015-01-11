package com.cantwellcode.fitfriend.nutrition;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private ParseQueryAdapter.QueryFactory<Food> mFactory;
    private ParseQueryAdapter<Food> mAdapter;

    public static SearchFoodFragment newInstance() {
        return new SearchFoodFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_food_search, null);

        mSearch = (SearchView) root.findViewById(R.id.search);
        mList = (ListView) root.findViewById(R.id.list);

        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {

                if (query.trim().length() < 3) {
                    Toast.makeText(getActivity(), "Please enter at leat 3 letters", Toast.LENGTH_SHORT).show();
                    return false;
                }

                // Parse Query Factory + Adapter to fill list
                mFactory = new ParseQueryAdapter.QueryFactory<Food>() {
                    @Override
                    public ParseQuery<Food> create() {
                        /* Query for food where the input matches the name */
                        ParseQuery<Food> nameQuery = Food.getQuery();
                        nameQuery.whereContains("name", query);

                        /* Query for food where the input matches the brand */
                        ParseQuery<Food> brandQuery = Food.getQuery();
                        brandQuery.whereContains("brand", query);

                        /* Put both queries in a list */
                        List<ParseQuery<Food>> queries = new ArrayList<ParseQuery<Food>>();
                        queries.add(nameQuery);
                        queries.add(brandQuery);

                        /* Create the "OR" of the two queries */
                        ParseQuery<Food> searchQuery = ParseQuery.or(queries);
                        return searchQuery;
                    }
                };

                mAdapter = new ParseQueryAdapter<Food>(getActivity(), mFactory) {
                    @Override
                    public View getItemView(final Food food, View view, ViewGroup parent) {

                        ViewHolder holder = null;

                        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                        if (view == null) {
                            view = inflater.inflate(R.layout.nutrition_data, null);
                            holder = new ViewHolder();
                            holder.name = (TextView) view.findViewById(R.id.n_data_name);
                            holder.type = (TextView) view.findViewById(R.id.n_data_type);
                            holder.date = (TextView) view.findViewById(R.id.n_data_date);
                            holder.calories = (TextView) view.findViewById(R.id.n_data_calories);
                            holder.protein = (TextView) view.findViewById(R.id.n_data_protein);
                            holder.carbs = (TextView) view.findViewById(R.id.n_data_carbs);
                            holder.fat = (TextView) view.findViewById(R.id.n_data_fat);
                            view.setTag(holder);
                        } else {
                            holder = (ViewHolder) view.getTag();
                        }

                        holder.name.setText(food.getName());
                        holder.calories.setText("" + food.getCalories());
                        holder.protein.setText("" + food.getProtein());
                        holder.carbs.setText("" + food.getCarbs());
                        holder.fat.setText("" + food.getFat());

                        // In the search interface
                        // the date textview is used for the brand
                        // the type textview is used for the serving size
                        holder.date.setText(food.getBrand());
                        holder.type.setText(food.getSize());

                        holder.name.setTextColor(getActivity().getResources().getColor(R.color.pomegranate));

                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mActivity.setDetails(food.getName(),
                                    food.getCalories(), food.getFat(), food.getCarbs(), food.getProtein());
                                Toast.makeText(mActivity, "(" + food.getBrand() + ") " + food.getName(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        return view;
                    }
                };

                mList.setAdapter(mAdapter);

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
        TextView type;
        TextView date;
        TextView calories;
        TextView protein;
        TextView carbs;
        TextView fat;
    }
}
