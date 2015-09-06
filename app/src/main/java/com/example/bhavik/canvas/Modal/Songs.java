package com.example.bhavik.canvas.Modal;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Bhavik on 8/22/2015.
 */
public class Songs implements Serializable, Parcelable{

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

    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
