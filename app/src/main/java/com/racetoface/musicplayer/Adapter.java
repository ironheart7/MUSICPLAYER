package com.racetoface.musicplayer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;


import android.net.Uri;
import android.os.AsyncTask;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;


import android.widget.PopupMenu;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public  class Adapter extends RecyclerView.Adapter<Adapter.PlaceHolder> {
   static List<Content> songlist;




    public Activity activity;

   static byte[] art;


   static Bitmap songImage;
    static String SONGURL;
    Bitmap bbbbb;
    String SONGARTIST;
    String SONGTITLE;
   public static Context context;
    Drawable drawable;
    int position2;


    static PlaceHolder hddd;
  static   AsyncTask asyncTask;



    public Adapter() {

    }


    public Adapter(Context context) {
        this.context = context;
        Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);

        context.getApplicationContext().startActivity(intent);


    }

    public Adapter(Context context, List<Content> songlist) {

        this.songlist = songlist;
        this.context = context;


    }


    @NonNull
    @Override


    public PlaceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);


        return new PlaceHolder(v);
    }





    @Override
    public void onBindViewHolder(@NonNull final PlaceHolder holder, final int position) {



position2=position;
hddd=holder;
        holder.ARTIST.setText(songlist.get(position).ATRIST);
        holder.URL.setText(songlist.get(position).URL);
        holder.TITLE.setText(songlist.get(position).TITLE);




        holder.TITLE.setSelected(true);
        holder.TITLE.setSingleLine(true);
        SONGURL = holder.URL.getText().toString();


try {

    asyncTask = new AsyncTask() {
        @Override
        protected void onPreExecute() {

           holder.imageView.setVisibility(View.GONE);
        }

        @Override
        protected void onPostExecute(Object o) {


            holder.imageView.setVisibility(View.VISIBLE);

            holder.imageView.setImageBitmap((Bitmap) o);


        }

        @Override
        protected void onProgressUpdate(Object[] values) {

        }

        @Override
        protected Object doInBackground(Object[] objects) {

            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(songlist.get(position).URL);
            byte[] artBytes = mmr.getEmbeddedPicture();
            if (artBytes != null) {
                InputStream is = new ByteArrayInputStream(mmr.getEmbeddedPicture());


                Bitmap bm = BitmapFactory.decodeStream(is);
                bbbbb=bm;
         return bm;
            } else {

                Drawable dd = context.getResources().getDrawable(R.drawable.musiccd);
                Bitmap bitmap = ((BitmapDrawable) dd).getBitmap();
                bbbbb=bitmap;
                return bitmap;
            }
        }
    }.execute();
}catch (IndexOutOfBoundsException e){e.printStackTrace();}

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SONGURL = holder.URL.getText().toString();
                SONGARTIST = holder.ARTIST.getText().toString();
                SONGTITLE = holder.TITLE.getText().toString();
                Intent intent = new Intent(context.getApplicationContext(), MusicControls2.class);

                intent.putExtra("T", SONGTITLE);
                intent.putExtra("A", SONGARTIST);
                intent.putExtra("U", SONGURL);
                intent.putExtra("P", position2);


                context.getApplicationContext().startActivity(intent);

            }
        });




holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
    @Override
    public boolean onLongClick(View view) {
      final Dialog dialog=new Dialog(context);
        dialog.setContentView(R.layout.dialoglayout);
        TextView title=(TextView)dialog.findViewById(R.id.titledialog);
        TextView artist=(TextView)dialog.findViewById(R.id.artistdialog);
        ImageView dialogimageview=(ImageView)dialog.findViewById(R.id.dialogimageview);
        Button play=(Button)dialog.findViewById(R.id.playdialog);
        title.setText(songlist.get(position).TITLE);
        artist.setText(songlist.get(position).ATRIST);

         MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();//metadataRetriver is used to get the embedded image from the file
        mediaMetadataRetriever.setDataSource(songlist.get(position).URL);//this is the file url
        try {
            art = mediaMetadataRetriever.getEmbeddedPicture();//this will get the embedded image from file

            songImage = BitmapFactory.decodeByteArray(art, 0, art.length);//this will decode the file to bitmap
            Drawable drawable = new BitmapDrawable(context.getResources(), songImage);

            dialogimageview.setImageDrawable(drawable);//setting bitmap to imageview

        } catch (Exception e) {
            dialogimageview.setImageDrawable(context.getResources().getDrawable(R.drawable.musiccd)); //if image not found then default image
        }
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), MusicControls2.class);

                intent.putExtra("T", songlist.get(position).TITLE);
                intent.putExtra("A", songlist.get(position).ATRIST);
                intent.putExtra("U", songlist.get(position).URL);
                intent.putExtra("P", position2);




                context.getApplicationContext().startActivity(intent);
                dialog.dismiss();
            }
        });

        dialog.show();




        return false;
    }
});
holder.popubbutton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        PopupMenu popupMenu=new PopupMenu(context,holder.popubbutton);
        popupMenu.getMenuInflater().inflate(R.menu.popupmenu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
               AlertDialog.Builder ab=new AlertDialog.Builder(context);
               ab.setTitle("CONFIRM");
               ab.setMessage("Are you sure you want to share this file ?");
               ab.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                   }
               }).setPositiveButton("YES", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                       Uri screenshotUri = Uri.parse(songlist.get(position).URL);
                       sharingIntent.setType("audio/*");
                       sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                       context.startActivity(Intent.createChooser(sharingIntent, "Share This Music using"));



                   }
               }).create().show();

                return false;
            }
        });
popupMenu.show();
    }
});

    }
    public String nexturl(int i)
    {


String url=songlist.get(i).URL;
return url;

    }
    public String nextartist(int i)
    {


        String artist=songlist.get(i).ATRIST;
        return artist;

    }
    public String nexttitle(int i)
    {


        String title=songlist.get(i).TITLE;
        return title;

    }



    @Override
    public int getItemCount() {
        return songlist.size();
    }
    public int sizeb()
    {
        int i=songlist.size();
        return i;
    }






    public class PlaceHolder extends RecyclerView.ViewHolder {
        TextView TITLE,ARTIST,URL;
        CardView cardView;
        Button popubbutton;

        ImageView imageView;


        public PlaceHolder(View itemView) {
            super(itemView);





imageView=(ImageView) itemView.findViewById(R.id.videoimage);
            URL=(TextView)itemView.findViewById(R.id.song_url);
            TITLE=(TextView)itemView.findViewById(R.id.videotitle);
            ARTIST=(TextView)itemView.findViewById(R.id.Song_Artist);
            popubbutton=(Button)itemView.findViewById(R.id.popubbutton);

            cardView=(CardView)itemView.findViewById(R.id.cardview);




        }
    }
    public void setFilter(List<Content>newlist)

    {
        songlist = new ArrayList<>();
        songlist.addAll(newlist);
        notifyDataSetChanged();

    }




}


