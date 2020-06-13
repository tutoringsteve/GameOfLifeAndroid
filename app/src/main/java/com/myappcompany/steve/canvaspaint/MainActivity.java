package com.myappcompany.steve.canvaspaint;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    private boolean[][] boardState;
    private int numColumns = 17;
    private int numRows = 17;
    private int maxTurns = 32;
    private boolean isAutoPlaying = false;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        PixelGridView pixelGrid = findViewById(R.id.pixelGridView2);
        pixelGrid.setNumColumns(numColumns);
        pixelGrid.setNumRows(numRows);

        handler = new Handler();

    }

    public void onClick(View view) {

        PixelGridView pixelBoard = findViewById(R.id.pixelGridView2);
        boardState = pixelBoard.getCellChecked();

        GameOfLifeBoard board = new GameOfLifeBoard(boardState, numRows, numColumns);
        board.oneTurn();

        boardState = board.getBooleanGameBoard();
        pixelBoard.setCellChecked(boardState);

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


}
