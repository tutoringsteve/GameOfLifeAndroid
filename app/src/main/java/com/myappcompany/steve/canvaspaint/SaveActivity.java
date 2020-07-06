package com.myappcompany.steve.canvaspaint;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class SaveActivity extends AppCompatActivity implements RecyclerViewAdapter.OnItemListener {

    private final String TAG = "SaveActivity";
    private ArrayList<String> mSaveNames = new ArrayList<>();
    private ArrayList<String> mSaveStrings = new ArrayList<>();
    private ArrayList<String> mSaveDates = new ArrayList<>();
    RecyclerViewAdapter adapter;
    private EditText editText;
    private String mSaveString;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        //todo: finish implementing permanent storage with sharedPreferences
        sharedPreferences = this.getSharedPreferences("com.myappcompany.steve.canvaspaint", Context.MODE_PRIVATE);
        restorePreviousSession();

        mSaveString = getIntent().getStringExtra("saveString");

        editText = findViewById(R.id.editText);
        initRecyclerView();
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview.");

        RecyclerView saveRecyclerView = findViewById(R.id.saveRecyclerView);
        adapter = new RecyclerViewAdapter(this, mSaveNames, mSaveDates, this);
        saveRecyclerView.setAdapter(adapter);
        saveRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void restorePreviousSession() {
        try {
            mSaveStrings = (ArrayList<String>) ObjectSerializer
                    .deserialize(sharedPreferences
                            .getString("saveStrings",
                                    ObjectSerializer.serialize(new ArrayList<String>())));
            mSaveNames = (ArrayList<String>) ObjectSerializer
                    .deserialize(sharedPreferences
                            .getString("saveNames",
                                    ObjectSerializer.serialize(new ArrayList<String>())));
            mSaveDates = (ArrayList<String>) ObjectSerializer
                    .deserialize(sharedPreferences
                            .getString("saveDates",
                                    ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "Issue with restorePreviousSession:" + e);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClickSave(View view) {
        String saveFileName = editText.getText().toString();

        DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss")
                .withZone(ZoneId.systemDefault());

        String saveDate = DATE_TIME_FORMATTER.format(new Date().toInstant());

        if(saveFileName.isEmpty()) {
            Toast.makeText(this, "You must enter a name for your save.", Toast.LENGTH_SHORT).show();
        } else {
            mSaveNames.add(saveFileName);
            mSaveStrings.add(mSaveString);
            mSaveDates.add(saveDate);
            adapter.notifyDataSetChanged();
            editText.setText("");

            updateSharedPreferences();
        }
    }

    private void updateSharedPreferences() {
        try {
            String serializedSaveNames = ObjectSerializer.serialize(mSaveNames);
            String serializedSaveStrings = ObjectSerializer.serialize(mSaveStrings);
            String serializedDateStrings = ObjectSerializer.serialize(mSaveDates);
            sharedPreferences.edit().putString("saveNames", serializedSaveNames).apply();
            sharedPreferences.edit().putString("saveStrings", serializedSaveStrings).apply();
            sharedPreferences.edit().putString("saveDates", serializedDateStrings).apply();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "Error in onClickSave during object serialization:" + e);
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
        mSaveString = mSaveStrings.get(position);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("saveString", mSaveString);
        startActivity(intent);
    }

    private void deleteSave(final int position) {
        final String saveName = mSaveNames.get(position);
        new AlertDialog.Builder(SaveActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are you sure!?")
                .setMessage("Pressing yes will delete the save named " + saveName + ". Click no to keep the save.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),
                                "A save named " + saveName + " was deleted.", Toast.LENGTH_SHORT).show();
                        mSaveNames.remove(position);
                        mSaveDates.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyDataSetChanged();
                        updateSharedPreferences();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}