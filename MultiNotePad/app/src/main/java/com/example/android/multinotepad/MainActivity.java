package com.example.android.multinotepad;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener  {

    private ArrayList<Notes> notesArrayList = new ArrayList<Notes>();
    private RecyclerView recyclerView; // Layout's recyclerview
    private NotesAdapter notesAdapter; // Data to recyclerview adapter
    private static final int ADD_REQUEST_CODE = 1;
    private static final int UPDATE_REQUEST_CODE = 2;
    private static final String TAG = "MainActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyAsyncTask myAsyncTask = (MyAsyncTask) new MyAsyncTask(this);
        myAsyncTask.execute(getString(R.string.file_name));
        
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        notesAdapter = new NotesAdapter(notesArrayList, MainActivity.this);
        recyclerView.setAdapter(notesAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: .. MainActivity");
        outState.putSerializable("NOTE_LIST", notesArrayList);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "onRestoreInstanceState .. MainActivity");
        super.onRestoreInstanceState(savedInstanceState);
        notesArrayList = (ArrayList<Notes>) savedInstanceState.getSerializable("NOTE_LIST");
       //notesAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: Main");
        getMenuInflater().inflate(R.menu.menu_layout, menu);
        return true;
    }

    Intent intent;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_menu_item:
                intent = new Intent(MainActivity.this, EditActivity.class);
                startActivityForResult(intent, ADD_REQUEST_CODE);
                return true;
            case R.id.info_menu_item:
                intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: ");
        if (requestCode == ADD_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Notes note = (Notes)data.getSerializableExtra("NOTE_DETAILS");
                notesArrayList.add(0,note);
                notesAdapter.notifyDataSetChanged();
                Log.d(TAG, "onActivityResult: NOTE_DETAILS: " + note);
            } else if(resultCode == RESULT_CANCELED){
                Toast.makeText(this, getString(R.string.no_title), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onActivityResult: result Code: " + resultCode);
                notesAdapter.notifyDataSetChanged();
            }
        }else if (requestCode == UPDATE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Notes note = (Notes)data.getSerializableExtra("NOTE_DETAILS");
                int pos = (int)data.getIntExtra("NOTE_POS",-1);
                if(pos != -1) {
                    notesArrayList.get(pos).setNoteTitle(note.getNoteTitle());
                    notesArrayList.get(pos).setNoteText(note.getNoteText());
                    notesArrayList.get(pos).setNotetimeStamp(note.getNotetimeStamp());
                    Collections.sort(notesArrayList, new Notes());
                    notesAdapter.notifyDataSetChanged();
                }
                Log.d(TAG, "onActivityResult: notesArrayList : "+notesArrayList.toString());
                Log.d(TAG, "onActivityResult: pos : "+pos);
            } else if(resultCode == RESULT_CANCELED){
                Toast.makeText(this, getString(R.string.no_title), Toast.LENGTH_SHORT).show();
                notesAdapter.notifyDataSetChanged();
                Log.d(TAG, "onActivityResult: result Code: " + resultCode);
            }

        } else {
            Log.d(TAG, "onActivityResult: Request Code " + requestCode);
            notesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "onClick: ....");
        int pos = recyclerView.getChildLayoutPosition(view);
        Notes note = notesArrayList.get(pos);
        intent = new Intent(MainActivity.this, EditActivity.class);
        intent.putExtra("NOTE_UPDATE", note);
        intent.putExtra("NOTE_POS", pos);
        startActivityForResult(intent, UPDATE_REQUEST_CODE);
        
    }

    @Override
    public boolean onLongClick(View view) {
        final int pos = recyclerView.getChildLayoutPosition(view);
        Notes note = notesArrayList.get(pos);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                notesArrayList.remove(pos);
                notesAdapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        builder.setMessage("");
        builder.setTitle("Delete Note '"+note.getNoteTitle()+"'?");

        AlertDialog dialog = builder.create();
        dialog.show();
        return false;
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: ");
        if(!notesArrayList.isEmpty()) {
            String str = new Gson().toJson(notesArrayList);
            saveNotes(str);
        }
        super.onPause();
    }



    private void saveNotes(String str) {
        Log.d(TAG, "saveNotes: Saving JSON File.." + str);
        try {
            FileOutputStream fos = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);

            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, getString(R.string.encoding)));
            writer.setIndent("  ");
            writer.beginObject();
            writer.name("noteList").value(str);
            writer.endObject();
            writer.close();
            //Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public void getNoteListFromAsync(ArrayList<Notes> noteList) {
        Log.d(TAG, "getNoteListFromAsync: ");
        if(!noteList.isEmpty()){
            notesArrayList = noteList;
            recyclerView = (RecyclerView) findViewById(R.id.recycler);
            notesAdapter = new NotesAdapter(notesArrayList, MainActivity.this);
            recyclerView.setAdapter(notesAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
       
    }
}
