package com.cantwellcode.ipsum.Exercise;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Daniel on 4/15/2014.
 */
public class Run extends Workout implements Serializable {

    private String name;
    private String date;
    private String type;
    private String notes;
    private String distance;
    private String time;
    private String avgPace;
    private String maxPace;
    private String avgHR;
    private String maxHR;
    private String calBurned;
    private String elevation;

    private int numLaps;

    private ArrayList<Run> laps;

    /**
     * Default Constructor
     */
    public Run() {
    }

    public Run(String date) {
        this.date = date;
    }

    /**
     * Constructor containing everything but lap information
     * Typically used for a lap itself
     *
     * @param name
     * @param date
     * @param type
     * @param notes
     * @param distance
     * @param time
     * @param avgPace
     * @param maxPace
     * @param avgHR
     * @param maxHR
     * @param calBurned
     * @param elevation
     */
    public Run(String name, String date, String type, String notes,
               String distance, String time, String avgPace, String maxPace,
               String avgHR, String maxHR, String calBurned, String elevation) {
        this.name = name;
        this.date = date;
        this.type = type;
        this.notes = notes;
        this.distance = distance;
        this.time = time;
        this.avgPace = avgPace;
        this.maxPace = maxPace;
        this.avgHR = avgHR;
        this.maxHR = maxHR;
        this.calBurned = calBurned;
        this.elevation = elevation;

        numLaps = 0;
        laps = new ArrayList<Run>();
    }

    /**
     * Constructing with everything except for a list of laps
     *
     * @param name
     * @param date
     * @param type
     * @param notes
     * @param distance
     * @param time
     * @param avgPace
     * @param maxPace
     * @param avgHR
     * @param maxHR
     * @param calBurned
     * @param elevation
     * @param numLaps
     */
    public Run(String name, String date, String type, String notes,
               String distance, String time, String avgPace, String maxPace,
               String avgHR, String maxHR, String calBurned, String elevation, int numLaps) {
        this.name = name;
        this.date = date;
        this.type = type;
        this.notes = notes;
        this.distance = distance;
        this.time = time;
        this.avgPace = avgPace;
        this.maxPace = maxPace;
        this.avgHR = avgHR;
        this.maxHR = maxHR;
        this.calBurned = calBurned;
        this.elevation = elevation;

        this.numLaps = numLaps;
        laps = new ArrayList<Run>();
    }

    /**
     * Full Constructor
     *
     * @param name
     * @param date
     * @param type
     * @param notes
     * @param distance
     * @param time
     * @param avgPace
     * @param maxPace
     * @param avgHR
     * @param maxHR
     * @param calBurned
     * @param elevation
     * @param numLaps
     * @param laps
     */
    public Run(String name, String date, String type, String notes,
               String distance, String time, String avgPace, String maxPace,
               String avgHR, String maxHR, String calBurned, String elevation,
               int numLaps, ArrayList<Run> laps) {
        this.name = name;
        this.date = date;
        this.type = type;
        this.notes = notes;
        this.distance = distance;
        this.time = time;
        this.avgPace = avgPace;
        this.maxPace = maxPace;
        this.avgHR = avgHR;
        this.maxHR = maxHR;
        this.calBurned = calBurned;
        this.elevation = elevation;

        this.numLaps = numLaps;
        this.laps = laps;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAvgPace() {
        return avgPace;
    }

    public void setAvgPace(String avgPace) {
        this.avgPace = avgPace;
    }

    public String getMaxPace() {
        return maxPace;
    }

    public void setMaxPace(String maxPace) {
        this.maxPace = maxPace;
    }

    public String getAvgHR() {
        return avgHR;
    }

    public void setAvgHR(String avgHR) {
        this.avgHR = avgHR;
    }

    public String getMaxHR() {
        return maxHR;
    }

    public void setMaxHR(String maxHR) {
        this.maxHR = maxHR;
    }

    public String getCalBurned() {
        return calBurned;
    }

    public void setCalBurned(String calBurned) {
        this.calBurned = calBurned;
    }

    public String getElevation() {
        return elevation;
    }

    public void setElevation(String elevation) {
        this.elevation = elevation;
    }

    public int getNumLaps() {
        return numLaps;
    }

    public void setNumLaps(int numLaps) {
        this.numLaps = numLaps;
    }

    public ArrayList<Run> getLaps() {
        return laps;
    }

    public void setLaps(ArrayList<Run> laps) {
        this.laps = laps;
    }

    public void addLap(Run lap) {
        this.laps.add(lap);
    }

    public void removeLap(Run lap) {
        this.laps.remove(lap);
    }
}
