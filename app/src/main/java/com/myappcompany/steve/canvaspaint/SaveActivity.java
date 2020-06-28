package com.myappcompany.steve.canvaspaint;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class SaveActivity extends AppCompatActivity {

    private final String TAG = "SaveActivity";
    private ArrayList<String> saveNames = new ArrayList<>();
    private ArrayList<String> saveStrings = new ArrayList<>();
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

        saveString = getIntent().getStringExtra("saveString");

        editText = findViewById(R.id.editText);

        ListView saveListView = findViewById(R.id.saveListView);
        saveArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, saveNames);
        saveListView.setAdapter(saveArrayAdapter);

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
        }
    }
}