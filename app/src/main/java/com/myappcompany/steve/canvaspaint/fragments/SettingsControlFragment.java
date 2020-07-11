package com.myappcompany.steve.canvaspaint.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.myappcompany.steve.canvaspaint.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsControlFragment extends Fragment {
    private static final String TAG = "SettingsControlFragment";

    private View mSettingsControlView;

    public SettingsControlFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mSettingsControlView = inflater.inflate(R.layout.fragment_settings_control, container, false);

        setupAutoPlaySpeedSpinner();
        setupRandomFillProbabilitySpinner();

        // Inflate the layout for this fragment
        return mSettingsControlView;
    }

    private void setupAutoPlaySpeedSpinner() {
        Spinner autoPlaySpeedSpinner = mSettingsControlView.findViewById(R.id.autoPlaySpeedSpinner);
        ArrayAdapter<CharSequence> autoPlaySpinnerAdapter = ArrayAdapter.createFromResource(mSettingsControlView.getContext(),
                R.array.auto_play_spinner_speeds_array, android.R.layout.simple_spinner_item);
        autoPlaySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoPlaySpeedSpinner.setAdapter(autoPlaySpinnerAdapter);
        autoPlaySpeedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "autoPlaySpeedSpinner " +
                        getResources().getStringArray(R.array.auto_play_spinner_speeds_array)[position] +
                        " selected.");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "autoPlaySpeedSpinner dropdown menu aborted.");
            }
        });
    }

    private void setupRandomFillProbabilitySpinner() {
        Spinner randomFillProbabilitySpinner = mSettingsControlView.findViewById(R.id.randomFillProbabilitySpinner);
        ArrayAdapter<CharSequence> randomFillSpinnerAdapter = ArrayAdapter.createFromResource(mSettingsControlView.getContext(),
                R.array.random_fill_spinner_probabilities_array, android.R.layout.simple_spinner_item);
        randomFillSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        randomFillProbabilitySpinner.setAdapter(randomFillSpinnerAdapter);
        randomFillProbabilitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "randomFillProbabilitySpinner " +
                        getResources().getStringArray(R.array.random_fill_spinner_probabilities_array)[position] +
                        " selected.");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "randomFillProbabilitySpinner dropdown menu aborted.");
            }
        });
    }
}