package com.cantwellcode.athletejournal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Daniel on 2/7/14.
 */
public class Database extends SQLiteOpenHelper {

    // enums
    public static enum NutritionListType { Day, Week, Month, Total };

    // Database Name
    public static final String DATABASE_NAME = "journalData";
    public static final int DATABASE_VERSION = 1;

    // Workout Table
    private static final String TABLE_WORKOUTS = "workouts";
    // Columns
    private static final String WORKOUT_ID          = "workout_id";
    private static final String WORKOUT_NAME        = "workout_name";
    private static final String WORKOUT_DATE        = "workout_date";
    private static final String WORKOUT_TYPE        = "workout_type";
    private static final String WORKOUT_TIME        = "workout_time";
    private static final String WORKOUT_DISTANCE    = "workout_distance";
    private static final String WORKOUT_SPEED       = "workout_speed";
    private static final String WORKOUT_CALORIES    = "workout_calories";
    private static final String WORKOUT_HEART_RATE  = "workout_heartRate";
    private static final String WORKOUT_NOTES       = "workout_notes";

    // Nutrition Table
    private static final String TABLE_NUTRITION = "nutrition";
    // Columns
    private static final String NUTRITION_ID        = "nutrition_id";
    private static final String NUTRITION_NAME      = "nutrition_name";
    private static final String NUTRITION_DATE      = "nutrition_date";
    private static final String NUTRITION_TYPE      = "nutrition_type";
    private static final String NUTRITION_CALORIES  = "nutrition_calories";
    private static final String NUTRITION_PROTEIN   = "nutrition_protein";
    private static final String NUTRITION_CARBS     = "nutrition_carbs";
    private static final String NUTRITION_FAT       = "nutrition_fat";

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_WORKOUT_TABLE = "CREATE TABLE " +  TABLE_WORKOUTS + "("
                + WORKOUT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + WORKOUT_NAME + " TEXT,"
                + WORKOUT_DATE + " TEXT," + WORKOUT_TYPE + " TEXT," + WORKOUT_TIME
                + " TEXT," + WORKOUT_DISTANCE + " TEXT," + WORKOUT_SPEED + " TEXT,"
                + WORKOUT_CALORIES + " TEXT," + WORKOUT_HEART_RATE + " TEXT,"
                + WORKOUT_NOTES + " TEXT" + ")";
        String CREATE_NUTRITION_TABLE = "CREATE TABLE " + TABLE_NUTRITION + "("
                + NUTRITION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + NUTRITION_NAME + " TEXT," + NUTRITION_DATE
                + " TEXT," + NUTRITION_TYPE + " TEXT," + NUTRITION_CALORIES + " TEXT,"
                + NUTRITION_PROTEIN + " TEXT," + NUTRITION_CARBS + " TEXT," + NUTRITION_FAT
                + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_WORKOUT_TABLE);
        sqLiteDatabase.execSQL(CREATE_NUTRITION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUTS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NUTRITION);
        onCreate(sqLiteDatabase);
    }

    public void addWorkout(Workout workout) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(WORKOUT_NAME, workout.get_name());
        values.put(WORKOUT_DATE, workout.get_date());
        values.put(WORKOUT_TYPE, workout.get_type());
        values.put(WORKOUT_TIME, workout.get_time());
        values.put(WORKOUT_DISTANCE, workout.get_distance());
        values.put(WORKOUT_SPEED, workout.get_speed());
        values.put(WORKOUT_CALORIES, workout.get_calories());
        values.put(WORKOUT_HEART_RATE, workout.get_heartRate());
        values.put(WORKOUT_NOTES, workout.get_notes());

        // Insert Row
        db.insert(TABLE_WORKOUTS, null, values);
        db.close();
    }

    public void addNutrition(Nutrition nutrition) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NUTRITION_NAME, nutrition.get_name());
        values.put(NUTRITION_DATE, nutrition.get_date());
        values.put(NUTRITION_TYPE, nutrition.get_type());
        values.put(NUTRITION_CALORIES, nutrition.get_calories());
        values.put(NUTRITION_PROTEIN, nutrition.get_protein());
        values.put(NUTRITION_CARBS, nutrition.get_carbs());
        values.put(NUTRITION_FAT, nutrition.get_fat());

        // Insert Row
        db.insert(TABLE_NUTRITION, null, values);
        db.close();
    }

    public Workout getWorkout(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_WORKOUTS, new String[] { WORKOUT_ID, WORKOUT_NAME, WORKOUT_DATE,
                WORKOUT_TYPE, WORKOUT_TIME, WORKOUT_DISTANCE, WORKOUT_SPEED, WORKOUT_CALORIES, WORKOUT_HEART_RATE, WORKOUT_NOTES },
                WORKOUT_ID + "=?", new String[] { String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Workout workout = new Workout(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9));

        return workout;
    }

    public Nutrition getNutrition(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NUTRITION, new String[] { NUTRITION_ID, NUTRITION_NAME, NUTRITION_DATE,
                NUTRITION_TYPE, NUTRITION_CALORIES, NUTRITION_PROTEIN, NUTRITION_CARBS, NUTRITION_FAT},
                NUTRITION_ID + "=?", new String[] { String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Nutrition nutrition = new Nutrition(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                cursor.getString(5), cursor.getString(6), cursor.getString(7));

        return nutrition;
    }

    public List<Nutrition> getNutritionList(NutritionListType type) {

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) + 1;
        int day = c.get(Calendar.DAY_OF_MONTH);

        List<Nutrition> nutritionList = new ArrayList<Nutrition>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NUTRITION;
        String dateOptions = " WHERE " + NUTRITION_DATE + "=?";

        String[] currentDay = {month + "_" + day + "_" + year};

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;

        switch (type) {
            case Total:
                cursor = db.rawQuery(selectQuery, null);
                break;
            case Day:
                cursor = db.rawQuery(selectQuery + dateOptions, currentDay);
                break;
            default:
                cursor = db.rawQuery(selectQuery, null);
                break;
        }


        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                // check if the row is an entry from today
               //if (cursor.getString(2).equals(month + "_" + day + "_" + year)) {
                    Nutrition nutrition = new Nutrition();
                    nutrition.set_id(Integer.parseInt(cursor.getString(0)));
                    nutrition.set_name(cursor.getString(1));
                    nutrition.set_date(cursor.getString(2));
                    nutrition.set_type(cursor.getString(3));
                    nutrition.set_calories(cursor.getString(4));
                    nutrition.set_protein(cursor.getString(5));
                    nutrition.set_carbs(cursor.getString(6));
                    nutrition.set_fat(cursor.getString(7));
                    // Adding nutrition to list
                    nutritionList.add(nutrition);
               //}
            } while (cursor.moveToNext());
        }

        // return nutrition list
        return nutritionList;
    }

    public List<Workout> getAllWorkouts() {
        List<Workout> workoutList = new ArrayList<Workout>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_WORKOUTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Workout workout = new Workout();
                workout.set_id(Integer.parseInt(cursor.getString(0)));
                workout.set_name(cursor.getString(1));
                workout.set_date(cursor.getString(2));
                workout.set_type(cursor.getString(3));
                workout.set_time(cursor.getString(4));
                workout.set_distance(cursor.getString(5));
                workout.set_speed(cursor.getString(6));
                workout.set_calories(cursor.getString(7));
                workout.set_heartRate(cursor.getString(8));
                workout.set_notes(cursor.getString(9));
                // Adding workout to list
                workoutList.add(workout);
            } while (cursor.moveToNext());
        }

        // return workout list
        return workoutList;
    }

    public List<Nutrition> getAllNutrition() {
        List<Nutrition> nutritionList = new ArrayList<Nutrition>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NUTRITION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Nutrition nutrition = new Nutrition();
                nutrition.set_id(Integer.parseInt(cursor.getString(0)));
                nutrition.set_name(cursor.getString(1));
                nutrition.set_date(cursor.getString(2));
                nutrition.set_type(cursor.getString(3));
                nutrition.set_calories(cursor.getString(4));
                nutrition.set_protein(cursor.getString(5));
                nutrition.set_carbs(cursor.getString(6));
                nutrition.set_fat(cursor.getString(7));
                // Adding nutrition to list
                nutritionList.add(nutrition);
            } while (cursor.moveToNext());
        }

        // return nutrition list
        return nutritionList;
    }

    public int getWorkoutCount() {
        String countQuery = "SELECT  * FROM " + TABLE_WORKOUTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        // return count
        return cursor.getCount();
    }

    public int getNutritionCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NUTRITION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        // return count
        return cursor.getCount();
    }

    public int updateWorkout(Workout workout) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(WORKOUT_NAME, workout.get_name());
        values.put(WORKOUT_DATE, workout.get_date());
        values.put(WORKOUT_TYPE, workout.get_type());
        values.put(WORKOUT_TIME, workout.get_time());
        values.put(WORKOUT_DISTANCE, workout.get_distance());
        values.put(WORKOUT_SPEED, workout.get_speed());
        values.put(WORKOUT_CALORIES, workout.get_calories());
        values.put(WORKOUT_HEART_RATE, workout.get_heartRate());
        values.put(WORKOUT_NOTES, workout.get_notes());

        // updating row
        return db.update(TABLE_WORKOUTS, values, WORKOUT_ID + " = ?",
                new String[] { String.valueOf(workout.get_id()) });
    }

    public int updateNutrition(Nutrition nutrition) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NUTRITION_NAME, nutrition.get_name());
        values.put(NUTRITION_DATE, nutrition.get_date());
        values.put(NUTRITION_TYPE, nutrition.get_type());
        values.put(NUTRITION_CALORIES, nutrition.get_calories());
        values.put(NUTRITION_PROTEIN, nutrition.get_protein());
        values.put(NUTRITION_CARBS, nutrition.get_carbs());
        values.put(NUTRITION_FAT, nutrition.get_fat());

        // updating row
        return db.update(TABLE_WORKOUTS, values, WORKOUT_ID + " = ?",
                new String[] { String.valueOf(nutrition.get_id()) });
    }

    public void deleteWorkout(Workout workout) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WORKOUTS, WORKOUT_ID + " = ?",
                new String[] { String.valueOf(workout.get_id()) });
        db.close();
    }

    public void deleteNutrition(Nutrition nutrition) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NUTRITION, NUTRITION_ID + " = ?",
                new String[] { String.valueOf(nutrition.get_id()) });
        db.close();
    }
}
