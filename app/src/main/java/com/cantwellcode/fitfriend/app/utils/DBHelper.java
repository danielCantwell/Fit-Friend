package com.cantwellcode.fitfriend.app.utils;

import android.content.Context;

import com.cantwellcode.fitfriend.app.exercise.types.Workout;
import com.cantwellcode.fitfriend.app.nutrition.FavoriteMeal;
import com.cantwellcode.fitfriend.app.nutrition.Meal;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

import java.util.ArrayList;

/**
 * Created by Daniel on 4/17/2014.
 */
public class DBHelper {

    private String dbPath;
    private ObjectContainer db;

    public DBHelper(Context context) {
        dbPath = context.getFilesDir() + "/android.fitfriend";
    }

    public static String DB_NAME_NUTRITION = "Nutrition_Database";
    public static String DB_NAME_FAVORITE_MEALS = "FavoriteMeals_Database";
    public static String DB_NAME_WORKOUTS = "Workout_Database";

    public boolean openDb(String name) {
        if (name != null) {
            db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), dbPath + "." + name);
            return true;
        }
        return false;
    }

    public void closeDb() {
        db.close();
    }

    /***********************************************************************
     ****                    D a t a   S t o r a g e                    ****
     ***********************************************************************/

    /**
    Store a meal
     */
    public void store(Object object) {
        if (object instanceof FavoriteMeal)
            openDb(DB_NAME_FAVORITE_MEALS);
        else if (object instanceof Meal)
            openDb(DB_NAME_NUTRITION);
        else
            openDb(DB_NAME_WORKOUTS);
        db.store(object);
        db.commit();
        closeDb();
    }

    /***********************************************************************
     ****           S i n g l e   D a t a   R e t r i e v a l           ****
     ***********************************************************************/

    /**
    get a meal from the database
     */
    public Object get(Object object) {

        if (object instanceof FavoriteMeal)
            openDb(DB_NAME_FAVORITE_MEALS);
        else if (object instanceof Meal)
            openDb(DB_NAME_NUTRITION);
        else
            openDb(DB_NAME_WORKOUTS);

        ObjectSet result = db.queryByExample(object);

        if (result.hasNext()) {
            Object o = result.next();
            closeDb();
            return o;
        }
        closeDb();
        return null;
    }

    /***********************************************************************
     ****              A l l   D a t a   R e t r i e v a l              ****
     ***********************************************************************/

    /**
    return all meals
     */
    public ArrayList<Meal> getAllMeals() {

        ArrayList<Meal> list = new ArrayList<Meal>();
        Meal meal = new Meal();

        openDb(DB_NAME_NUTRITION);
        ObjectSet<Meal> result = db.queryByExample(meal);

        while (result.hasNext()) {
            list.add(result.next());
        }

        closeDb();
        return list;
    }

    /**
    return all favorite meals
     */
    public ArrayList<FavoriteMeal> getAllFavorites() {

        ArrayList<FavoriteMeal> list = new ArrayList<FavoriteMeal>();
        FavoriteMeal meal = new FavoriteMeal();

        openDb(DB_NAME_FAVORITE_MEALS);
        ObjectSet<FavoriteMeal> result = db.queryByExample(meal);

        while (result.hasNext()) {
            list.add(result.next());
        }

        closeDb();
        return list;
    }
    /**
     return all workouts
     */
    public ArrayList<Workout> getAllWorkouts() {

        ArrayList<Workout> list = new ArrayList<Workout>();
        Workout workout = new Workout();

        openDb(DB_NAME_WORKOUTS);
        ObjectSet<Workout> result = db.queryByExample(workout);

        while (result.hasNext()) {
            list.add(result.next());
        }

        closeDb();
        return list;
    }

    /***********************************************************************
     ****             L i s t   D a t a   R e t r i e v a l             ****
     ***********************************************************************/

    /**
    return a list of meals
     */
    public ArrayList<Meal> getMealList(Meal meal) {

        ArrayList<Meal> list = new ArrayList<Meal>();

        openDb(DB_NAME_NUTRITION);
        ObjectSet<Meal> result = db.queryByExample(meal);

        while (result.hasNext()) {
            list.add(result.next());
        }

        closeDb();
        return list;
    }

    /**
     return a list of favorite meals
     */
    public ArrayList<FavoriteMeal> getFavoriteMealList(FavoriteMeal meal) {

        ArrayList<FavoriteMeal> list = new ArrayList<FavoriteMeal>();

        openDb(DB_NAME_FAVORITE_MEALS);
        ObjectSet<FavoriteMeal> result = db.queryByExample(meal);

        while (result.hasNext()) {
            list.add(result.next());
        }

        closeDb();
        return list;
    }

    /**
     return a list of workouts
     */
    public ArrayList<Workout> getWorkoutList(Workout workout) {

        ArrayList<Workout> list = new ArrayList<Workout>();

        openDb(DB_NAME_WORKOUTS);
        ObjectSet<Workout> result = db.queryByExample(workout);

        while (result.hasNext()) {
            list.add(result.next());
        }

        closeDb();
        return list;
    }

    /***********************************************************************
     ****                     U p d a t e   D a t a                     ****
     ***********************************************************************/

    /**
    update a meal in the database
     */
    public boolean updateMeal(Meal ObjTo, Meal ObjFrom) {
        Meal found = null;

        openDb(DB_NAME_NUTRITION);
        ObjectSet<Meal> result = db.queryByExample(ObjFrom);

        if (result.hasNext()) { // if found

            found = result.next();

            found.setName(ObjTo.getName());
            found.setDate(ObjTo.getDate());
            found.setType(ObjTo.getType());
            found.setCalories(ObjTo.getCalories());
            found.setFat(ObjTo.getFat());
            found.setCarbs(ObjTo.getCarbs());
            found.setProtein(ObjTo.getProtein());

            db.store(found);
            db.commit();

            closeDb();
            return true;
        }

        closeDb();
        return false;
    }

    /***********************************************************************
     ****                     D e l e t e   D a t a                     ****
     ***********************************************************************/

    /**
    deal a meal in the database
     */
    public boolean delete(Object object) {
        Object found = null;

        if (object instanceof FavoriteMeal)
            openDb(DB_NAME_FAVORITE_MEALS);
        else if (object instanceof Meal)
            openDb(DB_NAME_NUTRITION);
        else
            openDb(DB_NAME_WORKOUTS);
        ObjectSet<Object> result = db.queryByExample(object);

        if (result.hasNext()) {

            found = result.next();
            db.delete(found);
            closeDb();
            return true;
        } else {
            closeDb();
            return false;
        }

    }
}
