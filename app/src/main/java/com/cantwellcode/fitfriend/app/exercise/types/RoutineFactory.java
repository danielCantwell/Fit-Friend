package com.cantwellcode.fitfriend.app.exercise.types;

import com.cantwellcode.fitfriend.app.utils.Statics;

import java.util.List;

/**
 * Created by Daniel on 6/23/2014.
 */
public class RoutineFactory {

    public Routine createRoutine(Statics.RoutineType type, String name, List<Set> sets) {
        return new BaseRoutine(name, sets);
    }
}
