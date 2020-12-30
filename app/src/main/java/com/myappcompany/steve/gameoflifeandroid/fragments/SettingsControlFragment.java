package com.myappcompany.steve.gameoflifeandroid.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.myappcompany.steve.gameoflifeandroid.R;
import com.myappcompany.steve.gameoflifeandroid.data.SettingsData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsControlFragment extends Fragment {
    private static final String TAG = "SettingsControlFragment";

    private SettingsData settingsData = SettingsData.getInstance();
    private ArrayList<Integer> autoPlaySpeeds = new ArrayList<>(Arrays.asList(1000, 800, 600, 500, 400, 300, 200, 100, 50, 25));
    private ArrayList<Integer> randomFillProbabilities = new ArrayList<>(Arrays.asList(0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100));

    private View mSettingsControlView;

    public SettingsControlFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mSettingsControlView = inflater.inflate(R.layout.fragment_settings_control, container, false);

        setupAutoPlaySpeedSpinner();
        setupRandomFillProbabilitySpinner();

        return mSettingsControlView;
    }

    private void setupAutoPlaySpeedSpinner() {
        Spinner autoPlaySpeedSpinner = mSettingsControlView.findViewById(R.id.autoPlaySpeedSpinner);
        ArrayAdapter<CharSequence> autoPlaySpinnerAdapter = ArrayAdapter.createFromResource(mSettingsControlView.getContext(),
                R.array.auto_play_spinner_speeds_array, android.R.layout.simple_spinner_item);
        autoPlaySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        autoPlaySpeedSpinner.setAdapter(autoPlaySpinnerAdapter);

        autoPlaySpeedSpinner.setSelection(autoPlaySpeeds.indexOf(settingsData.getAutoPlaySpeed()));

        autoPlaySpeedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                settingsData.setAutoPlaySpeed(autoPlaySpeeds.get(position));
                try {
                    settingsData.saveData(getContext());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupRandomFillProbabilitySpinner() {
        Spinner randomFillProbabilitySpinner = mSettingsControlView.findViewById(R.id.randomFillProbabilitySpinner);
        ArrayAdapter<CharSequence> randomFillSpinnerAdapter = ArrayAdapter.createFromResource(mSettingsControlView.getContext(),
                R.array.random_fill_spinner_probabilities_array, android.R.layout.simple_spinner_item);
        randomFillSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        randomFillProbabilitySpinner.setAdapter(randomFillSpinnerAdapter);

        randomFillProbabilitySpinner.setSelection(randomFillProbabilities.indexOf(settingsData.getRandomFillProbability()));

        randomFillProbabilitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                settingsData.setRandomFillProbability(randomFillProbabilities.get(position));
                try {
                    settingsData.saveData(getContext());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}