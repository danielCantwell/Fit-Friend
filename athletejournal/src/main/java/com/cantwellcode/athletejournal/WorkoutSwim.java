package com.cantwellcode.athletejournal;

import java.util.ArrayList;

/**
 * Created by Daniel on 4/17/2014.
 */
public class WorkoutSwim extends Workout {

    private String distance;
    private String time;
    private String avgPace;
    private String maxPace;
    private String calBurned;
    private String strokeRate;
    private int numLaps;

    private ArrayList<WorkoutSwim> laps;

    /**
     * Default Constructor
     */
    public WorkoutSwim() {
        super();

        distance = "";
        time = "";
        avgPace = "";
        maxPace = "";
        calBurned = "";
        strokeRate = "";

        numLaps = 0;
        laps = new ArrayList<WorkoutSwim>();
    }

    /**
     * Constructor containing only basic information
     * @param name
     * @param date
     * @param type
     * @param notes
     */
    public WorkoutSwim(String name, String date, String type, String notes) {
        super(name, date, type, notes);

        distance = "";
        time = "";
        avgPace = "";
        maxPace = "";
        calBurned = "";
        strokeRate = "";

        numLaps = 0;
        laps = new ArrayList<WorkoutSwim>();
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
    public WorkoutSwim(String name, String date, String type, String notes,
                      String distance, String time, String avgPace, String maxPace,
                      String calBurned, String strokeRate) {
        super(name, date, type, notes);

        this.distance = distance;
        this.time = time;
        this.avgPace = avgPace;
        this.maxPace = maxPace;
        this.calBurned = calBurned;
        this.strokeRate = strokeRate;

        numLaps = 0;
        laps = new ArrayList<WorkoutSwim>();
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
    public WorkoutSwim(String name, String date, String type, String notes,
                      String distance, String time, String avgPace, String maxPace,
                      String calBurned, String strokeRate, int numLaps) {
        super(name, date, type, notes);

        this.distance = distance;
        this.time = time;
        this.avgPace = avgPace;
        this.maxPace = maxPace;
        this.calBurned = calBurned;
        this.strokeRate = strokeRate;

        this.numLaps = numLaps;
        laps = new ArrayList<WorkoutSwim>();
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
    public WorkoutSwim(String name, String date, String type, String notes,
                      String distance, String time, String avgPace, String maxPace,
                      String calBurned, String strokeRate,
                      int numLaps, ArrayList<WorkoutSwim> laps) {
        super(name, date, type, notes);

        this.distance = distance;
        this.time = time;
        this.avgPace = avgPace;
        this.maxPace = maxPace;
        this.calBurned = calBurned;
        this.strokeRate = strokeRate;

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

    public ArrayList<WorkoutSwim> getLaps() {
        return laps;
    }

    public void setLaps(ArrayList<WorkoutSwim> laps) {
        this.laps = laps;
    }

    public void addLap(WorkoutSwim lap) {
        this.laps.add(lap);
    }

    public void removeLap(WorkoutSwim lap) {
        this.laps.remove(lap);
    }
}
