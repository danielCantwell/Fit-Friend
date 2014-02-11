package com.cantwellcode.athletejournal;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;

/**
 * Created by Daniel on 2/8/14.
 */
public class NutritionFragment extends Fragment {

    DialogFragment dateFragment;

    EditText name;
    Button date;
    EditText calories;
    EditText protein;
    EditText carbs;
    EditText fat;

    int year;
    int month;
    int day;

    Spinner type;

    public static Fragment newInstance(Context context) {
        NutritionFragment f = new NutritionFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.nutrition_fragment, null);

        name        = (EditText) root.findViewById(R.id.n_name);
        date        = (Button)   root.findViewById(R.id.n_date);
        calories    = (EditText) root.findViewById(R.id.n_calories);
        protein     = (EditText) root.findViewById(R.id.n_protein);
        carbs       = (EditText) root.findViewById(R.id.n_carbs);
        fat         = (EditText) root.findViewById(R.id.n_fat);

        final Calendar c = Calendar.getInstance();
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);

        date.setText(" " + (m + 1) + " / " + d + " / " + y + " ");

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateFragment = new DatePickerFragment();
                dateFragment.show(getActivity().getSupportFragmentManager(), "datPicker");
            }
        });

        return root;
    }

    public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int y = c.get(Calendar.YEAR);
            int m = c.get(Calendar.MONTH);
            int d = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, y, m, d);
        }

        @Override
        public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
            year = i;
            month = i2;
            day = i3;
            date.setText(" " + (month + 1) + " / " + day + " / " + year + " ");
        }
    }
}

