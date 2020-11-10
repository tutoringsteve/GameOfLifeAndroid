package com.myappcompany.steve.canvaspaint.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.myappcompany.steve.canvaspaint.GameOfLifeBoard;
import com.myappcompany.steve.canvaspaint.data.GameOfLifeData;
import com.myappcompany.steve.canvaspaint.PixelGridView;
import com.myappcompany.steve.canvaspaint.R;
import com.myappcompany.steve.canvaspaint.data.SettingsData;

import org.json.JSONException;

import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String CONTROL_STATE_KEY_INDEX = "controlState";
    private boolean isAutoPlaying = false;
    private static final int EDITING = 0;
    private static final int PANNING = 1;
    private int controlState = EDITING;
    private PixelGridView pixelGrid;
    private Handler handler;
    private String saveString;
    public static GameOfLifeData gameOfLifeData;
    public static SettingsData settingsData = SettingsData.getInstance();

    int activeButtonColor = 0xff0000ff; //blue
    int inactiveButtonColor = 0xff000000; //black

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.showOverflowMenu();

        try {
            settingsData.loadData(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "onCreate : IOException + " + e.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "onCreate : JSONException + " + e.toString());
        }

        gameOfLifeData = GameOfLifeData.getInstance();

        pixelGrid = findViewById(R.id.pixelGridView);

        //preserves control state across multiple calls to onCreate
        if(savedInstanceState != null) {
            controlState = savedInstanceState.getInt(CONTROL_STATE_KEY_INDEX, EDITING);
            pixelGrid.setControlState(controlState);
            Log.i(TAG, "A savedInstanceState was found! controlState set to " + (controlState == EDITING ? "EDITING" : "PANNING"));
        }

        //Checks for a save passed from the SAVE/LOAD menu activity_save / SAVE_ACTIVITY
        saveString = getIntent().getStringExtra("saveString");
        if(saveString != null && !saveString.isEmpty()) {
            loadSaveState();
        }

        handler = new Handler();

        ImageView imageViewEdit = findViewById(R.id.imageViewEdit);
        imageViewEdit.setColorFilter(activeButtonColor);
        imageViewEdit.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                eraseBoard();
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.mainMenuSettings:
                Log.i(TAG, "Main Activity : Settings Was Pressed.");
                openSettings();
                break;
            case R.id.mainMenuHelp:
                Log.i(TAG, "Main Activity : Settings Was Pressed.");
                openHelp();
                break;
            default:
                break;
        }

        return true;
    }

    private void openSettings() {
        cancelAutoPlay();
        //can also pass a bundle
        startActivity(new Intent(this, SettingsActivity.class));
    }

    private void openHelp() {
        cancelAutoPlay();
        startActivity(new Intent(this, HelpActivity.class));
    }

    private void eraseBoard() {

        cancelAutoPlay();

        new AlertDialog.Builder(MainActivity.this)
                .setIcon(R.drawable.ic_erase)
                .setTitle("Clear the board?")
                .setMessage("Pressing yes will uncheck all grid squares, resetting the grid to its original state.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resetBoard();
                        pixelGrid.invalidate();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void resetBoard() {
        boolean[][] cellChecked = gameOfLifeData.getCellChecked();
        for(int row = 0; row < cellChecked.length; row++) {
            for(int column = 0; column < cellChecked[0].length; column++) {
                cellChecked[row][column] = false;
            }
        }
        gameOfLifeData.setCellChecked(cellChecked);
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CONTROL_STATE_KEY_INDEX, controlState);
        Log.i(TAG, CONTROL_STATE_KEY_INDEX);
    }

    public void playClick(View view) {
        boolean[][] cellChecked = gameOfLifeData.getCellChecked();
        GameOfLifeBoard board = new GameOfLifeBoard(cellChecked);
        board.oneTurn();
        gameOfLifeData.setCellChecked(board.getBooleanGameBoard());
        pixelGrid.invalidate();
    }

    public void autoPlayClick(final View view) {
        ImageView btnAutoPlay = findViewById(R.id.imageViewAutoPlay);

        if(!isAutoPlaying) {
            btnAutoPlay.setImageResource(R.drawable.ic_stopbutton);
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    playClick(view);
                    handler.postDelayed(this, settingsData.getAutoPlaySpeed());
                }
            };

            handler.post(runnable);
        } else {
            btnAutoPlay.setImageResource(R.drawable.ic_autoplay);
            handler.removeCallbacksAndMessages(null);
        }

        isAutoPlaying = !isAutoPlaying;
    }

    public void zoomInClick(View view) {
        if(gameOfLifeData.getZoomX() < gameOfLifeData.getMaxZoomX() && gameOfLifeData.getZoomY() < gameOfLifeData.getMaxZoomY()) {
            gameOfLifeData.setZoomX(gameOfLifeData.getZoomX() + 0.25f);
            gameOfLifeData.setZoomY(gameOfLifeData.getZoomY() + 0.25f);
            pixelGrid.invalidate();
        }
    }

    public void zoomOutClick(View view) {
        if(gameOfLifeData.getZoomY() > gameOfLifeData.getMinZoomX() && gameOfLifeData.getZoomY() > gameOfLifeData.getMinZoomY()) {
            gameOfLifeData.setZoomX(gameOfLifeData.getZoomX() - 0.25f);
            gameOfLifeData.setZoomY(gameOfLifeData.getZoomY() - 0.25f);
            pixelGrid.invalidate();
        }
    }

    public void editClick(View view) {
        if(controlState != EDITING) {
            ImageView btnEdit = findViewById(R.id.imageViewEdit);
            ImageView btnPanning = findViewById(R.id.imageViewPan);
            btnEdit.setColorFilter(activeButtonColor);
            btnPanning.setColorFilter(inactiveButtonColor);

            controlState = EDITING;
            pixelGrid.setControlState(EDITING);
        }
    }

    public void panningClick(View view) {
        if(controlState != PANNING) {
            ImageView btnEdit = findViewById(R.id.imageViewEdit);
            ImageView btnPanning = findViewById(R.id.imageViewPan);
            btnEdit.setColorFilter(inactiveButtonColor);
            btnPanning.setColorFilter(activeButtonColor);

            controlState = PANNING;
            pixelGrid.setControlState(PANNING);
        }
    }

    public void saveClick(View view) {
        cancelAutoPlay();

        try {
            saveString = gameOfLifeData.dataToJSON().toString();
            Log.i(TAG, "data saved to save state!" + saveString);

            //passes saveString to Save/Load activity and opens that activity.
            Intent intent = new Intent(this, SaveActivity.class);
            intent.putExtra("saveString", saveString);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "Error with saveClick");
        }
    }

    public void randomizeClick(View view) {
        cancelAutoPlay();

        new AlertDialog.Builder(MainActivity.this)
                .setIcon(R.drawable.ic_random)
                .setTitle("Randomize the board?")
                .setMessage("Pressing yes will reset the board, and then randomly check different squares for a random configuration.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        randomizeBoard();
                        pixelGrid.invalidate();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    public void randomizeBoard() {
        Random rand = new Random();
        boolean[][] cellChecked = gameOfLifeData.getCellChecked();
        for(int row = 0; row < cellChecked.length ; row++) {
            for(int column = 0; column < cellChecked[0].length; column++) {
                cellChecked[row][column] = (rand.nextInt(100) + 1 <= settingsData.getRandomFillProbability());
            }
        }
        gameOfLifeData.setCellChecked(cellChecked);

        if(settingsData.getRandomFillProbability() == 0) {
            Toast.makeText(this, R.string.the_board_was_erased_because_the_probability, Toast.LENGTH_SHORT).show();
        }
    }

    public void loadSaveState() {
        try {
            gameOfLifeData.stringToData(saveString);
            pixelGrid.invalidate();
            Log.i(TAG, "data loaded from save state!" + gameOfLifeData.dataToJSON().toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, "Error with loadClick");
        }
    }

    private void cancelAutoPlay() {
        if(isAutoPlaying) {
            handler.removeCallbacksAndMessages(null);

            ImageView btnAutoPlay = findViewById(R.id.imageViewAutoPlay);
            btnAutoPlay.setImageResource(R.drawable.ic_autoplay);

            isAutoPlaying = !isAutoPlaying;
        }
    }

}
