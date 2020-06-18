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
    private int offsetX = -50, offsetY = 100;
    private float zoomX = 1.0f, zoomY = 1.0f;
    private int cellWidth = 20, cellHeight = 20;
    private Paint blackPaint = new Paint();
    private Paint bluePaint = new Paint();
    private Paint darkGreyPaint = new Paint();
    private Paint whitePaint = new Paint();
    private boolean[][] cellChecked;
    private boolean isEditing = true;

    public PixelGridView(Context context) {
        this(context, null);
    }

    public PixelGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        blackPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        bluePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        bluePaint.setColor(Color.BLUE);
        darkGreyPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        darkGreyPaint.setColor(Color.DKGRAY);
        whitePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        whitePaint.setColor(Color.WHITE);
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

        cellChecked = new boolean[numColumns][numRows];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //draw the background dark grey so that off the grid appears dark grey
        canvas.drawColor(Color.DKGRAY);

        if (numColumns == 0 || numRows == 0) {
            return;
        }

        int viewWidth = getWidth();
        int viewHeight = getHeight();

        //determines the grids left side on PixelGridView, bounding between 0 and screen width of View
        int minGridX = (offsetX < 0 ? 0 : Math.min(offsetX, viewWidth));
        //determines the grids right side on PixelGridView, bounding between 0 and screen width of View
        int maxGridX = (offsetX > viewWidth ? viewWidth : Math.min(viewWidth, offsetX + cellWidth*numColumns));
        //determines the grids top side on PixelGridView, bounding between 0 and screen width of View
        int minGridY = (offsetY < 0 ? 0 : Math.min(offsetY, viewHeight));
        //determines the grids bottom side on PixelGridView, bounding between 0 and screen height of View
        int maxGridY = (offsetY > viewHeight ? viewHeight : Math.min(viewHeight, offsetY + cellHeight*numRows));

        //guarantee that grid is on screen before drawing
        if( minGridX < maxGridX && minGridY < maxGridY) {

            //draw the part of the grid that overlaps with the View
            canvas.drawRect(minGridX, minGridY, maxGridX, maxGridY, whitePaint);

            //todo: make this more efficient by checking whether the rectangle is fully on screen
            for (int i = 0; i < numColumns; i++) {
                for (int j = 0; j < numRows; j++) {
                    if (cellChecked[i][j]) {
                        //Makes sure that at least part of the square is on screen before drawing it
                        if((worldXToScreenX(i * cellWidth) > 0 && worldXToScreenX(i * cellWidth) < viewWidth)
                                || (worldXToScreenX((i + 1) * cellWidth) > 0 && worldXToScreenX((i + 1) * cellWidth) < viewWidth)
                                && (worldYToScreenY(j * cellHeight) > 0 && worldYToScreenY(j * cellHeight) < viewHeight)
                                || (worldYToScreenY((j + 1) * cellHeight) > 0 && worldYToScreenY((j + 1) * cellHeight) < viewHeight))
                        {
                            canvas.drawRect(worldXToScreenX(i * cellWidth), worldYToScreenY(j * cellHeight),
                                    worldXToScreenX((i + 1) * cellWidth), worldYToScreenY((j + 1) * cellHeight),
                                    bluePaint);
                        }
                    }
                }
            }

            for (int i = 1; i < numColumns; i++) {
                if(minGridX <= i * cellWidth && i * cellWidth <= maxGridX) {
                    canvas.drawLine(worldXToScreenX(i * cellWidth), minGridY, worldXToScreenX(i * cellWidth), maxGridY, blackPaint);
                }
            }

            for (int i = 1; i < numRows; i++) {
                if(minGridY <= i * cellHeight && i * cellHeight <= maxGridY) {
                    canvas.drawLine(minGridX, worldYToScreenY(i * cellHeight), maxGridX, worldYToScreenY(i * cellHeight), blackPaint);
                }
            }
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

    public float getZoomX() {
        return zoomX;
    }

    public void setZoomX(float zoomX) {
        this.zoomX = zoomX;
    }

    public float getZoomY() {
        return zoomY;
    }

    public void setZoomY(float zoomY) {
        this.zoomY = zoomY;
    }

    public void setCellWidth(int cellWidth) {
        this.cellWidth = cellWidth;
    }

    public void setCellHeight(int cellHeight) {
        this.cellHeight = cellHeight;
    }

    public float worldXToScreenX(float worldX) {
        return worldX + offsetX;
    }

    public float worldYToScreenY(float worldY) {
        return worldY + offsetY;
    }
}
