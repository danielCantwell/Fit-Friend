package com.cantwellcode.athletejournal;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Daniel on 4/15/2014.
 */
public class ProfileSocial extends Fragment {

    public static Fragment newInstance() {
        ProfileSocial f = new ProfileSocial();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.profile_social, null);

        return root;
    }
}