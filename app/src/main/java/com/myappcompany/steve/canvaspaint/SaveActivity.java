package com.myappcompany.steve.canvaspaint;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

public class SaveActivity extends AppCompatActivity {

    private final String TAG = "SaveActivity";
    private ArrayList<String> saveNames;
    private ArrayList<String> saveStrings;
    private ArrayAdapter saveArrayAdapter;
    private EditText editText;
    private String saveString;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        //todo: finish implementing permanent storage with sharedPreferences
        sharedPreferences = this.getSharedPreferences("com.myappcompany.steve.canvaspaint", Context.MODE_PRIVATE);
        restorePreviousSession();

        saveString = getIntent().getStringExtra("saveString");

        editText = findViewById(R.id.editText);

        ListView saveListView = findViewById(R.id.saveListView);
        saveArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, saveNames);
        saveListView.setAdapter(saveArrayAdapter);

        saveListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                saveString = saveStrings.get(position);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("saveString", saveString);
                startActivity(intent);
            }
        });

        saveListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final String saveName = saveNames.get(position);

                //give an alert
                new AlertDialog.Builder(SaveActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure!?")
                        .setMessage("Pressing yes will delete the save named " + saveName + ". Click no to keep the save.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(),
                                        "A save named " + saveName + " was deleted.", Toast.LENGTH_SHORT).show();
                                saveNames.remove(position);
                                saveStrings.remove(position);
                                saveArrayAdapter.notifyDataSetChanged();
                                updateSharedPreferences();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });

    }

    private void restorePreviousSession() {
        try {
            saveNames = (ArrayList<String>) ObjectSerializer
                    .deserialize(sharedPreferences
                            .getString("saveNames",
                                    ObjectSerializer.serialize(new ArrayList<String>())));
            saveStrings = (ArrayList<String>) ObjectSerializer
                    .deserialize(sharedPreferences
                            .getString("saveStrings",
                                    ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "Issue with restorePreviousSession:" + e);
        }
    }

    public void onClickSave(View view) {
        String saveFileName = editText.getText().toString();

        if(saveFileName.isEmpty()) {
            Toast.makeText(this, "You must enter a name for your save.", Toast.LENGTH_SHORT).show();
        } else {
            saveNames.add(saveFileName);
            saveStrings.add(saveString);
            saveArrayAdapter.notifyDataSetChanged();
            editText.setText("");

            updateSharedPreferences();
        }
    }

    private void updateSharedPreferences() {
        try {
            String serializedSaveNames = ObjectSerializer.serialize(saveNames);
            String serializedSaveStrings = ObjectSerializer.serialize(saveStrings);
            sharedPreferences.edit().putString("saveNames", serializedSaveNames).apply();
            sharedPreferences.edit().putString("saveStrings", serializedSaveStrings).apply();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "Error in onClickSave during object serialization:" + e);
        }
    }
}