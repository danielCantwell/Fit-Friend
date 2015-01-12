package com.cantwellcode.fitfriend.exercise.log;

/**
 * Created by Daniel on 1/12/2015.
 */

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.cantwellcode.fitfriend.R;
import com.cantwellcode.fitfriend.exercise.types.Exercise;
import com.cantwellcode.fitfriend.utils.Statics;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

/**
 * A placeholder fragment containing a simple view.
 */
public class ExerciseListFragment extends Fragment {

    private ListView mList;

    private ParseQueryAdapter<Exercise> mAdapter;
    private ParseQueryAdapter.QueryFactory<Exercise> factory;

    private ExerciseStatsActivity mActivity;

    public ExerciseListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_exercises, container, false);

        mList = (ListView) root.findViewById(R.id.listView);

        factory = new ParseQueryAdapter.QueryFactory<Exercise>() {
            @Override
            public ParseQuery<Exercise> create() {
                ParseQuery<Exercise> query = Exercise.getQuery();
                query.orderByAscending("name");
                query.fromPin(Statics.SAVED_EXERCISES);
                return query;
            }
        };

        mAdapter = new ParseQueryAdapter<Exercise>(getActivity(), factory) {
            @Override
            public View getItemView(Exercise exercise, View v, ViewGroup parent) {

                if (v == null) {
                    v = v.inflate(getActivity(), R.layout.exercise_list_item, null);
                }

                TextView name = (TextView) v.findViewById(R.id.name);
                TextView details = (TextView) v.findViewById(R.id.sets);
                TextView arms = (TextView) v.findViewById(R.id.arms);
                TextView shoulders = (TextView) v.findViewById(R.id.shoulders);
                TextView chest = (TextView) v.findViewById(R.id.chest);
                TextView back = (TextView) v.findViewById(R.id.back);
                TextView abs = (TextView) v.findViewById(R.id.abs);
                TextView legs = (TextView) v.findViewById(R.id.legs);
                TextView glutes = (TextView) v.findViewById(R.id.glutes);


                name.setText(exercise.getName());

                String detailsText = "";
                if (exercise.recordWeight()) {
                    detailsText = "Weight  ";
                }

                if (exercise.recordReps()) {
                    detailsText += "Reps  ";
                }

                if (exercise.recordTime()) {
                    detailsText += "Time";
                }

                details.setText(detailsText);

                arms.setVisibility(exercise.usesArms() ? View.VISIBLE : View.GONE);
                shoulders.setVisibility(exercise.usesShoulders() ? View.VISIBLE : View.GONE);
                chest.setVisibility(exercise.usesChest() ? View.VISIBLE : View.GONE);
                back.setVisibility(exercise.usesBack() ? View.VISIBLE : View.GONE);
                abs.setVisibility(exercise.usesAbs() ? View.VISIBLE : View.GONE);
                legs.setVisibility(exercise.usesLegs() ? View.VISIBLE : View.GONE);
                glutes.setVisibility(exercise.usesGlutes() ? View.VISIBLE : View.GONE);

                return v;
            }
        };

        mList.setAdapter(mAdapter);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mActivity.showStatsFragment(mAdapter.getItem(position));
            }
        });

        return root;
    }

    public void setActivity(ExerciseStatsActivity a) {
        mActivity = a;
    }
}