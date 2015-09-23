package com.example.bhavik.canvas.Interfaces;

import com.example.bhavik.canvas.Modal.Songs;
import com.example.bhavik.canvas.Service.MusicService;

import java.util.ArrayList;

/**
 * Created by Bhavik on 9/12/2015.
 */
public interface MusicFunctionInterface {

    boolean checkMusicIsPlaying();

    void pauseMusic();

    void playMusic();

    MusicService getMusicService();

    void stopMusic();

    void playNext();

    void startMusic();

    void playPrevious();

    void seekToPosition(int position);

    int getMusicDuration();

    int getCurrentMusicPosition();

    Songs getCurrentPlayingSong();

    ArrayList<Songs> getCompleteSongList();

    int currentSongIndex();
}
