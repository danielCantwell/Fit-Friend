package com.cantwellcode.athletejournal;

import android.content.Context;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

import java.util.ArrayList;

/**
 * Created by Daniel on 4/17/2014.
 */
public class DBHelper {

    Context context;
    String dbPath;
    ObjectContainer db;

    public DBHelper(Context context) {
        this.context = context;
        dbPath = context.getFilesDir() + "/android.athleteJournal";
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
    void store(Object object) {
        if (object instanceof Nutrition)
            openDb(DB_NAME_NUTRITION);
        else if (object instanceof Favorite)
            openDb(DB_NAME_FAVORITES);
        else if (object instanceof Workout)
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
    Object get(Object object) {

        if (object instanceof Nutrition)
            openDb(DB_NAME_NUTRITION);
        else if (object instanceof Favorite)
            openDb(DB_NAME_FAVORITES);
        else if (object instanceof Workout)
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
    ArrayList<Nutrition> getAllMeals() {

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
    ArrayList<Favorite> getAllFavorites() {

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
    return all workouts
     */
    ArrayList<Workout> getAllWorkouts() {

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

    /**
    return all swim workouts
     */
    ArrayList<WorkoutSwim> getAllSwimWorkouts() {

        ArrayList<WorkoutSwim> list = new ArrayList<WorkoutSwim>();
        WorkoutSwim workout = new WorkoutSwim();

        openDb(DB_NAME_WORKOUTS);
        ObjectSet<WorkoutSwim> result = db.queryByExample(workout);

        while (result.hasNext()) {
            list.add(result.next());
        }

        closeDb();
        return list;
    }

    /**
    return all bike workouts
     */
    ArrayList<WorkoutBike> getAllBikeWorkouts() {

        ArrayList<WorkoutBike> list = new ArrayList<WorkoutBike>();
        WorkoutBike workout = new WorkoutBike();

        openDb(DB_NAME_WORKOUTS);
        ObjectSet<WorkoutBike> result = db.queryByExample(workout);

        while (result.hasNext()) {
            list.add(result.next());
        }

        closeDb();
        return list;
    }

    /**
    return all run workouts
     */
    ArrayList<WorkoutRun> getAllRunWorkouts() {

        ArrayList<WorkoutRun> list = new ArrayList<WorkoutRun>();
        WorkoutRun workout = new WorkoutRun();

        openDb(DB_NAME_WORKOUTS);
        ObjectSet<WorkoutRun> result = db.queryByExample(workout);

        while (result.hasNext()) {
            list.add(result.next());
        }

        closeDb();
        return list;
    }

    /**
    return all gym workouts
     */
    ArrayList<WorkoutGym> getAllGymWorkouts() {

        ArrayList<WorkoutGym> list = new ArrayList<WorkoutGym>();
        WorkoutGym workout = new WorkoutGym();

        openDb(DB_NAME_WORKOUTS);
        ObjectSet<WorkoutGym> result = db.queryByExample(workout);

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
    ArrayList<Nutrition> getMealList(Nutrition meal) {

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
    ArrayList<Favorite> getFavoriteMealList(Favorite meal) {

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
    ArrayList<String> getFavoritesCategories() {

        ArrayList<Favorite> favorites = getAllFavorites();
        ArrayList<String> categories = new ArrayList<String>();

        for (Favorite favorite : favorites) {
            if (!categories.contains(favorite._category)) {
                categories.add(favorite._category);
            }
        }

        return categories;
    }

    /**
    return a list of workouts
     */
    ArrayList<Workout> getWorkoutList(Workout workout) {

        ArrayList<Workout> list = new ArrayList<Workout>();

        openDb(DB_NAME_WORKOUTS);
        ObjectSet<Workout> result = db.queryByExample(workout);

        while (result.hasNext()) {
            list.add(result.next());
        }

        closeDb();
        return list;
    }

    /**
    return a list of swim workouts
     */
    ArrayList<WorkoutSwim> getSwimList(WorkoutSwim workout) {

        ArrayList<WorkoutSwim> list = new ArrayList<WorkoutSwim>();

        openDb(DB_NAME_WORKOUTS);
        ObjectSet<WorkoutSwim> result = db.queryByExample(workout);

        while (result.hasNext()) {
            list.add(result.next());
        }

        closeDb();
        return list;
    }

    /**
    return a list of bike workouts
     */
    ArrayList<WorkoutBike> getBikeList(WorkoutBike workout) {

        ArrayList<WorkoutBike> list = new ArrayList<WorkoutBike>();

        openDb(DB_NAME_WORKOUTS);
        ObjectSet<WorkoutBike> result = db.queryByExample(workout);

        while (result.hasNext()) {
            list.add(result.next());
        }

        closeDb();
        return list;
    }

    /**
    return a list of run workouts
     */
    ArrayList<WorkoutRun> getRunList(WorkoutRun workout) {

        ArrayList<WorkoutRun> list = new ArrayList<WorkoutRun>();

        openDb(DB_NAME_WORKOUTS);
        ObjectSet<WorkoutRun> result = db.queryByExample(workout);

        while (result.hasNext()) {
            list.add(result.next());
        }

        closeDb();
        return list;
    }

    /**
    return a list of gym workouts
     */
    ArrayList<WorkoutGym> getGymList(WorkoutGym workout) {

        ArrayList<WorkoutGym> list = new ArrayList<WorkoutGym>();

        openDb(DB_NAME_WORKOUTS);
        ObjectSet<WorkoutGym> result = db.queryByExample(workout);

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
    boolean updateMeal(Nutrition ObjTo, Nutrition ObjFrom) {
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
    boolean updateFavorite(Favorite ObjTo, Favorite ObjFrom) {
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

    /***********************************************************************
     ****                     D e l e t e   D a t a                     ****
     ***********************************************************************/

    /**
    deal a meal in the database
     */
    boolean delete(Object object) {
        Object found = null;

        if (object instanceof Nutrition)
            openDb(DB_NAME_NUTRITION);
        else if (object instanceof Favorite)
            openDb(DB_NAME_FAVORITES);
        else if (object instanceof Workout)
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
