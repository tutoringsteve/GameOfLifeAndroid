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
import android.widget.Toast;

import com.myappcompany.steve.canvaspaint.R;
import com.myappcompany.steve.canvaspaint.data.GameOfLifeData;
import com.myappcompany.steve.canvaspaint.data.SettingsData;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsGridFragment extends Fragment {
    private static final String TAG = "SettingsGridFragment";
    private View view;

    private SettingsData settingsData = SettingsData.getInstance();
    private GameOfLifeData gameOfLifeData = GameOfLifeData.getInstance();

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
        String boardWidthString = String.valueOf(settingsData.getBoardWidth());
        boardWidthEditText.setText(boardWidthString);
        boardWidthEditText.setOnKeyListener(new EditText.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String widthString = ((EditText) v).getText().toString();
                    Log.d(TAG, "boardWidthEditText changed set to " + widthString);
                    setBoardWidth(widthString);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    private void setBoardWidth(String boardWidthString) {
        int newBoardWidth = 0;

        try {
            newBoardWidth = Integer.valueOf(boardWidthString);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Board width set to a non-integer value!");
        }

        if(newBoardWidth >= SettingsData.MINIMUM_BOARD_SIDE_LENGTH && newBoardWidth <= SettingsData.MAXIMUM_BOARD_SIDE_LENGTH) {
            settingsData.setBoardWidth(newBoardWidth);
            try {
                settingsData.saveData(getContext());
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed to save the changes to board width!");
            }
        } else {
            //Remind the user which entries are allowed.
            String message = getString(R.string.type_a_whole_number_between_and,
                    SettingsData.MINIMUM_BOARD_SIDE_LENGTH, SettingsData.MAXIMUM_BOARD_SIDE_LENGTH);
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

            //Display the current value
            EditText boardWidthEditText = view.findViewById(R.id.boardWidthEditText);
            String originalWidthString = String.valueOf(settingsData.getBoardWidth());
            boardWidthEditText.setText(originalWidthString);
        }
    }

    private void setupBoardHeightEditText() {
        EditText boardHeightEditText = view.findViewById(R.id.boardHeightEditText);
        String boardHeightString = String.valueOf(settingsData.getBoardHeight());
        boardHeightEditText.setText(boardHeightString);
        boardHeightEditText.setOnKeyListener(new EditText.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String text = ((EditText) v).getText().toString();
                    Log.d(TAG, "boardHeightEditText changed set to " + text);
                    setBoardHeight(text);
                    return true;
                } else {
                    return false;
                }
            }
        });

    }

    private void setBoardHeight(String boardHeightString) {
        int newBoardHeight = 0;

        try {
            newBoardHeight = Integer.valueOf(boardHeightString);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Board height set to a non-integer value!");
        }

        if(newBoardHeight >= SettingsData.MINIMUM_BOARD_SIDE_LENGTH && newBoardHeight <= SettingsData.MAXIMUM_BOARD_SIDE_LENGTH) {
            settingsData.setBoardHeight(newBoardHeight);
            gameOfLifeData.updateBoard();
            try {
                settingsData.saveData(getContext());
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed to save the changes to board height!");
            }
        } else {
            //Remind the user which entries are allowed.
            String message = getString(R.string.type_a_whole_number_between_and,
                    SettingsData.MINIMUM_BOARD_SIDE_LENGTH, SettingsData.MAXIMUM_BOARD_SIDE_LENGTH);
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

            //Display the current value
            EditText boardHeightEditText = view.findViewById(R.id.boardHeightEditText);
            String originalHeightString = String.valueOf(settingsData.getBoardHeight());
            boardHeightEditText.setText(originalHeightString);
        }
    }

    private void setupHorizontalWrappingCheckBox() {
        CheckBox horizontalWrappingCheckBox = view.findViewById(R.id.horizontalWrappingCheckBox);
        horizontalWrappingCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "horizontal wrapping check box set to " + (isChecked? "Checked" : "Not Checked"));
                settingsData.setHorizontalWrap(isChecked);
                try {
                    settingsData.saveData(getContext());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Horizontal wrap data could not be saved!");
                }
            }
        });
    }

    private void setupVerticalWrappingCheckBox() {
        CheckBox verticalWrappingCheckBox = view.findViewById(R.id.verticalWrappingCheckBox);
        verticalWrappingCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "vertical wrapping check box set to " + (isChecked? "Checked" : "Not Checked"));
                settingsData.setVerticalWrap(isChecked);
                try {
                    settingsData.saveData(getContext());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d(TAG, "Vertical wrap data could not be saved!");
                }
            }
        });
    }
}