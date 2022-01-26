package com.racetoface.musicplayer;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;
import android.widget.VideoView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

public class VideoPlayer extends AppCompatActivity {
    android.support.v7.widget.Toolbar toolbar;
   RecyclerView recyclerView;
    VideoAdapter videoAdapter;

    List<VideoContent> videolist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.videoplayertoolbar);
        toolbar.setTitle("VIDEO PLAYER");
        toolbar.setTitleTextColor(getResources().getColor(R.color.White));
recyclerView=(RecyclerView)findViewById(R.id.videorecyler);
        videolist = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        VideoAdapter videoAdapter=new VideoAdapter();
        checkUserPermission();


    }
    public void loadvideos() {
        ContentResolver contentResolver = getContentResolver();


        Uri song = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String sort = MediaStore.Video.Media.DEFAULT_SORT_ORDER;

        Cursor cursor = contentResolver.query(song, null,
                null, null, sort, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String ATRIST = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.ARTIST));
                String TITLE = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                String aURL = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));


                VideoContent cc = new VideoContent(ATRIST, TITLE, aURL);


                videolist.add(cc);
                 videoAdapter = new VideoAdapter(videolist,this);
                recyclerView.setAdapter(videoAdapter);
            } while (cursor.moveToNext());
        }

    }

    private void checkUserPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
                return;
            }
        }
        loadvideos();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 123:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadvideos();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    checkUserPermission();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }

    }

}

