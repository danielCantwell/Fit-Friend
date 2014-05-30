package com.cantwellcode.ipsum.Exercise;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Daniel on 4/17/2014.
 */
public class Swim extends Workout implements Serializable {

    private String name;
    private String date;
    private String type;
    private String notes;
    private String distance;
    private String time;
    private String avgPace;
    private String maxPace;
    private String calBurned;
    private String strokeRate;
    private int numLaps;

    private ArrayList<Swim> laps;

    /**
     * Default Constructor
     */
    public Swim() {

//        distance = "";
//        time = "";
//        avgPace = "";
//        maxPace = "";
//        calBurned = "";
//        strokeRate = "";
//
//        numLaps = 0;
//        laps = new ArrayList<Swim>();
    }

    /**
     * used for retrieving objects with a specified date
     * @param date
     */
    public Swim(String date) {
//        name = "";
        this.date = date;
//        type = "";
//        notes = "";
//        distance = "";
//        time = "";
//        avgPace = "";
//        maxPace = "";
//        calBurned = "";
//        strokeRate = "";
//
//        numLaps = 0;
//        laps = new ArrayList<Swim>();
    }

    /**
     * Constructor containing only basic information
     * @param name
     * @param date
     * @param type
     * @param notes
     */
    public Swim(String name, String date, String type, String notes) {
        this.name = name;
        this.date = date;
        this.type = type;
        this.notes = notes;
//        distance = "";
//        time = "";
//        avgPace = "";
//        maxPace = "";
//        calBurned = "";
//        strokeRate = "";
//
//        numLaps = 0;
//        laps = new ArrayList<Swim>();
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
     * @param calBurned
     * @param strokeRate
     */
    public Swim(String name, String date, String type, String notes,
                String distance, String time, String avgPace, String maxPace,
                String calBurned, String strokeRate) {
        this.name = name;
        this.date = date;
        this.type = type;
        this.notes = notes;
        this.distance = distance;
        this.time = time;
        this.avgPace = avgPace;
        this.maxPace = maxPace;
        this.calBurned = calBurned;
        this.strokeRate = strokeRate;

//
//        numLaps = 0;
//        laps = new ArrayList<Swim>();
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
     * @param calBurned
     * @param strokeRate
     * @param numLaps
     */
    public Swim(String name, String date, String type, String notes,
                String distance, String time, String avgPace, String maxPace,
                String calBurned, String strokeRate, int numLaps) {
        this.name = name;
        this.date = date;
        this.type = type;
        this.notes = notes;
        this.distance = distance;
        this.time = time;
        this.avgPace = avgPace;
        this.maxPace = maxPace;
        this.calBurned = calBurned;
        this.strokeRate = strokeRate;

        this.numLaps = numLaps;
//        laps = new ArrayList<Swim>();
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
     * @param calBurned
     * @param strokeRate
     * @param numLaps
     * @param laps
     */
    public Swim(String name, String date, String type, String notes,
                String distance, String time, String avgPace, String maxPace,
                String calBurned, String strokeRate,
                int numLaps, ArrayList<Swim> laps) {
        this.name = name;
        this.date = date;
        this.type = type;
        this.notes = notes;
        this.distance = distance;
        this.time = time;
        this.avgPace = avgPace;
        this.maxPace = maxPace;
        this.calBurned = calBurned;
        this.strokeRate = strokeRate;

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

    public String getCalBurned() {
        return calBurned;
    }

    public void setCalBurned(String calBurned) {
        this.calBurned = calBurned;
    }

    public String getStrokeRate() {
        return strokeRate;
    }

    public void setStrokeRate(String strokeRate) {
        this.strokeRate = strokeRate;
    }

    public int getNumLaps() {
        return numLaps;
    }

    public void setNumLaps(int numLaps) {
        this.numLaps = numLaps;
    }

    public ArrayList<Swim> getLaps() {
        return laps;
    }

    public void setLaps(ArrayList<Swim> laps) {
        this.laps = laps;
    }

    public void addLap(Swim lap) {
        this.laps.add(lap);
    }

    public void removeLap(Swim lap) {
        this.laps.remove(lap);
    }
}
