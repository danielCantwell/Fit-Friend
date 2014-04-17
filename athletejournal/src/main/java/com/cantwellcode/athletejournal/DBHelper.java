package com.cantwellcode.athletejournal;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.internal.Null;

import java.util.ArrayList;

/**
 * Created by Daniel on 4/17/2014.
 */
public class DBHelper {

    String dbPath;
    ObjectContainer db;

    boolean openDb(String name) {
        if (name != null) {
            db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), name);
            return true;
        }
        return false;
    }

    void closeDb() {
        db.close();
    }

    /***********************************************************************
     ****                    D a t a   S t o r a g e                    ****
     ***********************************************************************/

    /*
    Store a meal
     */
    void storeMeal(Nutrition meal) {
            db.store(meal);
            db.commit();
    }

    /*
    Store a swim workout
     */
    void storeSwim(WorkoutSwim swim) {
        db.store(swim);
        db.commit();
    }

    /*
    Store a bike workout
     */
    void storeBike(WorkoutBike bike) {
        db.store(bike);
        db.commit();
    }

    /*
    Store a run workout
     */
    void storeRun(WorkoutRun run) {
        db.store(run);
        db.commit();
    }

    /*
    Store a gym workout
     */
    void storeGym(WorkoutGym gym) {
        db.store(gym);
        db.commit();
    }

    /***********************************************************************
     ****           S i n g l e   D a t a   R e t r i e v a l           ****
     ***********************************************************************/

    /*
    get a meal from the database
     */
    Nutrition getMeal(Nutrition object) {

        ObjectSet result = db.queryByExample(object);

        if (result.hasNext()) {
            return (Nutrition) result.next();
        }
        return null;
    }

    /*
    get a swim workout from the database
     */
    WorkoutSwim getSwim(WorkoutSwim object) {
        ObjectSet result = db.queryByExample(object);

        if (result.hasNext()) {
            return (WorkoutSwim) result.next();
        }
        return null;
    }

    /*
    get a bike workout from the database
     */
    WorkoutBike getBike(WorkoutBike object) {
        ObjectSet result = db.queryByExample(object);

        if (result.hasNext()) {
            return (WorkoutBike) result.next();
        }
        return null;
    }

    /*
    get a run workout from the database
     */
    WorkoutRun getRun(WorkoutRun object) {
        ObjectSet result = db.queryByExample(object);

        if (result.hasNext()) {
            return (WorkoutRun) result.next();
        }
        return null;
    }

    /*
    get a gym workout from the database
     */
    WorkoutGym getGym(WorkoutGym object) {
        ObjectSet result = db.queryByExample(object);

        if (result.hasNext()) {
            return (WorkoutGym) result.next();
        }
        return null;
    }

    /***********************************************************************
     ****              A l l   D a t a   R e t r i e v a l              ****
     ***********************************************************************/

    /*
    return all meals
     */
    ArrayList<Nutrition> getAllMeals() {

        ArrayList<Nutrition> list = new ArrayList<Nutrition>();
        Nutrition meal = new Nutrition();
        ObjectSet<Nutrition> result = db.queryByExample(meal);

        while (result.hasNext()) {
            list.add(result.next());
        }

        return list;
    }

    /*
    return all workouts
     */
    ArrayList<Workout> getAllWorkouts() {

        ArrayList<Workout> list = new ArrayList<Workout>();
        Workout workout = new Workout();
        ObjectSet<Workout> result = db.queryByExample(workout);

        while (result.hasNext()) {
            list.add(result.next());
        }

        return list;
    }

    /*
    return all swim workouts
     */
    ArrayList<WorkoutSwim> getAllSwimWorkouts() {

        ArrayList<WorkoutSwim> list = new ArrayList<WorkoutSwim>();
        WorkoutSwim workout = new WorkoutSwim();
        ObjectSet<WorkoutSwim> result = db.queryByExample(workout);

        while (result.hasNext()) {
            list.add(result.next());
        }

        return list;
    }

    /*
    return all bike workouts
     */
    ArrayList<WorkoutBike> getAllBikeWorkouts() {

        ArrayList<WorkoutBike> list = new ArrayList<WorkoutBike>();
        WorkoutBike workout = new WorkoutBike();
        ObjectSet<WorkoutBike> result = db.queryByExample(workout);

        while (result.hasNext()) {
            list.add(result.next());
        }

        return list;
    }

    /*
    return all run workouts
     */
    ArrayList<WorkoutRun> getAllRunWorkouts() {

        ArrayList<WorkoutRun> list = new ArrayList<WorkoutRun>();
        WorkoutRun workout = new WorkoutRun();
        ObjectSet<WorkoutRun> result = db.queryByExample(workout);

        while (result.hasNext()) {
            list.add(result.next());
        }

        return list;
    }

    /*
    return all gym workouts
     */
    ArrayList<WorkoutGym> getAllGymWorkouts() {

        ArrayList<WorkoutGym> list = new ArrayList<WorkoutGym>();
        WorkoutGym workout = new WorkoutGym();
        ObjectSet<WorkoutGym> result = db.queryByExample(workout);

        while (result.hasNext()) {
            list.add(result.next());
        }

        return list;
    }

    /***********************************************************************
     ****              A l l   D a t a   R e t r i e v a l              ****
     ***********************************************************************/

    /*
    return a list of meals
     */
    ArrayList<Nutrition> getMealList(Nutrition meal) {

        ArrayList<Nutrition> list = new ArrayList<Nutrition>();
        ObjectSet<Nutrition> result = db.queryByExample(meal);

        while (result.hasNext()) {
            list.add(result.next());
        }

        return list;
    }

    /*
    return a list of workouts
     */
    ArrayList<Workout> getWorkoutList(Workout workout) {

        ArrayList<Workout> list = new ArrayList<Workout>();
        ObjectSet<Workout> result = db.queryByExample(workout);

        while (result.hasNext()) {
            list.add(result.next());
        }

        return list;
    }

    /*
    return a list of swim workouts
     */
    ArrayList<WorkoutSwim> getSwimList(WorkoutSwim workout) {

        ArrayList<WorkoutSwim> list = new ArrayList<WorkoutSwim>();
        ObjectSet<WorkoutSwim> result = db.queryByExample(workout);

        while (result.hasNext()) {
            list.add(result.next());
        }

        return list;
    }

    /*
    return a list of bike workouts
     */
    ArrayList<WorkoutBike> getBikeList(WorkoutBike workout) {

        ArrayList<WorkoutBike> list = new ArrayList<WorkoutBike>();
        ObjectSet<WorkoutBike> result = db.queryByExample(workout);

        while (result.hasNext()) {
            list.add(result.next());
        }

        return list;
    }

    /*
    return a list of run workouts
     */
    ArrayList<WorkoutRun> getRunList(WorkoutRun workout) {

        ArrayList<WorkoutRun> list = new ArrayList<WorkoutRun>();
        ObjectSet<WorkoutRun> result = db.queryByExample(workout);

        while (result.hasNext()) {
            list.add(result.next());
        }

        return list;
    }

    /*
    return a list of gym workouts
     */
    ArrayList<WorkoutGym> getGymList(WorkoutGym workout) {

        ArrayList<WorkoutGym> list = new ArrayList<WorkoutGym>();
        ObjectSet<WorkoutGym> result = db.queryByExample(workout);

        while (result.hasNext()) {
            list.add(result.next());
        }

        return list;
    }

    /***********************************************************************
     ****                     U p d a t e   D a t a                     ****
     ***********************************************************************/


    /***********************************************************************
     ****                     D e l e t e   D a t a                     ****
     ***********************************************************************/
}
