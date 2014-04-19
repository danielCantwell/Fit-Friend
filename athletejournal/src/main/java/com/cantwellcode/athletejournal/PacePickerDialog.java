//package com.cantwellcode.athletejournal;
//
//import android.os.Bundle;
//import android.support.v4.app.DialogFragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.NumberPicker;
//
///**
// * Created by Daniel on 4/19/2014.
// */
//public class PacePickerDialog extends DialogFragment {
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.time_picker_ms, container);
//        getDialog().setTitle("Set Pace");
//
//        final NumberPicker minutePicker = (NumberPicker) view.findViewById(R.id.time_minute);
//        final NumberPicker secondPicker = (NumberPicker) view.findViewById(R.id.time_second);
//
//        Button cancel = (Button) view.findViewById(R.id.time_cancel);
//        Button save = (Button) view.findViewById(R.id.time_save);
//
//        minutePicker.setMaxValue(59);
//        secondPicker.setMaxValue(59);
//
//        minutePicker.setMinValue(0);
//        secondPicker.setMinValue(0);
//
//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int minute = minutePicker.getValue();
//                int second = secondPicker.getValue();
//
//                switch (type) {
//                    case AVG:
//                        avgPaceMinute = String.valueOf(minute);
//                        avgPaceSecond = String.valueOf(second);
//                        break;
//                    case MAX:
//                        maxPaceMinute = String.valueOf(minute);
//                        maxPaceSecond = String.valueOf(second);
//                        break;
//                }
//            }
//        });
//
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getDialog().cancel();
//            }
//        });
//
//        return view;
//    }
//}
