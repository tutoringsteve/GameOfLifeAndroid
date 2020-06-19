package com.myappcompany.steve.canvaspaint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by GEX_Dev on 1/25/2020.
 */

public class PixelGridView extends View {
    private int numColumns, numRows;
    private float offsetX = -50, offsetY = 100;
    private float zoomX = 1.0f, zoomY = 1.0f;
    private int cellWidth = 20, cellHeight = 20;

    private boolean[][] cellChecked;
    private final int EDITING = 0;
    private final int PANNING = 1;
    private int state = EDITING;
    private float mStartX = 0,  mStartY = 0;
    private int viewWidth, viewHeight;


    private Paint blackPaint = new Paint();
    private Paint bluePaint = new Paint();
    private Paint darkGreyPaint = new Paint();
    private Paint whitePaint = new Paint();

    private float minGridX, minGridY, maxGridX, maxGridY;

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

    /**
     * Sets the view coordinates for the rectangular region that the grid overlaps the screen.
     */
    private void setGridRange() {
        //determines the grids left side on PixelGridView, bounding between 0 and screen width of View
        minGridX = (offsetX < 0 ? 0 : Math.min(offsetX, viewWidth));
        //determines the grids right side on PixelGridView, bounding between 0 and screen width of View
        maxGridX = (offsetX > viewWidth ? viewWidth : Math.min(viewWidth, offsetX + cellWidth*numColumns));
        //determines the grids top side on PixelGridView, bounding between 0 and screen width of View
        minGridY = (offsetY < 0 ? 0 : Math.min(offsetY, viewHeight));
        //determines the grids bottom side on PixelGridView, bounding between 0 and screen height of View
        maxGridY = (offsetY > viewHeight ? viewHeight : Math.min(viewHeight, offsetY + cellHeight*numRows));
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



        viewWidth = getWidth();
        viewHeight = getHeight();

        setGridRange();

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
                if(minGridX <= worldXToScreenX(i * cellWidth) && worldXToScreenX(i * cellWidth) <= maxGridX) {
                    canvas.drawLine(worldXToScreenX(i * cellWidth), minGridY, worldXToScreenX(i * cellWidth), maxGridY, blackPaint);
                }
            }

            for (int i = 1; i < numRows; i++) {
                if(minGridY <= worldYToScreenY(i * cellHeight) && worldYToScreenY(i * cellHeight) <= maxGridY) {
                    canvas.drawLine(minGridX, worldYToScreenY(i * cellHeight), maxGridX, worldYToScreenY(i * cellHeight), blackPaint);
                }
            }
        } else {
            Log.i("onDraw", "Something fucky with the grid range");
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if(state == EDITING) {
                    //Translates the press location into the world coordinates
                    int column = (int) ((event.getX() - offsetX) / cellWidth);
                    int row = (int) ((event.getY() - offsetY) / cellHeight);

                    cellChecked[column][row] = !cellChecked[column][row];
                    invalidate();
                }
                else if(state == PANNING) {
                    mStartX = event.getX();
                    mStartY = event.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(state == PANNING) {
                    offsetX = event.getX() - mStartX;
                    offsetY = event.getY() - mStartY;
                    invalidate();
                }
                break;
        }

/*
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
*/
        return true;

    }

    public void toggleEditing() {
        switch (state) {
            case EDITING:
                state = PANNING;
                break;
            case PANNING:
                state = EDITING;
                break;
        }
    }

    public float getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(float offsetY) {
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
