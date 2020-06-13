package com.myappcompany.steve.canvaspaint;

public class Grid {
    private int numRows, numCols;

    //intention to have rectW = rectH for squares but no reason not to keep flexible
    private int rectW, rectH;

    public void Grid(int numRows, int numCols, int rectW, int rectH) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.rectW = rectW;
        this.rectH = rectH;
    }

    public void Grid(int numRows, int numCols, int squareSideLength) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.rectW = squareSideLength;
        this.rectH = squareSideLength;
    }

    public int getNumRows() {
        return numRows;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public void setNumCols(int numCols) {
        this.numCols = numCols;
    }

    public int getRectW() {
        return rectW;
    }

    public void setRectW(int rectW) {
        this.rectW = rectW;
    }

    public int getRectH() {
        return rectH;
    }

    public void setRectH(int rectH) {
        this.rectH = rectH;
    }
}
