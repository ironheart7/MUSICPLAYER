package com.racetoface.musicplayer;

import android.graphics.Bitmap;

public class Content  {
    String ATRIST,TITLE,URL;
    long ID;

    public long getID() {
        return ID;
    }

    public String getATRIST() {
        return ATRIST;
    }

    public String getURL() {
        return URL;
    }



    public String getTITLE() {
        return TITLE;

    }

    public Content(String ATRIST, String TITLE,String URL,long ID) {
        this.ATRIST = ATRIST;
        this.TITLE = TITLE;
        this.ID=ID;
        this.URL=URL;

    }
}
