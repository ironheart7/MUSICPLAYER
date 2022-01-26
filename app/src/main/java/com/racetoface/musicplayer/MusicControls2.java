package com.racetoface.musicplayer;

import android.app.AlertDialog;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import android.view.View;
import android.graphics.PorterDuff;

import android.graphics.drawable.BitmapDrawable;


import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;


import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;



import java.io.IOException;

import java.util.List;

import static com.racetoface.musicplayer.Adapter.art;
import static com.racetoface.musicplayer.Adapter.songImage;


public class MusicControls2 extends AppCompatActivity {
    TextView songtitleTV, artistTV, currentdurationTV, totaldurationTV;

    private Intent playIntent;
    private boolean musicBound = false;
    ImageView IV;
    ImageButton play_pauseIB;
    ImageButton nextbtnB, previousbtnB;
    SeekBar seekBarSB;
    CheckBox checkBox,autochange;
    public final String NC="notification325";
    public final int nid =352;
    RemoteViews remoteViews;
    int notifyID = 11;
    boolean nextsong=false;

    NotificationCompat.Builder nunotification;
    RemoteViews rv;

    NotificationManager mNotificationManager;
    String UUUU, AAAA, TTTT;
    Context context;
    private MediaPlayer mediaPlayer = new MediaPlayer();
    Runnable runnable;
    Handler handler = new Handler();
    SharedPreferences sharedPreferences;
    Bitmap songimages;

    int positions;
    NotificationManagerCompat notificationManagerCompat;

    List<Content> songlll;
    Notification.Builder notification;
    String title, artist;
    Button forword, replay;
    GestureDetector g;
    private static final String PREVIOUS = "com.tinbytes.simplenotificationapp.YES_ACTION";
    private static final String PLAYPAUSE = "com.tinbytes.simplenotificationapp.MAYBE_ACTION";
    private static final String NEXT = "com.tinbytes.simplenotificationapp.NO_ACTION";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_controls2);


        songtitleTV = (TextView) findViewById(R.id.songtitleTV);

autochange=(CheckBox)findViewById(R.id.autochange);

        artistTV = (TextView) findViewById(R.id.artistTV);
        currentdurationTV = (TextView) findViewById(R.id.currentdurationTV);
        totaldurationTV = (TextView) findViewById(R.id.totaldurationTV);
        IV = (ImageView) findViewById(R.id.IV);
        play_pauseIB = (ImageButton) findViewById(R.id.play_pauseIB);
        nextbtnB = (ImageButton) findViewById(R.id.nextbtnB);
        previousbtnB = (ImageButton) findViewById(R.id.previousbtnB);
        seekBarSB = (SeekBar) findViewById(R.id.seekBarSB);
        checkBox = (CheckBox) findViewById(R.id.setloop);

        pre();


        Intent I = getIntent();
        UUUU = I.getStringExtra("U");
        AAAA = I.getStringExtra("A");
        TTTT = I.getStringExtra("T");
        positions = I.getIntExtra("P", 00);


        final MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();//metadataRetriver is used to get the embedded image from the file
        mediaMetadataRetriever.setDataSource(UUUU);//this is the file url
        try {
            art = mediaMetadataRetriever.getEmbeddedPicture();//this will get the embedded image from file

            songImage = BitmapFactory.decodeByteArray(art, 0, art.length);//this will decode the file to bitmap
            Drawable drawable = new BitmapDrawable(getResources(), songImage);
            songimages = songImage;
            IV.setImageDrawable(drawable);//setting bitmap to imageview

        } catch (Exception e) {
            IV.setImageDrawable(getResources().getDrawable(R.drawable.musiccd)); //if image not found then default image
        }
        try {
            mediaPlayer.setDataSource(UUUU);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                    seekBarSB.setMax(mediaPlayer.getDuration());//setting seekbar max by getting the duration from music
                    play();//t
                    oreonoti(songimages, TTTT, AAAA);

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        songtitleTV.setText(TTTT);

        artistTV.setText(AAAA);

        seekBarSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // mediaPlayer.seekTo(i);
                if (b) {
                    mediaPlayer.seekTo(i);//this will seek to the music to the desired using seekbar
                }
            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        IV.setOnTouchListener(new OnSwipeTouchListener(MusicControls2.this) {
            public void onSwipeLeft() {
                //Toast.makeText(MusicControls2.this, "top", Toast.LENGTH_SHORT).show();
                next();
            }

            public void onSwipeRight() {
                previous();
            }
        });


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mediaPlayer.setLooping(true);
                } else {
                    mediaPlayer.setLooping(false);
                }
            }
        });

        autochange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    checkBox.setChecked(false);
                    Toast.makeText(MusicControls2.this,"AutoChange Enabled",Toast.LENGTH_SHORT).show();
                    nextsong=true;
                }
                else if (!isChecked)
                {
                    nextsong=false;
                    
                }

            }
        });


        play_pauseIB.setImageResource(R.drawable.pause_white);
        play_pauseIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    play_pauseIB.setImageResource(R.drawable.play_white);
                    //oreonotipause(songimages, TTTT, AAAA);


                    oreonoti(songimages, TTTT, AAAA);



                } else {
                    mediaPlayer.start();
                    play_pauseIB.setImageResource(R.drawable.pause_white);


                    oreonoti(songimages, TTTT, AAAA);

                }

            }
        });

        nextbtnB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                next();


            }
        });
        previousbtnB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(MusicControls2.this, "THIS BUTTON IS CURRENTLY UNUSABLE", Toast.LENGTH_SHORT).show();
                previous();

            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                play_pauseIB.setImageResource(R.drawable.play_white);
if (nextsong==true)
{
    next();
}
else {

}

            }
        });


    }


    public void previous() {
        if (checkBox.isChecked()) {
            checkBox.setChecked(false);
        }

        Adapter adapter = new Adapter();
        if (positions == 0) {
        }
        positions--;
        if (positions < 0) {
            Toast.makeText(MusicControls2.this, "Previous Song Not Available", Toast.LENGTH_LONG).show();
        } else {

            String url = adapter.nexturl(positions);
            String title = adapter.nexttitle(positions);
            String artist = adapter.nextartist(positions);
            nextprevious(url, title, artist);
            play_pauseIB.setImageResource(R.drawable.pause_white);
            oreonoti(songimages, title, artist);

            TTTT = title;
            AAAA = artist;
        }

    }


    public void next() {
        if (checkBox.isChecked()) {
            checkBox.setChecked(false);
        }

        Adapter adapter = new Adapter();
        if (positions == 0) {
        }
        positions++;
        int ii = adapter.sizeb();

        if (positions >= ii) {
            // Toast.makeText(MusicControls2.this, "Next Song Not Available", Toast.LENGTH_LONG).show();
            positions = 0;
        } else {
            String url = adapter.nexturl(positions);
            String title = adapter.nexttitle(positions);
            String artist = adapter.nextartist(positions);
            nextprevious(url, title, artist);
            play_pauseIB.setImageResource(R.drawable.pause_white);
            oreonoti(songimages, title, artist);


            TTTT = title;
            AAAA = artist;

        }


    }

    public void play() {
        seekBarSB.setProgress(mediaPlayer.getCurrentPosition());
        runnable = new Runnable() {
            @Override
            public void run() {


                long currentduration = mediaPlayer.getCurrentPosition();
                long totalduration = mediaPlayer.getDuration();
                totaldurationTV.setText("" + milliSecondsToTimer(totalduration));
                currentdurationTV.setText("" + milliSecondsToTimer(currentduration));


                play();
            }


        };
        handler.postDelayed(runnable, 100);
    }


    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";


        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

        if (hours > 0) {
            finalTimerString = hours + ":";
        }


        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = finalTimerString + minutes + ":" + secondsString;


        return finalTimerString;


    }


    public void pre() {
        //  seekBarSB = (SeekBar) findViewById(R.id.seekBarSB);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int COLORRED = sharedPreferences.getInt("COLOR", 0);
        if (COLORRED == 0) {

        } else {
            //seekBarSB.getProgressDrawable().setColorFilter(COLORRED, PorterDuff.Mode.MULTIPLY);

            Window window = getWindow();

            window.setStatusBarColor(COLORRED);
            Drawable thumb = getResources().getDrawable( R.color.red );
            seekBarSB = (SeekBar) findViewById(R.id.seekBarSB);
            autochange=(CheckBox)findViewById(R.id.autochange);
            checkBox=(CheckBox)findViewById(R.id.setloop);

            //seekBarSB.getThumb().setColorFilter(COLORRED, PorterDuff.Mode.MULTIPLY);
            seekBarSB.getProgressDrawable().setColorFilter(COLORRED, PorterDuff.Mode.SRC_ATOP);
            seekBarSB.getThumb().setColorFilter(COLORRED, PorterDuff.Mode.SRC_ATOP);

           // seekBarSB.setBackgroundColor(COLORRED);

        }

    }

    private Intent getNotificationIntent() {
        Intent intent = new Intent(this, MusicControls2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        processIntentAction(intent);
        super.onNewIntent(intent);
    }




    public void oreonoti(Bitmap bitmap, String TITLE, String ARTIST) {
         remoteViews=new RemoteViews(getPackageName(),R.layout.notification_layout);
        RemoteViews smallremote=new RemoteViews(getPackageName(),R.layout.notificationlay2);

        createNotificationChannel();
        Intent previousIntent = getNotificationIntent();
        previousIntent.setAction(PREVIOUS);

        Intent playIntent = getNotificationIntent();
        playIntent.setAction(PLAYPAUSE);


        Intent nextIntent = getNotificationIntent();
        nextIntent.setAction(NEXT);



NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"notification-id-musicplayer");
builder.setSmallIcon(R.drawable.musiccd);
builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
builder.setContentIntent(  PendingIntent.getActivity(this,0,Inr(),PendingIntent.FLAG_UPDATE_CURRENT));
remoteViews.setTextViewText(R.id.notificationartist,ARTIST);
remoteViews.setImageViewBitmap(R.id.notificationalbumart,bitmap);
        remoteViews.setOnClickPendingIntent(R.id.notificationplaypause,PendingIntent.getActivity(this,0,playIntent,PendingIntent.FLAG_UPDATE_CURRENT));
        remoteViews.setOnClickPendingIntent(R.id.notificationprevious,PendingIntent.getActivity(this,0,previousIntent,PendingIntent.FLAG_UPDATE_CURRENT));
        remoteViews.setOnClickPendingIntent(R.id.notificationnext,PendingIntent.getActivity(this,0,nextIntent,PendingIntent.FLAG_UPDATE_CURRENT));
        smallremote.setTextViewText(R.id.artistnotxp,ARTIST);
        if (bitmap==null)
        {
            Drawable myDrawable = getResources().getDrawable(R.drawable.musiccd);
            Bitmap anImage     = ((BitmapDrawable) myDrawable).getBitmap();
            smallremote.setImageViewBitmap(R.id.albumartnotexp,anImage);
        }else{ smallremote.setImageViewBitmap(R.id.albumartnotexp,bitmap);}




smallremote.setImageViewBitmap(R.id.albumartnotexp,bitmap);
        if (mediaPlayer.isPlaying())
        {
            remoteViews.setTextViewText(R.id.notificationtitile, "Now Playing " + TITLE);
            remoteViews.setImageViewResource(R.id.notificationplaypause,R.drawable.pause_white);
            smallremote.setTextViewText(R.id.titlenotxp, "Now Playing " + TITLE);


        }
        else{
            remoteViews.setTextViewText(R.id.notificationtitile, "Now Paused " + TITLE); remoteViews.setImageViewResource(R.id.notificationplaypause,R.drawable.play_white);
        remoteViews.setTextViewText(R.id.notificationartist,ARTIST);
            smallremote.setTextViewText(R.id.titlenotxp, "Now Paused " + TITLE);
            if (bitmap==null)
            { Drawable myDrawable = getResources().getDrawable(R.drawable.musiccd);
                Bitmap anImage     = ((BitmapDrawable) myDrawable).getBitmap();
                remoteViews.setImageViewBitmap(R.id.albumartnotexp,anImage);}
                else {
                remoteViews.setImageViewBitmap(R.id.notificationalbumart, bitmap);
            }


    }
builder.setStyle(new NotificationCompat.DecoratedCustomViewStyle());
        builder.setCustomContentView(smallremote);
builder.setCustomBigContentView(remoteViews);


        notificationManagerCompat=NotificationManagerCompat.from(this);
       notificationManagerCompat.notify(nid,builder.build());

        }








    public void nextprevious(String url, final String title, final String artist) {
        songtitleTV = (TextView) findViewById(R.id.songtitleTV);
        artistTV = (TextView) findViewById(R.id.artistTV);

        mediaPlayer.reset();
        songtitleTV.setText(title);
        artistTV.setText(artist);
        metadataretriver(url);
        oreonoti(songimages, title, artist);




        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                public void onPrepared(MediaPlayer mp) {
                    play_pauseIB.setImageResource(R.drawable.pause_white);
                    mediaPlayer.start();
                    seekBarSB.setMax(mediaPlayer.getDuration());//setting seekbar max by getting the duration from music
                    play();
                    if (mediaPlayer.isPlaying()) {
                        oreonoti(songimages, title, artist);


                    } else {
                        oreonoti(songimages, title, artist);

                    }
                    if (!mediaPlayer.isPlaying()) {
                        notification.setOngoing(false);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void processIntentAction(Intent intent) {
        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case PREVIOUS:
                    previous();

                    break;
                case PLAYPAUSE:
                    playpause();


                    break;
                case NEXT:
                    next();
                    break;
            }
        }
    }
    public void metadataretriver(String url) {
        IV = (ImageView) findViewById(R.id.IV);

        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();//metadataRetriver is used to get the embedded image from the file
        mediaMetadataRetriever.setDataSource(url);//this is the file url
        try {
            art = mediaMetadataRetriever.getEmbeddedPicture();//this will get the embedded image from file

            songImage = BitmapFactory.decodeByteArray(art, 0, art.length);//this will decode the file to bitmap
            Drawable drawable = new BitmapDrawable(getResources(), songImage);
            songimages = songImage;
            IV.setImageDrawable(drawable);//setting bitmap to imageview

        } catch (Exception e) {
            IV.setImageDrawable(getResources().getDrawable(R.drawable.musiccd));
        }


    }

    public void playpause() {
        if (mediaPlayer.isPlaying()) {
            play_pauseIB.setImageResource(R.drawable.play_white);
             remoteViews.setImageViewResource(R.id.notificationplaypause,R.drawable.play_white);
            mediaPlayer.pause();
            oreonoti(songimages, TTTT, AAAA);
        } else {
            mediaPlayer.start();
            play_pauseIB.setImageResource(R.drawable.pause_white);
            remoteViews.setImageViewResource(R.id.notificationplaypause,R.drawable.pause_white);


               oreonoti(songimages, TTTT, AAAA);


        }

    }

    private void clearNotification() {
        int notificationId = getIntent().getIntExtra("notificationId", 0);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);
    }


    @Override
    public void onBackPressed() {
        if (mediaPlayer.isPlaying()) {
            AlertDialog.Builder ab = new AlertDialog.Builder(this);
            ab.setTitle("CONFIRM");
            ab.setMessage("Music is Playing").setNegativeButton("STOP", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).setPositiveButton("KEEP PLAYING IN BACKGROUND", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(Intent.ACTION_MAIN);
                    i.addCategory(Intent.CATEGORY_HOME);
                    startActivity(i);

                }
            }).create().show();
        } else {
            finish();
        }


    }



    protected void onDestroy() {
        mediaPlayer.reset();

        nextsong=false;
if (Build.VERSION.SDK_INT>=27) {

}




        super.onDestroy();
    }
    private Intent Inr() {
        Intent intent = new Intent(this, MusicControls2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "NOTIFICATIONNAME";
            String description ="DES";

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notification-id-musicplayer", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }


}




