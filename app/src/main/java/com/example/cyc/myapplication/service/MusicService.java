package com.example.cyc.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.cyc.myapplication.utils.App;
import com.example.cyc.myapplication.utils.MusicInfo;

import java.util.List;

/**
 * Created by cyc on 17-9-7.
 */

public class MusicService extends Service {

    private MediaPlayer mediaPlayer;
    private MusicInfo musicInfo;
    private List<MusicInfo>musicInfoList;
    private int position;
    private SharedPreferences sharedPreferences;
    private int repeatState;
    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer=new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MusicPlayCompleteListener());
        sharedPreferences=getSharedPreferences(App.APP_DATE,MODE_PRIVATE);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent,  int flags, int startId) {

        repeatState=sharedPreferences.getInt("repeatState", App.allRepeat);
        position=intent.getIntExtra("position",0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt("lastPosition",position);
        editor.commit();
        Log.i("repeateState---->",repeatState+"");
        musicInfoList=(List)intent.getCharSequenceArrayListExtra("musicInfoList");
        musicInfo=musicInfoList.get(position);
        if(musicInfo!=null){
            int MSG = intent.getIntExtra("MSG",0);
            Log.i("MSG--->",MSG+"");
            switch (MSG){
                case App.MEDIA_PLAY:
                    playMusic(musicInfo);
                    break;
                case App.MEDIA_PAUSE:
                    pauseMusic();
                    break;
                case App.MEDIA_NEXT:
                    playMusic(musicInfo);
                    pauseMusic();
                    break;
                case App.MEDIA_SEEKTO:
                    int progress = intent.getIntExtra("progress",0);
                    mediaPlayer.seekTo(progress);
                    continueMusic();
                    break;
                case App.MEDIA_CONTINUE:
                    continueMusic();
                    break;
            }
        }
        Log.i("posituon--->",position+"");
        Log.i("musicInfoList--->",musicInfo.toString());
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopMusic();
        mediaPlayer.release();
    }
    public void playMusic(MusicInfo music){
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(music.getMusicPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void stopMusic(){
        mediaPlayer.stop();
    }
    public void pauseMusic(){
        mediaPlayer.pause();
    }
    public void continueMusic(){mediaPlayer.start();}
    public class MusicPlayCompleteListener implements MediaPlayer.OnCompletionListener{

        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            switch (repeatState){
                case App.allRepeat:
                    if (position == musicInfoList.size() - 1) {
                        position = 0;
                    } else {
                        position += 1;
                    }
                    break;
                case App.randomRepeat:
                    position = (int)((musicInfoList.size()-1)*Math.random());
                    break;
                case App.singleRepeat:
                    break;
            }
            musicInfo = musicInfoList.get(position);
            playMusic(musicInfo);
            Intent sendIntent = new Intent(App.UPDATE_VIEW);
            sendIntent.putExtra("position",position);
            SharedPreferences.Editor editor= sharedPreferences.edit();
            editor.putInt("lastPosition",position);
            editor.commit();
            sendBroadcast(sendIntent);
        }
    }
}
