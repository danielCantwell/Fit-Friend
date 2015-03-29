package com.cantwellcode.fitfriend.exercise.log;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cantwellcode.fitfriend.R;
import com.cantwellcode.fitfriend.exercise.types.Cardio;
import com.cantwellcode.fitfriend.exercise.types.Exercise;
import com.cantwellcode.fitfriend.exercise.types.Workout;
import com.cantwellcode.fitfriend.utils.SmallLabelTextView;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by danielCantwell on 3/24/15.
 */
public class WorkoutListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    private List<ParseObject> parseObjects;
    private LogItemClickListener mListener;

    public WorkoutListAdapter(List<ParseObject> dataset, LogItemClickListener listener) {
        parseObjects = dataset;
        mListener = listener;
    }

    public void updateList(List<ParseObject> newObjects) {
        parseObjects = newObjects;
    }

    public void addItemToList(ParseObject o) {
        parseObjects.add(o);
    }

    public void removeItemFromList(ParseObject o) {
        parseObjects.remove(o);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // "i" is viewtype
        View view = null;
        switch (i) {
            case 0:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardio_log_item, viewGroup, false);
                view.setTag("Cardio");
                view.setOnClickListener(this);
                view.setOnLongClickListener(this);
                CardioViewHolder cardioViewHolder = new CardioViewHolder(view);
                return cardioViewHolder;
            case 1:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.workout_log_item, viewGroup, false);
                view.setTag("Workout");
                view.setOnClickListener(this);
                view.setOnLongClickListener(this);
                WorkoutViewHolder workoutViewHolder = new WorkoutViewHolder(view);
                return workoutViewHolder;
            default:    // should not enter here
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.workout_log_item, viewGroup, false);
                WorkoutViewHolder defaultHolder = new WorkoutViewHolder(view);
                return defaultHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        // "i" is position
        ParseObject object = parseObjects.get(i);

        /*                                   *
         *      Handle Cardio View Item      *
         *                                   */
        if (object instanceof Cardio) {
            Cardio cardio = (Cardio) object;

            ((CardioViewHolder) viewHolder).date.setText(cardio.getDateString());

        /* Display Time */

            ((CardioViewHolder) viewHolder).time.setText(cardio.getTimeString());


        /* Display Pace */

            ((CardioViewHolder) viewHolder).pace.setTextWithLabel(cardio.getPaceString(), "/mi");


        /* Display Distance */

            ((CardioViewHolder) viewHolder).distance.setTextWithLabel(cardio.getMilesString(), " mi");


        }
        /*                                      *
         *      Handle Workout View Item        *
         *                                      */
        else if (object instanceof Workout) {

            Workout workout = (Workout) object;

            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
            String dateFormat = formatter.format(workout.getDate());
            ((WorkoutViewHolder) viewHolder).date.setText(dateFormat);

            if (workout.getNotes().trim().isEmpty()) {
                ((WorkoutViewHolder) viewHolder).notes.setVisibility(View.GONE);
            } else {
                ((WorkoutViewHolder) viewHolder).notes.setVisibility(View.VISIBLE);
                ((WorkoutViewHolder) viewHolder).notes.setText(workout.getNotes());
            }

            boolean usesArms = false;
            boolean usesShoulders = false;
            boolean usesChest = false;
            boolean usesBack = false;
            boolean usesAbs = false;
            boolean usesLegs = false;
            boolean usesGlutes = false;

            String detailsText = "";

            List<Exercise> exerciseList = null;

            int count = 0;
            boolean armsCounted = false;
            boolean shouldersCounted = false;
            boolean chestCounted = false;
            boolean backCounted = false;
            boolean absCounted = false;
            boolean legsCounted = false;
            boolean glutesCounted = false;

            try {

                exerciseList = workout.getLocalExerciseList();

                for (Exercise e : exerciseList) {
                    if (e.usesArms()) {
                        usesArms = true;
                        if (!armsCounted) {
                            count++;
                            armsCounted = true;
                        }
                    }
                    if (e.usesShoulders()) {
                        usesShoulders = true;
                        if (!shouldersCounted) {
                            count++;
                            shouldersCounted = true;
                        }
                    }
                    if (e.usesChest()) {
                        usesChest = true;
                        if (!chestCounted) {
                            count++;
                            chestCounted = true;
                        }
                    }
                    if (e.usesBack()) {
                        usesBack = true;
                        if (!backCounted) {
                            count++;
                            backCounted = true;
                        }
                    }
                    if (e.usesAbs()) {
                        usesAbs = true;
                        if (!absCounted) {
                            count++;
                            absCounted = true;
                        }
                    }
                    if (e.usesLegs()) {
                        usesLegs = true;
                        if (!legsCounted) {
                            count++;
                            legsCounted = true;
                        }
                    }
                    if (e.usesGlutes()) {
                        usesGlutes = true;
                        if (!glutesCounted) {
                            count++;
                            glutesCounted = true;
                        }
                    }

                    detailsText += e.getName() + ",  ";
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // this next line removes the extra 'plus' at the end from the for loop
            if (!detailsText.trim().isEmpty()) {
                detailsText = detailsText.substring(0, detailsText.length() - 3);
            }
            ((WorkoutViewHolder) viewHolder).details.setText(detailsText);

            ((WorkoutViewHolder) viewHolder).arms.setVisibility(usesArms ? View.VISIBLE : View.GONE);
            ((WorkoutViewHolder) viewHolder).shoulders.setVisibility(usesShoulders ? View.VISIBLE : View.GONE);
            ((WorkoutViewHolder) viewHolder).chest.setVisibility(usesChest ? View.VISIBLE : View.GONE);

            ((WorkoutViewHolder) viewHolder).back.setVisibility(usesBack && count <= 5 ? View.VISIBLE : View.GONE);
            ((WorkoutViewHolder) viewHolder).abs.setVisibility(usesAbs && count <= 5 ? View.VISIBLE : View.GONE);
            ((WorkoutViewHolder) viewHolder).legs.setVisibility(usesLegs && count <= 5 ? View.VISIBLE : View.GONE);
            ((WorkoutViewHolder) viewHolder).glutes.setVisibility(usesGlutes && count <= 5 ? View.VISIBLE : View.GONE);

            ((WorkoutViewHolder) viewHolder).backOverflow.setVisibility(usesBack && count > 5 ? View.VISIBLE : View.GONE);
            ((WorkoutViewHolder) viewHolder).absOverflow.setVisibility(usesAbs && count > 5 ? View.VISIBLE : View.GONE);
            ((WorkoutViewHolder) viewHolder).legsOverflow.setVisibility(usesLegs && count > 5 ? View.VISIBLE : View.GONE);
            ((WorkoutViewHolder) viewHolder).glutesOverflow.setVisibility(usesGlutes && count > 5 ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return parseObjects.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (parseObjects.get(position) instanceof Cardio)
            return 0;
        else
            return 1;
    }

    @Override
    public void onClick(View view) {
        if (view.getTag().equals("Cardio")) {
            mListener.onCardioClick(view);
        } else if (view.getTag().equals("Workout")) {
            mListener.onWorkoutClick(view);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (view.getTag().equals("Cardio")) {
            mListener.onCardioLongClick(view);
            return true;
        } else if (view.getTag().equals("Workout")) {
            mListener.onWorkoutLongClick(view);
            return true;
        }
        return false;
    }

    public static class CardioViewHolder extends RecyclerView.ViewHolder {

        protected TextView date;
        protected TextView time;
        protected SmallLabelTextView pace;
        protected SmallLabelTextView distance;
        protected TextView notes;


        public CardioViewHolder(View v) {
            super(v);

            date = (TextView) v.findViewById(R.id.date);
            time = (TextView) v.findViewById(R.id.time);
            pace = (SmallLabelTextView) v.findViewById(R.id.pace);
            distance = (SmallLabelTextView) v.findViewById(R.id.distance);
            notes = (TextView) v.findViewById(R.id.notes);
        }
    }

    public static class WorkoutViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        TextView details;
        TextView notes;
        TextView arms;
        TextView shoulders;
        TextView chest;
        TextView back;
        TextView abs;
        TextView legs;
        TextView glutes;

        TextView backOverflow;
        TextView absOverflow;
        TextView legsOverflow;
        TextView glutesOverflow;

        public WorkoutViewHolder(View v) {
            super(v);

            date = (TextView) v.findViewById(R.id.date);
            details = (TextView) v.findViewById(R.id.details);
            notes = (TextView) v.findViewById(R.id.notes);
            arms = (TextView) v.findViewById(R.id.arms);
            shoulders = (TextView) v.findViewById(R.id.shoulders);
            chest = (TextView) v.findViewById(R.id.chest);
            back = (TextView) v.findViewById(R.id.back);
            abs = (TextView) v.findViewById(R.id.abs);
            legs = (TextView) v.findViewById(R.id.legs);
            glutes = (TextView) v.findViewById(R.id.glutes);

            backOverflow = (TextView) v.findViewById(R.id.backOverflow);
            absOverflow = (TextView) v.findViewById(R.id.absOverflow);
            legsOverflow = (TextView) v.findViewById(R.id.legsOverflow);
            glutesOverflow = (TextView) v.findViewById(R.id.glutesOverflow);
        }
    }

    public static interface LogItemClickListener {
        public void onCardioClick(View v);
        public void onWorkoutClick(View v);

        public void onCardioLongClick(View v);
        public void onWorkoutLongClick(View v);
    }
}
