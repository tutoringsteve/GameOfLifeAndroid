package com.myappcompany.steve.gameoflifeandroid.data;

import android.content.Context;
import android.graphics.Color;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class SettingsData {

    private static final String TAG = "SettingsData";
    private static final String FILE_NAME = "settings.json";

    private static final String AUTO_PLAY_SPEED_TAG = "autoPlaySpeed";
    private static final String RANDOM_FILL_PROBABILITY_TAG = "randomFillProbability";
    private static final String BOARD_WIDTH_TAG = "boardWidth";
    private static final String BOARD_HEIGHT_TAG = "boardHeight";
    private static final String HORIZONTAL_WRAP_TAG = "horizontalWrap";
    private static final String VERTICAL_WRAP_TAG = "verticalWrap";
    private static final String BACKGROUND_COLOR_TAG = "backgroundColor";
    private static final String ALIVE_SQUARE_COLOR_TAG = "aliveSquareColor";
    private static final String DEAD_SQUARE_COLOR_TAG = "deadSquareColor";
    private static final String GRID_LINES_COLOR_TAG = "gridLinesColor";


    private static final int DEFAULT_AUTO_PLAY_SPEED = 500;
    private static final int DEFAULT_RANDOM_FILL_PROBABILITY = 30;
    private static final int DEFAULT_BOARD_WIDTH = 20;
    private static final int DEFAULT_BOARD_HEIGHT = 20;
    private static final boolean DEFAULT_HORIZONTAL_WRAP = true;
    private static final boolean DEFAULT_VERTICAL_WRAP = true;
    private static final int DEFAULT_BACKGROUND_COLOR = Color.parseColor("#FF444444");
    private static final int DEFAULT_ALIVE_SQUARE_COLOR = Color.parseColor("#FF0000FF");
    private static final int DEFAULT_DEAD_SQUARE_COLOR = Color.parseColor("#FFFFFFFF");
    private static final int DEFAULT_GRID_LINES_COLOR = Color.parseColor("#FF000000");

    public static final int MINIMUM_BOARD_SIDE_LENGTH = 1;
    public static final int MAXIMUM_BOARD_SIDE_LENGTH = 500;

    //Controls
    private int autoPlaySpeed;
    private int randomFillProbability;
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
        loadDefault();
    }

    public JSONObject dataToJSON() {

        JSONObject jsonObject = null;

        try {
             jsonObject = new JSONObject();

            jsonObject.put(AUTO_PLAY_SPEED_TAG, autoPlaySpeed);
            jsonObject.put(RANDOM_FILL_PROBABILITY_TAG, randomFillProbability);
            jsonObject.put(BOARD_WIDTH_TAG, boardWidth);
            jsonObject.put(BOARD_HEIGHT_TAG, boardHeight);
            jsonObject.put(HORIZONTAL_WRAP_TAG, horizontalWrap);
            jsonObject.put(VERTICAL_WRAP_TAG, verticalWrap);
            jsonObject.put(BACKGROUND_COLOR_TAG, backgroundColor);
            jsonObject.put(ALIVE_SQUARE_COLOR_TAG, aliveSquareColor);
            jsonObject.put(DEAD_SQUARE_COLOR_TAG, deadSquareColor);
            jsonObject.put(GRID_LINES_COLOR_TAG, gridLinesColor);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;

    }

    public void jsonToData(JSONObject jsonObject) {

        try {

            autoPlaySpeed = jsonObject.getInt(AUTO_PLAY_SPEED_TAG);
            randomFillProbability = jsonObject.getInt(RANDOM_FILL_PROBABILITY_TAG);
            boardWidth = jsonObject.getInt(BOARD_WIDTH_TAG);

            //ensure boardWidth is within the bounds, and if not, set it to the default
            if(boardWidth < MINIMUM_BOARD_SIDE_LENGTH || boardWidth > MAXIMUM_BOARD_SIDE_LENGTH) {
                boardWidth = DEFAULT_BOARD_WIDTH;
            }

            //ensure boardHeight  is within the bounds, and if not, set it to the default
            boardHeight = jsonObject.getInt(BOARD_HEIGHT_TAG);
            if(boardHeight < MINIMUM_BOARD_SIDE_LENGTH || boardHeight > MAXIMUM_BOARD_SIDE_LENGTH) {
                boardHeight = DEFAULT_BOARD_HEIGHT;
            }
            horizontalWrap = jsonObject.getBoolean(HORIZONTAL_WRAP_TAG);
            verticalWrap = jsonObject.getBoolean(VERTICAL_WRAP_TAG);
            backgroundColor = jsonObject.getInt(BACKGROUND_COLOR_TAG);
            aliveSquareColor = jsonObject.getInt(ALIVE_SQUARE_COLOR_TAG);
            deadSquareColor = jsonObject.getInt(DEAD_SQUARE_COLOR_TAG);
            gridLinesColor  = jsonObject.getInt(GRID_LINES_COLOR_TAG);
        } catch (Exception e) {
            e.printStackTrace();
            loadDefault();
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
        autoPlaySpeed = DEFAULT_AUTO_PLAY_SPEED;
        randomFillProbability = DEFAULT_RANDOM_FILL_PROBABILITY;
        boardWidth = DEFAULT_BOARD_WIDTH;
        boardHeight = DEFAULT_BOARD_HEIGHT;
        horizontalWrap = DEFAULT_HORIZONTAL_WRAP;
        verticalWrap = DEFAULT_VERTICAL_WRAP;
        loadDefaultColors();
    }

    public void loadDefaultColors() {
        backgroundColor = DEFAULT_BACKGROUND_COLOR;
        aliveSquareColor = DEFAULT_ALIVE_SQUARE_COLOR;
        deadSquareColor = DEFAULT_DEAD_SQUARE_COLOR;
        gridLinesColor  = DEFAULT_GRID_LINES_COLOR;
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
            JSONObject jsonObject = new JSONObject(jsonString.toString());
            jsonToData(jsonObject);
        } catch (FileNotFoundException e) {
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

    public int getRandomFillProbability() {
        return randomFillProbability;
    }

    public void setRandomFillProbability(int randomFillProbability) {
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
