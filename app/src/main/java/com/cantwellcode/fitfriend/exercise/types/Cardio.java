package com.cantwellcode.fitfriend.exercise.types;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by danielCantwell on 3/19/15.
 */
@ParseClassName("Cardio")
public class Cardio extends ParseObject {

    public static final String RUN = "run";
    public static final String BIKE = "bike";

    public ParseUser getUser() { return getParseUser("user"); }

    public void setUser(ParseUser user) { put("user", user);}

    public ParseFile getGPX() {
        return getParseFile("gpx");
    }

    public void setGPX(ParseFile gpxFile) {
        put("gpx", gpxFile);
    }

    /**
     * Get the type of the run, either "run" or "bike"
     * @return
     */
    public String getType() {
        return getString("type");
    }

    public boolean setType(String type) {
        if (!type.equals(RUN) || !type.equals(BIKE)) return false;

        put("type", type);
        return true;
    }

    /**
     * Get the date-time the run was started
     * @return
     */
    public Date getDate() {
        return getDate("date");
    }

    public void setDate(Date date) {
        put("date", date);
    }

    public String getDateString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
        return formatter.format(getDate());
    }

    /**
     * Get the average pace
     * @return the number of seconds per mile
     */
    public int getPace() {
        return getInt("pace");
    }

    public void setPace(int seconds) {
        put("pace", seconds);
    }

    public String getPaceString() {
        int pace = getPace();
        int paceSeconds = pace % 60;
        int paceMinutes = pace / 60;

        return String.format("%d:%02d", paceMinutes, paceSeconds);
    }

    /**
     * Get the distance
     * @return the total meters run
     */
    public float getDistance() {
        return (float)getDouble("distance");
    }

    public void setDistance(float meters) {
        put("distance", meters);
    }

    public String getMilesString() {
        double miles = getDistance() / 1609.34;

        return String.format("%.2f", miles);
    }

    /**
     * Get the time spent running
     * @return the total seconds
     */
    public long getTime() {
        return getLong("time");
    }

    public void setTime(long seconds) {
        put("time", seconds);
    }

    public String getTimeString() {
        long seconds = getTime() / 1000;
        int timeSeconds = (int) seconds % 60;
        int timeMinutes = (int) seconds / 60;
        int hours = 0;

        if (timeMinutes >= 60) {
            hours = (int) seconds / 3600;
        }
        return String.format("%d:%02d:%02d", hours, timeMinutes, timeSeconds);
    }

    /**
     * Get the notes for the run (Defaulted to the city of the run)
     * @return the notes
     */
    public String getNotes() { return getString("notes"); }

    public void setNotes(String notes) { put("notes", notes); }

    /*
    QUERY
     */

    public static ParseQuery<Cardio> getQuery() { return ParseQuery.getQuery("Cardio"); }
}
