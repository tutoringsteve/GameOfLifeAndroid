package com.myappcompany.steve.canvaspaint;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    private final String CONTROL_STATE_KEY_INDEX = "controlState";
    private boolean isAutoPlaying = false;
    private final int EDITING = 0;
    private final int PANNING = 1;
    private int controlState = EDITING;
    private PixelGridView pixelGrid;
    private Handler handler;
    private String saveString;
    public static GameOfLifeData data = GameOfLifeData.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pixelGrid = findViewById(R.id.pixelGridView);

        if(savedInstanceState != null) {
            controlState = savedInstanceState.getInt(CONTROL_STATE_KEY_INDEX, EDITING);
            pixelGrid.setControlState(controlState);
            Log.i(TAG, "A savedInstanceState was found! controlState set to " + (controlState == EDITING ? "EDITING" : "PANNING"));
        }

        handler = new Handler();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CONTROL_STATE_KEY_INDEX, controlState);
        Log.i(TAG, CONTROL_STATE_KEY_INDEX);
    }

    public void playClick(View view) {
        GameOfLifeBoard board = new GameOfLifeBoard(data.getCellChecked(), data.getNumRows(), data.getNumColumns());
        board.oneTurn();
        data.setCellChecked(board.getBooleanGameBoard());
        pixelGrid.invalidate();
    }

    public void autoPlayClick(final View view) {

        isAutoPlaying = !isAutoPlaying;

        if(isAutoPlaying) {
            Toast.makeText(MainActivity.this,"isAutoPlaying is true", Toast.LENGTH_SHORT).show();

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    playClick(view);
                    handler.postDelayed(this, 1000);
                }
            };

            handler.post(runnable);
        } else {
            Toast.makeText(MainActivity.this,"isAutoPlaying is false", Toast.LENGTH_SHORT).show();
            handler.removeCallbacksAndMessages(null);
        }
    }

    public void zoomInClick(View view) {
        if(data.getZoomX() < data.getMaxZoomX() && data.getZoomY() < data.getMaxZoomY()) {
            data.setZoomX(data.getZoomX() + 0.25f);
            data.setZoomY(data.getZoomY() + 0.25f);
            pixelGrid.invalidate();
        }
    }

    public void zoomOutClick(View view) {
        if(data.getZoomY() > data.getMinZoomX() && data.getZoomY() > data.getMinZoomY()) {
            data.setZoomX(data.getZoomX() - 0.25f);
            data.setZoomY(data.getZoomY() - 0.25f);
            pixelGrid.invalidate();
        }
    }

    public void editClick(View view) {

            controlState = EDITING;
            pixelGrid.setControlState(EDITING);

    }

    public void panningClick(View view) {
        if(controlState != PANNING) {
            controlState = PANNING;
            pixelGrid.setControlState(PANNING);
        }
    }

    public void saveClick(View view) {
        //todo: open the SaveActivity activity, passing the saveState as a String through the intent
        try {
            saveString = data.dataToJSON().toString();
            Log.i(TAG, "data saved to save state!" + saveString);

            //passes saveString to Save/Load activity and opens that activity.
            Intent intent = new Intent(this, SaveActivity.class);
            intent.putExtra("saveString", saveString);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "Error with saveClick");
        }
    }

    public void loadClick(View view) {
        try {
            data.stringToData(saveString);
            pixelGrid.invalidate();
            Log.i(TAG, "data loaded from save state!" + data.dataToJSON().toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "Error with loadClick");
        }
    }

}
