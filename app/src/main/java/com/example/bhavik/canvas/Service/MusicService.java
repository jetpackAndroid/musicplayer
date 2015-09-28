package com.example.bhavik.canvas.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;

import com.example.bhavik.canvas.Action.action;
import com.example.bhavik.canvas.Acttivities.MainActivity;
import com.example.bhavik.canvas.Fragments.SingleMusicFragment;
import com.example.bhavik.canvas.Modal.Songs;
import com.example.bhavik.canvas.R;

import java.util.ArrayList;
import java.util.Random;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener{

    //Media Player
    private MediaPlayer mediaPlayer;
    //Song List
    private ArrayList<Songs> songs;
    // Song Position
    private int songPosition;
    //To remove seekbar callback of a song on next song
    Handler handler = new Handler();
    // Current playing song variable.
    Songs currentPlayingSong = null;
    Notification notification;
    PendingIntent pi;
    private final IBinder musicBind = new MusicBinder();

    private String songTitle="";
    private static final int NOTIFY_ID=1;

    private boolean shuffle=false;
    private Random rand;
    NotificationManager notificationManager;

    public SingleMusicFragment.SeekbarHandler seekbarHandler;

    public MusicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        songPosition = 0;
        mediaPlayer = new MediaPlayer();
        initMusicPlayer();
        rand=new Random();

        // Setting Handler for setting album art and duration.
        seekbarHandler = new SingleMusicFragment().new SeekbarHandler();

    }

    public void initMusicPlayer(){

        //set player properties
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
    }

    public void stopMusic() {
        mediaPlayer.stop();
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
//        return super.onStartCommand(intent, flags, startId);
        return START_FLAG_REDELIVERY;
    }
//
//
//    @Override
//    public void onDestroy() {
//        super
//        stopForeground(true);
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
//        mediaPlayer.stop();
//        mediaPlayer.release();
        return  false;
    }
    public void setSong(int songIndex){
        songPosition=songIndex;
    }

    public int getSongIndex() {
        return songPosition;
    }

    public void playSong(){

        Message message = Message.obtain();
        Bundle bundle = new Bundle();

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
        // Save current playing song, to give access to other class.
        setCurrentSong(playSong);
//         Pass Duration in message.
//        bundle.putSerializable("CurrentSong", playSong);
//        message.setData(bundle);
//        seekbarHandler.sendMessage(message);

        mediaPlayer.prepareAsync();
        seekbarHandler.sendMessage(seekbarHandler.obtainMessage(action.PLAY_SONG));
        notificationLauncher(true);
    }

    public void setCurrentSong(Songs playSong) {
        currentPlayingSong = playSong;
    }

    public ArrayList<Songs> getCompleteSongList() {
        return songs;
    }
    public Songs getCurrentPlayingSong() {
            return currentPlayingSong;
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
        notificationLauncher(false);
    }

    public void seek(int posn){
        mediaPlayer.seekTo(posn);
    }

    public void go(){
        mediaPlayer.start();
        notificationLauncher(true);
    }

    public void playPrev(){
        songPosition--;
        if(songPosition<0)
            songPosition=songs.size()-1;
        playSong();
    }

    //skip to next
    public void playNext(){

        // Remove previous Callback and pass another call back for music duration when song changed automatically.
        handler.removeCallbacksAndMessages(new SingleMusicFragment());
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

    public void notificationLauncher(boolean notify) {
        if (notify) {
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            //      assign the song name to songName
            pi = PendingIntent.getActivity(MainActivity.getMainActivity(), 0,
                    new Intent(MainActivity.getMainActivity(), MainActivity.class),
                    PendingIntent.FLAG_UPDATE_CURRENT);
            notification = new Notification();
            notification.tickerText = "Ticker Text";
            notification.icon = R.drawable.playgreen;
            notification.flags |= Notification.FLAG_ONGOING_EVENT;
            notification.setLatestEventInfo(getApplicationContext(), "MusicPlayerSample",
                    "Playing: " + songTitle, pi);
            //        startForeground(NOTIFY_ID, notification);
            notificationManager.notify(NOTIFY_ID, notification);
        } else {
            if (notificationManager != null)
                notificationManager.cancel(NOTIFY_ID);
        }
    }
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
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
