package com.myappcompany.steve.canvaspaint.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.myappcompany.steve.canvaspaint.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsGridFragment extends Fragment {
    private static final String TAG = "SettingsGridFragment";
    private View view;

    public SettingsGridFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings_grid, container, false);

        setupBoardWidthEditText();
        setupBoardHeightEditText();
        setupHorizontalWrappingCheckBox();
        setupVerticalWrappingCheckBox();
        return view;
    }

    private void setupBoardWidthEditText() {
        EditText boardWidthEditText = view.findViewById(R.id.boardWidthEditText);
        boardWidthEditText.setOnKeyListener(new EditText.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String text = ((EditText) v).getText().toString();
                    Log.d(TAG, "boardWidthEditText changed set to " + text);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void setupBoardHeightEditText() {
        EditText boardHeightEditText = view.findViewById(R.id.boardHeightEditText);
        boardHeightEditText.setOnKeyListener(new EditText.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String text = ((EditText) v).getText().toString();
                    Log.d(TAG, "boardHeightEditText changed set to " + text);
                    return true;
                } else {
                    return false;
                }
            }
        });

    }

    private void setupHorizontalWrappingCheckBox() {
        CheckBox horizontalWrappingCheckBox = view.findViewById(R.id.horizontalWrappingCheckBox);
        horizontalWrappingCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "horizontal wrapping check box set to " + (isChecked? "Checked" : "Not Checked"));
            }
        });
    }

    private void setupVerticalWrappingCheckBox() {
        CheckBox verticalWrappingCheckBox = view.findViewById(R.id.verticalWrappingCheckBox);
        verticalWrappingCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "vertical wrapping check box set to " + (isChecked? "Checked" : "Not Checked"));
            }
        });
    }
}