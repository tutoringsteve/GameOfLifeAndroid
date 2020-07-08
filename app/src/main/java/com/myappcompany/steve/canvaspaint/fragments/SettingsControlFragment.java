package com.myappcompany.steve.canvaspaint.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.myappcompany.steve.canvaspaint.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsControlFragment extends Fragment {

    public SettingsControlFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_control_fragment, container, false);
        // Inflate the layout for this fragment
        return view;
    }
}