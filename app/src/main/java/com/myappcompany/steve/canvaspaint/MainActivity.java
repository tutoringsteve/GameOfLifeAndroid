package com.myappcompany.steve.canvaspaint;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private boolean[][] boardState;
    private int numColumns = 17;
    private int numRows = 17;
    private int maxTurns = 32;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PixelGridView pixelGrid = findViewById(R.id.pixelGridView2);
        pixelGrid.setNumColumns(numColumns);
        pixelGrid.setNumRows(numRows);

    }

    public void onClick(View view) {
        PixelGridView pixelBoard = findViewById(R.id.pixelGridView2);
        boardState = pixelBoard.getCellChecked();

        GameOfLifeBoard board = new GameOfLifeBoard(boardState, numRows, numColumns);
        board.oneTurn();

        boardState = board.getBooleanGameBoard();
        pixelBoard.setCellChecked(boardState);

    }


}
