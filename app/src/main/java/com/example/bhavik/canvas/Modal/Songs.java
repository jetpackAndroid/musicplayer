package com.example.bhavik.canvas.Modal;

import android.net.Uri;

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

    public Uri getAlbumArtPath() {
        return mAlbumArtPath;
    }

    public void setAlbumArtPath(Uri mAlbumArtPath) {
        this.mAlbumArtPath = mAlbumArtPath;
    }

    private Uri mAlbumArtPath;

    public Songs(long songId, String songTitle, String songArtist, Uri albumArtPath) {
        id = songId;
        title = songTitle;
        artist = songArtist;
        mAlbumArtPath = albumArtPath;
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
