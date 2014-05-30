package com.cantwellcode.ipsum.Utils;

import android.content.Context;

import com.cantwellcode.ipsum.Exercise.Bike;
import com.cantwellcode.ipsum.Exercise.Gym;
import com.cantwellcode.ipsum.Exercise.Run;
import com.cantwellcode.ipsum.Exercise.Swim;
import com.cantwellcode.ipsum.Nutrition.Log.Favorite;
import com.cantwellcode.ipsum.Nutrition.Log.Nutrition;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

import java.util.ArrayList;

/**
 * Created by Daniel on 4/17/2014.
 */
public class DBHelper {

    private Context context;
    private String dbPath;
    private ObjectContainer db;

    public DBHelper(Context context) {
        this.context = context;
        dbPath = context.getFilesDir() + "/android.ipsum";
    }

    public static String DB_NAME_NUTRITION = "Nutrition_Database";
    public static String DB_NAME_FAVORITES = "Favorites_Database";
    public static String DB_NAME_WORKOUTS = "Workout_Database";

    private boolean openDb(String name) {
        if (name != null) {
            db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), dbPath + "." + name);
            return true;
        }
        return false;
    }

    private void closeDb() {
        db.close();
    }

    /***********************************************************************
     ****                    D a t a   S t o r a g e                    ****
     ***********************************************************************/

    /**
    Store a meal
     */
    public void store(Object object) {
        if (object instanceof Nutrition)
            openDb(DB_NAME_NUTRITION);
        else if (object instanceof Favorite)
            openDb(DB_NAME_FAVORITES);
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

        if (object instanceof Nutrition)
            openDb(DB_NAME_NUTRITION);
        else if (object instanceof Favorite)
            openDb(DB_NAME_FAVORITES);
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
    public ArrayList<Nutrition> getAllMeals() {

        ArrayList<Nutrition> list = new ArrayList<Nutrition>();
        Nutrition meal = new Nutrition();

        openDb(DB_NAME_NUTRITION);
        ObjectSet<Nutrition> result = db.queryByExample(meal);

        while (result.hasNext()) {
            list.add(result.next());
        }

        closeDb();
        return list;
    }

    /**
    return all favorite meals
     */
    public ArrayList<Favorite> getAllFavorites() {

        ArrayList<Favorite> list = new ArrayList<Favorite>();
        Favorite meal = new Favorite();

        openDb(DB_NAME_FAVORITES);
        ObjectSet<Favorite> result = db.queryByExample(meal);

        while (result.hasNext()) {
            list.add(result.next());
        }

        closeDb();
        return list;
    }

    /**
    return all swim workouts
     */
    public ArrayList<Swim> getAllSwimWorkouts() {

        ArrayList<Swim> list = new ArrayList<Swim>();
        Swim workout = new Swim();

        openDb(DB_NAME_WORKOUTS);
        ObjectSet<Swim> result = db.queryByExample(workout);

        while (result.hasNext()) {
            list.add(result.next());
        }

        closeDb();
        return list;
    }

    /**
    return all bike workouts
     */
    public ArrayList<Bike> getAllBikeWorkouts() {

        ArrayList<Bike> list = new ArrayList<Bike>();
        Bike workout = new Bike();

        openDb(DB_NAME_WORKOUTS);
        ObjectSet<Bike> result = db.queryByExample(workout);

        while (result.hasNext()) {
            list.add(result.next());
        }

        closeDb();
        return list;
    }

    /**
    return all run workouts
     */
    public ArrayList<Run> getAllRunWorkouts() {

        ArrayList<Run> list = new ArrayList<Run>();
        Run workout = new Run();

        openDb(DB_NAME_WORKOUTS);
        ObjectSet<Run> result = db.queryByExample(workout);

        while (result.hasNext()) {
            list.add(result.next());
        }

        closeDb();
        return list;
    }

    /**
    return all gym workouts
     */
    public ArrayList<Gym> getAllGymWorkouts() {

        ArrayList<Gym> list = new ArrayList<Gym>();
        Gym workout = new Gym();

        openDb(DB_NAME_WORKOUTS);
        ObjectSet<Gym> result = db.queryByExample(workout);

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
    public ArrayList<Nutrition> getMealList(Nutrition meal) {

        ArrayList<Nutrition> list = new ArrayList<Nutrition>();

        openDb(DB_NAME_NUTRITION);
        ObjectSet<Nutrition> result = db.queryByExample(meal);

        while (result.hasNext()) {
            list.add(result.next());
        }

        closeDb();
        return list;
    }

    /**
    return a list of favorite meals
     */
    public ArrayList<Favorite> getFavoriteMealList(Favorite meal) {

        ArrayList<Favorite> list = new ArrayList<Favorite>();

        openDb(DB_NAME_FAVORITES);
        ObjectSet<Favorite> result = db.queryByExample(meal);

        while (result.hasNext()) {
            list.add(result.next());
        }

        closeDb();
        return list;
    }

    /**
    return favorites categories
     */
    public ArrayList<String> getFavoritesCategories() {

        ArrayList<Favorite> favorites = getAllFavorites();
        ArrayList<String> categories = new ArrayList<String>();

        for (Favorite favorite : favorites) {
            if (!categories.contains(favorite.get_category())) {
                categories.add(favorite.get_category());
            }
        }

        return categories;
    }

    /**
    return a list of swim workouts
     */
    public ArrayList<Swim> getSwimList(Swim workout) {

        ArrayList<Swim> list = new ArrayList<Swim>();

        openDb(DB_NAME_WORKOUTS);
        ObjectSet<Swim> result = db.queryByExample(workout);

        while (result.hasNext()) {
            list.add(result.next());
        }

        closeDb();
        return list;
    }

    /**
    return a list of bike workouts
     */
    public ArrayList<Bike> getBikeList(Bike workout) {

        ArrayList<Bike> list = new ArrayList<Bike>();

        openDb(DB_NAME_WORKOUTS);
        ObjectSet<Bike> result = db.queryByExample(workout);

        while (result.hasNext()) {
            list.add(result.next());
        }

        closeDb();
        return list;
    }

    /**
    return a list of run workouts
     */
    public ArrayList<Run> getRunList(Run workout) {

        ArrayList<Run> list = new ArrayList<Run>();

        openDb(DB_NAME_WORKOUTS);
        ObjectSet<Run> result = db.queryByExample(workout);

        while (result.hasNext()) {
            list.add(result.next());
        }

        closeDb();
        return list;
    }

    /**
    return a list of gym workouts
     */
    public ArrayList<Gym> getGymList(Gym workout) {

        ArrayList<Gym> list = new ArrayList<Gym>();

        openDb(DB_NAME_WORKOUTS);
        ObjectSet<Gym> result = db.queryByExample(workout);

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
    public boolean updateMeal(Nutrition ObjTo, Nutrition ObjFrom) {
        Nutrition found = null;

        openDb(DB_NAME_NUTRITION);
        ObjectSet<Nutrition> result = db.queryByExample(ObjTo);

        if (result.hasNext()) { // if found

            found = result.next();

            found.set_name(ObjFrom.get_name());
            found.set_date(ObjFrom.get_date());
            found.set_type(ObjFrom.get_type());
            found.set_calories(ObjFrom.get_calories());
            found.set_fat(ObjFrom.get_fat());
            found.set_carbs(ObjFrom.get_carbs());
            found.set_protein(ObjFrom.get_protein());

            db.store(found);
            db.commit();

            closeDb();
            return true;
        }

        closeDb();
        return false;
    }

    /**
    update a favorite meal in the database
     */
    public boolean updateFavorite(Favorite ObjTo, Favorite ObjFrom) {
        Favorite found = null;

        openDb(DB_NAME_FAVORITES);
        ObjectSet<Favorite> result = db.queryByExample(ObjTo);

        if (result.hasNext()) { // if found

            found = result.next();

            found.set_name(ObjFrom.get_name());
            found.set_category(ObjFrom.get_category());
            found.set_type(ObjFrom.get_type());
            found.set_calories(ObjFrom.get_calories());
            found.set_fat(ObjFrom.get_fat());
            found.set_carbs(ObjFrom.get_carbs());
            found.set_protein(ObjFrom.get_protein());

            db.store(found);
            db.commit();

            closeDb();
            return true;
        }

        closeDb();
        return false;
    }

    /**
     update a swim in the database
     */
    public boolean updateSwim(Swim ObjTo, Swim ObjFrom) {
        Swim found = null;

        openDb(DB_NAME_WORKOUTS);
        ObjectSet<Swim> result = db.queryByExample(ObjTo);

        if (result.hasNext()) { // if found

            found = result.next();

            found.setName(ObjFrom.getName());
            found.setDate(ObjFrom.getDate());
            found.setType(ObjFrom.getType());
            found.setNotes(ObjFrom.getNotes());
            found.setAvgPace(ObjFrom.getAvgPace());
            found.setMaxPace(ObjFrom.getMaxPace());
            found.setDistance(ObjFrom.getDistance());
            found.setTime(ObjFrom.getTime());
            found.setStrokeRate(ObjFrom.getStrokeRate());
            found.setCalBurned(ObjFrom.getCalBurned());

            db.store(found);
            db.commit();

            closeDb();
            return true;
        }

        closeDb();
        return false;
    }

    /**
     update a run in the database
     */
    public boolean updateRun(Run ObjTo, Run ObjFrom) {
        Run found = null;

        openDb(DB_NAME_WORKOUTS);
        ObjectSet<Run> result = db.queryByExample(ObjTo);

        if (result.hasNext()) { // if found

            found = result.next();

            found.setName(ObjFrom.getName());
            found.setDate(ObjFrom.getDate());
            found.setType(ObjFrom.getType());
            found.setNotes(ObjFrom.getNotes());
            found.setAvgPace(ObjFrom.getAvgPace());
            found.setMaxPace(ObjFrom.getMaxPace());
            found.setAvgHR(ObjFrom.getAvgHR());
            found.setMaxHR(ObjFrom.getMaxHR());
            found.setDistance(ObjFrom.getDistance());
            found.setTime(ObjFrom.getTime());
            found.setElevation(ObjFrom.getElevation());
            found.setCalBurned(ObjFrom.getCalBurned());

            db.store(found);
            db.commit();

            closeDb();
            return true;
        }

        closeDb();
        return false;
    }

    /**
     update a bike in the database
     */
    public boolean updateBike(Bike ObjTo, Bike ObjFrom) {
        Bike found = null;

        openDb(DB_NAME_WORKOUTS);
        ObjectSet<Bike> result = db.queryByExample(ObjTo);

        if (result.hasNext()) { // if found

            found = result.next();

            found.setName(ObjFrom.getName());
            found.setDate(ObjFrom.getDate());
            found.setType(ObjFrom.getType());
            found.setNotes(ObjFrom.getNotes());
            found.setAvgSpeed(ObjFrom.getAvgSpeed());
            found.setMaxSpeed(ObjFrom.getMaxSpeed());
            found.setAvgCadence(ObjFrom.getAvgCadence());
            found.setMaxCadence(ObjFrom.getMaxCadence());
            found.setAvgHR(ObjFrom.getAvgHR());
            found.setMaxHR(ObjFrom.getMaxHR());
            found.setDistance(ObjFrom.getDistance());
            found.setTime(ObjFrom.getTime());
            found.setElevation(ObjFrom.getElevation());
            found.setCalBurned(ObjFrom.getCalBurned());

            db.store(found);
            db.commit();

            closeDb();
            return true;
        }

        closeDb();
        return false;
    }

    /**
     update a gym in the database
     */
    public boolean updateGym(Gym ObjTo, Gym ObjFrom) {
        Gym found = null;

        openDb(DB_NAME_WORKOUTS);
        ObjectSet<Gym> result = db.queryByExample(ObjTo);

        if (result.hasNext()) { // if found

            found = result.next();

            found.setName(ObjFrom.getName());
            found.setDate(ObjFrom.getDate());
            found.setType(ObjFrom.getType());
            found.setRoutines(ObjFrom.getRoutines());

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

        if (object instanceof Nutrition)
            openDb(DB_NAME_NUTRITION);
        else if (object instanceof Favorite)
            openDb(DB_NAME_FAVORITES);
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
