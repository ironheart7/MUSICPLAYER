package com.racetoface.musicplayer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.SimpleDateFormat;
import java.util.Date;

public class FeedBackActivity extends AppCompatActivity {
    Spinner feedspiner;
    EditText feedbacke;
    Button submit;
    android.support.v7.widget.Toolbar toolbar2;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference root = database.getReference("FEEDBACKS");

    //DatabaseReference EditTEXT=root.child("Feedback");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        feedbacke = (EditText) findViewById(R.id.feedbacke);
        feedspiner = (Spinner) findViewById(R.id.feedspinner);
        submit = (Button) findViewById(R.id.submit);
        toolbar2=(android.support.v7.widget.Toolbar)findViewById(R.id.toolbar2);
       // setSupportActionBar(toolbar2);
        toolbar2.setTitle("FeedBack");

        toolbar2.setSubtitleTextColor(getResources().getColor(R.color.White));
        toolbar2.setTitleTextColor(getResources().getColor(R.color.White));
        feedbacke.setVisibility(View.INVISIBLE);
        pre();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdatas();

            }
        });


    }

    public void getdatas() {
        String id = root.push().getKey().toString();
        {
            String spinner = feedspiner.getSelectedItem().toString();
            if (spinner.equals("CUSTOM FEEDBACK")) {
                feedbacke.setVisibility(View.VISIBLE);
                if (feedbacke.getText().toString().equals(""))
                {
                    Toast.makeText(this,"Enter your FeedBack",Toast.LENGTH_SHORT).show();
                    feedspiner.setEnabled(false);

                }
                else {
                    String feed = feedbacke.getText().toString();
                    Date today = new Date();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                    String dateToStr = format.format(today);
                    SharedPreferences sharedPreferences1=getSharedPreferences("NAMESTORE",MODE_PRIVATE);
                    String n=sharedPreferences1.getString("NAME","ggg");
                    root.child(id).setValue(feed+" "+"("+dateToStr+")"+" "+" USER: "+n);
                    feedbacke.setText("");
                    Toast.makeText(this,"Thanks For your Feedback :)",Toast.LENGTH_SHORT).show();
                    submit.setEnabled(false);
                    if (Build.VERSION.SDK_INT>=27)
                    {
                        oreonoti(feed);
                    }
                    else {
                        noti(feed);
                    }
                }
            } else {
                Date today = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                String dateToStr = format.format(today);
                SharedPreferences sharedPreferences1=getSharedPreferences("NAMESTORE",MODE_PRIVATE);
                String n=sharedPreferences1.getString("NAME","ggg");
                root.child(id).setValue(spinner+" "+"("+dateToStr+")"+" "+"USER: "+n);
                Toast.makeText(this,"Thanks For your Feedback :)",Toast.LENGTH_SHORT).show();
                submit.setEnabled(false);

                if (Build.VERSION.SDK_INT>=27)
                {
                    oreonoti(spinner);
                }
                else {
                    noti(spinner);
                }
            }



        }



    }
    public void pre() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int COLORRED = sharedPreferences.getInt("COLOR", 0);
        if (COLORRED == 0) {

        } else {
            toolbar2=(android.support.v7.widget.Toolbar)findViewById(R.id.toolbar2);

          toolbar2.setBackground(new ColorDrawable(COLORRED));

            Window window = getWindow();

            window.setStatusBarColor(COLORRED);
        }
    }

    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }

    @Override
    protected void onStart() {


        super.onStart();
    }
    public void noti(String fee) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);//notification manager
        //setting the image to the notification

//setting the song title to the notification


        // notificationid = (int) System.currentTimeMillis();//generating unique id of notification using system currenttimemillis
       //the intent is used switch into the activity
        // PendingIntent pendingIntent = PendingIntent.getActivity(context, 555, notification_intent, 0);//pending intent



       NotificationCompat.Builder notification = new NotificationCompat.Builder(this);//notification builder
        notification.setSmallIcon(R.drawable.musiccd);//setting content remoteviews
        notification.setBadgeIconType(R.drawable.musiccd);
        notification.setContentTitle("Thanks For Your Feedback :)");
        // notification.setContentIntent(pendingIntent);

        notification.setStyle(new NotificationCompat.BigTextStyle().bigText(fee));



        notificationManager.notify(412, notification.build());//to build and display the notifications
    }
    public void oreonoti(String fee)
    {
        int notifyID = 1;
        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        CharSequence name ="IRON SUMIT";// The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;
        if (Build.VERSION.SDK_INT>=26) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
// Create a notification and set the notification channel.
            Notification notification = new Notification.Builder(FeedBackActivity.this)
                    .setContentTitle("Thanks For Your Feedback  :)")

                    .setSmallIcon(R.drawable.musiccd)
                    .setStyle(new Notification.BigTextStyle().bigText(fee))

                    .setChannelId(CHANNEL_ID)
                    .build();

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(mChannel);


// Issue the notification.
            mNotificationManager.notify(notifyID, notification);
        }
    }
}