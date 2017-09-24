package com.example.cyc.myapplication.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.cyc.myapplication.R;
import com.example.cyc.myapplication.service.MusicService;
import com.example.cyc.myapplication.utils.AppConstant;
import com.example.cyc.myapplication.utils.MusicInfo;


import java.util.ArrayList;
import java.util.List;

import static com.example.cyc.myapplication.utils.AppConstant.CommandPlay;

/**
 * Created by cyc on 17-9-7.
 */

public class MusicListActivity extends BaseActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        FavoriteFragment.FavoriteMusicCallbacks,
        MusicMenuFragment.AllMusicCallbacks {
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private SharedPreferences sharedPreferences;
    private TextView playingTitle;
    private ImageButton playAndPause;
    private int position;
    private int playPosition;
    private boolean isPlaying = false;

    private List<MusicInfo>musicInfoList;
    private MusicInfo musicInfo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);
        sharedPreferences=getSharedPreferences(AppConstant.APP_DATE,MODE_PRIVATE);
        mToolbar=(Toolbar)findViewById(R.id.toolbar);
        position=1;
        setSupportActionBar(mToolbar);
        mToolbar.inflateMenu(R.menu.music_list);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerToggle=new ActionBarDrawerToggle(this,mDrawerLayout,
                mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(R.string.app_name);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                switch (position){
                    case 1:
                        getSupportActionBar().setTitle(R.string.title_all_song);
                        break;
                    case 2:
                        getSupportActionBar().setTitle(R.string.title_my_song);
                        break;
            }
            }
        };
        mDrawerToggle.syncState();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new MusicMenuFragment()).commit();
        playingTitle = (TextView)findViewById(R.id.title_playing);
        playAndPause = (ImageButton)findViewById(R.id.playing_button);




    }

    @Override
    protected void onStart() {
        super.onStart();
        isPlaying=sharedPreferences.getBoolean("isPlaying",false);
        if(isPlaying){
            playAndPause.setBackgroundResource(R.mipmap.btn_pause_normal);
        }
        else playAndPause.setBackgroundResource(R.mipmap.btn_play_normal);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        changeFragment(position);
        if(mDrawerLayout != null){
            mDrawerLayout.closeDrawers();
        }
        this.position = position;
    }
    @Override
    public void onListItemCliceked(int position, final List<MusicInfo> musicInfoList) {
        updateView(position,musicInfoList);
        this.musicInfoList=musicInfoList;
        musicInfo=musicInfoList.get(position);
        playPosition = position;
        playAndPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying){
                    playService(AppConstant.MEDIA_PAUSE,musicInfoList);
                    playAndPause.setBackgroundResource(R.mipmap.btn_play_normal);
                }
                else{
                    playService(AppConstant.MEDIA_CONTINUE,musicInfoList);
                    playAndPause.setBackgroundResource(R.mipmap.btn_pause_normal);
                }

                isPlaying = isPlaying?false:true;

            }
        });
    }

    @Override
    public void onFavoriteItemClicked(int position, final List<MusicInfo> favoriteMusicInfoList) {
        updateView(position,favoriteMusicInfoList);
        playPosition = position;
        playAndPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying){
                    playService(AppConstant.MEDIA_PAUSE,favoriteMusicInfoList);
                    playAndPause.setBackgroundResource(R.mipmap.btn_play_normal);
                }
                else{
                    playService(AppConstant.MEDIA_CONTINUE,favoriteMusicInfoList);
                    playAndPause.setBackgroundResource(R.mipmap.btn_pause_normal);
                }

                isPlaying = isPlaying?false:true;

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.music_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_exit) {

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isPlaying",false);
            editor.commit();
            Intent intent = new Intent(MusicListActivity.this, MusicService.class);
            stopService(intent);
            System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }
    private void changeFragment(int position) {
        FragmentManager manager = getSupportFragmentManager();
        switch (position) {
            case 1:
                manager.beginTransaction().replace(R.id.container ,new MusicMenuFragment()).commit();
                break;
            case 2:
                manager.beginTransaction().replace(R.id.container , new FavoriteFragment()).commit();
                break;

        }

    }
    public void updateView(int position,List<MusicInfo> musicInfoList){
        playingTitle.setText(musicInfoList.get(position).getMusicTitle());
        if (isPlaying)
            playAndPause.setBackgroundResource(R.mipmap.btn_pause_normal);
        else
            playAndPause.setBackgroundResource(R.mipmap.btn_play_normal);
    }
    private void playService(int i,List<MusicInfo> musicInfoList){
        Intent serviceIntent = new Intent(this, MusicService.class);
        serviceIntent.putExtra("position",playPosition);
        serviceIntent.putCharSequenceArrayListExtra("musicInfoList", (ArrayList) musicInfoList);
        serviceIntent.putExtra("MSG",i);
        this.startService(serviceIntent);
    }
    public void selfNotification(){
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this);
        builder.setAutoCancel(false);
        builder.setOngoing(true);
        builder.setShowWhen(false);
        builder.setContentTitle("Notification");
        builder.setSmallIcon(R.mipmap.icon_launcher);
        RemoteViews remoteViews=new RemoteViews(getPackageName(),R.layout.notification_layout);
            remoteViews.setImageViewResource(R.id.playandpause_notif,R.mipmap.btn_pause_normal);
        if(isPlaying) {
            Intent intent1 = new Intent(this, MusicService.class);
            intent1.putExtra("position",playPosition);
            intent1.putCharSequenceArrayListExtra("musicInfoList", (ArrayList) musicInfoList);
            intent1.putExtra("MSG", AppConstant.MEDIA_PLAY);
            PendingIntent pendingIntent1 = PendingIntent.getService(this, 5, intent1, 0);
            isPlaying=false;
            remoteViews.setOnClickPendingIntent(R.id.playandpause_notif, pendingIntent1);
        }
        if(!isPlaying) {
            Intent intent2 = new Intent(this, MusicService.class);
            intent2.putExtra("MSG", AppConstant.MEDIA_PAUSE);
            intent2.putExtra("position",playPosition);
            intent2.putCharSequenceArrayListExtra("musicInfoList", (ArrayList) musicInfoList);
            PendingIntent pendingIntent2 = PendingIntent.getService(this, 6, intent2, 0);
            remoteViews.setOnClickPendingIntent(R.id.playandpause_notif, pendingIntent2);
        }
        Intent intent3=new Intent(this,MusicService.class);
        intent3.putExtra("MSG",AppConstant.MEDIA_NEXT);
        intent3.putExtra("position",playPosition);
        intent3.putCharSequenceArrayListExtra("musicInfoList", (ArrayList) musicInfoList);
        PendingIntent pendingIntent3=PendingIntent.getService(this,7,intent3,0);
        remoteViews.setOnClickPendingIntent(R.id.previous_btn_notif,pendingIntent3);

        Intent intent4=new Intent(this,MusicService.class);
        intent4.putExtra("MSG",AppConstant.MEDIA_NEXT);
        intent4.putExtra("position",playPosition);
        intent4.putCharSequenceArrayListExtra("musicInfoList", (ArrayList) musicInfoList);
        PendingIntent pendingIntent4=PendingIntent.getService(this,8,intent4,0);
        remoteViews.setOnClickPendingIntent(R.id.previous_btn_notif,pendingIntent4);
        builder.setContent(remoteViews);
        Notification notification=builder.build();
        NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0,notification);

    }
}
