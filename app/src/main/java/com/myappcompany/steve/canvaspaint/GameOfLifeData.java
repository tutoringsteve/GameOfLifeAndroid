package com.myappcompany.steve.canvaspaint;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GameOfLifeData {

    private final String TAG = "GameOfLifeData";
    private int numColumns;
    private int numRows;
    private boolean[][] cellChecked;
    private float offsetX, offsetY, zoomX, zoomY;
    private int defaultCellWidth, defaultCellHeight;
    private int cellWidth, cellHeight;
    private final float minZoomX, minZoomY, maxZoomX, maxZoomY;

    private static GameOfLifeData instance = null;
    public static GameOfLifeData getInstance() {
        if(instance == null) {
            instance = new GameOfLifeData();
        }
        return instance;
    }

    private GameOfLifeData() {
        numColumns = 20;
        numRows = 20;
        cellChecked = new boolean[numColumns][numRows];
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

    public int getNumColumns() {
        return numColumns;
    }

    public void setNumColumns(int numColumns) {
        this.numColumns = numColumns;
    }

    public int getNumRows() {
        return numRows;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
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

    //todo: test
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

    public JSONArray dataToJSON() {

        JSONArray dataArray = null;

        try {
            dataArray = new JSONArray();
            JSONObject tmpObj = new JSONObject();

            dataArray.put(tmpObj.put("numColumns", numColumns));
            dataArray.put(tmpObj.put("numRows", numRows));
            dataArray.put(tmpObj.put("cellChecked", cellCheckedToJSONArray()));
            dataArray.put(tmpObj.put("offsetX", offsetX));
            dataArray.put(tmpObj.put("offsetY", offsetY));
            dataArray.put(tmpObj.put("zoomX", zoomX));
            dataArray.put(tmpObj.put("zoomY", zoomY));
            dataArray.put(tmpObj.put("defaultCellWidth", defaultCellWidth));
            dataArray.put(tmpObj.put("defaultCellHeight", defaultCellHeight));
            dataArray.put(tmpObj.put("cellWidth", cellWidth));
            dataArray.put(tmpObj.put("cellHeight", cellHeight));
            dataArray.put(tmpObj.put("minZoomX", minZoomX));
            dataArray.put(tmpObj.put("minZoomY", minZoomY));
            dataArray.put(tmpObj.put("maxZoomX", maxZoomX));
            dataArray.put(tmpObj.put("maxZoomY", maxZoomY));

        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "Error in dataToJSON function e:" + e);
        }

        return dataArray;

    }

    public void jsonToData(JSONArray dataArray) {
        
    }
}
