package com.myappcompany.steve.canvaspaint.data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Random;

public class GameOfLifeData {

    private static final String TAG = "GameOfLifeData";

    private static final String CELL_CHECKED_TAG = "cellChecked";
    private static final String OFFSET_X_TAG = "offsetX";
    private static final String OFFSET_Y_TAG = "offsetY";
    private static final String ZOOM_X_TAG = "zoomX";
    private static final String ZOOM_Y_TAG = "zoomY";
    private static final String DEFAULT_CELL_WIDTH_TAG = "defaultCellWidth";
    private static final String DEFAULT_CELL_HEIGHT_TAG = "defaultCellHeight";
    private static final String CELL_WIDTH_TAG = "cellWidth";
    private static final String CELL_HEIGHT_TAG = "cellHeight";
    private static final String MIN_ZOOM_X_TAG = "minZoomX";
    private static final String MIN_ZOOM_Y_TAG = "minZoomY";
    private static final String MAX_ZOOM_X_TAG = "maxZoomX";
    private static final String MAX_ZOOM_Y_TAG = "maxZoomY";

    private boolean[][] cellChecked;
    private float offsetX, offsetY, zoomX, zoomY;
    private int defaultCellWidth, defaultCellHeight;
    private int cellWidth, cellHeight;
    private float minZoomX, minZoomY, maxZoomX, maxZoomY;

    private SettingsData settingsData = SettingsData.getInstance();

    private static GameOfLifeData instance = null;
    public static GameOfLifeData getInstance() {
        if(instance == null) {
            instance = new GameOfLifeData();
        }
        return instance;
    }

    private GameOfLifeData() {
        //todo: currently changing these settings breaks save/load.
        loadDefaults();
    }

    private void loadDefaults() {
        cellChecked = new boolean[settingsData.getBoardHeight()][settingsData.getBoardWidth()];
        Log.d(TAG, "cellChecked initialized with height " + settingsData.getBoardHeight()
                + " and width " + settingsData.getBoardWidth());
        offsetX = 0;
        offsetY = 0;
        zoomX = 1.0f;
        zoomY = 1.0f;
        defaultCellWidth = 100;
        defaultCellHeight = 100;
        cellWidth = defaultCellWidth;
        cellHeight = defaultCellHeight;
        minZoomX = 0.25f;
        minZoomY = 0.25f;
        maxZoomX = 2.5f;
        maxZoomY = 2.5f;
    }

    public boolean[][] getCellChecked() {
        return cellChecked;
    }

    public void setCellChecked(boolean[][] cellChecked) {
        this.cellChecked = cellChecked;
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
        cellWidth = (int) (defaultCellWidth * zoomX);
    }

    public float getZoomY() {
        return zoomY;
    }

    public void setZoomY(float zoomY) {
        this.zoomY = zoomY;
        cellHeight = (int) (defaultCellHeight * zoomY);
    }

    public int getDefaultCellWidth() {
        return defaultCellWidth;
    }

    public void setDefaultCellWidth(int defaultCellWidth) {
        this.defaultCellWidth = defaultCellWidth;
    }

    public int getDefaultCellHeight() {
        return defaultCellHeight;
    }

    public void setDefaultCellHeight(int defaultCellHeight) {
        this.defaultCellHeight = defaultCellHeight;
    }

    public float getMinZoomX() {
        return minZoomX;
    }

    public float getMinZoomY() {
        return minZoomY;
    }

    public float getMaxZoomX() {
        return maxZoomX;
    }

    public float getMaxZoomY() {
        return maxZoomY;
    }

    public int getCellWidth() {
        return cellWidth;
    }

    public int getCellHeight() {
        return cellHeight;
    }

    public JSONArray cellCheckedToJSONArray(){

        StringBuilder sbOuter = new StringBuilder();
        StringBuilder sbInner = new StringBuilder();

        sbOuter.append("[");
        for(int i = 0; i < cellChecked.length; i++) {
            sbInner.append("[");
            for(int j = 0; j < cellChecked[0].length; j++) {
                sbInner.append((cellChecked[i][j] ? "true" : "false") + (j < cellChecked[0].length - 1 ? "," : ""));
            }
            sbInner.append("]" + (i < cellChecked.length - 1 ? "," : ""));
            sbOuter.append(sbInner.toString());
            sbInner.setLength(0);
        }
        sbOuter.append("]");

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(sbOuter.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "Error in cellCheckedToJSONArray e:" + e);
        }

        return jsonArray;
    }

    public JSONObject dataToJSON() {

        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject();

            jsonObject.put(CELL_CHECKED_TAG, cellCheckedToJSONArray());
            jsonObject.put(OFFSET_X_TAG, offsetX);
            jsonObject.put(OFFSET_Y_TAG, offsetY);
            jsonObject.put(ZOOM_X_TAG, zoomX);
            jsonObject.put(ZOOM_Y_TAG, zoomY);
            jsonObject.put(DEFAULT_CELL_HEIGHT_TAG, defaultCellHeight);
            jsonObject.put(DEFAULT_CELL_WIDTH_TAG, defaultCellWidth);
            jsonObject.put(CELL_HEIGHT_TAG, cellHeight);
            jsonObject.put(CELL_WIDTH_TAG, cellWidth);
            jsonObject.put(MIN_ZOOM_X_TAG, minZoomX);
            jsonObject.put(MIN_ZOOM_Y_TAG, minZoomY);
            jsonObject.put(MAX_ZOOM_X_TAG, maxZoomX);
            jsonObject.put(MAX_ZOOM_Y_TAG, maxZoomY);

        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "Error in dataToJSON function e:" + e);
        }

        return jsonObject;

    }

    public boolean[][] jsonArrayToCellChecked(JSONArray cellCheckedJSONArray) {
        boolean[][] cellChecked;
        JSONArray cellCheckedRow = null;

        try {
            int numRows = cellCheckedJSONArray.length();
            int numColumns = cellCheckedJSONArray.getJSONArray(0).length();

            settingsData.setBoardHeight(numRows);
            settingsData.setBoardWidth(numColumns);

            cellChecked = new boolean[numRows][numColumns];
            for (int i = 0; i < numRows; i++) {
                cellCheckedRow = cellCheckedJSONArray.getJSONArray(i);
                for (int j = 0; j < numColumns; j++) {
                    cellChecked[i][j] = cellCheckedRow.getBoolean(j);
                }
            }

            return cellChecked;

        } catch (Exception e) {
            e.printStackTrace();
            int numRows = settingsData.getBoardHeight();
            int numColumns = settingsData.getBoardWidth();
            Log.d(TAG, "Error in jsonArrayToCellChecked function e:" + e);
            Log.d(TAG, "Initializing cellChecked to a dead (false) grid with height "
                            + numRows + " and width " + numColumns);

            return new boolean[numRows][numColumns];
        }
    }

    public void jsonToData(JSONObject jsonObject) {

        try {
            cellChecked = jsonArrayToCellChecked(jsonObject.getJSONArray("cellChecked"));
            offsetX = jsonObject.getInt("offsetX");
            offsetY = jsonObject.getInt("offsetY");
            zoomX = (float) jsonObject.getDouble("zoomX");
            zoomY = (float) jsonObject.getDouble("zoomY");
            defaultCellWidth = jsonObject.getInt("defaultCellWidth");
            defaultCellHeight = jsonObject.getInt("defaultCellHeight");
            cellWidth  = jsonObject.getInt("cellWidth");
            cellHeight = jsonObject.getInt("cellHeight");
            minZoomX = (float) jsonObject.getDouble("minZoomX");
            minZoomY = (float) jsonObject.getDouble("minZoomY");
            maxZoomX = (float) jsonObject.getDouble("maxZoomX");
            maxZoomY = (float) jsonObject.getDouble("maxZoomY");

        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "Error in jsonToData function e:" + e);
            Log.d(TAG, "Loading defaults for GameOfLifeData instead.");
            loadDefaults();
        }

    }

    public void stringToData(String saveString) {
        try {
            jsonToData(new JSONObject(saveString));
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "Error in stringToData function e:" + e);
        }
    }

}
