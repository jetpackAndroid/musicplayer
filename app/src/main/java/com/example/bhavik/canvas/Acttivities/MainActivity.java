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
import com.example.bhavik.canvas.Fragments.MusicFragment;
import com.example.bhavik.canvas.Modal.Songs;
import com.example.bhavik.canvas.R;
import com.example.bhavik.canvas.Service.MusicService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends ActionBarActivity implements MusicFragment.passSongList{

    public static MusicService musicService;
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
        setMainActivity(this);
        MusicFragment musicFragment = new MusicFragment();
        getFragmentManager().beginTransaction().replace(R.id.mainContainer, musicFragment,MusicFragment.TAG).commit();


    }
    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder)iBinder;
            //get service
            musicService = binder.getService();

            musicBound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicBound = false;

        }
    };

    @Override
    public void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, this.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    public void onDestroy() {
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

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    private static MainActivity mainActivity;

    public static void setMainActivity(MainActivity mainActivity) {
        MainActivity.mainActivity = mainActivity;
    }

    @Override
    public void completeSongList(ArrayList<Songs> songList) {
        //pass list
        musicService.setMusicList(songList);
    }
}
