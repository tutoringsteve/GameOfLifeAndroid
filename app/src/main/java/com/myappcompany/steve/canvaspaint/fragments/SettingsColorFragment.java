package com.myappcompany.steve.canvaspaint.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.myappcompany.steve.canvaspaint.ColorUtil;
import com.myappcompany.steve.canvaspaint.R;

import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsColorFragment extends Fragment {
    private static final String TAG = "SettingsColorFragment";
    View view;
    OnClickListener onClickListener = new OnClickListener();

    public SettingsColorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings_color, container, false);
        setupBackgroundColorPicker();
        setupAliveSquareColorPicker();
        setupDeadSquareColorPicker();
        setupGridLinesColorPicker();
        return view;
    }

    private void setupBackgroundColorPicker() {
        ImageView backgroundCircleImageView = view.findViewById(R.id.image);
        backgroundCircleImageView.setOnClickListener(onClickListener);

    }

    private void setupAliveSquareColorPicker() {
        ImageView aliveSquareCircleImageView = view.findViewById(R.id.image2);
        aliveSquareCircleImageView.setOnClickListener(onClickListener);
    }

    private void setupDeadSquareColorPicker() {
        ImageView deadSquareCircleImageView = view.findViewById(R.id.image3);
        deadSquareCircleImageView.setOnClickListener(onClickListener);
    }

    private void setupGridLinesColorPicker() {
        ImageView gridLinesCircleImageView = view.findViewById(R.id.image4);
        gridLinesCircleImageView.setOnClickListener(onClickListener);
    }

    public class OnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            buildColorPickerDialogue(v);
        }
    }

    private void buildColorPickerDialogue(final View v) {

        new AmbilWarnaDialog(getContext(), v.getSolidColor(), new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                ColorUtil oldColor = new ColorUtil(v.getSolidColor());
                ColorUtil newColor = new ColorUtil(color);
                Log.d(TAG, "Color switched from " + oldColor.printRGB() + " to " + newColor.printRGB());
                v.setBackgroundColor(color);
            }
        }).show();


    }
}