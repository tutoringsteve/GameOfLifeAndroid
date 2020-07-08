package com.myappcompany.steve.canvaspaint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.myappcompany.steve.canvaspaint.activities.MainActivity;
import com.myappcompany.steve.canvaspaint.data.GameOfLifeData;

/**
 * Created by GEX_Dev on 1/25/2020.
 */

public class PixelGridView extends View {

    private float oldOffsetX, oldOffsetY;
    private final int EDITING = 0;
    private final int PANNING = 1;
    private int controlState = EDITING;
    private float mStartX = 0,  mStartY = 0;
    private int viewWidth, viewHeight;

    private GameOfLifeData data = MainActivity.data;

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
        minGridX = (data.getOffsetX() < 0 ? 0 : Math.min(data.getOffsetX(), viewWidth));
        //determines the grids right side on PixelGridView, bounding between 0 and screen width of View
        maxGridX = (data.getOffsetX() > viewWidth ? viewWidth : Math.min(viewWidth, data.getOffsetX() + data.getCellWidth() * data.getNumColumns()));
        //determines the grids top side on PixelGridView, bounding between 0 and screen width of View
        minGridY = (data.getOffsetY() < 0 ? 0 : Math.min(data.getOffsetY(), viewHeight));
        //determines the grids bottom side on PixelGridView, bounding between 0 and screen height of View
        maxGridY = (data.getOffsetY() > viewHeight ? viewHeight : Math.min(viewHeight, data.getOffsetY() + data.getCellHeight() * data.getNumRows()));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //makes it so panning does not snap back to the origin when using after a screen rotation
        oldOffsetX = data.getOffsetX();
        oldOffsetY = data.getOffsetY();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //draw the background dark grey so that off the grid appears dark grey
        canvas.drawColor(Color.DKGRAY);

        if (data.getNumColumns() == 0 || data.getNumRows() == 0) {
            return;
        }

        //pull the data from data source
        int numColumns = data.getNumColumns();
        int numRows = data.getNumRows();
        boolean[][] cellChecked = data.getCellChecked();
        int cellWidth = data.getCellWidth();
        int cellHeight = data.getCellHeight();

        viewWidth = getWidth();
        viewHeight = getHeight();

        //sets minGridX, maxGridX, minGridY, maxGridY
        setGridRange();

        //guarantee that grid is on screen before drawing
        if( minGridX < maxGridX && minGridY < maxGridY) {

            //draw the part of the grid that overlaps with the View
            canvas.drawRect(minGridX, minGridY, maxGridX, maxGridY, whitePaint);

            for (int i = 0; i < numColumns; i++) {
                for (int j = 0; j < numRows; j++) {
                    if (cellChecked[i][j]) {
                        //Makes sure that at least part of the checked square is on screen before drawing it
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
                //Draws vertical grid lines only if the line is on screen
                if(minGridX <= worldXToScreenX(i * cellWidth) && worldXToScreenX(i * cellWidth) <= maxGridX) {
                    canvas.drawLine(worldXToScreenX(i * cellWidth), minGridY, worldXToScreenX(i * cellWidth), maxGridY, blackPaint);
                }
            }

            for (int i = 1; i < numRows; i++) {
                //Draws horizontal grid lines only if the line is on screen
                if(minGridY <= worldYToScreenY(i * cellHeight) && worldYToScreenY(i * cellHeight) <= maxGridY) {
                    canvas.drawLine(minGridX, worldYToScreenY(i * cellHeight), maxGridX, worldYToScreenY(i * cellHeight), blackPaint);
                }
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                if(controlState == EDITING) {
                    //Translates the press location into the world coordinates
                    int column = (int) ((event.getX() - data.getOffsetX()) / data.getCellWidth());
                    int row = (int) ((event.getY() - data.getOffsetY()) / data.getCellHeight());

                    //Check to make sure that the clicked region corresponds to a part of the grid
                    if( ((event.getX() - data.getOffsetX()) >=0 && column < data.getNumColumns())
                            && ( (event.getY() - data.getOffsetY()) >= 0 && row < data.getNumRows())) {
                        data.getCellChecked()[column][row] = !data.getCellChecked()[column][row];
                        invalidate();
                    }
                }
                else if(controlState == PANNING) {
                    mStartX = event.getX() - oldOffsetX;
                    mStartY = event.getY() - oldOffsetY;
                }
                break;
            case MotionEvent.ACTION_UP:
                if(controlState == PANNING) {
                    oldOffsetX = data.getOffsetX();
                    oldOffsetY = data.getOffsetY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(controlState == PANNING) {
                    data.setOffsetX(event.getX() - mStartX);
                    data.setOffsetY(event.getY() - mStartY);
                    invalidate();
                }
                break;
        }

        return true;

    }

    public void setControlState(int controlState) {
        this.controlState = controlState;
    }

    public float worldXToScreenX(float worldX) {
        return worldX + data.getOffsetX();
    }

    public float worldYToScreenY(float worldY) {
        return worldY + data.getOffsetY();
    }

}
