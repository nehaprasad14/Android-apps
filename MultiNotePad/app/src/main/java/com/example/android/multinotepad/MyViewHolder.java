package com.example.android.multinotepad;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Neha on 10-Feb-18.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView noteTitleText;
    public TextView noteDateText;
    public TextView noteText;
    CardView cv;
    private static final String TAG = "MyViewHolder";

    public MyViewHolder(View view) {
        super(view);
        cv = (CardView)itemView.findViewById(R.id.cardView);
        noteTitleText = (TextView) view.findViewById(R.id.noteTitleText);
        noteDateText = (TextView) view.findViewById(R.id.noteDateText);
        noteText = (TextView) view.findViewById(R.id.noteText);
        Log.d(TAG, "MyViewHolder: ");
    }

}
