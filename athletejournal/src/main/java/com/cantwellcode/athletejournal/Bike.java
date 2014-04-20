package com.cantwellcode.athletejournal;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Daniel on 4/17/2014.
 */
public class Bike extends Workout implements Serializable {

    private String name;
    private String type;
    private String date;
    private String notes;
    private String distance;
    private String time;
    private String avgSpeed;
    private String maxSpeed;
    private String avgCadence;
    private String maxCadence;
    private String avgHR;
    private String maxHR;
    private String calBurned;
    private String elevation;

    private int numLaps;

    private ArrayList<Bike> laps;

    /**
     * Default Constructor
     */
    public Bike() {
    }

    public Bike(String date) {
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
     * @param avgSpeed
     * @param maxSpeed
     * @param avgHR
     * @param maxHR
     * @param calBurned
     * @param elevation
     */
    public Bike(String name, String date, String type, String notes,
                String distance, String time, String avgSpeed, String maxSpeed,
                String avgCadence, String maxCadence, String avgHR, String maxHR,
                String calBurned, String elevation) {
        this.name = name;
        this.date = date;
        this.type = type;
        this.notes = notes;
        this.distance = distance;
        this.time = time;
        this.avgSpeed = avgSpeed;
        this.maxSpeed = maxSpeed;
        this.avgCadence = avgCadence;
        this.maxCadence = maxCadence;
        this.avgHR = avgHR;
        this.maxHR = maxHR;
        this.calBurned = calBurned;
        this.elevation = elevation;

        numLaps = 0;
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
     * @param avgSpeed
     * @param maxSpeed
     * @param avgHR
     * @param maxHR
     * @param calBurned
     * @param elevation
     * @param numLaps
     */
    public Bike(String name, String date, String type, String notes,
                String distance, String time, String avgSpeed, String maxSpeed,
                String avgCadence, String maxCadence, String avgHR, String maxHR,
                String calBurned, String elevation, int numLaps) {
        this.name = name;
        this.date = date;
        this.type = type;
        this.notes = notes;
        this.distance = distance;
        this.time = time;
        this.avgSpeed = avgSpeed;
        this.maxSpeed = maxSpeed;
        this.avgCadence = avgCadence;
        this.maxCadence = maxCadence;
        this.avgHR = avgHR;
        this.maxHR = maxHR;
        this.calBurned = calBurned;
        this.elevation = elevation;

        this.numLaps = numLaps;
        laps = new ArrayList<Bike>();
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
     * @param avgSpeed
     * @param maxSpeed
     * @param avgHR
     * @param maxHR
     * @param calBurned
     * @param elevation
     * @param numLaps
     * @param laps
     */
    public Bike(String name, String date, String type, String notes,
                String distance, String time, String avgSpeed, String maxSpeed,
                String avgCadence, String maxCadence, String avgHR, String maxHR,
                String calBurned, String elevation,
                int numLaps, ArrayList<Bike> laps) {
        this.name = name;
        this.date = date;
        this.type = type;
        this.notes = notes;
        this.distance = distance;
        this.time = time;
        this.avgSpeed = avgSpeed;
        this.maxSpeed = maxSpeed;
        this.avgCadence = avgCadence;
        this.maxCadence = maxCadence;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(String avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public String getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(String maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public String getAvgCadence() {
        return avgCadence;
    }

    public void setAvgCadence(String avgCadence) {
        this.avgCadence = avgCadence;
    }

    public String getMaxCadence() {
        return maxCadence;
    }

    public void setMaxCadence(String maxCadence) {
        this.maxCadence = maxCadence;
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

    public ArrayList<Bike> getLaps() {
        return laps;
    }

    public void setLaps(ArrayList<Bike> laps) {
        this.laps = laps;
    }

    public void addLap(Bike lap) {
        this.laps.add(lap);
    }

    public void removeLap(Bike lap) {
        this.laps.remove(lap);
    }
}
