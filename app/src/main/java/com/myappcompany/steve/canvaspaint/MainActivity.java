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
    private int state = EDITING;
    private ImageView imageViewEdit;
    private PixelGridView pixelGrid;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pixelGrid = findViewById(R.id.pixelGridView);
        pixelGrid.setNumColumns(numColumns);
        pixelGrid.setNumRows(numRows);

        imageViewEdit = findViewById(R.id.imageViewEdit);

        handler = new Handler();

    }

    public void onClick(View view) {

        boardState = pixelGrid.getCellChecked();

        GameOfLifeBoard board = new GameOfLifeBoard(boardState, numRows, numColumns);
        board.oneTurn();

        boardState = board.getBooleanGameBoard();
        pixelGrid.setCellChecked(boardState);

    }

    public void autoClick(final View view) {

        isAutoPlaying = !isAutoPlaying;

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                onClick(view);
                handler.postDelayed(this, 1000);
            }
        };

        if(isAutoPlaying) {
            Toast.makeText(MainActivity.this,"isAutoPlaying is true", Toast.LENGTH_SHORT).show();
            handler.post(runnable);
        } else {
            Toast.makeText(MainActivity.this,"isAutoPlaying is false", Toast.LENGTH_SHORT).show();
            handler.removeCallbacksAndMessages(null);
        }
    }

    public void zoomIn(View view) {
        if(pixelGrid.getZoomX() <= pixelGrid.maxZoomX && pixelGrid.getZoomY() <= pixelGrid.maxZoomY) {
            pixelGrid.setZoomX(pixelGrid.getZoomX() + 0.25f);
            pixelGrid.setZoomY(pixelGrid.getZoomY() + 0.25f);
            pixelGrid.invalidate();
        }
    }

    public void zoomOut(View view) {
        if(pixelGrid.getZoomY() >= pixelGrid.minZoomX && pixelGrid.getZoomY() >= pixelGrid.minZoomY) {
            pixelGrid.setZoomX(pixelGrid.getZoomX() - 0.25f);
            pixelGrid.setZoomY(pixelGrid.getZoomY() - 0.25f);
            pixelGrid.invalidate();
        }
    }

    public void toggleEditing(View view) {

        switch (state) {
            case EDITING:
                imageViewEdit.setImageResource(R.drawable.ic_pan);
                state = PANNING;
                break;
            case PANNING:
                imageViewEdit.setImageResource(R.drawable.ic_edit);
                state = EDITING;
                break;
        }

        pixelGrid.toggleEditing();
    }


}
