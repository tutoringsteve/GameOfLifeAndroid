package com.myappcompany.steve.canvaspaint;

public class GameOfLifeData {

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
}
