package com.cantwellcode.athletejournal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Daniel on 3/12/14.
 */
public class CategoryDialog extends DialogFragment {

    private enum InstanceType {
        Single, Group
    };

    private InstanceType type;
    private Nutrition meal;
    private DBHelper db;
    private String category;

    private NutritionFavoritesView viewFragment;

    public CategoryDialog(Nutrition meal, DBHelper db) {
        this.meal = meal;
        this.db = db;
        type = InstanceType.Single;
    }

    public CategoryDialog(NutritionFavoritesView viewFragment,String category, DBHelper db) {
        this.db = db;
        this.category = category;
        type = InstanceType.Group;
        this.viewFragment = viewFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View root = inflater.inflate(R.layout.category_dialog, null);

        final AutoCompleteTextView categoryText = (AutoCompleteTextView) root.findViewById(R.id.categoryAutoComplete);

        List<String> categoryList = db.getFavoritesCategories();
        ArrayAdapter categoryAdapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_dropdown_item_1line, categoryList);
        categoryText.setAdapter(categoryAdapter);

        if (type == InstanceType.Single) {
            builder.setView(root)
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            db.storeFavorite(new Favorite(meal.get_name(), categoryText.getText().toString(), meal.get_type(),
                                    meal.get_calories(), meal.get_protein(), meal.get_carbs(), meal.get_fat()));
                            Toast.makeText(getActivity(),
                                    meal.get_name() + " has been added as a favorite under the " +
                                            categoryText.getText().toString() + " category", Toast.LENGTH_LONG
                            ).show();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            CategoryDialog.this.getDialog().cancel();
                        }
                    });
        }
        else if (type == InstanceType.Group) {
            categoryText.setHint("New Name for Category: \n" + category);
            builder.setView(root)
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            Favorite f = new Favorite(null, category, null, null, null, null, null);
                            List<Favorite> favs = db.getFavoriteMealList(f);

                            for (Favorite favorite : favs) {
                                Favorite newFav = favorite;
                                newFav.set_category(categoryText.getText().toString());
                                db.updateFavorite(favorite, newFav);
                            }

                            viewFragment.reloadList();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            CategoryDialog.this.getDialog().cancel();
                        }
                    });
        }
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
