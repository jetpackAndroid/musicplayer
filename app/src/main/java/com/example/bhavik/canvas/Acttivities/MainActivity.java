package com.example.bhavik.canvas.Acttivities;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.bhavik.canvas.Fragments.MusicFragment;
import com.example.bhavik.canvas.Fragments.SingleMusicFragment;
import com.example.bhavik.canvas.Modal.Songs;
import com.example.bhavik.canvas.R;
import com.example.bhavik.canvas.Service.MusicService;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity {

    public static MusicService musicService;
    Intent playIntent;
    boolean musicBound = false;
    String fragmentTAG = null;
    private static MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setMainActivity(this);

        if(playIntent==null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, this.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }
    public ServiceConnection musicConnection = new ServiceConnection() {
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

    public void applyFragment(String TAG, Bundle bundle){
        fragmentTAG = TAG;
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainContainer,getFragment(TAG,bundle)).addToBackStack(null).commitAllowingStateLoss();
    }
    public Fragment getFragment(String tag, Bundle bundle){
        Fragment fragment = null;
        if(tag.equalsIgnoreCase(SingleMusicFragment.class.getSimpleName())){
            fragment = getFragmentManager().findFragmentByTag(tag);
            if(fragment == null){
                fragment = new SingleMusicFragment();
            }
            if(bundle != null){
                fragment.setArguments(bundle);
            }
        }

        return fragment;
    }
    @Override
    public void onStart() {
        super.onStart();

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

    public static void setMainActivity(MainActivity mainActivity) {
        MainActivity.mainActivity = mainActivity;
    }

    // Calling from MusicFragment to pass song list to service.
    public void passMusicList(ArrayList<Songs> songList){
        musicService.setMusicList(songList);
    }

    public void passSelectedSongPosition(int position) {
        musicService.setSong(position);
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() == 0){
            this.finish();
        }
        else
            getFragmentManager().popBackStack();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        // Start Main fragment after service connection. Just to pass song list otherwise Fragment called first then service,
        // which gives NULL pointer Exception
        MusicFragment musicFragment = new MusicFragment();
        getFragmentManager().beginTransaction().replace(R.id.mainContainer, musicFragment,MusicFragment.TAG).commit();
    }

    /**
     * Music Events
     */
    public void start() {
        MainActivity.musicService.go();
    }

    public boolean checkIsPlaying() {
        return MainActivity.musicService.isPng();
    }

    public void pauseMusic() {
        MainActivity.musicService.pausePlayer();
    }

    public void playMusic() {
        MainActivity.musicService.go();
    }
}
