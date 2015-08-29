package com.example.bhavik.canvas.Service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.Random;

import com.example.bhavik.canvas.Acttivities.MainActivity;
import com.example.bhavik.canvas.Modal.Songs;
import com.example.bhavik.canvas.R;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener{

    //Media Player
    private MediaPlayer mediaPlayer;
    //Song List
    private ArrayList<Songs> songs;
    // Song Position
    private int songPosition;

    private final IBinder musicBind = new MusicBinder();

    private String songTitle="";
    private static final int NOTIFY_ID=1;

    private boolean shuffle=false;
    private Random rand;

    public MusicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        songPosition = 0;
        mediaPlayer = new MediaPlayer();
        initMusicPlayer();
        rand=new Random();
    }

    public void initMusicPlayer(){

        //set player properties
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
    }

    public void setMusicList(ArrayList<Songs> theList){
        songs = theList;
    }
    public class MusicBinder extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }
   }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mediaPlayer.stop();
        mediaPlayer.release();
        return  false;
    }
    public void setSong(int songIndex){
        songPosition=songIndex;
    }

    public void playSong(){
        mediaPlayer.reset();
        Songs playSong = songs.get(songPosition);
        songTitle = playSong.getTitle();
        long currentSong = playSong.getId();
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currentSong);
        try{
            mediaPlayer.setDataSource(getApplicationContext(),trackUri);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        mediaPlayer.prepareAsync();
    }

    public int getPosn(){
        return mediaPlayer.getCurrentPosition();
    }

    public int getDur(){
        return mediaPlayer.getDuration();
    }

    public boolean isPng(){
        return mediaPlayer.isPlaying();
    }

    public void pausePlayer(){
        mediaPlayer.pause();
    }

    public void seek(int posn){
        mediaPlayer.seekTo(posn);
    }

    public void go(){
        mediaPlayer.start();
    }

    public void playPrev(){
        songPosition--;
        if(songPosition<0)
            songPosition=songs.size()-1;
        playSong();
    }

    //skip to next
    public void playNext(){
        if(shuffle){
            int newSong=songPosition;
            while(newSong == songPosition){
                newSong=rand.nextInt(songs.size());
            }
            songPosition = newSong;
        }else{
        songPosition++;
        if(songPosition>=songs.size())
            songPosition=0;
        }
        playSong();
    }

    public void setShuffle(){
        if(shuffle) shuffle=false;
        else shuffle=true;
    }

    @Override
    public IBinder onBind(Intent intent) {

        return musicBind;

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
//      assign the song name to songName
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getApplicationContext(), MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification();
        notification.tickerText = "Ticker Text";
        notification.icon = R.drawable.playgreen;
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        notification.setLatestEventInfo(getApplicationContext(), "MusicPlayerSample",
                "Playing: " + songTitle, pi);
        startForeground(NOTIFY_ID, notification);
    }


    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        mediaPlayer.reset();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if(mediaPlayer.getCurrentPosition() > 0 ){
            mediaPlayer.reset();
            playNext();
        }
    }
}
