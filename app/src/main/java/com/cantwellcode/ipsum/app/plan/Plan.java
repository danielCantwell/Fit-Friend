package com.cantwellcode.ipsum.app.plan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cantwellcode.ipsum.app.R;

/**
 * Created by Daniel on 6/1/2014.
 */
public class Plan extends Fragment {

    public static Fragment newInstance() {
        return new Plan();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plan, null);
    }
}
