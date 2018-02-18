package com.example.android.multinotepad;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Locale;

/**
 * Created by Neha on 10-Feb-18.
 */

public class Notes implements Serializable, Comparator {

    private String noteTitle;
    private String noteText;
    private String notetimeStamp;

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getNotetimeStamp() {
        return notetimeStamp;
    }

    public void setNotetimeStamp(String notetimeStamp) {
        this.notetimeStamp = notetimeStamp;
    }

    @Override
    public String toString() {
        return "Notes{" +
                "noteTitle='" + noteTitle + '\'' +
                ", noteText='" + noteText + '\'' +
                ", notetimeStamp='" + notetimeStamp + '\'' +
                '}';
    }

    @Override
    public int compare(Object o1, Object o2) {
        Notes note1 = (Notes)o1;
        Notes note2 = (Notes)o2;
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd, hh:mm a", Locale.getDefault());

        int res = 0;
        try {
            res = dateFormat.parse(note2.getNotetimeStamp()).compareTo(dateFormat.parse(note1.getNotetimeStamp()));
            if(res == 0 || res < 0 )
                res = -1;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return res;
    }
}
