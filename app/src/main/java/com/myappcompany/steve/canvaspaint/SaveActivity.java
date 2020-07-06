package com.myappcompany.steve.canvaspaint;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;

public class SaveActivity extends AppCompatActivity {

    private final String TAG = "SaveActivity";
    private ArrayList<String> mSaveNames = new ArrayList<>();
    private ArrayList<String> mSaveStrings = new ArrayList<>();
    private ArrayList<String> mDates = new ArrayList<>();
    private ArrayAdapter saveArrayAdapter;
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
        //restorePreviousSession();

        mSaveString = getIntent().getStringExtra("saveString");

        editText = findViewById(R.id.editText);
        initRecyclerView();

        /*
        saveRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                saveString = saveStrings.get(position);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("saveString", saveString);
                startActivity(intent);
            }
        });
*/
        /*
        saveRecyclerView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
*/
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview.");

        RecyclerView saveRecyclerView = findViewById(R.id.saveRecyclerView);
        adapter = new RecyclerViewAdapter(this, mSaveNames, mDates);
        saveRecyclerView.setAdapter(adapter);
        //look into this more
        saveRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void restorePreviousSession() {
        try {
            mSaveNames = (ArrayList<String>) ObjectSerializer
                    .deserialize(sharedPreferences
                            .getString("saveNames",
                                    ObjectSerializer.serialize(new ArrayList<String>())));
            mSaveStrings = (ArrayList<String>) ObjectSerializer
                    .deserialize(sharedPreferences
                            .getString("saveStrings",
                                    ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "Issue with restorePreviousSession:" + e);
        }
    }
/*
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
*/

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClickSave(View view) {
        String saveFileName = editText.getText().toString();
        String saveDate = Instant.now().toString();

        if(saveFileName.isEmpty()) {
            Toast.makeText(this, "You must enter a name for your save.", Toast.LENGTH_SHORT).show();
        } else {
            mSaveNames.add(saveFileName);
            mSaveStrings.add(mSaveString);
            mDates.add(saveDate);
            adapter.notifyDataSetChanged();
            editText.setText("");

            //updateSharedPreferences();
        }
    }

    private void updateSharedPreferences() {
        try {
            String serializedSaveNames = ObjectSerializer.serialize(mSaveNames);
            String serializedSaveStrings = ObjectSerializer.serialize(mSaveStrings);
            sharedPreferences.edit().putString("saveNames", serializedSaveNames).apply();
            sharedPreferences.edit().putString("saveStrings", serializedSaveStrings).apply();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "Error in onClickSave during object serialization:" + e);
        }
    }
}