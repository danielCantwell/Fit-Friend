package com.cantwellcode.fitfriend.exercise.types;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;

/**
 * Created by danielCantwell on 3/19/15.
 */
@ParseClassName("Cardio")
public class Cardio extends ParseObject {

    public ParseUser getUser() { return getParseUser("user"); }

    public void setUser(ParseUser user) { put("user", user);}

    public ParseFile getGPX() {
        return getParseFile("gpx");
    }

    public void setGPX(ParseFile gpxFile) {
        put("gpx", gpxFile);
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
