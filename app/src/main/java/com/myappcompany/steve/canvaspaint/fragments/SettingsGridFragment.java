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
public class SettingsGridFragment extends Fragment {

    public SettingsGridFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings_grid, container, false);
    }
}