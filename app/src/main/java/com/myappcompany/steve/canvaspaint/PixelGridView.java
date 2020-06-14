package com.myappcompany.steve.canvaspaint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by GEX_Dev on 1/25/2020.
 */

public class PixelGridView extends View {
    private int numColumns, numRows;
    private int offsetX = 0, offsetY = 0;
    private int cellWidth, cellHeight;
    private Paint blackPaint = new Paint();
    private Paint bluePaint = new Paint();
    private boolean[][] cellChecked;
    private boolean isEditing = true;

    public PixelGridView(Context context) {
        this(context, null);
    }

    public PixelGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        blackPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        bluePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        bluePaint.setColor(Color.parseColor("#ff0000ff"));
    }

    public boolean[][] getCellChecked() {
        return cellChecked;
    }

    public void setCellChecked(boolean[][] cellChecked) {
        this.cellChecked = cellChecked;
        invalidate();
    }

    public void setNumColumns(int numColumns) {
        this.numColumns = numColumns;
        calculateDimensions();
        invalidate();
    }

    public int getNumColumns() {
        return numColumns;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
        calculateDimensions();
        invalidate();
    }

    public int getNumRows() {
        return numRows;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculateDimensions();
        invalidate();
    }

    private void calculateDimensions() {
        if (numColumns < 1 || numRows < 1) {
            return;
        }

        cellWidth = getWidth() / numColumns;
        cellHeight = getHeight() / numRows;

        cellChecked = new boolean[numColumns][numRows];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        if (numColumns == 0 || numRows == 0) {
            return;
        }

        int width = getWidth();
        int height = getHeight();

        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {
                if (cellChecked[i][j]) {

                    canvas.drawRect(i * cellWidth, j * cellHeight,
                            (i + 1) * cellWidth, (j + 1) * cellHeight,
                            bluePaint);
                }
            }
        }

        for (int i = 1; i < numColumns; i++) {
            canvas.drawLine(i * cellWidth, 0, i * cellWidth, height, blackPaint);
        }

        for (int i = 1; i < numRows; i++) {
            canvas.drawLine(0, i * cellHeight, width, i * cellHeight, blackPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isEditing) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                //Translates the press location into the world coordinates
                int column = (int) ((event.getX() - offsetX) / cellWidth);
                int row = (int) ((event.getY() - offsetY) / cellHeight);

                cellChecked[column][row] = !cellChecked[column][row];
                invalidate();
            }

            return true;
        } else {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                //change camera coordinates
            }

            return true;
        }
    }

    public void toggleEditing() {
        isEditing = !isEditing;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }
}
