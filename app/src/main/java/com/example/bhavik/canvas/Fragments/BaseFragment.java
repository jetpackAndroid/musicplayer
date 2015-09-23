package com.example.bhavik.canvas.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.View;

import com.example.bhavik.canvas.Acttivities.MainActivity;
import com.example.bhavik.canvas.Interfaces.MusicFunctionInterface;
import com.example.bhavik.canvas.Modal.Songs;
import com.example.bhavik.canvas.Service.MusicService;

import java.util.ArrayList;

public class BaseFragment extends Fragment implements MusicFunctionInterface {

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean checkMusicIsPlaying() {
        return MainActivity.getMainActivity().checkIsPlaying();
    }

    @Override
    public void pauseMusic() {
        MainActivity.getMainActivity().pauseMusic();
    }

    @Override
    public void playMusic() {
        getMusicService().playSong();
    }

    @Override
    public void startMusic() {
        getMusicService().go();
    }

    @Override
    public void playNext() {
        getMusicService().playNext();
    }

    @Override
    public int getMusicDuration() {
        return getMusicService().getDur();
    }

    @Override
    public int getCurrentMusicPosition() {
        return getMusicService().getPosn();
    }

    @Override
    public void playPrevious() {
        getMusicService().playPrev();
    }

    @Override
    public void seekToPosition(int position) {
        getMusicService().seek(position);
    }

    @Override
    public void stopMusic() {
        getMusicService().stopMusic();
    }

    @Override
    public MusicService getMusicService() {
        return MainActivity.getMusicService();
    }

    @Override
    public ArrayList<Songs> getCompleteSongList() {
        return getMusicService().getCompleteSongList();
    }

    @Override
    public int currentSongIndex() {
        return MainActivity.getMusicService().getSongIndex();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Songs getCurrentPlayingSong() {
        return getMusicService().getCurrentPlayingSong();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
