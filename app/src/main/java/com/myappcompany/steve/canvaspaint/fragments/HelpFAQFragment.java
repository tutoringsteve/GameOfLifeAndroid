package com.myappcompany.steve.canvaspaint.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.myappcompany.steve.canvaspaint.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HelpFAQFragment extends Fragment {
    private static final String TAG = "HelpControlFragment";

    private View mHelpFAQView;

    public HelpFAQFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mHelpFAQView = inflater.inflate(R.layout.fragment_help_faq, container, false);

        return mHelpFAQView;
    }
}