package com.racetoface.musicplayer;

import android.graphics.Bitmap;

public class VideoContent  {
    String ATRIST,TITLE,URL;




    public String getATRIST() {
        return ATRIST;
    }

    public String getURL() {
        return URL;
    }



    public String getTITLE() {
        return TITLE;

    }

    public VideoContent(String ATRIST, String TITLE,String URL) {
        this.ATRIST = ATRIST;
        this.TITLE = TITLE;

        this.URL=URL;

    }
}
