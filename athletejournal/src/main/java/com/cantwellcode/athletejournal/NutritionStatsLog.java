package com.cantwellcode.athletejournal;

import android.support.v4.app.DialogFragment;

/**
 * Created by Daniel on 4/12/2014.
 */
public class NutritionStatsLog {
//
//    private enum CurrentView {Day, Week, Month, Total};
//    private CurrentView currentView = CurrentView.Day;
//
//    int year;
//    int month;
//    int day;
//
//
//    switch (currentView) {
//        case Day:
//            menuInflater.inflate(R.menu.n_view_day, menu);
//            menu.getItem(0).setTitle((month) + " / " + day + " / " + year);
//            break;
//        case Month:
//            menuInflater.inflate(R.menu.n_view_month, menu);
//            switch (month) {
//                case 1:
//                    menu.getItem(0).setTitle("January");
//                    break;
//                case 2:
//                    menu.getItem(0).setTitle("February");
//                    break;
//                case 3:
//                    menu.getItem(0).setTitle("March");
//                    break;
//                case 4:
//                    menu.getItem(0).setTitle("April");
//                    break;
//                case 5:
//                    menu.getItem(0).setTitle("May");
//                    break;
//                case 6:
//                    menu.getItem(0).setTitle("June");
//                    break;
//                case 7:
//                    menu.getItem(0).setTitle("July");
//                    break;
//                case 8:
//                    menu.getItem(0).setTitle("August");
//                    break;
//                case 9:
//                    menu.getItem(0).setTitle("September");
//                    break;
//                case 10:
//                    menu.getItem(0).setTitle("October");
//                    break;
//                case 11:
//                    menu.getItem(0).setTitle("November");
//                    break;
//                case 12:
//                    menu.getItem(0).setTitle("December");
//                    break;
//            }
//            break;
//        case Total:
//            menuInflater.inflate(R.menu.n_view_total, menu);
//            break;
//    }
//
//
//
//
//    case R.id.action_changeDate:
//    DialogFragment dateFragment = new DatePickerFragment();
//    dateFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
//    break;
//    case R.id.action_selectJanuary:
//    monthSelect = 1;
//    menu.getItem(0).setTitle("January");
//    break;
//    case R.id.action_selectFebruary:
//    monthSelect = 2;
//    menu.getItem(0).setTitle("February");
//    break;
//    case R.id.action_selectMarch:
//    monthSelect = 3;
//    menu.getItem(0).setTitle("March");
//    break;
//    case R.id.action_selectApril:
//    monthSelect = 4;
//    menu.getItem(0).setTitle("April");
//    break;
//    case R.id.action_selectMay:
//    monthSelect = 5;
//    menu.getItem(0).setTitle("May");
//    break;
//    case R.id.action_selectJune:
//    monthSelect = 6;
//    menu.getItem(0).setTitle("June");
//    break;
//    case R.id.action_selectJuly:
//    monthSelect = 7;
//    menu.getItem(0).setTitle("July");
//    break;
//    case R.id.action_selectAugust:
//    monthSelect = 8;
//    menu.getItem(0).setTitle("August");
//    break;
//    case R.id.action_selectSeptember:
//    monthSelect = 9;
//    menu.getItem(0).setTitle("September");
//    break;
//    case R.id.action_selectOctober:
//    monthSelect = 10;
//    menu.getItem(0).setTitle("October");
//    break;
//    case R.id.action_selectNovember:
//    monthSelect = 11;
//    menu.getItem(0).setTitle("November");
//    break;
//    case R.id.action_selectDecember:
//    monthSelect = 12;
//    menu.getItem(0).setTitle("December");
//    break;
//}
//if (monthSelect != 0) {
//        meals = db.getNutritionList(Database.NutritionListType.Month, monthSelect, day, year);
//        mAdapter.clear();
//        mAdapter.addAll(meals);
//        updateTotals();
//        }
}