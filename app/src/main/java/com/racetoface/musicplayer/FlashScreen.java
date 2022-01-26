package com.racetoface.musicplayer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;



public class FlashScreen extends AppCompatActivity {
    public static int SPLASH=2000;
    public static final int DEFAULT=0;
    SharedPreferences sharedPreferences;
    TextView textView;
    ImageView imageView;
    Adapter adapter=new Adapter();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preffe();
        setContentView(R.layout.activity_flash_screen);
     //   getSupportActionBar().hide();
        imageView=(ImageView)findViewById(R.id.imageView2);
        textView=(TextView)findViewById(R.id.textView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences1=getSharedPreferences("NAMESTORE",MODE_PRIVATE);
                String n=sharedPreferences1.getString("NAME",null);
                if (n!=null) {
                    Intent intent = new Intent(FlashScreen.this, MainActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(FlashScreen.this, Name.class);
                    startActivity(intent);
                }

                finish();
            }
        },SPLASH);

        Animation fade=AnimationUtils.loadAnimation(FlashScreen.this,R.anim.fadein);
        textView.startAnimation(fade);
    }
    public void preffe()
    {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int COLORRED = sharedPreferences.getInt("COLOR", DEFAULT);
        if (COLORRED == DEFAULT) {

        } else {


          //  getSupportActionBar().setBackgroundDrawable(new ColorDrawable(COLORRED));

            Window window = getWindow();

           // window.setStatusBarColor(COLORRED);
        }
    }

    @Override
    protected void onStart() {

        super.onStart();
    }
}
