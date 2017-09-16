package com.example.cyc.myapplication.utils;

import java.io.Serializable;

/**
 * Created by cyc on 17-9-7.
 */

public class MusicInfo implements Serializable{

    private static final long serializableVersion=1l;
    private long musicId;
    private String musicPath=null;
    private String musicTitle=null;
    private String musicArtist=null;
    private long musicDuration;
    private long musicSize;
public MusicInfo(){
    super();
}
    public void setMusicId(long musicId){
        this.musicId = musicId;
    }
    public void setMusicPath(String musicPath){
        this.musicPath = musicPath;
    }
    public void setMusicTitle(String musicTitle){
        this.musicTitle = musicTitle;
    }
    public void setMusicArtist(String musicArtist){
        this.musicArtist = musicArtist;
    }
    public void setMusicDuration(long musicDuration){
        this.musicDuration = musicDuration;
    }
    public void setMusicSize(long musicSize){
        this.musicSize = musicSize;
    }

    public long getMusicId(){
        return musicId;
    }
    public String getMusicPath(){
        return musicPath;
    }
    public String getMusicTitle(){
        return musicTitle;
    }
    public String getMusicArtist(){
        return musicArtist;
    }
    public long getMusicDuration(){
        return musicDuration;
    }
    public long getMusicSize(){
        return musicSize;
    }

    @Override
    public String toString() {
        return "MusicInfo [musicId="+musicId+",musicPath="+musicPath+",musicTitle="+musicTitle
                +",musicArtist="+musicArtist+",musicDuration="+musicDuration+",musicSize="+musicSize
                +"]";
    }

}
