package com.example.android.multinotepad;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditActivity extends AppCompatActivity {

    private EditText noteText;
    private EditText noteTitle;
    private Notes notes = new Notes();
    private static final String TAG = "EditActivity";
    int position = -1;
    String orgNoteTile = "";
    String orgNoteText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        noteTitle = (EditText)findViewById(R.id.noteTitle);
        noteText = (EditText)findViewById(R.id.noteText);

        noteText.setMovementMethod(new ScrollingMovementMethod());
        noteText.setTextIsSelectable(true);

       Intent intent = getIntent();
        if (intent.hasExtra("NOTE_UPDATE")) {
            notes = (Notes)intent.getSerializableExtra("NOTE_UPDATE");
            noteTitle.setText(notes.getNoteTitle());
            noteText.setText(notes.getNoteText());
        }

        if(intent.hasExtra("NOTE_POS")){
            position = intent.getIntExtra("NOTE_POS", -1);
            Log.d(TAG, "onCreate: NOTE_POS -- "+position);
        }

        orgNoteTile = noteTitle.getText().toString();
        orgNoteText = noteText.getText().toString();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ..");
        outState.putString("TITLE", noteTitle.getText().toString());
        outState.putString("TEXT", noteText.getText().toString());
        outState.putInt("NOTE_POS", position);
        outState.putSerializable("NOTE_DETAILS", notes);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState ..");
        super.onRestoreInstanceState(savedInstanceState);
        noteTitle.setText(savedInstanceState.getString("TITLE"));
        noteText.setText(savedInstanceState.getString("TEXT"));
        position = savedInstanceState.getInt("NOTE_POS");
        notes = (Notes) savedInstanceState.getSerializable("NOTE_DETAILS");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_layout, menu);
        return true;
    }

    public static String getDateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd, hh:mm a", Locale.getDefault());
        Date date = new Date();
        return simpleDateFormat.format(date);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveNote:
                String nt = noteTitle.getText().toString();
                if(!nt.trim().isEmpty() && nt.trim()!= null){
                    notes.setNotetimeStamp(getDateTime());
                    notes.setNoteTitle(noteTitle.getText().toString());
                    notes.setNoteText(noteText.getText().toString());
                    Intent data = new Intent();
                    data.putExtra("NOTE_DETAILS", notes);
                    if (position != -1)
                        data.putExtra("NOTE_POS",position);

                    Log.d(TAG, "onOptionsItemSelected: NOTE_POS : "+position);
                    setResult(RESULT_OK, data);
                    finish();
                    return true;
                }else{
                    setResult(RESULT_CANCELED);
                    finish();
                    return true;
                }

            default:
                Log.d(TAG, "onOptionsItemSelected: default case");
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if(!(orgNoteTile.equals(noteTitle.getText().toString()) && orgNoteText.equals(noteText.getText().toString()))) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    String nt = noteTitle.getText().toString();
                    if (!nt.trim().isEmpty() && nt.trim() != null) {
                        notes.setNotetimeStamp(getDateTime());
                        notes.setNoteTitle(noteTitle.getText().toString());
                        notes.setNoteText(noteText.getText().toString());

                        Intent data = new Intent();
                        data.putExtra("NOTE_DETAILS", notes);
                        if (position != -1)
                            data.putExtra("NOTE_POS", position);

                        Log.d(TAG, "onOptionsItemSelected: NOTE_POS : " + position);
                        setResult(RESULT_OK, data);
                    } else {
                        setResult(RESULT_CANCELED);
                    }
                    EditActivity.super.onBackPressed();
                }
            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    setResult(999);
                    EditActivity.super.onBackPressed();
                }
            });

            builder.setMessage("Save note '" + noteTitle.getText().toString() + "'?");
            builder.setTitle("Your note is not saved!");

            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            setResult(999);
            super.onBackPressed();
        }
    }

}
