package com.myappcompany.steve.canvaspaint.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.myappcompany.steve.canvaspaint.ColorUtil;
import com.myappcompany.steve.canvaspaint.R;
import com.myappcompany.steve.canvaspaint.data.SettingsData;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsColorFragment extends Fragment {
    private static final String TAG = "SettingsColorFragment";

    private static final String BACKGROUND_COLOR_TAG = "background";
    private static final String ALIVE_SQUARE_COLOR_TAG = "aliveSquare";
    private static final String DEAD_SQUARE_COLOR_TAG = "deadSquare";
    private static final String GRID_LINES_COLOR_TAG = "gridLines";

    private SettingsData settingsData = SettingsData.getInstance();
    View view;
    ImageView backgroundCircleImageView, aliveSquareCircleImageView,
            deadSquareCircleImageView, gridLinesCircleImageView;
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

        TextView resetToDefaultTextView = view.findViewById(R.id.resetToDefault);
        resetToDefaultTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.are_you_sure)
                        .setMessage(R.string.pressing_yes_will_reset_the_colors)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getContext(),
                                        getString(R.string.the_colors_were_reset_to_their_defaults),
                                        Toast.LENGTH_SHORT).show();
                                loadDefaults();
                            }
                        })
                        .setNegativeButton(R.string.no, null)
                        .show();
            }
        });
        return view;
    }

    private void loadDefaults() {
        settingsData.loadDefaultColors();
        backgroundCircleImageView.setColorFilter(settingsData.getBackgroundColor());
        aliveSquareCircleImageView.setColorFilter(settingsData.getAliveSquareColor());
        deadSquareCircleImageView.setColorFilter(settingsData.getDeadSquareColor());
        gridLinesCircleImageView.setColorFilter(settingsData.getGridLinesColor());
        try {
            settingsData.saveData(getContext());
            Log.d(TAG, "Successfully reset colors to their default values and saved.");
        } catch (IOException e) {
            Log.d(TAG, "Failed to save after resetting colors to their default value");
            e.printStackTrace();
        }
    }

    private void setupBackgroundColorPicker() {
        backgroundCircleImageView = view.findViewById(R.id.image);
        backgroundCircleImageView.setTag(BACKGROUND_COLOR_TAG);
        backgroundCircleImageView.setColorFilter(settingsData.getBackgroundColor());
        backgroundCircleImageView.setOnClickListener(onClickListener);

    }

    private void setupAliveSquareColorPicker() {
        aliveSquareCircleImageView = view.findViewById(R.id.image2);
        aliveSquareCircleImageView.setTag(ALIVE_SQUARE_COLOR_TAG);
        aliveSquareCircleImageView.setColorFilter(settingsData.getAliveSquareColor());
        aliveSquareCircleImageView.setOnClickListener(onClickListener);
    }

    private void setupDeadSquareColorPicker() {
        deadSquareCircleImageView = view.findViewById(R.id.image3);
        deadSquareCircleImageView.setTag(DEAD_SQUARE_COLOR_TAG);
        deadSquareCircleImageView.setColorFilter(settingsData.getDeadSquareColor());
        deadSquareCircleImageView.setOnClickListener(onClickListener);
    }

    private void setupGridLinesColorPicker() {
        gridLinesCircleImageView = view.findViewById(R.id.image4);
        gridLinesCircleImageView.setTag(GRID_LINES_COLOR_TAG);
        gridLinesCircleImageView.setColorFilter(settingsData.getGridLinesColor());
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
                try {
                    updateColor(v, color);
                } catch (IOException e) {
                    Log.d(TAG, "Issue saving color with " + v.getTag());
                    e.printStackTrace();
                }
            }
        }).show();


    }

    private void updateColor(View v, int color) throws IOException {
        ColorUtil newColor = new ColorUtil(color);
        Log.d(TAG, "Color switched to " + newColor.printRGB());
        ImageView iv = (ImageView) v;
        iv.setColorFilter(color);

        ColorUtil oldColor;
        switch ((String) iv.getTag()) {
            case BACKGROUND_COLOR_TAG:
                oldColor = new ColorUtil(settingsData.getBackgroundColor());
                Log.d(TAG, BACKGROUND_COLOR_TAG + " color was switched from " + oldColor.printRGB() + " to " + newColor.printRGB());
                settingsData.setBackgroundColor(color);
                settingsData.saveData(getContext());
                break;
            case ALIVE_SQUARE_COLOR_TAG:
                oldColor = new ColorUtil(settingsData.getAliveSquareColor());
                Log.d(TAG, ALIVE_SQUARE_COLOR_TAG + " color was switched from " + oldColor.printRGB() + " to " + newColor.printRGB());
                settingsData.setAliveSquareColor(color);
                settingsData.saveData(getContext());
                break;
            case DEAD_SQUARE_COLOR_TAG:
                oldColor = new ColorUtil(settingsData.getDeadSquareColor());
                Log.d(TAG, DEAD_SQUARE_COLOR_TAG + " color was switched from " + oldColor.printRGB() + " to " + newColor.printRGB());
                settingsData.setDeadSquareColor(color);
                settingsData.saveData(getContext());
                break;
            case GRID_LINES_COLOR_TAG:
                oldColor = new ColorUtil(settingsData.getGridLinesColor());
                Log.d(TAG, GRID_LINES_COLOR_TAG + " color was switched from " + oldColor.printRGB() + " to " + newColor.printRGB());
                settingsData.setGridLinesColor(color);
                settingsData.saveData(getContext());
                break;
            default:
                break;
        }
    }
}