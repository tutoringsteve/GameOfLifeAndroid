package com.myappcompany.steve.gameoflifeandroid.data;

import org.json.JSONException;
import org.json.JSONObject;

public class SaveData {

    private static final String SAVE_NAME_TAG = "saveName";
    private static final String BOARD_STATE_STRING_TAG = "boardState";
    private static final String SAVE_DATE_TAG = "saveDate";
    private static final String BACKGROUND_COLOR_TAG = "backgroundColor";
    private static final String ALIVE_SQUARE_COLOR_TAG = "aliveSquareColor";
    private static final String DEAD_SQUARE_COLOR_TAG = "deadSquareColor";
    private static final String GRID_LINES_COLOR_TAG = "gridLinesColor";
    private static final String BOARD_HEIGHT_TAG = "boardHeight";
    private static final String BOARD_WIDTH_TAG = "boardWidth";

    String mSaveName;
    String mBoardStateString;
    String mSaveDate;
    int mBackgroundColor;
    int mAliveSquareColor;
    int mDeadSquareColor;
    int mGridLinesColor;
    int mBoardHeight, mBoardWidth;

    public String getSaveName() {
        return mSaveName;
    }

    public String getBoardStateString() {
        return mBoardStateString;
    }

    public String getSaveDate() {
        return mSaveDate;
    }

    public int getBoardHeight() {
        return mBoardHeight;
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public int getAliveSquareColor() {
        return mAliveSquareColor;
    }

    public int getDeadSquareColor() {
        return mDeadSquareColor;
    }

    public int getGridLinesColor() {
        return mGridLinesColor;
    }

    public int getBoardWidth() {
        return mBoardWidth;
    }

    public SaveData(String saveName, String boardStateString, String saveDate, int backgroundColor,
                    int aliveSquareColor, int deadSquareColor, int gridLinesColor, int boardWidth,
                    int boardHeight) {
        mSaveName = saveName;
        mBoardStateString = boardStateString;
        mSaveDate = saveDate;
        mBackgroundColor = backgroundColor;
        mAliveSquareColor = aliveSquareColor;
        mDeadSquareColor = deadSquareColor;
        mGridLinesColor = gridLinesColor;
        mBoardHeight = boardHeight;
        mBoardWidth = boardWidth;
    }

    public SaveData(JSONObject jsonObject) throws JSONException{
        mSaveName = jsonObject.getString(SAVE_NAME_TAG);
        mBoardStateString = jsonObject.getString(BOARD_STATE_STRING_TAG);
        mSaveDate = jsonObject.getString(SAVE_DATE_TAG);
        mBackgroundColor = jsonObject.getInt(BACKGROUND_COLOR_TAG);
        mAliveSquareColor = jsonObject.getInt(ALIVE_SQUARE_COLOR_TAG);
        mDeadSquareColor = jsonObject.getInt(DEAD_SQUARE_COLOR_TAG);
        mGridLinesColor = jsonObject.getInt(GRID_LINES_COLOR_TAG);
        mBoardHeight = jsonObject.getInt(BOARD_HEIGHT_TAG);
        mBoardWidth = jsonObject.getInt(BOARD_WIDTH_TAG);
    }

    public JSONObject toJSON() throws JSONException {
    JSONObject jsonObject = new JSONObject();

    jsonObject.put(SAVE_NAME_TAG, mSaveName);
    jsonObject.put(BOARD_STATE_STRING_TAG, mBoardStateString);
    jsonObject.put(SAVE_DATE_TAG, mSaveDate);
    jsonObject.put(BACKGROUND_COLOR_TAG, mBackgroundColor);
    jsonObject.put(ALIVE_SQUARE_COLOR_TAG, mAliveSquareColor);
    jsonObject.put(DEAD_SQUARE_COLOR_TAG, mDeadSquareColor);
    jsonObject.put(GRID_LINES_COLOR_TAG, mGridLinesColor);
    jsonObject.put(BOARD_HEIGHT_TAG, mBoardHeight);
    jsonObject.put(BOARD_WIDTH_TAG, mBoardWidth);

    return  jsonObject;
    }

}
