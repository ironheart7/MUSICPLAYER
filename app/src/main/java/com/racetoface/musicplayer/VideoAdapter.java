package com.racetoface.musicplayer;

import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.graphics.Bitmap;

import android.graphics.drawable.Drawable;


import android.os.AsyncTask;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;


public  class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.PlaceHolder> {
     List<VideoContent> videolist;

    public VideoAdapter() {
    }

    Context context;


    public VideoAdapter(List<VideoContent> videolist, Context context) {
        this.videolist = videolist;
        this.context = context;
    }

    public VideoAdapter(Context context) {
        Intent intent = new Intent(context.getApplicationContext(), VideoPlayer.class);

        context.getApplicationContext().startActivity(intent);


    }

    @NonNull
    @Override


    public PlaceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list, parent, false);


        return new PlaceHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceHolder holder, final int position) {
holder.videotitle.setText(videolist.get(position).TITLE);
holder.videoartist.setText(videolist.get(position).ATRIST);
holder.videocardview.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context.getApplicationContext(), VideoPlayer2.class);

        intent.putExtra("T", videolist.get(position).TITLE);
        intent.putExtra("A", videolist.get(position).ATRIST);
        intent.putExtra("URL", videolist.get(position).URL);





        context.getApplicationContext().startActivity(intent);
    }
});
    }






    @Override
    public int getItemCount() {
        return videolist.size();
    }





    public class PlaceHolder extends RecyclerView.ViewHolder {

CardView videocardview;
TextView videotitle,videoartist;
ImageView videoimage;

        public PlaceHolder(View itemView) {
            super(itemView);
videoartist=(TextView)itemView.findViewById(R.id.videoartist);
            videotitle=(TextView)itemView.findViewById(R.id.videotitle);
            videocardview=(CardView)itemView.findViewById(R.id.videocardview);
            videoimage=(ImageView)itemView.findViewById(R.id.videoimage);







        }
    }





}


