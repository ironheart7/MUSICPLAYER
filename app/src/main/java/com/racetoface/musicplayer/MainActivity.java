package com.racetoface.musicplayer;

import android.Manifest;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;

import android.graphics.drawable.ColorDrawable;
import android.net.Uri;

import android.os.Build;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.skydoves.colorpickerpreference.ColorEnvelope;
import com.skydoves.colorpickerpreference.ColorListener;
import com.skydoves.colorpickerpreference.ColorPickerDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;




public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<Content> songlist;
    Adapter adapter;
    byte[] art;
    ProgressBar progressBar;
    SwipeRefreshLayout mSwipeRefreshLayout;
Handler handler;
    String aURL;
    NavigationView navigationView;
    Bitmap songImage;
    DrawerLayout drawerLayout;
    SearchView searchView;
    public static final int DEFAULT = -1;
    LinearLayout linearLayout;

    public static boolean DEFAULTB = true;
    boolean b = false;
    ActionBarDrawerToggle actionBarDrawerToggle;

    android.support.v7.app.ActionBar actionBar;
    android.support.v7.widget.Toolbar toolbar;
    private FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
    private HashMap<String, Object> firebaseDefaultMap;
    int defaultcolor;
public static final String VERSION_CODE_KEY="latest_version";
    Context context;
    SharedPreferences sharedPreferences;

    public static String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
        firebaseDefaultMap = new HashMap<>();
        firebaseDefaultMap.put(VERSION_CODE_KEY, getCurrentVersionCode());
        mFirebaseRemoteConfig.setDefaults(firebaseDefaultMap);
        mFirebaseRemoteConfig.setConfigSettings(
                new FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(BuildConfig.DEBUG)
                        .build());
        mFirebaseRemoteConfig.fetch().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mFirebaseRemoteConfig.activateFetched();
                    Log.d(TAG, "Fetched value: " + mFirebaseRemoteConfig.getString(VERSION_CODE_KEY));
                    //calling function to check if new version is available or not
                    checkForUpdate();
                } else {
                    Toast.makeText(MainActivity.this, "Someting went wrong please try again",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        prefference();
linearLayout=(LinearLayout)findViewById(R.id.linearlayoutcolor);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.OPEN, R.string.CLOSE);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
//        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        handler = new Handler();
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recreate();

            }
        });


        //  actionBarDrawerToggle.syncState();

        songlist = new ArrayList<>();

        Adapter adapter = new Adapter(this, songlist);
        adapter.notifyDataSetChanged();


        recyclerView = (RecyclerView) findViewById(R.id.Recycler);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        final Adapter a2 = new Adapter();
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.White));
    actionBar=getSupportActionBar();

       actionBar.setDisplayHomeAsUpEnabled(true);


       actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);



        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));


        defaultcolor = ContextCompat.getColor(MainActivity.this, R.color.colorPrimaryDark);

        checkUserPermission();


        SharedPreferences sharedPreferences1 = getSharedPreferences("NAMESTORE", MODE_PRIVATE);
        String n = sharedPreferences1.getString("NAME", "UNKNOWN");



        int songggg = a2.getItemCount();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.colors) {
                    coslor();
                } else if (id == R.id.feedback) {
                    Intent intent = new Intent(MainActivity.this, FeedBackActivity.class);
                    startActivity(intent);
                } else if (id == R.id.online) {
                    try {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_SUBJECT, "Music Player");
                        String sAux = "\nLet me recommend you this application\n\n";
                        sAux = sAux + "https://drive.google.com/open?id=15K_6UTJSq_M-dkyRQFLL6CxxVV4VFcjm\n\n";
                        i.putExtra(Intent.EXTRA_TEXT, sAux);
                        startActivity(Intent.createChooser(i, "choose one"));
                    } catch (Exception e) {
                        //e.toString();
                    }

                } else if (id == R.id.video) {
                    Intent intent=new Intent(MainActivity.this,VideoPlayer.class);
                    startActivity(intent);
                }
                else if (id==R.id.changename)
                {
                    AlertDialog.Builder alertdialog=new AlertDialog.Builder(MainActivity.this);
                    alertdialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent=new Intent(MainActivity.this,Name.class);
                            startActivity(intent);
                        }
                    }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setMessage("Are you sure you want to change your name").setTitle("CONFIRM").create().show();


                             }

                return false;
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.navigationname);
        navUsername.setText("Name:" + " " + n);





    }

    private int getCurrentVersionCode() {
        try{
            getPackageManager().getPackageInfo(getPackageName(), 0);

        }catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return -1;

    }


    public void loadsongs() {
        ContentResolver contentResolver = getContentResolver();


        Uri song = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String sort = MediaStore.Audio.Media.DEFAULT_SORT_ORDER;

        Cursor cursor = contentResolver.query(song, null,
                null, null, sort, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String ATRIST = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String TITLE = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                this.aURL = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));

                Content cc = new Content(ATRIST, TITLE, aURL, id);


                songlist.add(cc);
                adapter = new Adapter(this, songlist);
                recyclerView.setAdapter(adapter);
            } while (cursor.moveToNext());
        }

    }

    private void checkUserPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
                return;
            }
        }
        loadsongs();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 123:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadsongs();
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    checkUserPermission();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        MenuItem sear = menu.findItem(R.id.search);
        MenuItem menuItem = menu.findItem(R.id.colors);
        MenuItem feed = menu.findItem(R.id.feedback);
        MenuItem online = menu.findItem(R.id.online);
        MenuItem video = menu.findItem(R.id.video);

        searchView = (SearchView) sear.getActionView();
        searchView.setQueryHint("Search Library");


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                ArrayList<Content> newlist = new ArrayList<>();
                for (Content x : songlist) {
                    String name = x.getTITLE().toLowerCase();
                    if (name.contains(newText)) {
                        try {
                            newlist.add(x);

                            adapter.setFilter(newlist);
                        }catch (Exception e)
                        {

                        }

                    }
                }

                return true;
            }
        });


        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);




                return true;
        }


        return  super.onOptionsItemSelected(item);
        //actionBarDrawerToggle.onOptionsItemSelected(item)||
    }

    public void coslor() {
        ColorPickerDialog.Builder builder = new ColorPickerDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        builder.setTitle("ColorPicker Dialog");
        builder.setPreferenceName("MyColorPickerDialog");

        builder.setPositiveButton("ok", new ColorListener() {
            @Override
            public void onColorSelected(ColorEnvelope colorEnvelope) {
               defaultcolor=colorEnvelope.getColor();

                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("COLOR",defaultcolor);

                linearLayout.setBackgroundColor(defaultcolor);
                mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(defaultcolor);
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                View headerView = navigationView.getHeaderView(0);
                LinearLayout linearbackground = (LinearLayout) headerView.findViewById(R.id.linearnavig);
                linearbackground.setBackgroundColor(defaultcolor);

                editor.commit();

              toolbar.setBackgroundDrawable(new ColorDrawable(defaultcolor));

                Window window=getWindow();

                window.setStatusBarColor(defaultcolor);;




            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }


    public void prefference()
    {
        linearLayout=(LinearLayout)findViewById(R.id.linearlayoutcolor);

       SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);

         sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int COLORRED = sharedPreferences.getInt("COLOR", DEFAULT);

        if (COLORRED==DEFAULT)
        {

        }


else {



          //  getSupportActionBar().setBackgroundDrawable(new ColorDrawable(COLORRED));
            toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
            toolbar.setBackground(new ColorDrawable(COLORRED));

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View headerView = navigationView.getHeaderView(0);
            LinearLayout linearbackground = (LinearLayout) headerView.findViewById(R.id.linearnavig);
            linearbackground.setBackgroundColor(COLORRED);
            linearLayout.setBackgroundColor(COLORRED);
            mSwipeRefreshLayout.setProgressBackgroundColorSchemeColor(COLORRED);

            Window window=getWindow();

            window.setStatusBarColor(COLORRED);
        }


    }





    @Override
    protected void onStart() {


        super.onStart();
    }
    private void checkForUpdate() {
        int latestAppVersion = (int) mFirebaseRemoteConfig.getDouble(VERSION_CODE_KEY);
        if (latestAppVersion > getCurrentVersionCode()) {
            new AlertDialog.Builder(this).setTitle("Please Update the App")
                    .setMessage("A new version of this app is available. Please update it").setPositiveButton(
                    "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast
                                    .makeText(MainActivity.this, "Take user to Google Play Store", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }).setCancelable(false).show();
        } else {
            Toast.makeText(this,"This app is already upto date", Toast.LENGTH_SHORT).show();
        }
    }


}










