package com.example.bhavik.canvas.Acttivities;


import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Toast;

import com.example.bhavik.canvas.Controller.MusicController;
import com.example.bhavik.canvas.CustomAdapters.SongAdapter;
import com.example.bhavik.canvas.Modal.Songs;
import com.example.bhavik.canvas.R;
import com.example.bhavik.canvas.Service.MusicService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends ActionBarActivity implements SongAdapter.customButtonListener, MediaController.MediaPlayerControl{

    MusicService musicService;
    Intent playIntent;
    boolean musicBound = false;
    private ArrayList<Songs> songList;
    private ListView songsView;
    public SongAdapter songAdapter;
    private MusicController controller;
    private boolean paused=true, playbackPaused=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        songsView = (ListView) findViewById(R.id.song_list);
        songList = new ArrayList<Songs>();
        getSongList();
        Collections.sort(songList, new Comparator<Songs>() {
            @Override
            public int compare(Songs songs, Songs t1) {
                return songs.getTitle().compareTo(t1.getTitle());
            }
        });
        songAdapter = new SongAdapter(this, songList, MainActivity.this);

        songsView.setAdapter(songAdapter);

        setController();
    }
    private void setController(){
        //set the controller up
        controller = new MusicController(this);
        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }

        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });
        controller.setMediaPlayer(this);
        controller.setAnchorView(findViewById(R.id.song_list));
        controller.setEnabled(true);
    }
    //play next
    private void playNext(){
        musicService.playNext();
        if(playbackPaused){
            setController();
            playbackPaused = false;
        }

        controller.show(0);
    }

    //play previous
    private void playPrev(){
        musicService.playPrev();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(0);
    }

    public void getSongList(){
//        We  can have _ID, ARTIST, TITLE, DISPLAY_NAME, DATA, ALBUM_ID, DURATION
//        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        ContentResolver contentResolver = getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = contentResolver.query(musicUri,null,null,null,null);
        if(musicCursor != null && musicCursor.moveToFirst()){
            int titleColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ARTIST);
            do{
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                songList.add(new Songs(thisId, thisTitle, thisArtist));
            }while(musicCursor.moveToNext());
        }
    }

    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)iBinder;
            //get service
            musicService = binder.getService();
            //pass list
            musicService.setMusicList(songList);
            musicBound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicBound = false;

        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        paused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        paused = false;
    }

    @Override
    protected void onStop() {
        controller.hide();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, this.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicService = null;
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){
            case R.id.action_shuffle:
                musicService.setShuffle();
                break;
            case R.id.action_end:
                stopService(playIntent);
                musicService = null;
                System.exit(0);
                break;
        }
//        noinspection SimplifiableIfStatement
//        if (id == R.id.action_shuffle) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onButtonClickListner(int position, String value) {
//        Toast.makeText(MainActivity.this, "hi" + position, Toast.LENGTH_LONG).show();
        musicService.setSong(position);
        musicService.playSong();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(0);
    }

    @Override
    public void start() {
        musicService.go();
    }

    @Override
    public void pause() {
        playbackPaused=true;
        musicService.pausePlayer();
    }

    @Override
    public int getDuration() {
        if(musicService != null && musicBound && musicService.isPng())
            return musicService.getDur();
        else
            return 0;
    }

    @Override
    public int getCurrentPosition() {
        if(musicService != null && musicBound && musicService.isPng())
            return musicService.getPosn();
        else
            return 0;
    }

    @Override
    public void seekTo(int pos) {
        musicService.seek(pos);
    }

    @Override
    public boolean isPlaying() {
        if(musicService != null && musicBound)
            return musicService.isPng();
        else
            return false;

    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }
}
