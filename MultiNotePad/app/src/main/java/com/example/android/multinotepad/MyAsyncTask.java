package com.example.android.multinotepad;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import com.google.gson.Gson;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Neha on 11-Feb-18.
 */

public class MyAsyncTask extends AsyncTask<String, Integer, String> {
    private static final String TAG = "MyAsyncTask";
    private MainActivity mainActivity;
    public static boolean running = false;

    public MyAsyncTask(MainActivity ma) {
        mainActivity = ma;
    }

    @Override
    protected String doInBackground(String... params) {

        Log.d(TAG, "doInBackground: Starting background execution");
        String notesStr="";
        try {
            InputStream is = mainActivity.getApplicationContext().openFileInput(params[0]);
            JsonReader reader = new JsonReader(new InputStreamReader(is, mainActivity.getString(R.string.encoding)));
            reader.beginObject();
            while (reader.hasNext()) {
                String value = reader.nextName();
                if (value.equals("noteList")) {
                    notesStr = reader.nextString();
                }else {
                    reader.skipValue();
                }
            }
            reader.endObject();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "doInBackground: Done ");
        return notesStr;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.d(TAG, "onPreExecute: ");
    }

    @Override
    protected void onPostExecute(String notesStr) {
        super.onPostExecute(notesStr);
        try {
            Log.d(TAG, "onPostExecute: " + notesStr);
            ArrayList<Notes> notesArrayList = new ArrayList<Notes>();

            Notes[] respone = new Gson().fromJson(notesStr, Notes[].class);
            for (Notes note : respone) {
                notesArrayList.add(note);
                //Log.d(TAG, "NoteTiltle: " + note.getNoteTitle());
            }

            List<Notes> notesList = notesArrayList;

            mainActivity.getNoteListFromAsync(notesArrayList);
            Log.d(TAG, "onPostExecute: AsyncTask terminating successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
