package com.myappcompany.steve.canvaspaint;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private boolean[][] boardState;
    private int numColumns = 20;
    private int numRows = 20;
    private boolean isAutoPlaying = false;
    private final int EDITING = 0;
    private final int PANNING = 1;
    private int controlState = EDITING;
    private PixelGridView pixelGrid;
    private Handler handler;
    public static GameOfLifeData data = GameOfLifeData.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pixelGrid = findViewById(R.id.pixelGridView);
        handler = new Handler();

    }

    public void playClick(View view) {

        boardState = data.getCellChecked();

        GameOfLifeBoard board = new GameOfLifeBoard(boardState, numRows, numColumns);
        board.oneTurn();

        boardState = board.getBooleanGameBoard();
        data.setCellChecked(boardState);
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
        if(controlState != EDITING) {
            controlState = EDITING;
            pixelGrid.toggleEditing();
        }
    }

    public void panningClick(View view) {
        if(controlState != PANNING) {
            controlState = PANNING;
            pixelGrid.toggleEditing();
        }
    }

}
