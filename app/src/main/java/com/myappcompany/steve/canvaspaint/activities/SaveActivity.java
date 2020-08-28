package com.myappcompany.steve.canvaspaint.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.myappcompany.steve.canvaspaint.R;
import com.myappcompany.steve.canvaspaint.SaveLoadRecyclerViewAdapter;
import com.myappcompany.steve.canvaspaint.data.GameOfLifeData;
import com.myappcompany.steve.canvaspaint.data.SaveData;
import com.myappcompany.steve.canvaspaint.data.SettingsData;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class SaveActivity extends AppCompatActivity implements SaveLoadRecyclerViewAdapter.OnItemListener {

    private final static String TAG = "SaveActivity";
    private final static String FILE_NAME = "savedata.json";
    private ArrayList<String> mSaveNames = new ArrayList<>();
    private ArrayList<SaveData> mSaves = new ArrayList<>();
    SaveLoadRecyclerViewAdapter adapter;
    private EditText editText;
    private String mSaveString;

    private SettingsData settingsData = SettingsData.getInstance();
    private GameOfLifeData gameOfLifeData = GameOfLifeData.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        //todo: finish implementing permanent storage with sharedPreferences
        try {
            loadSaves();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "JSONException reading save file");
        } catch (IOException e) {
            Log.d(TAG, "ISOException error reading saves file");
        }

        mSaveString = getIntent().getStringExtra("saveString");

        editText = findViewById(R.id.editText);
        initRecyclerView();
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview.");

        RecyclerView saveRecyclerView = findViewById(R.id.saveRecyclerView);
        adapter = new SaveLoadRecyclerViewAdapter(this, mSaves, this);
        saveRecyclerView.setAdapter(adapter);
        saveRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadSaves() throws IOException, JSONException {
        BufferedReader reader = null;

        try {
            //Open and read the file into a StringBuilder
            InputStream in = getApplicationContext().openFileInput(FILE_NAME);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                //Line breaks are omitted and irrelevant
                jsonString.append(line);
            }
            Log.d(TAG, "loadSaves: The following was read from the file : " + jsonString.toString());
            JSONArray jsonArray = new JSONArray(jsonString.toString());
            for(int i = 0; i < jsonArray.length(); i++) {
                mSaves.add(new SaveData(jsonArray.getJSONObject(i)));
                Log.d(TAG, "Save " + (i+1) + " was loaded successfully");
            }
        } catch (FileNotFoundException e) {
            Log.d(TAG, "loadData: file not found, loading defaults");
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClickSave(View view) {
        String saveFileName = editText.getText().toString();

        DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss")
                .withZone(ZoneId.systemDefault());

        String saveDate = DATE_TIME_FORMATTER.format(new Date().toInstant());

        if(saveFileName.isEmpty()) {
            Toast.makeText(this, R.string.you_must_enter_a_name_for_your_save, Toast.LENGTH_SHORT).show();
        } else {

            SaveData save = new SaveData(saveFileName, mSaveString, saveDate,
                    settingsData.getBackgroundColor(), settingsData.getAliveSquareColor(),
                    settingsData.getDeadSquareColor(), settingsData.getGridLinesColor(),
                    settingsData.getBoardWidth(), settingsData.getBoardHeight());

            mSaves.add(save);
            adapter.notifyDataSetChanged();
            editText.setText("");
            try {
                saveSaves();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Error writing the file");
            }
        }
    }



    @Override
    public void onItemClick(View view, int position) {
        if (view.getId() == R.id.deleteImageView) {
            deleteSave(position);
        } else {
            loadSave(position);
        }
        Log.d(TAG, "onItemClick at position " + position);
    }

    private void loadSave(int position) {
        SaveData save = mSaves.get(position);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("saveString", save.getBoardStateString());
        settingsData.setBoardHeight(save.getBoardHeight());
        settingsData.setBoardWidth(save.getBoardWidth());
        settingsData.setBackgroundColor(save.getBackgroundColor());
        settingsData.setAliveSquareColor(save.getAliveSquareColor());
        settingsData.setDeadSquareColor(save.getDeadSquareColor());
        settingsData.setGridLinesColor(save.getGridLinesColor());
        gameOfLifeData.updateBoard();
        try {
            settingsData.saveData(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Error saving settings data while loading a save from the list");
        }
        startActivity(intent);
    }

    private void deleteSave(final int position) {
        final String saveName = mSaves.get(position).getSaveName();
        new AlertDialog.Builder(SaveActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.are_you_sure)
                .setMessage(getString(R.string.pressing_yes_will_delete_the_save, saveName))
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),
                                getString(R.string.a_save_named_was_deleted, saveName), Toast.LENGTH_SHORT).show();
                        mSaves.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyDataSetChanged();
                        try {
                            saveSaves();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.d(TAG, "Error writing the file");
                        }

                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    private void saveSaves() throws IOException {
        JSONArray jsonArray = new JSONArray();

        for(int i = 0; i < mSaves.size(); i++) {
            try {
                jsonArray.put(mSaves.get(i).toJSON());
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d(TAG, "Trouble reading JSON from save " + i + " with save name " + mSaves.get(i).getSaveName() + " with date " + mSaves.get(i).getSaveDate());
            }
        }

        //Write the file to the disk
        Writer writer = null;
        try {
            OutputStream out = getApplicationContext().openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(jsonArray.toString());
        } finally {
            if(writer != null) {
                writer.close();
            }
        }

    }
}