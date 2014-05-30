package com.cantwellcode.ipsum.Connect.Trainer;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cantwellcode.ipsum.R;

/**
 * Created by Daniel on 4/15/2014.
 */
public class ConnectTrainer extends Fragment {

    public static Fragment newInstance() {
        ConnectTrainer f = new ConnectTrainer();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.connect_trainer, null);

        return root;
    }
}
