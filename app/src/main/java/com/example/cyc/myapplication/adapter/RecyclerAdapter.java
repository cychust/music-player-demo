package com.example.cyc.myapplication.adapter;



import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cyc.myapplication.R;
import com.example.cyc.myapplication.service.MusicService;
import com.example.cyc.myapplication.ui.MusicPlayingActivity;
import com.example.cyc.myapplication.utils.AppConstant;
import com.example.cyc.myapplication.utils.MusicInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cyc on 17-9-7.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {


    List<MusicInfo> musicList;
    private Context mContext;
    public RecyclerAdapter(List<MusicInfo> musiclist,Context context){

        mContext=context;
        musicList=musiclist;

    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView titleText,artistText;
        public ViewHolder(View view){
            super(view);
            titleText=(TextView)view.findViewById(R.id.music_title);
            artistText=(TextView)view.findViewById(R.id.music_artist);

        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.artistText.setText(musicList.get(position).getMusicArtist());
        holder.titleText.setText(musicList.get(position).getMusicTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext,"viewholder clicked",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(mContext,MusicPlayingActivity.class);
                intent.putExtra("position",position);
                intent.putCharSequenceArrayListExtra("musicInfoList",(ArrayList)musicList);
                playService(AppConstant.MEDIA_PLAY,position);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.content_music_list,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }
    public void playService(int i,int position){
        Intent serviceIntent=new Intent(mContext,MusicService.class);
        serviceIntent.putExtra("position",position);
        serviceIntent.putCharSequenceArrayListExtra("musicInfoList",(ArrayList)musicList);
        serviceIntent.putExtra("MSG",i);
        mContext.startActivity(serviceIntent);
    }
}
