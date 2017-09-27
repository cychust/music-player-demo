package com.example.cyc.myapplication.ui;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


import com.example.cyc.myapplication.R;
import com.example.cyc.myapplication.service.MusicService;
import com.example.cyc.myapplication.utils.App;
import com.example.cyc.myapplication.utils.MediaUtil;
import com.example.cyc.myapplication.utils.MusicInfo;

import java.util.ArrayList;
import java.util.List;



public class FavoriteFragment extends Fragment {
    private List<MusicInfo> favoriteMusicInfoList;
    private int mPosition;
    private MusicReceiver musicReceiver;
    private ListView favoriteList;
    private FavoriteMusicCallbacks favoriteMusicCallbacks;


    public FavoriteFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        musicReceiver = new MusicReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(App.UPDATE_VIEW);



        getActivity().registerReceiver(musicReceiver, filter);

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View favoriteView = inflater.inflate(R.layout.fragment_favorite, container, false);
        if (checkSDCard()){
            favoriteMusicInfoList = MediaUtil.getFavoriteMusicInfo(getActivity());
            favoriteList = (ListView)favoriteView.findViewById(R.id.favorite_list);
            if (favoriteMusicInfoList.size() > 0){
                SimpleAdapter adapter = new SimpleAdapter(getActivity(),
                        MediaUtil.getMusicList(favoriteMusicInfoList),
                        R.layout.content_music_list,
                        new String[]{"title", "artist"},
                        new int[]{R.id.music_title, R.id.music_artist});
                favoriteList.setAdapter(adapter);
            }
            favoriteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(favoriteMusicInfoList!=null){

                        sendData(position,favoriteMusicInfoList);
                        MusicInfo musicInfo = favoriteMusicInfoList.get(position);
                        Log.i("musicInfo---->",musicInfo.toString());
                        Intent intent = new Intent(getActivity(), MusicPlayingActivity.class);
                        mPosition = position;
                        intent.putExtra("position",mPosition);
                        intent.putCharSequenceArrayListExtra("musicInfoList",(ArrayList)favoriteMusicInfoList);
                        playService(App.MEDIA_PLAY);
                        getActivity().startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.activity_open,0);
                    }
                }
            });
        }else {
            Toast.makeText(getActivity(),"请插入SD卡！！！",Toast.LENGTH_SHORT).show();
        }


        return favoriteView;
    }

    private boolean checkSDCard()
    {
        if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            return true;
        else
            return false;
    }

    public class MusicReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(App.UPDATE_VIEW)){
                mPosition = intent.getIntExtra("position",0);
                //updateView(position);
                if (favoriteMusicInfoList != null){
                    sendData(mPosition,favoriteMusicInfoList);
                }
            }
        }
    }
    private void playService(int i){
        Intent serviceIntent = new Intent(getActivity(), MusicService.class);
        serviceIntent.putExtra("position",mPosition);
        serviceIntent.putCharSequenceArrayListExtra("musicInfoList", (ArrayList) favoriteMusicInfoList);
        serviceIntent.putExtra("MSG",i);
        getActivity().startService(serviceIntent);
    }

    private void sendData(int position,List<MusicInfo> musicInfoList){
        if (favoriteMusicCallbacks != null){
            favoriteMusicCallbacks.onFavoriteItemClicked(position,musicInfoList);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(musicReceiver);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            favoriteMusicCallbacks = (FavoriteMusicCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement FavoriteMusicCallbacks.");
        }
    }

    public static interface FavoriteMusicCallbacks{
        void onFavoriteItemClicked(int position,List<MusicInfo> favotiteMusicInfoList);
    }

}