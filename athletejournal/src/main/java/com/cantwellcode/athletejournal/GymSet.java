package com.cantwellcode.athletejournal;

import java.io.Serializable;

/**
 * Created by Daniel on 4/21/2014.
 */
public class GymSet implements Serializable {
    public int reps;
    public int weight;

    public GymSet() {

    }

    public GymSet(int reps, int weight) {
        this.reps = reps;
        this.weight = weight;
    }
}
