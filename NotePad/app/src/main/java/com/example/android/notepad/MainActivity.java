package com.example.android.notepad;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.Date;
import java.text.SimpleDateFormat;


import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText notesText;
    private TextView dateTimeText;
    private Note notes;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dateTimeText = (TextView)findViewById(R.id.dateTimeText);
        notesText = (EditText)findViewById(R.id.notesText);

        notesText.setMovementMethod(new ScrollingMovementMethod());
        notesText.setTextIsSelectable(true);
        dateTimeText.setText(getDateTime());
    }

    public static String getDateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd, hh:mm a", Locale.getDefault());
        Date date = new Date();
        return simpleDateFormat.format(date);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("TIMESTAMP", dateTimeText.getText().toString());
        outState.putString("NOTES", notesText.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        dateTimeText.setText(savedInstanceState.getString("TIMESTAMP"));
        notesText.setText(savedInstanceState.getString("NOTES"));
    }

    @Override
    protected void onResume() {
        notes = loadFile();  // Load the JSON containing the data
        if (notes.getNotes() != null) {
            dateTimeText.setText(notes.getTimeStamp());
            notesText.setText(notes.getNotes());
        }else{
            dateTimeText.setText(getDateTime());
            notesText.setText("");
        }
        super.onResume();
    }

    private Note loadFile() {

        Log.d(TAG, "loadFile: Loading JSON File");
        notes = new Note();
        try {
            InputStream is = getApplicationContext().openFileInput(getString(R.string.file_name));
            JsonReader reader = new JsonReader(new InputStreamReader(is, getString(R.string.encoding)));

            reader.beginObject();
            while (reader.hasNext()) {
                String value = reader.nextName();
                if (value.equals("timestamp")) {
                    notes.setTimeStamp(reader.nextString());
                } else if (value.equals("notes")) {
                    notes.setNotes(reader.nextString());
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();

        } catch (FileNotFoundException e) {
            Toast.makeText(this, getString(R.string.no_file), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return notes;
    }


    @Override
    protected void onStop() {
        notes.setTimeStamp(getDateTime());
        notes.setNotes(notesText.getText().toString());
        saveNotes();
        super.onStop();
    }

    private void saveNotes() {

        Log.d(TAG, "saveNotes: Saving JSON File..");
        try {
            FileOutputStream fos = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);

            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, getString(R.string.encoding)));
            writer.setIndent("  ");
            writer.beginObject();
            writer.name("timestamp").value(notes.getTimeStamp());
            writer.name("notes").value(notes.getNotes());
            writer.endObject();
            writer.close();

            Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}
