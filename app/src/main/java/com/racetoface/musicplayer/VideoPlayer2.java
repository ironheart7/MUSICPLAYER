package com.racetoface.musicplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import java.net.URL;

public class VideoPlayer2 extends AppCompatActivity {
VideoView videoView;
MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player2);
        videoView=(VideoView)findViewById(R.id.videoView);
        Intent intent=getIntent();
        String url=intent.getStringExtra("URL");
        mediaController=new MediaController(this);



        videoView.setVideoPath(url);


        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        videoView.start();





    }
}
