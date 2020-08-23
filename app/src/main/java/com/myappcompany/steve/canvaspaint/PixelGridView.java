package com.myappcompany.steve.canvaspaint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.myappcompany.steve.canvaspaint.activities.MainActivity;
import com.myappcompany.steve.canvaspaint.data.GameOfLifeData;
import com.myappcompany.steve.canvaspaint.data.SettingsData;

import java.util.Set;

/**
 * Created by GEX_Dev on 1/25/2020.
 */

public class PixelGridView extends View {

    private static final String TAG = "PixelGridView";

    private float oldOffsetX, oldOffsetY;
    private final static int EDITING = 0;
    private final static int PANNING = 1;
    private int controlState = EDITING;
    private float mStartX = 0,  mStartY = 0;
    private int viewWidth, viewHeight;

    private SettingsData settingsData = SettingsData.getInstance();
    private GameOfLifeData gameOfLifeData = GameOfLifeData.getInstance();

    private Paint gridLinesPaint = new Paint();
    private Paint aliveSquarePaint = new Paint();
    private Paint backgroundPaint = new Paint();
    private Paint deadSquarePaint = new Paint();

    private float minGridX, minGridY, maxGridX, maxGridY;

    public PixelGridView(Context context) {
        this(context, null);
    }

    public PixelGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gridLinesPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        aliveSquarePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        backgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        deadSquarePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        gridLinesPaint.setColor(settingsData.getGridLinesColor());
        backgroundPaint.setColor(settingsData.getBackgroundColor());
        aliveSquarePaint.setColor(settingsData.getAliveSquareColor());
        deadSquarePaint.setColor(settingsData.getDeadSquareColor());
    }

    /**
     * Sets the view coordinates for the rectangular region that the grid overlaps the screen.
     */
    private void setGridRange() {
        //determines the grids left side on PixelGridView, bounding between 0 and screen width of View
        minGridX = (gameOfLifeData.getOffsetX() < 0 ? 0 : Math.min(gameOfLifeData.getOffsetX(), viewWidth));
        //determines the grids right side on PixelGridView, bounding between 0 and screen width of View
        maxGridX = (gameOfLifeData.getOffsetX() > viewWidth ? viewWidth : Math.min(viewWidth, gameOfLifeData.getOffsetX() + gameOfLifeData.getCellWidth() * settingsData.getBoardWidth()));
        //determines the grids top side on PixelGridView, bounding between 0 and screen width of View
        minGridY = (gameOfLifeData.getOffsetY() < 0 ? 0 : Math.min(gameOfLifeData.getOffsetY(), viewHeight));
        //determines the grids bottom side on PixelGridView, bounding between 0 and screen height of View
        maxGridY = (gameOfLifeData.getOffsetY() > viewHeight ? viewHeight : Math.min(viewHeight, gameOfLifeData.getOffsetY() + gameOfLifeData.getCellHeight() * settingsData.getBoardHeight()));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //makes it so panning does not snap back to the origin when using after a screen rotation
        oldOffsetX = gameOfLifeData.getOffsetX();
        oldOffsetY = gameOfLifeData.getOffsetY();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {


        gridLinesPaint.setColor(settingsData.getGridLinesColor());
        backgroundPaint.setColor(settingsData.getBackgroundColor());
        aliveSquarePaint.setColor(settingsData.getAliveSquareColor());
        deadSquarePaint.setColor(settingsData.getDeadSquareColor());
        //draw the background dark grey so that off the grid appears dark grey
        canvas.drawColor(settingsData.getBackgroundColor());

        if (settingsData.getBoardWidth() == 0 || settingsData.getBoardHeight() == 0) {
            return;
        }

        //pull the data from data source
        int numColumns = settingsData.getBoardWidth();
        int numRows = settingsData.getBoardHeight();
        Log.d(TAG, "numColumns loaded from settingsData as " + numColumns + " and numRows loaded from settingsData as " + numRows);

        //Check to make sure that the gameOfLifeData has the correct board size.
        int gameOfLifeNumColumns = gameOfLifeData.getCellChecked()[0].length;
        int gameOfLifeNumRows = gameOfLifeData.getCellChecked().length;
        if(! (numColumns == gameOfLifeNumColumns && numRows == gameOfLifeNumRows)) {
            Log.d(TAG, "gameOfLifeBoard has numColumns " + gameOfLifeNumColumns + " and numRows " + gameOfLifeNumRows + " and needs to be updated to match settingsData dimensions.");
            gameOfLifeData.updateBoard();
        }

        boolean[][] cellChecked = gameOfLifeData.getCellChecked();



        int cellWidth = gameOfLifeData.getCellWidth();
        int cellHeight = gameOfLifeData.getCellHeight();

        viewWidth = getWidth();
        viewHeight = getHeight();

        //sets minGridX, maxGridX, minGridY, maxGridY
        setGridRange();

        //guarantee that grid is on screen before drawing
        if( minGridX < maxGridX && minGridY < maxGridY) {

            //draw the part of the grid that overlaps with the View
            canvas.drawRect(minGridX, minGridY, maxGridX, maxGridY, deadSquarePaint);

            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numColumns; j++) {
                    if (cellChecked[i][j]) {
                        //Makes sure that at least part of the checked square is on screen before drawing it
                        if((worldXToScreenX(i * cellWidth) > 0 && worldXToScreenX(i * cellWidth) < viewWidth)
                                || (worldXToScreenX((i + 1) * cellWidth) > 0 && worldXToScreenX((i + 1) * cellWidth) < viewWidth)
                                && (worldYToScreenY(j * cellHeight) > 0 && worldYToScreenY(j * cellHeight) < viewHeight)
                                || (worldYToScreenY((j + 1) * cellHeight) > 0 && worldYToScreenY((j + 1) * cellHeight) < viewHeight))
                        {
                            canvas.drawRect(worldXToScreenX(i * cellWidth), worldYToScreenY(j * cellHeight),
                                    worldXToScreenX((i + 1) * cellWidth), worldYToScreenY((j + 1) * cellHeight),
                                    aliveSquarePaint);
                        }
                    }
                }
            }

            for (int i = 1; i < numColumns; i++) {
                //Draws vertical grid lines only if the line is on screen
                if(minGridX <= worldXToScreenX(i * cellWidth) && worldXToScreenX(i * cellWidth) <= maxGridX) {
                    canvas.drawLine(worldXToScreenX(i * cellWidth), minGridY, worldXToScreenX(i * cellWidth), maxGridY, gridLinesPaint);
                }
            }

            for (int i = 1; i < numRows; i++) {
                //Draws horizontal grid lines only if the line is on screen
                if(minGridY <= worldYToScreenY(i * cellHeight) && worldYToScreenY(i * cellHeight) <= maxGridY) {
                    canvas.drawLine(minGridX, worldYToScreenY(i * cellHeight), maxGridX, worldYToScreenY(i * cellHeight), gridLinesPaint);
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
                    int column = (int) ((event.getX() - gameOfLifeData.getOffsetX()) / gameOfLifeData.getCellWidth());
                    int row = (int) ((event.getY() - gameOfLifeData.getOffsetY()) / gameOfLifeData.getCellHeight());

                    //Check to make sure that the clicked region corresponds to a part of the grid
                    if( ((event.getX() - gameOfLifeData.getOffsetX()) >=0 && column < settingsData.getBoardWidth())
                            && ( (event.getY() - gameOfLifeData.getOffsetY()) >= 0 && row < settingsData.getBoardHeight())) {
                        gameOfLifeData.getCellChecked()[column][row] = !gameOfLifeData.getCellChecked()[column][row];
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
                    oldOffsetX = gameOfLifeData.getOffsetX();
                    oldOffsetY = gameOfLifeData.getOffsetY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(controlState == PANNING) {
                    gameOfLifeData.setOffsetX(event.getX() - mStartX);
                    gameOfLifeData.setOffsetY(event.getY() - mStartY);
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
        return worldX + gameOfLifeData.getOffsetX();
    }

    public float worldYToScreenY(float worldY) {
        return worldY + gameOfLifeData.getOffsetY();
    }

}
