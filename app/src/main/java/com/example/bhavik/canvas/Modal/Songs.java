package com.example.bhavik.canvas.Modal;

import java.io.Serializable;

/**
 * Created by Bhavik on 8/22/2015.
 */
public class Songs implements Serializable {

    private long id;
    private String title;
    private String artist;
    private int totalNumberOfSongs;
    private String songName;
    private String mAlbumArtPath;

    public Songs(long songId, String songTitle, String songArtist, String albumArtPath, int duration) {
        id = songId;
        title = songTitle;
        artist = songArtist;
        mAlbumArtPath = albumArtPath;
        this.duration = duration;
    }


    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    private int duration;

    public String getAlbumArtPath() {
        return mAlbumArtPath;
    }

    public void setAlbumArtPath(String mAlbumArtPath) {
        this.mAlbumArtPath = mAlbumArtPath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }



    public int getTotalNumberOfSongs() {
        return totalNumberOfSongs;
    }

    public void setTotalNumberOfSongs(int totalNumberOfSongs) {
        this.totalNumberOfSongs = totalNumberOfSongs;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }
}
