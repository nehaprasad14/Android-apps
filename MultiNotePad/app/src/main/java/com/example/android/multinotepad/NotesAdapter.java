package com.example.android.multinotepad;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

/**
 * Created by Neha on 10-Feb-18.
 */

public class NotesAdapter extends RecyclerView.Adapter<MyViewHolder>{

    private static final String TAG = "NotesAdapter";
    private ArrayList<Notes> notesList;
    private MainActivity mainAct;

    public NotesAdapter(ArrayList<Notes> notesList, MainActivity ma) {
        Log.d(TAG, "NotesAdapter: ");
        this.notesList = notesList;
        mainAct = ma;
    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_list, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
        Notes notes = notesList.get(position); //Chk position
        holder.noteTitleText.setText(notes.getNoteTitle());
        holder.noteDateText.setText(notes.getNotetimeStamp());

        String noteText = notes.getNoteText();
        if(noteText.length() > 80){
            noteText = noteText.substring(0,79);
            noteText = noteText + "...";
            holder.noteText.setText(noteText);
        }else{
            holder.noteText.setText(notes.getNoteText());
        }

    }


    @Override
    public int getItemCount() {
        return notesList.size();
    }
}
