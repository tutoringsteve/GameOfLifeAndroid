package com.myappcompany.steve.canvaspaint.data;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.myappcompany.steve.canvaspaint.ColorUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Random;

public class SettingsData {

    private static final String TAG = "SettingsData";
    private static final String FILE_NAME = "settings.json";

    private static final String AUTO_PLAY_SPEED = "autoPlaySpeed";
    private static final String RANDOM_FILL_PROBABILITY = "randomFillProbability";
    private static final String BOARD_WIDTH = "boardWidth";
    private static final String BOARD_HEIGHT = "boardHeight";
    private static final String HORIZONTAL_WRAP = "horizontalWrap";
    private static final String VERTICAL_WRAP = "verticalWrap";
    private static final String BACKGROUND_COLOR = "backgroundColor";
    private static final String ALIVE_SQUARE_COLOR = "aliveSquareColor";
    private static final String DEAD_SQUARE_COLOR = "deadSquareColor";
    private static final String GRID_LINES_COLOR = "gridLinesColor";

    //Controls
    private int autoPlaySpeed;
    private float randomFillProbability;
    //Board
    private int boardWidth, boardHeight;
    private boolean horizontalWrap, verticalWrap;
    //Color
    private int backgroundColor, aliveSquareColor, deadSquareColor, gridLinesColor;

    private static SettingsData instance = null;
    public static SettingsData getInstance() {
        if(instance == null) {
            instance = new SettingsData();
        }
        return instance;
    }

    private SettingsData() {

    }

    public JSONArray dataToJSON() {

        JSONArray dataArray = null;

        try {
            dataArray = new JSONArray();
            JSONObject tmpObj = new JSONObject();

            dataArray.put(tmpObj.put(AUTO_PLAY_SPEED, autoPlaySpeed));
            dataArray.put(tmpObj.put(RANDOM_FILL_PROBABILITY, randomFillProbability));
            dataArray.put(tmpObj.put(BOARD_WIDTH, boardWidth));
            dataArray.put(tmpObj.put(BOARD_HEIGHT, boardHeight));
            dataArray.put(tmpObj.put(HORIZONTAL_WRAP, horizontalWrap));
            dataArray.put(tmpObj.put(VERTICAL_WRAP, verticalWrap));
            dataArray.put(tmpObj.put(BACKGROUND_COLOR, backgroundColor));
            dataArray.put(tmpObj.put(ALIVE_SQUARE_COLOR, aliveSquareColor));
            dataArray.put(tmpObj.put(DEAD_SQUARE_COLOR, deadSquareColor));
            dataArray.put(tmpObj.put(GRID_LINES_COLOR, gridLinesColor));
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "Error in dataToJSON function e:" + e);
        }

        return dataArray;

    }

    public void jsonToData(JSONArray dataArray) {
        try {
            autoPlaySpeed = dataArray.getJSONObject(0).getInt(AUTO_PLAY_SPEED);
            randomFillProbability = (float) dataArray.getJSONObject(1).getDouble(RANDOM_FILL_PROBABILITY);
            boardWidth = dataArray.getJSONObject(2).getInt(BOARD_WIDTH);
            boardHeight = dataArray.getJSONObject(3).getInt(BOARD_HEIGHT);
            horizontalWrap = dataArray.getJSONObject(4).getBoolean(HORIZONTAL_WRAP);
            verticalWrap = dataArray.getJSONObject(5).getBoolean(VERTICAL_WRAP);
            backgroundColor = dataArray.getJSONObject(6).getInt(BACKGROUND_COLOR);
            aliveSquareColor = dataArray.getJSONObject(7).getInt(ALIVE_SQUARE_COLOR);
            deadSquareColor = dataArray.getJSONObject(8).getInt(DEAD_SQUARE_COLOR);
            gridLinesColor  = dataArray.getJSONObject(9).getInt(GRID_LINES_COLOR);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "Error in jsonToData function e:" + e);
        }
    }

    public void saveData(Context context) throws IOException {
        Writer writer = null;
        try {
            OutputStream out = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(dataToJSON().toString());
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public void loadDefault() {
        autoPlaySpeed = 1000;
        randomFillProbability = 30;
        boardWidth = 20;
        boardHeight = 20;
        horizontalWrap = false;
        verticalWrap = true;
        loadDefaultColors();
    }

    public void loadDefaultColors() {
        backgroundColor = Color.parseColor("#FF444444");
        aliveSquareColor = Color.parseColor("#FF0000FF");
        deadSquareColor = Color.parseColor("#FFFFFFFF");
        gridLinesColor  = Color.parseColor("#FF000000");
    }

    public void loadData(Context context) throws IOException, JSONException {
        BufferedReader reader = null;
        try {
            //Open and read the file into a StringBuilder
            InputStream in = context.openFileInput(FILE_NAME);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                //Line breaks are omitted and irrelevant
                jsonString.append(line);
            }
            Log.d(TAG, "loadData: The following was read from the file : " + jsonString.toString());
            JSONArray array = new JSONArray(jsonString.toString());
            //JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            jsonToData(array);
        } catch (FileNotFoundException e) {
            Log.d(TAG, "loadData: file not found, loading defaults");
            loadDefault();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    public int getAutoPlaySpeed() {
        return autoPlaySpeed;
    }

    public void setAutoPlaySpeed(int autoPlaySpeed) {
        this.autoPlaySpeed = autoPlaySpeed;
    }

    public float getRandomFillProbability() {
        return randomFillProbability;
    }

    public void setRandomFillProbability(float randomFillProbability) {
        this.randomFillProbability = randomFillProbability;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public void setBoardWidth(int boardWidth) {
        this.boardWidth = boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    public void setBoardHeight(int boardHeight) {
        this.boardHeight = boardHeight;
    }

    public boolean isHorizontalWrap() {
        return horizontalWrap;
    }

    public void setHorizontalWrap(boolean horizontalWrap) {
        this.horizontalWrap = horizontalWrap;
    }

    public boolean isVerticalWrap() {
        return verticalWrap;
    }

    public void setVerticalWrap(boolean verticalWrap) {
        this.verticalWrap = verticalWrap;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getAliveSquareColor() {
        return aliveSquareColor;
    }

    public void setAliveSquareColor(int aliveSquareColor) {
        this.aliveSquareColor = aliveSquareColor;
    }

    public int getDeadSquareColor() {
        return deadSquareColor;
    }

    public void setDeadSquareColor(int deadSquareColor) {
        this.deadSquareColor = deadSquareColor;
    }

    public int getGridLinesColor() {
        return gridLinesColor;
    }

    public void setGridLinesColor(int gridLinesColor) {
        this.gridLinesColor = gridLinesColor;
    }
}
