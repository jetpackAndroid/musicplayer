package com.example.bhavik.canvas.Modal;

/**
 * Created by Bhavik on 8/22/2015.
 */
public class Songs {

    private long id;
    private String title;
    private String artist;
    private int totalNumberOfSongs;
    private String songName;

    public Songs(long songId, String songTitle, String songArtist){
        id = songId;
        title = songTitle;
        artist = songArtist;
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
