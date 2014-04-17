package com.cantwellcode.athletejournal;

import java.util.ArrayList;

/**
 * Created by Daniel on 4/15/2014.
 */
public class WorkoutRun extends Workout {

    private String distance;
    private String time;
    private String avgPace;
    private String maxPace;
    private String avgHR;
    private String maxHR;
    private String calBurned;
    private String elevation;

    private int numLaps;

    private ArrayList<WorkoutRun> laps;

    /**
     * Default Constructor
     */
    public WorkoutRun() {
        super();

        distance = "";
        time = "";
        avgPace = "";
        maxPace = "";
        avgHR = "";
        maxHR = "";
        calBurned = "";
        elevation = "";

        numLaps = 0;
        laps = new ArrayList<WorkoutRun>();
    }

    /**
     * Constructor containing only basic information
     * @param name
     * @param date
     * @param type
     * @param notes
     */
    public WorkoutRun(String name, String date, String type, String notes) {
        super(name, date, type, notes);

        distance = "";
        time = "";
        avgPace = "";
        maxPace = "";
        avgHR = "";
        maxHR = "";
        calBurned = "";
        elevation = "";

        numLaps = 0;
        laps = new ArrayList<WorkoutRun>();
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
    public WorkoutRun(String name, String date, String type, String notes,
                      String distance, String time, String avgPace, String maxPace,
                      String avgHR, String maxHR, String calBurned, String elevation) {
        super(name, date, type, notes);

        this.distance = distance;
        this.time = time;
        this.avgPace = avgPace;
        this.maxPace = maxPace;
        this.avgHR = avgHR;
        this.maxHR = maxHR;
        this.calBurned = calBurned;
        this.elevation = elevation;

        numLaps = 0;
        laps = new ArrayList<WorkoutRun>();
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
    public WorkoutRun(String name, String date, String type, String notes,
                      String distance, String time, String avgPace, String maxPace,
                      String avgHR, String maxHR, String calBurned, String elevation, int numLaps) {
        super(name, date, type, notes);

        this.distance = distance;
        this.time = time;
        this.avgPace = avgPace;
        this.maxPace = maxPace;
        this.avgHR = avgHR;
        this.maxHR = maxHR;
        this.calBurned = calBurned;
        this.elevation = elevation;

        this.numLaps = numLaps;
        laps = new ArrayList<WorkoutRun>();
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
    public WorkoutRun(String name, String date, String type, String notes,
                      String distance, String time, String avgPace, String maxPace,
                      String avgHR, String maxHR, String calBurned, String elevation,
                      int numLaps, ArrayList<WorkoutRun> laps) {
        super(name, date, type, notes);

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

    public ArrayList<WorkoutRun> getLaps() {
        return laps;
    }

    public void setLaps(ArrayList<WorkoutRun> laps) {
        this.laps = laps;
    }

    public void addLap(WorkoutRun lap) {
        this.laps.add(lap);
    }

    public void removeLap(WorkoutRun lap) {
        this.laps.remove(lap);
    }
}
